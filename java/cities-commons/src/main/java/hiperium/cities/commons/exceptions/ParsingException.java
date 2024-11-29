package hiperium.cities.commons.exceptions;

/**
 * The ParsingException class is an exception thrown when there is an error related to parsing.
 * It is a subclass of CityException.
 */
public final class ParsingException extends CityException {

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
