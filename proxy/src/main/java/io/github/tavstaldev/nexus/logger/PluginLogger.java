package io.github.tavstaldev.nexus.logger;

import org.jetbrains.annotations.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class PluginLogger {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    private final Logger _logger = LoggerFactory.getLogger("nexusProxy");
    private final String _module;

    public PluginLogger() {
        _module = null;
    }

    public PluginLogger(String module) {
        _module = module;
    }

    public PluginLogger withModule(String module) {
        return new PluginLogger(module);
    }

    public PluginLogger withModule(@NotNull Class<?> module) {
        return new PluginLogger(module.getSimpleName());
    }

    private void logRich(@NotNull Level level, @NotNull String text, @NotNull String color) {
        String moduleText = "";
        if (_module != null)
            moduleText = String.format("%s -> ", _module);

        _logger.atLevel(level).log(String.format("%s%s%s\u001B[0m", color, moduleText, text));
    }

    private @NotNull String getString(@NotNull Object text) {
        if (text instanceof Exception ex) {
            return ex.getMessage();
        }
        if (text instanceof String str) {
            return str;
        }
        return text.toString();
    }

    public void info(@NotNull Object text) {
        logRich(Level.INFO, getString(text), ANSI_CYAN);
    }

    public void ok(@NotNull Object text) {
        logRich(Level.INFO, getString(text), ANSI_GREEN);
    }

    public void warn(@NotNull Object text) {
        logRich(Level.WARN, getString(text), ANSI_YELLOW);
    }

    public void error(@NotNull Object text) {
        logRich(Level.ERROR, getString(text), ANSI_RED);
    }

    public void debug(@NotNull Object text) {
        logRich(Level.DEBUG, getString(text), ANSI_PURPLE);
    }
}