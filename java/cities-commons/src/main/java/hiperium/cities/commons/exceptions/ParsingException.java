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
     */
    public ParsingException(String message) {
        super(message);
    }
}
