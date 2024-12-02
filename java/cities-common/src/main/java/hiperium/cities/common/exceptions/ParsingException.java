package hiperium.cities.common.exceptions;

/**
 * The ParsingException class is an exception thrown when there is an error related to parsing.
 * It is a subclass of CityException.
 */
public final class ParsingException extends CityException {

    /**
     * Constructs a new ParsingException with the specified detail message.
     *
     * @param message The error message associated with this exception.
     */
    public ParsingException(String message) {
        super(message);
    }

    /**
     * The ParsingException class is an exception thrown when there is an error related to parsing.
     *
     * @param message The error message.
     * @param cause   The cause of the exception.
     */
    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
