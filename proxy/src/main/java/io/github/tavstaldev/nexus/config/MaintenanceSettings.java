package io.github.tavstaldev.nexus.config;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.config.maintenance.MaintenancePlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashSet;
import java.util.Set;

@ConfigSerializable
public class MaintenanceSettings {
    private boolean enabled = false;
    private Set<MaintenancePlayer> players;

    public MaintenanceSettings() {
        this.enabled = false;
        this.players = new HashSet<>();
    }

    public MaintenanceSettings(boolean enabled, Set<MaintenancePlayer> players) {
        this.enabled = enabled;
        this.players = players;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public Set<MaintenancePlayer> getPlayers() {
        return players;
    }

    public void addPlayer(Player player, Player adder) {
        players.add(new MaintenancePlayer(player.getUniqueId(), player.getUsername(), adder.getUsername(), System.currentTimeMillis()));
    }

    public void removePlayer(Player player) {
        players.removeIf(p -> p.getUuid().equals(player.getUniqueId()));
    }

    public boolean isPlayerAllowed(Player player) {
        return players.stream().anyMatch(p -> p.getUuid().equals(player.getUniqueId()));
    }
}
