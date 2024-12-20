package hiperium.city.functions.common.requests;

import hiperium.city.functions.common.annotations.ValidUUID;
import jakarta.validation.constraints.NotBlank;

/**
 * The CityDataRequest record represents a request to execute a function, encapsulating
 * the required parameters for processing within the function.
 *
 * @param cityId The unique identifier of the city to retrieve data for.
 */
public record CityIdRequest(
    @ValidUUID(message = "City ID must have a valid format.")
    @NotBlank(message = "City ID must not be blank.")
    String cityId
) {
}
