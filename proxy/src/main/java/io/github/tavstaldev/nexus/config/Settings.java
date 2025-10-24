package io.github.tavstaldev.nexus.config;

import io.github.tavstaldev.nexus.config.main.*;
import io.github.tavstaldev.nexus.config.main.pinger.ServerFavIconConfig;
import io.github.tavstaldev.nexus.config.main.pinger.ServerMotdConfig;
import io.github.tavstaldev.nexus.config.main.pinger.ServerSpooferConfig;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Set;

@ConfigSerializable
public class Settings {
    private final boolean debug;
    private final String prefix;

    private final Set<String> lobbyServers;

    private final AntiCrashHandlerConfig antiCrashHandler;

    private final AlertConfig alert;

    private final PlayerReportConfig playerReport;

    private final HelpopConfig helpop;

    private final Set<CustomChatConfig> customChats;

    private final ServerPingerConfig serverPinger;

    public Settings() {
        this.debug = false;
        this.prefix = "&3Nexus &8»&r";
        this.lobbyServers = Set.of("lobby", "lobby2");
        this.antiCrashHandler = new AntiCrashHandlerConfig();
        this.alert = new AlertConfig(
                true,
                Set.of("alert", "ac", "announce"),
                "nexus.alert.use",
                "&c<hover:show_text:'&cNetwork Alert'>Alert</hover> &8» &f%message%"
        );
        this.playerReport = new PlayerReportConfig(
                true,
                true,
                Set.of("report", "preport", "playerreport"),
                3,
                "&4&l[<hover:show_text:'&cPlayer Report'>Report</hover>]&r &2%player% &areported &2%reported% &8» &7%reason%",
                "nexus.report.use",
                "nexus.report.notify",
                "nexus.report.bypass",
                Set.of("Griefing", "Cheating", "Harassment", "Spamming", "Other")
        );
        this.helpop = new HelpopConfig(
                true,
                Set.of("hc", "seekhelp", "sos", "staff", "admin"),
                "&e&l[HelpOp]&r &6%player% &7(%server%) &8» &f%message%",
                30,
                "nexus.helpop.use",
                "nexus.helpop.see"
        );
        this.customChats = Set.of(
                new CustomChatConfig(
                        true,
                        "staffchat",
                        true,
                        Set.of("staffchat", "sc", "schat"),
                        true,
                        "nexus.staffchat",
                        "<hover:show_text:'&cStaff Chat'>&c&l[S]</hover>&r &7%player%&8: &f%message%"
                ),
                new CustomChatConfig(
                        false,
                        "adminchat",
                        false,
                        Set.of("adminchat", "ac", "achat"),
                        true,
                        "nexus.adminchat",
                        "<hover:show_text:'&4Admin Chat'>&4&l[A]</hover> &7%player%&8: &f%message%"
                ));
        this.serverPinger = new ServerPingerConfig(
                true,
                List.of(new ServerMotdConfig(new ServerFavIconConfig(), "&b&lExample &8[&31.21.х&8]", "&8> &7Welcome to the &bExample&7 network!")),
                new ServerSpooferConfig(),
                new ServerFavIconConfig()
        );
    }

    public Settings(boolean debug, String prefix, Set<String> lobbyServers, AntiCrashHandlerConfig antiCrashHandler, AlertConfig alert, PlayerReportConfig playerReport, HelpopConfig helpop, Set<CustomChatConfig> customChats, ServerPingerConfig serverPinger) {
        this.debug = debug;
        this.prefix = prefix;
        this.lobbyServers = lobbyServers;
        this.antiCrashHandler = antiCrashHandler;
        this.alert = alert;
        this.playerReport = playerReport;
        this.helpop = helpop;
        this.customChats = customChats;
        this.serverPinger = serverPinger;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getPrefix() {
        return prefix;
    }

    public Set<String> getLobbyServers() {
        return lobbyServers;
    }

    public AntiCrashHandlerConfig getAntiCrashHandler() {
        return antiCrashHandler;
    }

    public AlertConfig getAlert() {
        return alert;
    }

    public PlayerReportConfig getPlayerReport() {
        return playerReport;
    }

    public HelpopConfig getHelpop() {
        return helpop;
    }

    public Set<CustomChatConfig> getCustomChats() {
        return customChats;
    }

    public @Nullable CustomChatConfig getCustomChatByName(String name) {
        for (CustomChatConfig chat : customChats) {
            if (chat.getChatName().equalsIgnoreCase(name)) {
                return chat;
            }
        }
        return null;
    }

    public ServerPingerConfig getServerPinger() {
        return serverPinger;
    }
}
