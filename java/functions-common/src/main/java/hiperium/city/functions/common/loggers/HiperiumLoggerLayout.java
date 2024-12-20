package hiperium.city.functions.common.loggers;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hiperium.city.functions.common.utils.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
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
}
