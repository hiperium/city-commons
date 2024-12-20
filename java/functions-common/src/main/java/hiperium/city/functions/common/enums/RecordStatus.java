package hiperium.city.functions.common.enums;

import lombok.Getter;

/**
 * Represents the status of a record.
 */
@Getter
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
}
