package io.github.tavstaldev.nexus.config;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.config.reporting.Report;
import io.github.tavstaldev.nexus.logger.PluginLogger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;
import java.util.Set;

public class ConfigurationLoader {
    private final PluginLogger logger;
    private final Path CONFIG_PATH;
    private final Path MAINTENANCE_PATH;
    private final Path REPORT_PATH;
    private final Path MESSAGES_PATH;
    private Settings settings;
    private MaintenanceSettings maintenanceSettings;
    private ReportData reportData;
    private Messages messages;

    public ConfigurationLoader() {
        this.logger = Nexus.plugin.getLogger().withModule(this.getClass());
        var dataPath = Nexus.plugin.getDataFolder();
        CONFIG_PATH = dataPath.resolve("config.yml");
        MAINTENANCE_PATH = dataPath.resolve("maintenance.json");
        REPORT_PATH = dataPath.resolve("reports.json");
        MESSAGES_PATH = dataPath.resolve("messages.yml");
    }

    public void loadAll() {
        loadConfig();
        loadMaintenanceConfig();
        loadReports();
        loadMessages();
    }

    public void loadConfig() {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(CONFIG_PATH)
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
        try {
            final CommentedConfigurationNode node = loader.load();
            final Settings settings = node.get(Settings.class);
            if (settings == null) {
                this.settings = new Settings();
                saveConfig();
                logger.error("Failed to load config.yml, using default settings.");
                return;
            }
            this.settings = settings;
            loader.save(node);
            logger.info("Loaded config.yml successfully.");
        } catch (Exception e) {
            logger.error("Failed to load config.yml: \n" + e.getMessage());
        }
    }

    public void saveConfig() {
        final  YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(CONFIG_PATH)
                .build();
        try {
            final CommentedConfigurationNode node = loader.createNode();
            node.set(Settings.class, settings);
            loader.save(node);
            logger.info("Saved config.yml successfully.");
        } catch (Exception e) {
            logger.error("Failed to save config.yml: \n" + e.getMessage());
        }
    }

    public void loadMaintenanceConfig() {
        final GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                .path(MAINTENANCE_PATH)
                .build();
        try {
            final var node = loader.load();
            final MaintenanceSettings settings = node.get(MaintenanceSettings.class);
            if (settings == null) {
                this.maintenanceSettings = new MaintenanceSettings();
                saveMaintenanceConfig();
                logger.error("Failed to load maintenance.yml, using default settings.");
                return;
            }
            this.maintenanceSettings = settings;
            loader.save(node);
            logger.info("Loaded maintenance.yml successfully.");
        } catch (Exception e) {
            logger.error("Failed to load maintenance.yml: \n" + e.getMessage());
        }
    }

    public void saveMaintenanceConfig() {
        final GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                .path(MAINTENANCE_PATH)
                .build();
        try {
            final var node = loader.createNode();
            node.set(MaintenanceSettings.class, maintenanceSettings);
            loader.save(node);
            logger.info("Saved maintenance.yml successfully.");
        } catch (Exception e) {
            logger.error("Failed to save maintenance.yml: \n" + e.getMessage());
        }
    }

    public void loadReports() {
        final GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                .path(REPORT_PATH)
                .build();
        try {
            final var node = loader.load();
            final ReportData report = node.get(ReportData.class);
            if (report == null) {
                this.reportData = new ReportData();
                saveReports();
                logger.error("Failed to load reports.yml, creating default file.");
                return;
            }
            this.reportData = report;
            loader.save(node);
            logger.info("Loaded reports.yml successfully.");
        } catch (Exception e) {
            logger.error("Failed to load reports.yml: \n" + e.getMessage());
        }
    }

    public void saveReports() {
        final GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                .path(REPORT_PATH)
                .build();
        try {
            final var node = loader.createNode();
            node.set(ReportData.class, reportData);
            loader.save(node);
            logger.info("Saved reports.yml successfully.");
        } catch (Exception e) {
            logger.error("Failed to save reports.yml: \n" + e.getMessage());
        }
    }

    public void loadMessages() {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(MESSAGES_PATH)
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
        try {
            final CommentedConfigurationNode node = loader.load();
            final Messages settings = node.get(Messages.class);
            if (settings == null) {
                this.messages = new Messages();
                saveMessages();
                logger.error("Failed to load messages.yml, using default settings.");
                return;
            }
            this.messages = settings;
            loader.save(node);
            logger.info("Loaded messages.yml successfully.");
        } catch (Exception e) {
            logger.error("Failed to load messages.yml: \n" + e.getMessage());
        }
    }

    public void saveMessages() {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(MESSAGES_PATH)
                .build();
        try {
            final CommentedConfigurationNode node = loader.createNode();
            node.set(Messages.class, messages);
            loader.save(node);
            logger.info("Saved messages.yml successfully.");
        } catch (Exception e) {
            logger.error("Failed to save messages.yml: \n" + e.getMessage());
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public MaintenanceSettings getMaintenanceSettings() {
        return maintenanceSettings;
    }

    public ReportData getReportData() {
        return reportData;
    }

    public Set<Report> getReports() {
        return reportData.getReports();
    }

    public Messages getMessages() {
        return messages;
    }
}
