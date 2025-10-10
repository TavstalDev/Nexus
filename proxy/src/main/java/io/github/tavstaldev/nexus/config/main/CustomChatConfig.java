package io.github.tavstaldev.nexus.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Set;

@ConfigSerializable
public class CustomChatConfig {
    @Comment("Should this custom chat be enabled?")
    private boolean enabled;
    @Comment("Name of the chat, used for identification purposes.")
    private String chatName;
    @Comment("Should players be allowed to toggle this chat on or off?")
    private boolean allowToggle;
    @Comment("Commands that can be used to send messages to this chat.")
    private Set<String> commands;
    @Comment("Should permission be required to use this chat?")
    private boolean requirePermission;
    @Comment("Permission node required to use this chat, if permission is required.")
    private String permission;
    @Comment("Format of the chat messages. Use {player} for the player's name and {message} for the message.")
    private String format;

    public CustomChatConfig() {
        this.enabled = false;
        this.chatName = "customchat";
        this.allowToggle = true;
        this.commands = Set.of("customchat", "cc");
        this.requirePermission = false;
        this.permission = "nexus.customchat.use";
        this.format = "&8[&bCustomChat}&8] &f{player} &8» &f{message}";
    }

    public CustomChatConfig(boolean enabled, String chatName, boolean allowToggle, Set<String> commands, boolean requirePermission, String permission, String format) {
        this.enabled = enabled;
        this.chatName = chatName;
        this.allowToggle = allowToggle;
        this.commands = commands;
        this.requirePermission = requirePermission;
        this.permission = permission;
        this.format = format;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getChatName() {
        return chatName;
    }

    public boolean isAllowToggle() {
        return allowToggle;
    }

    public Set<String> getCommands() {
        return commands;
    }

    public boolean isRequirePermission() {
        return requirePermission;
    }

    public String getPermission() {
        return permission;
    }

    public String getFormat() {
        return format;
    }
}