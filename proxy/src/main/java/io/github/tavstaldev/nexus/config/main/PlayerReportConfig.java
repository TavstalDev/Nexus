package io.github.tavstaldev.nexus.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Set;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
public class PlayerReportConfig {
    @Comment("Should the player report system be enabled?")
    private boolean enabled;
    @Comment("Should staff be notified of reports when they log in?")
    private boolean notifyOnLogin;
    @Comment("Commands that can be used to report players.")
    private Set<String> aliases;
    @Comment("Cooldown in seconds between reports for each player.")
    private int cooldown;
    @Comment("Format of the report message. Use {reporter} for the reporter's name, {reported} for the reported player's name, {server} for the server the reporter is on, and {reason} for the reason.")
    private String format;
    @Comment("Permission node required to use the report command.")
    private String permission;
    @Comment("Permission node required to see report messages.")
    private String notifyPermission;
    @Comment("Set of report templates that players can choose from when reporting")
    private final Set<String> reportTemplates;

    public PlayerReportConfig() {
        enabled = true;
        notifyOnLogin = true;
        aliases = Set.of("report", "rt");
        cooldown = 3;
        format = "&8(&c<hover:show_text:'&cReport'>\uD83D\uDEA8</hover>&8) &8» &f{reporter} &7reported &f{reported} &7on &f{server} &7for &f{reason}";
        permission = "nexus.report.use";
        notifyPermission = "nexus.report.notify";
        reportTemplates = Set.of("Griefing", "Cheating", "Harassment", "Spamming", "Other");
    }

    public PlayerReportConfig(boolean enabled, boolean notifyOnLogin, Set<String> aliases, int cooldown, String format, String permission, String notifyPermission, Set<String> reportTemplates) {
        this.enabled = enabled;
        this.notifyOnLogin = notifyOnLogin;
        this.aliases = aliases;
        this.cooldown = cooldown;
        this.format = format;
        this.permission = permission;
        this.notifyPermission = notifyPermission;
        this.reportTemplates = reportTemplates;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isNotifyOnLogin() {
        return notifyOnLogin;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public int getCooldown() {
        return cooldown;
    }

    public String getFormat() {
        return format;
    }

    public String getPermission() {
        return permission;
    }

    public String getNotifyPermission() {
        return notifyPermission;
    }

    public Set<String> getReportTemplates() {
        return reportTemplates;
    }
}
