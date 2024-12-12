package hiperium.city.functions.common.exceptions;

import hiperium.city.functions.common.enums.ErrorCode;

/**
 * Exception thrown when a city is disabled.
 */
public final class InactiveCityException extends CityException {

    /**
     * Exception thrown when a city is disabled.
     *
     * @param message   The exception description.
     * @param requestId The request ID.
     */
    public InactiveCityException(String message, String requestId) {
        super(message, ErrorCode.BUSINESS_003, requestId);
    }
}
