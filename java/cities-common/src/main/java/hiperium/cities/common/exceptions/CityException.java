package hiperium.cities.common.exceptions;

/**
 * The CityException class is an exception thrown when there is an error related to a city.
 */
public sealed class CityException extends RuntimeException
    permits InactiveCityException, ResourceNotFoundException, ParsingException {

    /**
     * Constructs a new CityException with the specified detail message.
     *
     * @param message The detail message for this exception.
     */
    public CityException(String message) {
        super(message);
    }

    /**
     * The CityException class is an exception thrown when there is an error related to a city.
     *
     * @param message The error message.
     * @param cause   The cause of the exception.
     */
    public CityException(String message, Throwable cause) {
        super(message, cause);
    }
}
