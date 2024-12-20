package hiperium.city.functions.common.utils;

import hiperium.city.functions.common.enums.ErrorCode;
import hiperium.city.functions.common.exceptions.CityException;
import hiperium.city.functions.common.exceptions.ValidationException;
import hiperium.city.functions.common.requests.CityIdRequest;
import hiperium.city.functions.common.requests.FunctionRequest;

import java.io.IOException;
import java.util.Objects;

/**
 * The RequestDeserializerUtil class provides utility methods for deserializing JSON strings
 * into AWS API Gateway-specific request event objects.
 */
public final class DeserializerUtil {

    private DeserializerUtil() {
        throw new UnsupportedOperationException("Utility classes should not be instantiated.");
    }

    /**
     * Deserializes a JSON string into an {@link FunctionRequest} object.
     *
     * @param json the JSON string to be deserialized; must conform to the structure of {@link FunctionRequest}.
     * @return an instance of {@link FunctionRequest} containing the deserialized data from the JSON string.
     * @throws IOException if an error occurs while parsing the JSON string.
     */
    public static FunctionRequest fromJson(String json) throws IOException {
        return FunctionsUtil.OBJECT_MAPPER.readValue(json, FunctionRequest.class);
    }

    /**
     * Deserializes the request body of an FunctionRequest into a CityIdRequest object.
     * If the request body is null or blank, a ValidationException is thrown.
     * If the deserialization process fails, a CityException is thrown with details of the error.
     *
     * @param functionRequest The incoming request object containing headers, body, and context data.
     * @return The deserialized CityIdRequest object extracted from the request body.
     * @throws ValidationException If the request body is missing or empty.
     * @throws CityException If an error occurs during deserialization of the request body.
     */
    public static CityIdRequest deserializeCityId(final FunctionRequest functionRequest){
        if (Objects.isNull(functionRequest.body()) || functionRequest.body().isBlank()) {
            throw new ValidationException("Request body is missing or empty.",
                functionRequest.requestContext().requestId());
        }
        try {
            return FunctionsUtil.OBJECT_MAPPER.readValue(functionRequest.body(), CityIdRequest.class);
        } catch (IOException exception) {
            throw new CityException("Couldn't deserialize CityId from request body: " + functionRequest.body(),
                ErrorCode.INTERNAL_002, exception);
        }
    }
}
