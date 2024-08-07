package hiperium.cities.commons.loggers;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class defines the layout for logging events in the Hiperium format.
 * It extends the LayoutBase class from the logback library.
 * The layout converts the logging event into a JSON string representation.
 */
public class HiperiumLoggerLayout extends LayoutBase<ILoggingEvent> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'XXX";

    private ZoneId zoneId;
    private DateTimeFormatter dateTimeFormatter;

    private boolean includeMDC = true;
    private boolean prettyPrint = false;
    private boolean addLineSeparator = false;
    private boolean includeThreadName = true;
    private boolean includeContextName = true;

    private String timeZoneId;
    private String dateTimeFormat;

    /**
     * Represents a custom layout for log events in the HiperiumLogger.
     * This layout converts log events into JSON format.
     */
    public HiperiumLoggerLayout() {
        // Default constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        if (this.prettyPrint) {
            OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        } else {
            OBJECT_MAPPER.disable(SerializationFeature.INDENT_OUTPUT);
        }
        if (this.timeZoneId == null) {
            this.timeZoneId = ZoneId.systemDefault().getId();
        }
        if (this.dateTimeFormat == null) {
            this.dateTimeFormat = DEFAULT_TIMESTAMP_FORMAT;
        }
        this.zoneId = ZoneId.of(this.timeZoneId);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(this.dateTimeFormat);
        super.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doLayout(ILoggingEvent event) {
        Map<String, Object> logDataMap = new LinkedHashMap<>();

        logDataMap.put("timestamp", this.formatTimestamp(event));
        logDataMap.put("logger", event.getLoggerName());
        logDataMap.put("level", event.getLevel().toString());
        this.addMessage(event, logDataMap);
        this.addThread(event, logDataMap);
        this.addContext(event, logDataMap);
        this.addMDC(event, logDataMap);

        try {
            String logDataJson = OBJECT_MAPPER.writeValueAsString(logDataMap);
            return logDataJson + (this.addLineSeparator ? System.lineSeparator() : "");
        } catch (JsonProcessingException exception) {
            super.addError("Couldn't serialize a message map: ", exception);
            return logDataMap.toString();
        }
    }

    private String formatTimestamp(ILoggingEvent loggingEvent) {
        Instant instant = Instant.ofEpochMilli(loggingEvent.getTimeStamp());
        ZonedDateTime zonedDateTime = instant.atZone(this.zoneId);
        return zonedDateTime.format(this.dateTimeFormatter);
    }

    private void addThread(ILoggingEvent event, Map<String, Object> logDataMap) {
        if (this.includeThreadName) {
            logDataMap.put("thread", event.getThreadName());
        }
    }

    private void addMessage(ILoggingEvent loggingEvent, Map<String, Object> logDataMap) {
        Object[] argumentArray = loggingEvent.getArgumentArray();
        if (argumentArray != null && argumentArray[0] instanceof Map) {
            logDataMap.put("message", argumentArray[0]);
        } else {
            logDataMap.put("message", loggingEvent.getFormattedMessage());
        }
    }

    private void addContext(ILoggingEvent event, Map<String, Object> logDataMap) {
        if (this.includeContextName) {
            logDataMap.put("context", event.getLoggerContextVO().getName());
        }
    }

    private void addMDC(ILoggingEvent event, Map<String, Object> logDataMap) {
        if (this.includeMDC) {
            Map<String, String> mdc = event.getMDCPropertyMap();
            if (mdc != null && !mdc.isEmpty()) {
                logDataMap.put("mdc", mdc);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        super.stop();
    }

    /**
     * Checks if the log messages should be pretty-printed.
     *
     * @return true if the log messages should be pretty-printed, false otherwise.
     */
    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    /**
     * Sets the flag indicating whether the resulting log output should be pretty-printed.
     *
     * @param prettyPrint true if the log output should be pretty-printed, false otherwise
     */
    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    /**
     * Returns a boolean indicating whether the thread name should be included in the log message.
     * If true, the thread name will be included; if false, the thread name will be excluded.
     *
     * @return true if the thread name should be included, false otherwise
     */
    public boolean isIncludeThreadName() {
        return includeThreadName;
    }

    /**
     * Sets whether to include the thread name in the log output.
     *
     * @param includeThreadName true if the thread name should be included, false otherwise
     */
    public void setIncludeThreadName(boolean includeThreadName) {
        this.includeThreadName = includeThreadName;
    }

    /**
     * Determines whether the context name should be included in the log message.
     *
     * @return true if the context name should be included, false otherwise.
     */
    public boolean isIncludeContextName() {
        return includeContextName;
    }

    /**
     * Sets the flag to include the context name in the logging output.
     *
     * @param includeContextName true if the context name should be included, false otherwise
     */
    public void setIncludeContextName(boolean includeContextName) {
        this.includeContextName = includeContextName;
    }

    /**
     * Gets the time zone ID used by this logger layout.
     *
     * @return The time zone ID.
     */
    public String getTimeZoneId() {
        return timeZoneId;
    }

    /**
     * Sets the time zone ID for the logger layout.
     * This time zone ID is used to format the timestamp in the log records.
     *
     * @param timeZoneId the time zone ID to set
     */
    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    /**
     * Retrieves the current date and time format used by the logger layout.
     *
     * @return The current date and time format.
     */
    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**
     * Sets the date and time format for the logger layout.
     *
     * @param dateTimeFormat the new date and time format
     */
    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    /**
     * Determines whether the MDC (Mapped Diagnostic Context) should be included in the log output.
     *
     * @return true if the MDC should be included, false otherwise
     */
    public boolean isIncludeMDC() {
        return includeMDC;
    }

    /**
     * Sets the flag indicating whether to include MDC (Mapped Diagnostic Context) in the log output.
     * If set to true, MDC will be included in the log output. If set to false, MDC will be excluded.
     *
     * @param includeMDC true to include MDC, false to exclude MDC
     */
    public void setIncludeMDC(boolean includeMDC) {
        this.includeMDC = includeMDC;
    }

    /**
     * Determines whether to add a line separator in the log message.
     *
     * @return true if a line separator should be added, false otherwise
     */
    public boolean isAddLineSeparator() {
        return addLineSeparator;
    }

    /**
     * Sets the flag indicating whether a line separator should be added to the log message.
     * If true, a line separator will be added; if false, no line separator will be added.
     *
     * @param addLineSeparator true to add a line separator, false otherwise.
     */
    public void setAddLineSeparator(boolean addLineSeparator) {
        this.addLineSeparator = addLineSeparator;
    }
}
