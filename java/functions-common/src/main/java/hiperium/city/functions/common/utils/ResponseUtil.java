package hiperium.city.functions.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import hiperium.city.functions.common.enums.ErrorCode;
import hiperium.city.functions.common.exceptions.CityException;
import hiperium.city.functions.common.responses.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import java.time.ZonedDateTime;

/**
 * The ResponseUtil class provides utility methods for constructing and returning
 * structured API responses in a standardized format. This includes handling both
 * successful responses and error responses with detailed information.
 */
public final class ResponseUtil {

    /**
     * This key is used to specify the status code of the response as part of the AWS
     * API Gateway's integration with AWS Lambda functions. For example, it can be used
     * to set an HTTP 200 status for success or an HTTP 500 for errors.
     */
    public static final String LAMBDA_STATUS_CODE = "statusCode";

    private ResponseUtil() {
        throw new UnsupportedOperationException("Utility classes should not be instantiated.");
    }

    /**
     * Constructs a success response message with the specified body. The response
     * is serialized into a JSON payload and includes a content type header for JSON.
     *
     * @param body The body of the response to be serialized into JSON.
     * @return A {@code Message<String>} containing the serialized JSON payload and the
     *         appropriate content type header.
     * @throws CityException If an error occurs during JSON serialization of the body.
     */
    public static Message<String> success(Object body) {
        try {
            return MessageBuilder
                .withPayload(FunctionsUtil.OBJECT_MAPPER.writeValueAsString(body))
                .setHeader(LAMBDA_STATUS_CODE, HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE)
                .build();
        } catch (JsonProcessingException exception) {
            throw new CityException("Error serializing response body: " + body, ErrorCode.INTERNAL_002, exception);
        }
    }

    /**
     * Constructs an error response message containing details about a CityException.
     * This method formats the exception data into a structured JSON payload and adds the necessary headers.
     *
     * @param exception The CityException containing error details such as the error code, description, and request ID.
     * @return A Message object with a JSON payload of error details and relevant HTTP headers.
     * @throws CityException If there is an error serializing the ErrorDetails object into a JSON string.
     */
    public static Message<String> error(CityException exception) {
        ErrorDetails errorDetails = new ErrorDetails(
            exception.getCode().name(),
            exception.getCode().getDescription(),
            exception.getMessage(),
            ZonedDateTime.now(),
            exception.getRequestId());

        try {
            return MessageBuilder
                .withPayload(FunctionsUtil.OBJECT_MAPPER.writeValueAsString(errorDetails))
                .setHeader(LAMBDA_STATUS_CODE, exception.getCode().getHttpStatus())
                .setHeader(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE)
                .build();
        } catch (JsonProcessingException e) {
            throw new CityException("Error serializing response error details: " + errorDetails,
                ErrorCode.INTERNAL_002, exception);
        }
    }
}
