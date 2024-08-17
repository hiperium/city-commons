package hiperium.cities.commons.loggers;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class defines the layout for logging events in the Hiperium format.
 * It extends the LayoutBase class from the logback library.
 * The layout converts the logging event into a JSON string representation.
 */
public class HiperiumLoggerLayout extends LayoutBase<ILoggingEvent> {

    private static final String LINE_BREAK = "\n";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd' 'HH:mm:ss' 'XXX";
    private static final String ERROR_SERIALIZATION_MESSAGE = "Couldn't serialize a message map: ";

    // Object pool for LinkedHashMap
    private static final ConcurrentLinkedQueue<LinkedHashMap<String, Object>> MAP_POOL = new ConcurrentLinkedQueue<>();

    private ZoneId zoneId;
    private DateTimeFormatter dateTimeFormatter;

    @Setter
    @Getter
    private String timeZoneId;
    @Setter
    @Getter
    private String dateTimeFormat;
    @Setter
    @Getter
    private boolean prettyPrint = false;
    @Setter
    @Getter
    private boolean numericTimestamps = true;

    /**
     * HiperiumLoggerLayout is a custom layout used in the HiperiumLogger appender for the Logback framework.
     * This layout formats log events into a JSON string, with additional fields and customization options.
     */
    public HiperiumLoggerLayout() {
        // Default constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        this.configureObjectMapper();
        this.initializeDateTimeSettings();
        super.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doLayout(ILoggingEvent event) {
        LinkedHashMap<String, Object> logData = this.getMapFromPool();

        logData.clear(); // Make sure the map is empty before use
        logData.put("timestamp", this.formatTimestamp(event));
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
            logData.clear(); // Clear the map after use to avoid memory leaks.
            this.returnMapToPool(logData); // Return the map to the pool.
        }
    }

    private LinkedHashMap<String, Object> getMapFromPool() {
        LinkedHashMap<String, Object> map = MAP_POOL.poll();
        return (map == null) ? new LinkedHashMap<>() : map;
    }

    private Object formatTimestamp(final ILoggingEvent loggingEvent) {
        if (this.numericTimestamps) {
            return loggingEvent.getTimeStamp();
        } else {
            Instant instant = Instant.ofEpochMilli(loggingEvent.getTimeStamp());
            ZonedDateTime zonedDateTime = instant.atZone(this.zoneId);
            return zonedDateTime.format(this.dateTimeFormatter);
        }
    }

    private void addMessage(final ILoggingEvent loggingEvent, final Map<String, Object> logDataMap) {
        Object[] argumentArray = loggingEvent.getArgumentArray();
        if (argumentArray instanceof Object[] arr && arr[0] instanceof Map<?, ?>) {
            logDataMap.put("message", arr[0]);
        } else {
            logDataMap.put("message", loggingEvent.getFormattedMessage());
        }
    }

    private void addMDC(final ILoggingEvent event, final Map<String, Object> logDataMap) {
        Map<String, String> mdc = event.getMDCPropertyMap();
        if (mdc != null && !mdc.isEmpty()) {
            logDataMap.put("mdc", mdc);
        }
    }

    private void returnMapToPool(LinkedHashMap<String, Object> map) {
        MAP_POOL.offer(map);
    }

    private void configureObjectMapper() {
        if (this.prettyPrint) {
            OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        } else {
            OBJECT_MAPPER.disable(SerializationFeature.INDENT_OUTPUT);
        }
    }

    private void initializeDateTimeSettings() {
        if (this.timeZoneId == null) {
            this.timeZoneId = ZoneId.systemDefault().getId();
        }
        if (this.dateTimeFormat == null) {
            this.dateTimeFormat = DEFAULT_TIMESTAMP_FORMAT;
        }
        this.zoneId = ZoneId.of(this.timeZoneId);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(this.dateTimeFormat);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        super.stop();
    }

}
