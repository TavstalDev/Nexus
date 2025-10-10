package io.github.tavstaldev.nexus.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class AntiCrashHandlerConfig {
    @Comment("Should the anti-crash handler be enabled?")
    private boolean enabled;
    @Comment("Number of commands a user can send in the time defined below before being kicked.")
    private int commandLimit;
    @Comment("Message to be sent to the user when they are kicked for sending too many commands.")
    private String message;
    @Comment("Time in milliseconds after which the anti-crash handler will clear the connection queue.")
    private int clearTime;

    public AntiCrashHandlerConfig() {
        enabled = true;
        commandLimit = 10;
        message = "Túl sok üzenetet küldesz egyszerre!";
        clearTime = 1000;
    }

    public AntiCrashHandlerConfig(boolean enabled, int commandLimit, String message, int clearTime) {
        this.enabled = enabled;
        this.commandLimit = commandLimit;
        this.message = message;
        this.clearTime = clearTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getCommandLimit() {
        return commandLimit;
    }

    public String getMessage() {
        return message;
    }

    public int getClearTime() {
        return clearTime;
    }
}
