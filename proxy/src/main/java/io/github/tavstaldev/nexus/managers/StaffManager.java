package io.github.tavstaldev.nexus.managers;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * The StaffManager class is responsible for managing the online staff members in the Nexus plugin.
 * It keeps track of staff members' UUIDs and provides methods to add, remove, and retrieve online staff players.
 */
public class StaffManager {
    // A set to store the UUIDs of online staff members.
    private final Set<UUID> onlineStaff = new HashSet<>();

    /**
     * Adds a staff member to the online staff set.
     * If the staff member is already in the set, the method does nothing.
     *
     * @param uuid The UUID of the staff member to add.
     */
    public void addStaff(UUID uuid) {
        if (onlineStaff.contains(uuid))
            return;
        onlineStaff.add(uuid);
    }

    /**
     * Removes a staff member from the online staff set.
     * If the staff member is not in the set, the method does nothing.
     *
     * @param uuid The UUID of the staff member to remove.
     */
    public void removeStaff(UUID uuid) {
        onlineStaff.removeIf(x -> x.equals(uuid));
    }

    /**
     * Retrieves the set of online staff players.
     * If a staff member is no longer online, their UUID is removed from the set.
     *
     * @return A set of Player objects representing the online staff members.
     */
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