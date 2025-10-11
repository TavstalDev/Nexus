package io.github.tavstaldev.nexus.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
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
    private String generalCommandSyntax = "%prefix% &cUsage: &7/%command% %syntax%";
    private String generalFeatureDisabled = "%prefix% &cThis feature is disabled.";
    private String generalCooldown = "%prefix% &cYou must wait &e%time% &cseconds before using this command again.";

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
    public String getGeneralCommandSyntax() {
        return generalCommandSyntax;
    }

    public String getGeneralFeatureDisabled() {
        return generalFeatureDisabled;
    }

    public String getGeneralCooldown() {
        return generalCooldown;
    }
    //#endregion

    //#region Custom Chat
    private String customChatNoPermission = "%prefix% &cYou do not have permission to use this chat.";
    private String customChatToggleOn = "%prefix% &aYou have toggled &e%chat% &achat on.";
    private String customChatToggleOff = "%prefix% &aYou have toggled &e%chat% &cchat off.";
    private String customChatSwitchedTo = "%prefix% &aYou have switched to &e%chat% &achat.";
    private String customChatToggleDisabled = "%prefix% &cToggling is disabled for this chat.";

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

    public String getCustomChatToggleDisabled() {
        return customChatToggleDisabled;
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

    //#region Staff List
    private String staffListHeader = "&8&m                               ";
    private String staffListFooter = "&8&m                               ";
    private String staffListNoStaff = "&7There are no staff members online.";
    private String staffListFormat = "&e%player% &7- &6%server%";

    public String getStaffListHeader() {
        return staffListHeader;
    }

    public String getStaffListFooter() {
        return staffListFooter;
    }

    public String getStaffListNoStaff() {
        return staffListNoStaff;
    }

    public String getStaffListFormat() {
        return staffListFormat;
    }
    //#endregion

    //#region Nexus Information
    private String nexusInfoHeader = "&8&m                               ";
    private String nexusInfoFooter = "&8&m                               ";
    private String nexusInfoContent = "&7Running &b&lNexus&r &bv%version%\n&7The core velocity management plugin of MesterMC.\n\n&7Developed by &bTavstal.";

    public  String getNexusInfoHeader() {
        return nexusInfoHeader;
    }

    public String getNexusInfoFooter() {
        return nexusInfoFooter;
    }

    public String getNexusInfoContent() {
        return nexusInfoContent;
    }
    //#endregion

    //#region Player Report
    private String playerReportSelf = "%prefix% &cYou cannot report yourself.";

    public String getPlayerReportSelf() {
        return playerReportSelf;
    }
    //#endregion
}