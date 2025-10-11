package io.github.tavstaldev.nexus.managers;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffManager {
    private final Set<UUID> onlineStaff = new HashSet<>();

    public void addStaff(UUID uuid) {
        onlineStaff.add(uuid);
    }

    public void removeStaff(UUID uuid) {
        onlineStaff.removeIf(x -> x.equals(uuid));
    }

    public Set<Player> getOnlineStaff() {
        Set<Player> staffPlayers = new HashSet<>();
        var proxy = Nexus.plugin.getProxy();
        var staffCopy = new HashSet<>(onlineStaff);
        for (UUID uuid : staffCopy) {
            var player = proxy.getPlayer(uuid);
            if (player.isEmpty()) {
                onlineStaff.remove(uuid);
                continue;
            }
            staffPlayers.add(player.get());
        }
        return staffPlayers;
    }
}