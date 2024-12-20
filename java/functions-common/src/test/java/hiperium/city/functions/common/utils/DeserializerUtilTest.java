package hiperium.city.functions.common.utils;

import hiperium.city.functions.common.exceptions.CityException;
import hiperium.city.functions.common.exceptions.ValidationException;
import hiperium.city.functions.common.requests.CityIdRequest;
import hiperium.city.functions.common.requests.FunctionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class DeserializerUtilTest {

    private static final String VALID_API_GATEWAY_REQUEST_JSON = """
        {
           "version": "2.0",
           "routeKey": "$default",
           "headers": {
             "city-function": "function1"
           },
           "requestContext": {
             "accountId": "123456789012",
             "apiId": "api-id",
             "http": {
               "method": "POST",
               "path": "/my/path",
               "protocol": "HTTP/1.1",
               "sourceIp": "192.0.2.1",
               "userAgent": "agent"
             },
             "requestId": "id",
             "routeKey": "$default",
             "stage": "$default",
             "time": "12/Mar/2020:19:03:58 +0000"
           },
           "body": "{\\"cityId\\":\\"a0ecb466-7ef5-47bf-a1ca-12f9f9328528\\"}",
           "isBase64Encoded": false
        }
        """;

    @Test
    @DisplayName("Deserialize request event")
    void givenValidRequest_whenDeserializeIt_thenReturnValidObject() throws IOException {
        FunctionRequest gatewayRequest = DeserializerUtil.fromJson(VALID_API_GATEWAY_REQUEST_JSON);

        assertThat(gatewayRequest.body()).isNotNull();
        assertThat(gatewayRequest.headers()).isNotNull();
        assertThat(gatewayRequest.requestContext()).isNotNull();
    }

    @Test
    @DisplayName("Deserialize valid CityId")
    void givenValidCityIdRequest_whenDeserializeCityId_thenReturnValidObject() throws IOException {
        FunctionRequest functionRequest = DeserializerUtil.fromJson(VALID_API_GATEWAY_REQUEST_JSON);

        CityIdRequest cityIdRequest = DeserializerUtil.deserializeCityId(functionRequest);

        assertThat(cityIdRequest).isNotNull();
        assertThat(cityIdRequest.cityId()).isEqualTo("a0ecb466-7ef5-47bf-a1ca-12f9f9328528");
    }

    @Test
    @DisplayName("Throws ValidationException when request body is null or blank")
    void givenNullOrBlankBody_whenDeserializeCityId_thenThrowsValidationException() {
        FunctionRequest functionRequestWithNullBody = new FunctionRequest(null, new FunctionRequest.RequestContext(
                "account-id", "api-id", new FunctionRequest.RequestContext.Http(
                    "/path", "HTTP/1.1", "127.0.0.1", "agent"),
                "request-id", "route-key", "default", "time"), null);

        FunctionRequest functionRequestWithBlankBody = new FunctionRequest(null, new FunctionRequest.RequestContext(
                "account-id", "api-id", new FunctionRequest.RequestContext.Http(
                    "/path", "HTTP/1.1", "127.0.0.1", "agent"),
                "request-id", "route-key", "default", "time"), "");

        org.junit.jupiter.api.Assertions.assertThrows(ValidationException.class,
                () -> DeserializerUtil.deserializeCityId(functionRequestWithNullBody));

        org.junit.jupiter.api.Assertions.assertThrows(ValidationException.class,
                () -> DeserializerUtil.deserializeCityId(functionRequestWithBlankBody));
    }

    @Test
    @DisplayName("Throws CityException when request body cannot be deserialized")
    void givenInvalidBody_whenDeserializeCityId_thenThrowsCityException() {
        FunctionRequest functionRequest = new FunctionRequest(null, new FunctionRequest.RequestContext(
                "account-id", "api-id", new FunctionRequest.RequestContext.Http(
                    "/path", "HTTP/1.1", "127.0.0.1", "agent"),
                "request-id", "route-key", "default", "time"), "{ invalid json }");

        org.junit.jupiter.api.Assertions.assertThrows(CityException.class,
                () -> DeserializerUtil.deserializeCityId(functionRequest));
    }
}
