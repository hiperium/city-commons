package hiperium.cities.commons.enums;

/**
 * Represents the status of a record.
 */
public enum RecordStatus {

    /**
     * Status indicating the record is currently active and operational.
     */
    ACTIVE("ACTIVE"),

    /**
     * Status indicating the record is currently inactive and not in use.
     */
    INACTIVE("INACTIVE");

    private final String value;

    RecordStatus(String value) {
        this.value = value;
    }

    /**
     * Retrieves the value associated with the record status.
     *
     * @return the value as a String.
     */
    public String getValue() {
        return value;
    }
}
