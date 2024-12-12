package hiperium.city.functions.common.loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The HiperiumLogger class is a logger utility that provides methods to log debug and informational messages.
 * It uses the SLF4J logging framework to perform the actual logging.
 */
public class HiperiumLogger {

    private final Logger logger;

    /**
     * Constructs a new HiperiumLogger object for the specified class.
     *
     * @param clazz The class for which the logger is created.
     */
    public HiperiumLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * Logs a debug description, including any specified arguments, if debug logging is enabled.
     *
     * @param message The description to log, which can contain placeholders for the arguments.
     * @param arguments The arguments to be included in the log description. These will replace placeholders in the description.
     */
    public void debug(String message, Object... arguments) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(message, arguments);
        }
    }

    /**
     * Logs an informational description using the underlying SLF4J Logger, if info level logging is enabled.
     * The description can be formatted using a template where arguments can be substituted in place of placeholders.
     *
     * @param message The description template to log. It may contain placeholders like '{}' for the arguments.
     * @param arguments An array of objects that will replace placeholders in the description template.
     */
    public void info(String message, Object... arguments) {
        if (this.logger.isInfoEnabled()) {
            this.logger.info(message, arguments);
        }
    }

    /**
     * Logs a warning description using the configured logger if warning level logging is enabled.
     * The description can include placeholders for arguments, which will be replaced by the provided values.
     *
     * @param message   The warning description to be logged, which may contain placeholders for argument substitution.
     * @param arguments Optional arguments used to replace placeholders within the description.
     */
    public void warn(String message, Object... arguments) {
        if (this.logger.isWarnEnabled()) {
            this.logger.warn(message, arguments);
        }
    }

    /**
     * Logs an error description with optional arguments if error logging is enabled.
     *
     * @param message   the error description to log
     * @param arguments optional arguments to include in the log description
     */
    public void error(String message, Object... arguments) {
        if (this.logger.isErrorEnabled()) {
            this.logger.error(message, arguments);
        }
    }

    /**
     * Logs an error description along with a throwable using the configured logger
     * if error level logging is enabled.
     *
     * @param message The error description to log.
     * @param throwable The throwable associated with the error, providing stack trace information.
     */
    public void error(String message, Throwable throwable) {
        if (this.logger.isErrorEnabled()) {
            this.logger.error(message, throwable);
        }
    }
}
