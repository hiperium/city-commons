package hiperium.city.functions.common.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * The DateTimeUtil class provides utility methods for handling date-time operations,
 * specifically focusing on the ISO 8601 date-time format.
 * <p>
 * This class is not meant to be instantiated, as it serves purely as a static utility class.
 */
public final class DateTimeUtil {

    private static final String TIMESTAMP_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ssXXX";

    private DateTimeUtil() {
        throw new UnsupportedOperationException("Utility classes should not be instantiated.");
    }

    /**
     * Returns a DateTimeFormatter configured to use the ISO 8601 date and time format.
     *
     * @return a DateTimeFormatter for the ISO 8601 format, suitable for formatting
     *         and parsing date-time objects according to the pattern "yyyy-MM-dd'T'HH:mm:ssXXX".
     */
    public static DateTimeFormatter getDateTimeFormatterUsingISO8601() {
        return DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT_ISO8601);
    }

    /**
     * Parses a date-time string in ISO 8601 format into a {@link ZonedDateTime} object.
     *
     * @param dateTimeValue the date-time string in ISO 8601 format to be parsed.
     *                      Must not be null.
     * @param zoneId        the zone ID to be used for the parsed date-time value.
     * @return a {@link ZonedDateTime} object representing the parsed date-time value.
     * @throws IllegalArgumentException if the date time value is null or is not in a valid
     *                                  ISO 8601 date-time format.
     */
    public static ZonedDateTime getZonedDateTimeUsingISO8601(final String dateTimeValue, final String zoneId) {
        if (Objects.isNull(dateTimeValue) || dateTimeValue.isBlank()) {
            throw new IllegalArgumentException("Date time cannot be null.");
        } else if (Objects.isNull(zoneId) || zoneId.isBlank()) {
            throw new IllegalArgumentException("Zone ID cannot be null.");
        }

        try {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern(TIMESTAMP_FORMAT_ISO8601)
                .toFormatter();
            return ZonedDateTime.parse(dateTimeValue, formatter.withZone(ZoneId.of(zoneId)));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date time format: " + dateTimeValue);
        }
    }
}
