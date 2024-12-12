package hiperium.city.functions.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * The FunctionUtils class provides utility constants and methods that support
 * various functionalities within the application. It includes constants used
 * for configuration and an instance of an ObjectMapper for JSON operations.
 */
public final class FunctionUtils {

    /**
     * A constant representing the name of the function parameter used to route messages.
     */
    public static final String ROUTING_PARAMETER = "city-function";

    /**
     * An instance of Jackson's ObjectMapper configured for JSON serialization and deserialization.
     * It is used throughout the application for handling JSON-related operations.
     */
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .registerModule(new JavaTimeModule());

    private FunctionUtils() {
        throw new UnsupportedOperationException("Utility classes should not be instantiated.");
    }
}
