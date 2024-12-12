package hiperium.city.functions.common.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import hiperium.city.functions.common.enums.ErrorCode;
import hiperium.city.functions.common.exceptions.CityException;
import hiperium.city.functions.common.utils.FunctionUtils;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an immutable HTTP function response which includes a status code, headers,
 * and a body. Typically used to model the outcome of serverless functions or similar services.
 *
 * @param statusCode The HTTP status code of the response.
 * @param headers The headers included in the response.
 * @param body The body of the response, usually serialized in JSON format.
 * @param errorDetails Detailed error information, if the response represents an error.
 */
@JsonInclude(Include.NON_NULL)
public record FunctionResponse(

    int statusCode,
    Map<String, String> headers,
    String body,
    ErrorDetails errorDetails

) {
    /**
     * Builder class for constructing instances of FunctionResponse.
     * Allows configuration of HTTP status code, headers, and body content for the response.
     */
    public static class Builder {

        private int statusCode = 200;
        private final Map<String, String> headers = new HashMap<>();
        private String body;
        private ErrorDetails errorDetails;

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
         * @throws CityException if there is an error during JSON serialization of the object.
         */
        public Builder withBody(Object body) {
            if (Objects.isNull(body)) {
                this.body = null;
                return this;
            }
            try {
                this.body = FunctionUtils.OBJECT_MAPPER.writeValueAsString(body);
            } catch (JsonProcessingException exception) {
                throw new CityException("Error serializing response body: " + body, ErrorCode.INTERNAL_002, exception);
            }
            return this;
        }

        /**
         * Sets the error details for the response using an instance of ErrorDetails.
         *
         * @param errorDetails The ErrorDetails object containing specific error information,
         *                     such as code, description, and timestamp.
         * @return The Builder instance with the updated error details, allowing for method chaining.
         */
        public Builder withErrorDetails(ErrorDetails errorDetails) {
            this.errorDetails = errorDetails;
            return this;
        }

        /**
         * Builds a FunctionResponse instance using the current state of the Builder.
         *
         * @return A new FunctionResponse object configured with the provided
         * status code, headers, body content, and error details.
         */
        public FunctionResponse build() {
            if (this.statusCode == 0 || this.statusCode < 100 || this.statusCode > 599) {
                throw new CityException("Invalid status code: " + this.statusCode, ErrorCode.INTERNAL_003);
            }
            if (Objects.nonNull(this.body) && Objects.nonNull(this.errorDetails)) {
                throw new CityException("Cannot set both body and error details in the same response.", ErrorCode.INTERNAL_003);

            }
            if (this.headers.isEmpty()) {
                this.headers.put("Content-Type", "application/json");
            }
            return new FunctionResponse(this.statusCode, this.headers, this.body, this.errorDetails);
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
            .withStatusCode(HttpStatus.OK.value())
            .withBody(body)
            .withErrorDetails(null)
            .build();
    }

    /**
     * Generates an error response encapsulating details of the provided CityException.
     * This response includes the HTTP status code derived from the exception's error code,
     * and comprehensive error details in lieu of a response body.
     *
     * @param exception The CityException instance containing the error details to be used
     *                  in creating the response.
     * @return A FunctionResponse object containing the HTTP status code, and populated
     *         error details based on the provided exception.
     */
    public static FunctionResponse error(CityException exception) {
        return new Builder()
            .withStatusCode(exception.getCode().getHttpStatus())
            .withBody(null)
            .withErrorDetails(new ErrorDetails(
                exception.getCode().name(),
                exception.getCode().getDescription(),
                exception.getMessage(),
                ZonedDateTime.now(),
                exception.getRequestId()))
            .build();
    }
}
