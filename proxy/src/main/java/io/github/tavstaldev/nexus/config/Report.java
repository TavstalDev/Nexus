package io.github.tavstaldev.nexus.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.UUID;

@ConfigSerializable
public class Report {
    private final String reporterName;
    private final UUID reporterUUID;
    private final String targetName;
    private final UUID targetUUID;
    private final String reason;
    private final String server;
    private final long timestamp;

    public Report() {
        this.reporterName = "Unknown";
        this.reporterUUID = UUID.randomUUID();
        this.targetName = "Unknown";
        this.targetUUID = UUID.randomUUID();
        this.reason = "No reason provided";
        this.server = "Unknown";
        this.timestamp = System.currentTimeMillis();
    }

    public Report(String reporterName, UUID reporterUUID, String targetName, UUID targetUUID, String reason, String server, long timestamp) {
        this.reporterName = reporterName;
        this.reporterUUID = reporterUUID;
        this.targetName = targetName;
        this.targetUUID = targetUUID;
        this.reason = reason;
        this.server = server;
        this.timestamp = timestamp;
    }

    public String getReporterName() {
        return reporterName;
    }

    public UUID getReporterUUID() {
        return reporterUUID;
    }

    public String getTargetName() {
        return targetName;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public String getReason() {
        return reason;
    }

    public String getServer() {
        return server;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
