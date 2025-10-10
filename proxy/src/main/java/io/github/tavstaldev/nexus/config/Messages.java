package io.github.tavstaldev.nexus.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class Messages {
    //#region General
    private String generalNoPermission = "%prefix% &cYou do not have permission to do this.";
    private String generalPlayerNotFound = "%prefix% &cPlayer not found.";
    private String generalConsoleOnly = "%prefix% &cThis command can only be run by the console.";
    private String generalPlayerOnly = "%prefix% &cThis command can only be run by a player.";
    private String generalInvalidSyntax = "%prefix% &cInvalid command syntax. Use &e%syntax%&c.";
    private String generalErrorOccurred = "%prefix% &cAn error occurred while executing the command.";
    private String generalReloadComplete = "%prefix% &aConfiguration reloaded.";

    public String getGeneralNoPermission() {
        return generalNoPermission;
    }

    public String getGeneralPlayerNotFound() {
        return generalPlayerNotFound;
    }

    public String getGeneralConsoleOnly() {
        return generalConsoleOnly;
    }

    public String getGeneralPlayerOnly() {
        return generalPlayerOnly;
    }

    public String getGeneralInvalidSyntax() {
        return generalInvalidSyntax;
    }

    public String getGeneralErrorOccurred() {
        return generalErrorOccurred;
    }

    public String getGeneralReloadComplete() {
        return generalReloadComplete;
    }
    //#endregion

    //#region Maintenance
    private String maintenanceAlreadyEnabled = "%prefix% &cMaintenance mode is already enabled.";
    private String maintenanceAlreadyDisabled = "%prefix% &cMaintenance mode is already disabled.";
    private String maintenanceEnabled = "%prefix% &aMaintenance mode has been enabled.";
    private String maintenanceDisabled = "%prefix% &aMaintenance mode has been disabled.";
    private List<String> maintenanceKickMessage = List.of(
            "&c&lMaintenance Mode",
            "",
            "&7The server is currently under maintenance.",
            "&7Please try again later.",
            ""
    );

    public String getMaintenanceAlreadyEnabled() {
        return maintenanceAlreadyEnabled;
    }

    public String getMaintenanceAlreadyDisabled() {
        return maintenanceAlreadyDisabled;
    }

    public String getMaintenanceEnabled() {
        return maintenanceEnabled;
    }

    public String getMaintenanceDisabled() {
        return maintenanceDisabled;
    }

    public List<String> getMaintenanceKickMessage() {
        return maintenanceKickMessage;
    }
    //#endregion

    //#region Staff Messages
    private String staffJoinMessage = "<hover:show_text:'&cStaff Chat'>&c&l[S]</hover>&r &6%player% &7csatlakozott.";
    private String staffLeaveMessage = "<hover:show_text:'&cStaff Chat'>&c&l[S]</hover>&r &6%player% &7lecsatlakozott.";
    private String staffSwitch = "<hover:show_text:'&cStaff Chat'>&c&l[S]</hover>&r &6%player% &7szervert váltott &8(&e%from% &7-> &e%to%&8)";

    public String getStaffJoinMessage() {
        return staffJoinMessage;
    }

    public String getStaffLeaveMessage() {
        return staffLeaveMessage;
    }

    public String getStaffSwitch() {
        return staffSwitch;
    }
    //#endregion

    //#region Custom Chat
    private String customChatNoPermission = "%prefix% &cYou do not have permission to use this chat.";
    private String customChatToggleOn = "%prefix% &aYou have toggled &e%chat% &achat on.";
    private String customChatToggleOff = "%prefix% &aYou have toggled &e%chat% &cchat off.";
    private String customChatSwitchedTo = "%prefix% &aYou have switched to &e%chat% &achat.";

    public String getCustomChatNoPermission() {
        return customChatNoPermission;
    }

    public String getCustomChatToggleOn() {
        return customChatToggleOn;
    }

    public String getCustomChatToggleOff() {
        return customChatToggleOff;
    }

    public String getCustomChatSwitchedTo() {
        return customChatSwitchedTo;
    }
    //#endregion
}