package hiperium.cities.commons.exceptions;

/**
 * Exception thrown when a city is disabled.
 */
public final class DisabledCityException extends CityException {

    /**
     * Exception thrown when a city is disabled.
     *
     * @param message The exception message.
     */
    public DisabledCityException(String message) {
        super(message);
    }
}
