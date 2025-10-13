package io.github.tavstaldev.nexus.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.UUID;

@ConfigSerializable
public class Report {
    private final String reporterName;
    private final UUID reporterUuid;
    private final String targetName;
    private final UUID targetUuid;
    private final String reason;
    private final String server;
    private final long timestamp;

    public Report() {
        this.reporterName = "Unknown";
        this.reporterUuid = UUID.randomUUID();
        this.targetName = "Unknown";
        this.targetUuid = UUID.randomUUID();
        this.reason = "No reason provided";
        this.server = "Unknown";
        this.timestamp = System.currentTimeMillis();
    }

    public Report(String reporterName, UUID reporterUuid, String targetName, UUID targetUuid, String reason, String server, long timestamp) {
        this.reporterName = reporterName;
        this.reporterUuid = reporterUuid;
        this.targetName = targetName;
        this.targetUuid = targetUuid;
        this.reason = reason;
        this.server = server;
        this.timestamp = timestamp;
    }

    public String getReporterName() {
        return reporterName;
    }

    public UUID getReporterUUID() {
        return reporterUuid;
    }

    public String getTargetName() {
        return targetName;
    }

    public UUID getTargetUUID() {
        return targetUuid;
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
