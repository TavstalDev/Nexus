package io.github.tavstaldev.nexus;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import io.github.tavstaldev.nexus.config.*;
import io.github.tavstaldev.nexus.events.*;
import io.github.tavstaldev.nexus.logger.PluginLogger;
import io.github.tavstaldev.nexus.managers.CommandManager;
import io.github.tavstaldev.nexus.managers.FavIconManager;
import io.github.tavstaldev.nexus.managers.LobbyServerManager;
import io.github.tavstaldev.nexus.managers.StaffManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Plugin(
    id = "nexusproxy",
    name = "nexusProxy",
    version = NexusConstants.VERSION,
    url = "https://tavstaldev.github.io",
    authors = {"Tavstal"}
)
public class Nexus {
    public static Nexus plugin;
    private final ProxyServer proxy;
    private final Path dataFolder;
    private ScheduledTask reportCleanerTask;
    private final PluginLogger pluginLogger;
    private final ConfigurationLoader configurationLoader;
    private final FavIconManager favIconManager;
    private final CommandManager commandManager;
    private final StaffManager staffManager;
    private LobbyServerManager lobbyServerManager;

    @Inject
    public Nexus(ProxyServer proxy, @DataDirectory @NotNull Path dataFolder) {
        plugin = this;
        this.proxy = proxy;
        this.dataFolder = dataFolder;
        pluginLogger = new PluginLogger();
        configurationLoader = new ConfigurationLoader();
        favIconManager = new FavIconManager();
        commandManager = new CommandManager();
        staffManager = new StaffManager();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            configurationLoader.loadAll();
            favIconManager.loadIcons();
            lobbyServerManager = new LobbyServerManager(getConfig().getLobbyServers());

            commandManager.registerCommands();
            this.registerListeners();
            final long expirationTime = Duration.ofDays(1).toMillis();
            this.reportCleanerTask = this.proxy.getScheduler().buildTask(this, () -> {
                Set<Report> reports = this.getReports();
                final long currentTime = System.currentTimeMillis();
                if (reports.removeIf(x -> currentTime - x.getTimestamp() > expirationTime)) {
                    this.getConfigurationLoader().saveReports();
                }
            }).delay(1, TimeUnit.MINUTES).repeat(60, TimeUnit.MINUTES).schedule();

            pluginLogger.ok("nexusProxy v1.0.0 has been enabled!");
        }
        catch (Exception ex) {
            pluginLogger.error("An error occurred while enabling nexusProxy:");
            pluginLogger.error(ex);
            commandManager.unregisterCommands();
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (this.reportCleanerTask != null)
            this.reportCleanerTask.cancel();
        commandManager.unregisterCommands();
        pluginLogger.ok("nexusProxy v1.0.0 has been disabled!");
    }

    private void registerListeners() {
        new CommandEventListener().register();
        new ChatEventListener().register();
        new PreConnectEventListener().register();
        new ProxyPingEventListener().register();
        new ConnectEventListener().register();
        new DisconnectEventListener().register();
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public Path getDataFolder() {
        return dataFolder;
    }

    public PluginLogger getLogger() {
        return pluginLogger;
    }

    public ConfigurationLoader getConfigurationLoader() {
        return configurationLoader;
    }

    public FavIconManager getFavIconManager() {
        return favIconManager;
    }

    public Settings getConfig() {
        return configurationLoader.getSettings();
    }

    public Messages getMessages() {
        return configurationLoader.getMessages();
    }

    public MaintenanceSettings getMaintenanceSettings() {
        return configurationLoader.getMaintenanceSettings();
    }

    public Set<Report> getReports() {
        return configurationLoader.getReports();
    }

    public StaffManager getStaffManager() {
        return staffManager;
    }

    public LobbyServerManager getLobbyServerManager() {
        return lobbyServerManager;
    }
}
