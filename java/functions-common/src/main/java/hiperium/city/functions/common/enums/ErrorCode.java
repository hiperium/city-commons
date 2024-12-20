package hiperium.city.functions.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * The ErrorCode enum defines a set of predefined error codes used in the application.
 * Each error code is associated with a specific type of error and includes a default description
 * that provides a brief description of the error.
 */
@Getter
public enum ErrorCode {

    /**
     * Represents a validation error that occurs during business rule checks.
     * This error is typically associated with client errors where the request
     * does not meet specific validation criteria. It corresponds to an
     * HTTP status code of BAD_REQUEST.
     */
    BUSINESS_001("Validation error.", HttpStatus.BAD_REQUEST.value()),

    /**
     * Represents a specific error code indicating that a requested resource was not found.
     * This error typically corresponds to a 404 HTTP status code and is used when the server cannot locate
     * the requested content based on the given URI.
     */
    BUSINESS_002("Resource not found error.", HttpStatus.NOT_FOUND.value()),

    /**
     * Error code indicating an inactive city error.
     * This error occurs when an operation is attempted on a city record that is not currently active.
     * Associated with HTTP status code 403 Forbidden.
     */
    BUSINESS_003("Inactive city error.", HttpStatus.FORBIDDEN.value()),

    /**
     * Error code indicating an internal server error.
     * Associated with an HTTP status of 500 (Internal Server Error).
     */
    INTERNAL_001("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR.value()),

    /**
     * Represents a serialization error.
     * This error code signifies issues that occur during serialization processes,
     * often related to converting objects into a different data format.
     * Associated with HTTP status code 500 (Internal Server Error).
     */
    INTERNAL_002("Serialization error.", HttpStatus.INTERNAL_SERVER_ERROR.value()),

    /**
     * Represents an internal operation error within the application.
     * This error usually indicates that an unexpected condition was encountered
     * or an operation failed to complete successfully, which is not specifically
     * categorized by other internal error codes.
     * It is associated with the HTTP status code for internal server errors.
     */
    INTERNAL_003("Operation error.", HttpStatus.INTERNAL_SERVER_ERROR.value());

    private final int httpStatus;
    private final String description;

    ErrorCode(String description, int httpStatus) {
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
