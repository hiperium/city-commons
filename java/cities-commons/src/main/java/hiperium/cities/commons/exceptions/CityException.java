package hiperium.cities.commons.exceptions;

import lombok.Getter;

/**
 * The CityException class is an exception thrown when there is an error related to a city.
 */
@Getter
public sealed class CityException extends RuntimeException
    permits DisabledCityException, ResourceNotFoundException, ParsingException {

    /**
     * The CityException class is an exception thrown when there is an error related to a city.
     *
     * @param message The error message.
     */
    public CityException(String message) {
        super(message);
    }
}
