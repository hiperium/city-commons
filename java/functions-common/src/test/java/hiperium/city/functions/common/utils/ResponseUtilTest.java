package hiperium.city.functions.common.utils;

import hiperium.city.functions.common.enums.ErrorCode;
import hiperium.city.functions.common.exceptions.CityException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.util.MimeTypeUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseUtilTest {

    @Test
    void testSuccess_withComplexBody_returnsSerializedResponse() {
        // Arrange
        Map<String, String> testBody = Map.of("key", "value");
        String expectedJson = "{\"key\":\"value\"}";

        // Act
        Message<String> response = ResponseUtil.success(testBody);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getHeaders().get(ResponseUtil.LAMBDA_STATUS_CODE));
        assertEquals(expectedJson, response.getPayload());
        assertEquals(MimeTypeUtils.APPLICATION_JSON_VALUE, response.getHeaders().get(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    void testErrorResponse_withValidBody_returnsExpectedResponse() {
        CityException exception = new CityException("message", ErrorCode.BUSINESS_001, "requestId");

        // Act
        Message<String> response = ResponseUtil.error(exception);

        // Assert
        assertEquals(ErrorCode.BUSINESS_001.getHttpStatus(), response.getHeaders().get(ResponseUtil.LAMBDA_STATUS_CODE));
        assertEquals(MimeTypeUtils.APPLICATION_JSON_VALUE, response.getHeaders().get(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    void testSuccess_withNullBody_returnsExpectedResponse() {
        // Arrange
        String expectedJson = "null";

        // Act
        Message<String> response = ResponseUtil.success(null);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getHeaders().get(ResponseUtil.LAMBDA_STATUS_CODE));
        assertEquals(expectedJson, response.getPayload());
        assertEquals(MimeTypeUtils.APPLICATION_JSON_VALUE, response.getHeaders().get(HttpHeaders.CONTENT_TYPE));
    }
}
