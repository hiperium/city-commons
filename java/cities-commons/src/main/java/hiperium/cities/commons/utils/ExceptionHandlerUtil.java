package hiperium.cities.commons.utils;

import hiperium.cities.commons.dto.ErrorResponse;
import hiperium.cities.commons.exceptions.DisabledCityException;
import hiperium.cities.commons.exceptions.ParsingException;
import hiperium.cities.commons.exceptions.ResourceNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;

/**
 * The ExceptionHandlerUtil class provides utility methods for handling exceptions and generating error responses.
 * It is a final class with a private constructor that cannot be instantiated.
 */
public final class ExceptionHandlerUtil {

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private ExceptionHandlerUtil() {
        // Default constructor
    }

    /**
     * Generates an ErrorResponse object based on the given throwable.
     * The ErrorResponse object contains information about the error, such as the error code, status, message, and details.
     *
     * @param throwable The throwable object representing the error.
     * @return An ErrorResponse object representing the error response.
     */
    public static ErrorResponse generateErrorResponse(Throwable throwable) {
        HttpStatus httpStatus;
        String errorMessage = throwable.getMessage();

        switch (throwable) {
            case ParsingException ignored          -> httpStatus = HttpStatus.BAD_REQUEST;
            case ValidationException ignored       -> httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            case ResourceNotFoundException ignored -> httpStatus = HttpStatus.NOT_FOUND;
            case DisabledCityException ignored     -> httpStatus = HttpStatus.NOT_ACCEPTABLE;
            default                                -> httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ErrorResponse.Builder()
            .errorCode(httpStatus.value())
            .errorStatus(httpStatus.name())
            .errorMessage(errorMessage)
            .build();
    }
}
