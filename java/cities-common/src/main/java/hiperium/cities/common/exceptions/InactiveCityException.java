package hiperium.cities.common.exceptions;

/**
 * Exception thrown when a city is disabled.
 */
public final class InactiveCityException extends CityException {

    /**
     * Exception thrown when a city is disabled.
     *
     * @param message The exception message.
     */
    public InactiveCityException(String message) {
        super(message);
    }
}
