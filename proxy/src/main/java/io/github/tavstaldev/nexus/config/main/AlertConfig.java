package io.github.tavstaldev.nexus.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Set;

@ConfigSerializable
public class AlertConfig {
    @Comment("Should alerts be enabled?")
    private boolean enabled;
    @Comment("Commands to be used to send alerts.")
    private Set<String> aliases;
    @Comment("Permission node required to send alerts.")
    private String permission;
    @Comment("Format of the alert message. Use {player} for the player's name and {message} for the alert message.")
    private String format;

    public AlertConfig() {
        enabled = true;
        aliases = Set.of("alert", "ac", "announce");
        permission = "nexus.alert.use";
        format = "&8(&c<hover:show_text:'&cAlert'>\uD83D\uDCE3</hover>&8) &8» &f{message}";
    }

    public AlertConfig(boolean enabled, Set<String> aliases, String permission, String format) {
        this.enabled = enabled;
        this.aliases = aliases;
        this.permission = permission;
        this.format = format;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public String getPermission() {
        return permission;
    }

    public String getFormat() {
        return format;
    }
}
