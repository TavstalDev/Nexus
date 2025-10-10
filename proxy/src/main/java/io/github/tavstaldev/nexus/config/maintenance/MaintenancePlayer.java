package io.github.tavstaldev.nexus.config.maintenance;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.LocalDateTime;
import java.util.UUID;

@ConfigSerializable
public class MaintenancePlayer {
    private UUID uuid;
    private String name;
    private String addedBy;
    private long addedAt;

    public MaintenancePlayer() {
        this.uuid = UUID.randomUUID();
        this.name = "Unknown";
        this.addedBy = "Unknown";
        this.addedAt = System.currentTimeMillis();
    }

    public MaintenancePlayer(UUID uuid, String name, String addedBy, long addedAt) {
        this.uuid = uuid;
        this.name = name;
        this.addedBy = addedBy;
        this.addedAt = addedAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public long getAddedAt() {
        return addedAt;
    }

    public LocalDateTime getAddedAtDateTime() {
        return LocalDateTime.ofEpochSecond(addedAt / 1000, 0, java.time.ZoneOffset.UTC);
    }
}
