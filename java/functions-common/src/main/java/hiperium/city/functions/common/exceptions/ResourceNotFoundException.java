package hiperium.city.functions.common.exceptions;

import hiperium.city.functions.common.enums.ErrorCode;

/**
 * The ResourceNotFoundException class is an exception thrown when a resource is not found.
 */
public final class ResourceNotFoundException extends CityException {

    /**
     * Exception thrown when a resource is not found.
     *
     * @param message   The exception description.
     * @param requestId The request ID.
     */
    public ResourceNotFoundException(String message, String requestId) {
        super(message, ErrorCode.BUSINESS_002, requestId);
    }
}
