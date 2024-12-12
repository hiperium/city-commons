package hiperium.city.functions.common.exceptions;

import hiperium.city.functions.common.enums.ErrorCode;

/**
 * The CityException class is an exception thrown when there is an error related to a city.
 */
public sealed class CityException extends RuntimeException
    permits
    InactiveCityException,
    ResourceNotFoundException,
    ValidationException {

    /**
     * The error code.
     */
    private final ErrorCode code;

    /**
     * The request ID.
     */
    private final String requestId;

    private CityException() {
        throw new UnsupportedOperationException("Cannot instantiate a CityException without a code and description.");
    }

    /**
     * Constructs a new CityException with the specified detail message and error code.
     *
     * @param message The detail message for this exception.
     * @param code    The error code associated with the exception.
     */
    public CityException(String message, ErrorCode code) {
        super(message);
        this.code = code;
        this.requestId = null;
    }

    /**
     * Constructs a new CityException with the specified detail message, error code, and cause.
     *
     * @param message The detail message for this exception.
     * @param code    The error code associated with the exception.
     * @param cause   The cause of the exception.
     */
    public CityException(String message, ErrorCode code, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.requestId = null;
    }

    /**
     * Constructs a new CityException with the specified detail message, error code, and request ID.
     *
     * @param message   The detail message for this exception.
     * @param code      The error code associated with the exception.
     * @param requestId The request ID for tracing the exception.
     */
    public CityException(String message, ErrorCode code, String requestId) {
        super(message);
        this.code = code;
        this.requestId = requestId;
    }

    /**
     * Constructs a new CityException with the specified detail message, error code,
     * request ID, and cause of the exception.
     *
     * @param message   The detail message for this exception.
     * @param code      The error code associated with the exception.
     * @param requestId The request ID for tracing the exception.
     * @param cause     The cause of the exception.
     */
    public CityException(String message, ErrorCode code, String requestId, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.requestId = requestId;
    }

    /**
     * Retrieves the error code associated with this exception.
     *
     * @return the error code represented by an instance of ErrorCode.
     */
    public ErrorCode getCode() {
        return code;
    }

    /**
     * Returns the request ID associated with the exception. This ID can be used
     * for tracing and tracking the specific request that led to the exception.
     *
     * @return the request ID or null if no request ID is associated.
     */
    public String getRequestId() {
        return requestId;
    }
}
