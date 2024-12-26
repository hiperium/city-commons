package hiperium.city.functions.common.loggers;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hiperium.city.functions.common.utils.DateTimeUtil;

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
            this.dateTimeFormatter = DateTimeUtil.getDateTimeFormatterUsingISO8601();
        } else {
            try {
                this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
            } catch (IllegalArgumentException exception) {
                super.addError("Invalid date and time format: " + dateTimeFormat + ". Using defaults.");
                this.dateTimeFormatter = DateTimeUtil.getDateTimeFormatterUsingISO8601();
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
            if (throwableProxy.getCause() != null) {
                messageMap.put("cause", throwableProxy.getCause().getMessage());
            } else {
                messageMap.put("detail", throwableProxy.getMessage());
            }
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
     * Retrieves the current {@code ZoneId} used by the logger layout.
     *
     * @return the configured {@code ZoneId} instance.
     */
    public ZoneId getZoneId() {
        return zoneId;
    }

    /**
     * Sets the ZoneId used by this layout instance. The ZoneId determines the time zone
     * used for formatting timestamps and other time-related operations.
     *
     * @param zoneId The ZoneId to set. Must not be null. If null is passed, the method
     *               will throw a NullPointerException.
     */
    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    /**
     * Retrieves the configured {@link DateTimeFormatter} used for formatting date and time values.
     *
     * @return the {@link DateTimeFormatter} instance currently in use.
     */
    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    /**
     * Sets the DateTimeFormatter used for formatting timestamps in log events.
     *
     * @param dateTimeFormatter the DateTimeFormatter to be used for formatting timestamps.
     */
    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    /**
     * Retrieves the configured timezone for the logger layout.
     *
     * @return the configured timezone as a String.
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Sets the timezone for the logger layout. The provided timezone will be used
     * to format timestamps for log entries. If the provided timezone is invalid
     * or null, a default system timezone will be applied.
     *
     * @param timezone the timezone string to set. This should be a valid timezone
     *                 ID (e.g., "America/New_York"). If invalid, the default system
     *                 timezone will be used instead.
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * Retrieves the date and time format configured for logging.
     *
     * @return the currently configured date and time format as a String.
     */
    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**
     * Sets the date and time format used within the layout.
     *
     * @param dateTimeFormat The date and time format pattern to be used.
     *                       If the provided format is null or invalid,
     *                       a default ISO 8601 format will be applied.
     */
    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    /**
     * Checks whether the compact mode is currently enabled for the logger layout.
     *
     * @return {@code true} if compact mode is enabled, otherwise {@code false}.
     */
    public boolean isUseCompactMode() {
        return useCompactMode;
    }

    /**
     * Sets whether or not the layout should use compact mode for its output.
     * In compact mode, the output will not include additional formatting,
     * such as indentation, to keep it minimal.
     *
     * @param useCompactMode true to enable compact mode; false to disable it and include formatting like indentation.
     */
    public void setUseCompactMode(boolean useCompactMode) {
        this.useCompactMode = useCompactMode;
    }

    /**
     * Indicates whether formatted timestamps are being used in the logging output.
     *
     * @return true if formatted timestamps are enabled, otherwise false.
     */
    public boolean isUseFormattedTimestamps() {
        return useFormattedTimestamps;
    }

    /**
     * Sets whether formatted timestamps should be used in the logging output.
     * If enabled, timestamps will be formatted based on the configured date and time format.
     * If disabled, raw timestamps will be used as epoch milliseconds.
     *
     * @param useFormattedTimestamps A boolean flag indicating whether to use formatted timestamps.
     *                               {@code true} enables formatted timestamps,
     *                               {@code false} uses raw epoch milliseconds.
     */
    public void setUseFormattedTimestamps(boolean useFormattedTimestamps) {
        this.useFormattedTimestamps = useFormattedTimestamps;
    }
}
