package io.github.tavstaldev.nexus.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Set;

@ConfigSerializable
public class HelpopConfig {
    @Comment("Should the helpop system be enabled?")
    private boolean enabled;
    @Comment("Commands that can be used to send a helpop message.")
    private Set<String> aliases;
    @Comment("Format of the helpop message. Use %player% for the player's name, %server% for the server the player is on, and %message% for the message.")
    private String format;
    @Comment("Cooldown in seconds between helpop messages per player.")
    private int cooldown;
    @Comment("Permission node required to use the helpop command.")
    private String permission;
    @Comment("Permission node required to see helpop messages.")
    private String staffPermission;

    public HelpopConfig() {
        enabled = true;
        aliases = Set.of("helpop", "hc", "help");
        format = "&8(&b<hover:show_text:'&bHelpop'>\u2753</hover>&8) &7%player% &8[&7%server%&8] &8» &f%message%";
        cooldown = 30;
        permission = "nexus.helpop.use";
        staffPermission = "nexus.helpop.see";
    }

    public HelpopConfig(boolean enabled, Set<String> aliases, String format, int cooldown, String permission, String staffPermission) {
        this.enabled = enabled;
        this.aliases = aliases;
        this.format = format;
        this.cooldown = cooldown;
        this.permission = permission;
        this.staffPermission = staffPermission;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public String getFormat() {
        return format;
    }

    public int getCooldown() {
        return cooldown;
    }

    public String getPermission() {
        return permission;
    }

    public String getStaffPermission() {
        return staffPermission;
    }
}
