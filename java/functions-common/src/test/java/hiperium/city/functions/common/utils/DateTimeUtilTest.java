package hiperium.city.functions.common.utils;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateTimeUtilTest {

    @Test
    void testValidZonedDateTimeParsing() {
        String dateTimeValue = "2023-09-15T14:30:00+01:00";
        String zoneId = "Europe/London";
        ZonedDateTime zonedDateTime = DateTimeUtil.getZonedDateTimeUsingISO8601(dateTimeValue, zoneId);

        assertEquals("2023-09-15T14:30+01:00[Europe/London]", zonedDateTime.toString());
    }

    @Test
    void testNullDateTimeValueThrowsException() {
        String zoneId = "Europe/London";
        assertThrows(IllegalArgumentException.class, () ->
                DateTimeUtil.getZonedDateTimeUsingISO8601(null, zoneId)
        );
    }

    @Test
    void testEmptyDateTimeValueThrowsException() {
        String dateTimeValue = "";
        String zoneId = "Europe/London";
        assertThrows(IllegalArgumentException.class, () ->
                DateTimeUtil.getZonedDateTimeUsingISO8601(dateTimeValue, zoneId)
        );
    }

    @Test
    void testNullZoneIdThrowsException() {
        String dateTimeValue = "2023-09-15T14:30:00+01:00";
        assertThrows(IllegalArgumentException.class, () ->
                DateTimeUtil.getZonedDateTimeUsingISO8601(dateTimeValue, null)
        );
    }

    @Test
    void testEmptyZoneIdThrowsException() {
        String dateTimeValue = "2023-09-15T14:30:00+01:00";
        String zoneId = "";
        assertThrows(IllegalArgumentException.class, () ->
                DateTimeUtil.getZonedDateTimeUsingISO8601(dateTimeValue, zoneId)
        );
    }

    @Test
    void testInvalidDateTimeFormatThrowsException() {
        String invalidDateTimeValue = "invalid-datetime";
        String zoneId = "Europe/London";
        assertThrows(IllegalArgumentException.class, () ->
                DateTimeUtil.getZonedDateTimeUsingISO8601(invalidDateTimeValue, zoneId)
        );
    }

    @Test
    void testInvalidZoneIdThrowsException() {
        String dateTimeValue = "2023-09-15T14:30:00+01:00";
        String invalidZoneId = "Invalid/Zone";
        assertThrows(ZoneRulesException.class, () ->
                DateTimeUtil.getZonedDateTimeUsingISO8601(dateTimeValue, invalidZoneId)
        );
    }
}
