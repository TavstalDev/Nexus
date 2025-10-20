package io.github.tavstaldev.nexus.logger;

import org.jetbrains.annotations.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * The PluginLogger class provides a utility for logging messages with customizable
 * module names and ANSI color codes. It supports different log levels such as INFO,
 * WARN, ERROR, DEBUG, and OK (custom level).
 */
public class PluginLogger {
    // ANSI color codes for formatting log messages.
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // The underlying SLF4J logger instance.
    private final Logger _logger = LoggerFactory.getLogger("nexusProxy");

    // The module name associated with this logger instance.
    private final String _module;

    /**
     * Constructs a PluginLogger without a module name.
     */
    public PluginLogger() {
        _module = null;
    }

    /**
     * Constructs a PluginLogger with the specified module name.
     *
     * @param module The name of the module to associate with this logger.
     */
    public PluginLogger(String module) {
        _module = module;
    }

    /**
     * Creates a new PluginLogger instance with the specified module name.
     *
     * @param module The name of the module to associate with the new logger.
     * @return A new PluginLogger instance with the specified module name.
     */
    public PluginLogger withModule(String module) {
        return new PluginLogger(module);
    }

    /**
     * Creates a new PluginLogger instance with the module name derived from the
     * simple name of the specified class.
     *
     * @param module The class whose simple name will be used as the module name.
     * @return A new PluginLogger instance with the derived module name.
     */
    public PluginLogger withModule(@NotNull Class<?> module) {
        return new PluginLogger(module.getSimpleName());
    }

    /**
     * Logs a message at the specified log level with the given text and color.
     *
     * @param level The log level (e.g., INFO, WARN, ERROR, DEBUG).
     * @param text  The message text to log.
     * @param color The ANSI color code to use for formatting the message.
     */
    private void logRich(@NotNull Level level, @NotNull String text, @NotNull String color) {
        String moduleText = "";
        if (_module != null)
            moduleText = String.format("%s -> ", _module);

        _logger.atLevel(level).log(String.format("%s%s%s\u001B[0m", color, moduleText, text));
    }

    /**
     * Converts the given object to a string representation. If the object is an
     * exception, its message is returned. If it is a string, it is returned as-is.
     * Otherwise, the object's toString() method is used.
     *
     * @param text The object to convert to a string.
     * @return The string representation of the object.
     */
    private @NotNull String getString(@NotNull Object text) {
        if (text instanceof Exception ex) {
            return ex.getMessage();
        }
        if (text instanceof String str) {
            return str;
        }
        return text.toString();
    }

    /**
     * Logs an informational message with cyan color.
     *
     * @param text The message to log.
     */
    public void info(@NotNull Object text) {
        logRich(Level.INFO, getString(text), ANSI_CYAN);
    }

    /**
     * Logs a success message with green color.
     *
     * @param text The message to log.
     */
    public void ok(@NotNull Object text) {
        logRich(Level.INFO, getString(text), ANSI_GREEN);
    }

    /**
     * Logs a warning message with yellow color.
     *
     * @param text The message to log.
     */
    public void warn(@NotNull Object text) {
        logRich(Level.WARN, getString(text), ANSI_YELLOW);
    }

    /**
     * Logs an error message with red color.
     *
     * @param text The message to log.
     */
    public void error(@NotNull Object text) {
        logRich(Level.ERROR, getString(text), ANSI_RED);
    }

    /**
     * Logs a debug message with purple color.
     *
     * @param text The message to log.
     */
    public void debug(@NotNull Object text) {
        logRich(Level.DEBUG, getString(text), ANSI_PURPLE);
    }
}