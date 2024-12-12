package hiperium.city.functions.common.exceptions;

import hiperium.city.functions.common.enums.ErrorCode;

/**
 * The ValidationException class is an exception thrown when a validation error occurs.
 * It extends the CityException class, adding specific details related to validation failures.
 */
public final class ValidationException extends CityException {

    /**
     * Constructs a new ValidationException with the specified detail description, error code, and request ID.
     *
     * @param message   The detail description for this exception.
     * @param requestId The request ID that can be used to trace the origin of the exception.
     */
    public ValidationException(String message, String requestId) {
        super(message, ErrorCode.BUSINESS_001, requestId);
    }
}
