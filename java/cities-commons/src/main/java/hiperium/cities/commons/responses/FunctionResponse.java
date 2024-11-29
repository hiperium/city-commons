package hiperium.cities.commons.responses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hiperium.cities.commons.exceptions.ParsingException;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an immutable HTTP function response which includes a status code, headers,
 * and a body. Typically used to model the outcome of serverless functions or similar services.
 *
 * @param statusCode The HTTP status code of the response.
 * @param headers The headers included in the response.
 * @param body The body of the response, usually serialized in JSON format.
 */
public record FunctionResponse(
    int statusCode,
    Map<String, String> headers,
    String body
) {

    /**
     * Builder class for constructing instances of FunctionResponse.
     * Allows configuration of HTTP status code, headers, and body content for the response.
     */
    public static class Builder {

        private int statusCode = 200;
        private final Map<String, String> headers = new HashMap<>();
        private String body;

        /**
         * Initializes a new instance of the Builder class,
         * setting default values for the response status code, headers, and body.
         * By default, the status code is set to 200, headers are initialized as an empty map,
         * and the body is uninitialized.
         */
        public Builder() {
        }

        /**
         * Sets the status code for the response.
         *
         * @param statusCode The HTTP status code to set.
         * @return The Builder instance for method chaining.
         */
        public Builder withStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        /**
         * Adds a header to the request.
         *
         * @param key   The header key.
         * @param value The header value.
         * @return The Builder instance with the added header.
         */
        public Builder withHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        /**
         * Sets the body of the response by serializing the provided object to a JSON string.
         *
         * @param body The object to be serialized and set as the response body.
         * @return The Builder instance with the updated body.
         * @throws ParsingException if there is an error during JSON serialization of the object.
         */
        public Builder withBody(Object body) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                this.body = mapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                throw new ParsingException("Error serializing response body", e);
            }
            return this;
        }

        /**
         * Builds a FunctionResponse instance using the current state of the Builder.
         * If no headers are specified, a default 'Content-Type: application/json' header is added.
         *
         * @return A new FunctionResponse object configured with the provided
         * status code, headers, and body.
         */
        public FunctionResponse build() {
            if (this.headers.isEmpty()) {
                this.headers.put("Content-Type", "application/json");
            }
            return new FunctionResponse(this.statusCode, this.headers, this.body);
        }
    }

    /**
     * Generates a successful FunctionResponse with the specified body content.
     * The response is constructed with a status code of 200, and the body is
     * serialized to a JSON format.
     *
     * @param body The content to be serialized and used as the response body.
     * @return A FunctionResponse object containing a 200 HTTP status code and the specified body.
     */
    public static FunctionResponse success(Object body) {
        return new Builder()
            .withStatusCode(200)
            .withBody(body)
            .build();
    }

    /**
     * Creates an error response with the given HTTP status code and error message.
     *
     * @param statusCode The HTTP status code to set for the error response.
     * @param message A string message describing the error.
     * @return A FunctionResponse object configured with the given status code and error message.
     */
    public static FunctionResponse error(int statusCode, String message) {
        Map<String, String> errorBody = Map.of("message", message);
        return new Builder()
            .withStatusCode(statusCode)
            .withBody(errorBody)
            .build();
    }
}
