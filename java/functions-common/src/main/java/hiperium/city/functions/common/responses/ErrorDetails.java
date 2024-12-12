package hiperium.city.functions.common.responses;

import java.time.ZonedDateTime;

/**
 * Represents detailed information about an error. This class is used to encapsulate
 * error data that can be used for debugging and tracking purposes within the system.
 *
 * @param code          A custom error code that uniquely identifies the error (e.g., "BUSINESS_001").
 * @param description   A user-friendly description describing the error, intended to be displayed to end-users.
 * @param detail        A detailed description of the error, providing additional context for developers.
 * @param timestamp     A string representation of when the error occurred, useful for logging and auditing.
 * @param requestId     A unique identifier for the request where the error occurred, aiding in tracking and debugging.
 */
public record ErrorDetails(
    String code,                // Custom error code (e.g., "BUSINESS_001")
    String description,         // User-friendly description
    String detail,              // Detailed description for developers
    ZonedDateTime timestamp,    // When the error occurred
    String requestId            // For tracking/debugging purposes
) {
}
