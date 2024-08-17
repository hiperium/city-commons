package hiperium.cities.commons.loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The HiperiumLogger class is a logger utility that provides methods to log debug and informational messages.
 * It uses the SLF4J logging framework to perform the actual logging.
 */
public class HiperiumLogger {

    private static final String EMPTY_MESSAGE = "{}";

    private final Logger logger;

    /**
     * The HiperiumLogger class is a logger utility that provides methods to log debug and informational messages.
     * It uses the SLF4J logging framework to perform the actual logging.
     *
     * @param clazz The class for which the logger is created.
     */
    public HiperiumLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * Sends a debug log message with the provided message.
     *
     * @param message the debug log message
     */
    public void debug(String message) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(message);
        }
    }

    /**
     * Outputs a debug log message with a message and object.
     *
     * @param message The log message.
     * @param object  the object to be included in the log message.
     */
    public void debug(String message, Object object) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(EMPTY_MESSAGE, createRegularMessage(message, object));
        }
    }

    /**
     * Logs an informational message.
     *
     * @param message The message to log.
     */
    public void info(String message) {
        this.logger.info(message);
    }

    /**
     * Logs an informational message with a object object.
     *
     * @param message The message to be logged.
     * @param object  the object to be included in the log message.
     */
    public void info(String message, Object object) {
        this.logger.info(EMPTY_MESSAGE, createRegularMessage(message, object));
    }

    /**
     * Sends a warning log message with the provided message and object.
     * This method is used to log warning messages using the underlying logger implementation.
     *
     * @param message the warning log message
     * @param object the object to be included in the log message.
     */
    public void warn(String message, Object object) {
        this.logger.warn(EMPTY_MESSAGE, createRegularMessage(message, object));
    }

    /**
     * Logs an error message with the provided message and detail.
     *
     * @param message The error message.
     * @param object the object to be included in the log message.
     */
    public void error(String message, Object object) {
        this.logger.error(EMPTY_MESSAGE, createRegularMessage(message, object));
    }

    /**
     * Logs an error message with a specified message, detail, and object.
     *
     * @param message The error message.
     * @param detail  The error detail.
     * @param object the object to be included in the log message.
     */
    public void error(String message, String detail, Object object) {
        this.logger.error(EMPTY_MESSAGE, createDetailedMessage(message, detail, object));
    }

    private static Map<String, Object> createRegularMessage(String message, Object object) {
        Map<String, Object> messageMap = new LinkedHashMap<>();
        messageMap.put("text", message);
        messageMap.put("value", object);
        return messageMap;
    }

    private static Map<String, Object> createDetailedMessage(String message, String detail, Object object) {
        Map<String, Object> messageMap = new LinkedHashMap<>();
        messageMap.put("text", message);
        messageMap.put("detail", detail);
        messageMap.put("value", object);
        return messageMap;
    }
}
