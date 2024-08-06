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
        this.logger.debug(message);
    }

    /**
     * Outputs a debug log message with a message and detail.
     *
     * @param message The log message.
     * @param detail  The detail object to include in the log message.
     */
    public void debug(String message, Object detail) {
        this.logger.debug(EMPTY_MESSAGE, createMessageMap(message, detail));
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
     * Logs an informational message with a detail object.
     *
     * @param message The message to be logged.
     * @param detail  The detail object to be included in the log.
     */
    public void info(String message, Object detail) {
        this.logger.info(EMPTY_MESSAGE, createMessageMap(message, detail));
    }

    /**
     * Logs an error message with the provided message and detail.
     *
     * @param message The error message.
     * @param detail The error detail.
     */
    public void error(String message, Object detail) {
        this.logger.error(EMPTY_MESSAGE, createMessageMap(message, detail));
    }

    private static Map<String, Object> createMessageMap(String text, Object detail) {
        Map<String, Object> message = new LinkedHashMap<>();
        message.put("detail", text);
        message.put("payload", detail);
        return message;
    }
}
