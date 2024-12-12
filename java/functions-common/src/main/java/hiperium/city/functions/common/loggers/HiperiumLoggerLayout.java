package hiperium.city.functions.common.loggers;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hiperium.city.functions.common.utils.DateTimeUtils;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class defines the layout for logging events in the Hiperium format.
 * It extends the LayoutBase class from the logback library.
 * The layout converts the logging event into a JSON string representation.
 */
public class HiperiumLoggerLayout extends LayoutBase<ILoggingEvent> {

    private static final String LINE_BREAK = "\n";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String ERROR_SERIALIZATION_MESSAGE = "Couldn't serialize a description map: ";

    // Object pool for LinkedHashMap
    private static final ConcurrentLinkedQueue<LinkedHashMap<String, Object>> MAP_POOL = new ConcurrentLinkedQueue<>();

    private ZoneId zoneId;
    private DateTimeFormatter dateTimeFormatter;

    private String timezone;
    private String dateTimeFormat;
    private boolean useCompactMode = true;
    private boolean useFormattedTimestamps = false;

    /**
     * Constructs a new HiperiumLoggerLayout object.
     */
    public HiperiumLoggerLayout() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        this.initializeZoneId(this.timezone);
        this.initializeDateTimeFormat(this.dateTimeFormat);
        this.configureCompactMode(this.useCompactMode);
        super.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doLayout(ILoggingEvent event) {
        LinkedHashMap<String, Object> logData = this.getMapFromPool();

        logData.clear(); // Make sure the map is empty before use
        logData.put("timestamp", this.getTimestamp(event));
        logData.put("logger", event.getLoggerName());
        logData.put("level", event.getLevel().toString());
        this.addMessage(event, logData);
        logData.put("thread", event.getThreadName());
        logData.put("context", event.getLoggerContextVO().getName());
        this.addMDC(event, logData);

        try {
            String logDataJson = OBJECT_MAPPER.writeValueAsString(logData);
            return logDataJson + LINE_BREAK;
        } catch (JsonProcessingException exception) {
            super.addError(ERROR_SERIALIZATION_MESSAGE, exception);
            return logData.toString();
        } finally {
            logData.clear();                // Clear the map after use to avoid memory leaks.
            this.returnMapToPool(logData);  // Return the map to the pool.
        }
    }

    private void initializeZoneId(final String timezone) {
        if (Objects.isNull(timezone) || timezone.isBlank()) {
            this.zoneId = ZoneId.of(ZoneId.systemDefault().getId());
        } else {
            try {
                this.zoneId = ZoneId.of(timezone);
            } catch (DateTimeException exception) {
                super.addError("Invalid timezone: " + timezone + ". Using defaults.");
                this.zoneId = ZoneId.of(ZoneId.systemDefault().getId());
            }
        }
    }

    private void initializeDateTimeFormat(final String dateTimeFormat) {
        if (Objects.isNull(dateTimeFormat) || dateTimeFormat.isBlank()) {
            this.dateTimeFormatter = DateTimeUtils.getDateTimeFormatterUsingISO8601();
        } else {
            try {
                this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
            } catch (IllegalArgumentException exception) {
                super.addError("Invalid date and time format: " + dateTimeFormat + ". Using defaults.");
                this.dateTimeFormatter = DateTimeUtils.getDateTimeFormatterUsingISO8601();
            }
        }
    }

    private void configureCompactMode(boolean useCompactMode) {
        if (useCompactMode) {
            OBJECT_MAPPER.disable(SerializationFeature.INDENT_OUTPUT);
        } else {
            OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        }
    }

    private Object getTimestamp(final ILoggingEvent loggingEvent) {
        if (this.useFormattedTimestamps) {
            Instant instant = Instant.ofEpochMilli(loggingEvent.getTimeStamp());
            ZonedDateTime zonedDateTime = instant.atZone(this.zoneId);
            return zonedDateTime.format(this.dateTimeFormatter);

        } else {
            return loggingEvent.getTimeStamp();
        }
    }

    private void addMessage(final ILoggingEvent loggingEvent, final Map<String, Object> logDataMap) {
        IThrowableProxy throwableProxy = loggingEvent.getThrowableProxy();
        if (Objects.isNull(throwableProxy)) {
            logDataMap.put("message", loggingEvent.getFormattedMessage());
        } else {
            Map<String, Object> messageMap = new LinkedHashMap<>();
            messageMap.put("message", loggingEvent.getFormattedMessage());
            messageMap.put("detail",  throwableProxy.getMessage());
            logDataMap.put("error",   messageMap);
        }
    }

    private void addMDC(final ILoggingEvent event, final Map<String, Object> logDataMap) {
        Map<String, String> mdc = event.getMDCPropertyMap();
        if (Objects.nonNull(mdc) && !mdc.isEmpty()) {
            logDataMap.put("mdc", mdc);
        }
    }

    private LinkedHashMap<String, Object> getMapFromPool() {
        LinkedHashMap<String, Object> map = MAP_POOL.poll();
        return (Objects.isNull(map)) ? new LinkedHashMap<>() : map;
    }

    private void returnMapToPool(LinkedHashMap<String, Object> map) {
        MAP_POOL.offer(map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        super.stop();
    }

    /**
     * Retrieves the current date and time format used by the logger layout.
     *
     * @return A string representing the current date and time format.
     */
    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**
     * Sets the format for date and time used in the logging output.
     *
     * @param dateTimeFormat the desired date and time format pattern
     */
    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    /**
     * Retrieves the current timezone setting used by the logger layout.
     *
     * @return a string representing the current timezone setting.
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Sets the timezone used for formatting date and time in the log output.
     *
     * @param timezone the ID of the desired time zone (e.g., "UTC", "America/New_York")
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * Checks if the logger layout is in compact mode.
     *
     * @return true if compact mode is enabled, false otherwise.
     */
    public boolean isUseCompactMode() {
        return useCompactMode;
    }

    /**
     * Sets whether the compact mode is used for the logger layout.
     * Compact mode typically reduces the verbosity of the logging output.
     *
     * @param useCompactMode true to enable compact mode, false to disable it
     */
    public void setUseCompactMode(boolean useCompactMode) {
        this.useCompactMode = useCompactMode;
    }

    /**
     * Checks if the logger layout uses formatted timestamps.
     *
     * @return true if formatted timestamps are enabled, false otherwise.
     */
    public boolean isUseFormattedTimestamps() {
        return useFormattedTimestamps;
    }

    /**
     * Sets whether formatted timestamps should be used in the logger layout.
     *
     * @param useFormattedTimestamps true to enable formatted timestamps, false to use raw timestamps
     */
    public void setUseFormattedTimestamps(boolean useFormattedTimestamps) {
        this.useFormattedTimestamps = useFormattedTimestamps;
    }
}
