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
import io.github.tavstaldev.nexus.config.reporting.Report;
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

/**
 * The Nexus class serves as the main entry point for the Nexus plugin.
 * It handles initialization, shutdown, and provides access to core components of the plugin.
 */
@Plugin(
        id = "nexus",
        name = "nexusProxy",
        version = NexusConstants.VERSION,
        url = "https://tavstaldev.github.io",
        authors = {"Tavstal"}
)
public class Nexus {
    // Static reference to the Nexus plugin instance.
    public static Nexus plugin;

    // The ProxyServer instance provided by Velocity.
    private final ProxyServer proxy;

    // The data folder path for the plugin.
    private final Path dataFolder;

    // A scheduled task for cleaning up expired reports.
    private ScheduledTask reportCleanerTask;

    // The plugin's logger for logging messages.
    private final PluginLogger pluginLogger;

    // The configuration loader for managing plugin settings.
    private final ConfigurationLoader configurationLoader;

    // The manager for handling server icons (favicons).
    private final FavIconManager favIconManager;

    // The manager for registering and managing commands.
    private final CommandManager commandManager;

    // The manager for tracking online staff members.
    private final StaffManager staffManager;

    // The manager for handling lobby server status and selection.
    private LobbyServerManager lobbyServerManager;

    /**
     * Constructs the Nexus plugin instance.
     *
     * @param proxy      The ProxyServer instance provided by Velocity.
     * @param dataFolder The data folder path for the plugin.
     */
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

    /**
     * Handles the initialization of the plugin when the proxy starts.
     *
     * @param event The ProxyInitializeEvent triggered by Velocity.
     */
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
                final long currentTime = System.currentTimeMillis();
                final Set<Report> reports = this.getReports();
                if (reports == null)
                    return;

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

    /**
     * Handles the shutdown of the plugin when the proxy stops.
     *
     * @param event The ProxyShutdownEvent triggered by Velocity.
     */
    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (this.reportCleanerTask != null)
            this.reportCleanerTask.cancel();
        commandManager.unregisterCommands();
        pluginLogger.ok("nexusProxy v1.0.0 has been disabled!");
    }

    /**
     * Registers event listeners for the plugin.
     */
    private void registerListeners() {
        new CommandEventListener().register();
        new ChatEventListener().register();
        new PreConnectEventListener().register();
        new ProxyPingEventListener().register();
        new ConnectEventListener().register();
        new DisconnectEventListener().register();
    }

    /**
     * Retrieves the ProxyServer instance.
     *
     * @return The ProxyServer instance.
     */
    public ProxyServer getProxy() {
        return proxy;
    }

    /**
     * Retrieves the data folder path for the plugin.
     *
     * @return The data folder path.
     */
    public Path getDataFolder() {
        return dataFolder;
    }

    /**
     * Retrieves the plugin's logger.
     *
     * @return The PluginLogger instance.
     */
    public PluginLogger getLogger() {
        return pluginLogger;
    }

    /**
     * Retrieves the configuration loader for managing plugin settings.
     *
     * @return The ConfigurationLoader instance.
     */
    public ConfigurationLoader getConfigurationLoader() {
        return configurationLoader;
    }

    /**
     * Retrieves the manager for handling server icons (favicons).
     *
     * @return The FavIconManager instance.
     */
    public FavIconManager getFavIconManager() {
        return favIconManager;
    }

    /**
     * Retrieves the plugin's settings.
     *
     * @return The Settings instance.
     */
    public Settings getConfig() {
        return configurationLoader.getSettings();
    }

    /**
     * Retrieves the plugin's messages configuration.
     *
     * @return The Messages instance.
     */
    public Messages getMessages() {
        return configurationLoader.getMessages();
    }

    /**
     * Retrieves the maintenance settings for the plugin.
     *
     * @return The MaintenanceSettings instance.
     */
    public MaintenanceSettings getMaintenanceSettings() {
        return configurationLoader.getMaintenanceSettings();
    }

    /**
     * Retrieves the report data managed by the plugin.
     *
     * @return The ReportData instance containing information about reports.
     */
    public ReportData getReportData() {
        return configurationLoader.getReportData();
    }

    /**
     * Retrieves the set of reports managed by the plugin.
     *
     * @return A set of Report objects.
     */
    public Set<Report> getReports() {
        return configurationLoader.getReports();
    }

    /**
     * Retrieves the manager for tracking online staff members.
     *
     * @return The StaffManager instance.
     */
    public StaffManager getStaffManager() {
        return staffManager;
    }

    /**
     * Retrieves the manager for handling lobby server status and selection.
     *
     * @return The LobbyServerManager instance.
     */
    public LobbyServerManager getLobbyServerManager() {
        return lobbyServerManager;
    }
}