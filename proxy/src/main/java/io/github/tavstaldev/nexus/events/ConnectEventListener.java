package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.NexusConstants;
import io.github.tavstaldev.nexus.util.ChatUtil;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The ConnectEventListener class listens for server connection events and handles
 * staff-related functionality, such as notifying other staff members when a staff
 * member switches servers.
 */
public class ConnectEventListener implements AwaitingEventExecutor<ServerConnectedEvent> {

    /**
     * Registers the ConnectEventListener with the Velocity event manager.
     * This allows the listener to handle ServerConnectedEvent events.
     */
    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, ServerConnectedEvent.class, this);
        plugin.getLogger().debug("Registered ServerConnectedEvent");
    }

    /**
     * Handles the ServerConnectedEvent asynchronously. If the player is a staff member,
     * it adds them to the staff manager and notifies other staff members about the server switch.
     *
     * @param event The ServerConnectedEvent containing the player and server details.
     * @return An EventTask that executes the staff notification logic asynchronously, or null if no action is needed.
     */
    public @Nullable EventTask executeAsync(ServerConnectedEvent event) {
        return EventTask.async(() -> {
            // Check if the player is switching from another server.
            if (event.getPreviousServer().isEmpty()) {
                return;
            }
            Player player = event.getPlayer();

            // Only proceed if the player has the NexusConstants.STAFF_PERMISSION permission.
            if (!player.hasPermission(NexusConstants.STAFF_PERMISSION)) {
                return;
            }

            // Add the staff member to the staff manager.
            Nexus.plugin.getStaffManager().addStaff(player.getUniqueId());
            var rawMessage = Nexus.plugin.getMessages().getStaffSwitch();

            // Build the staff switch notification message.
            Component message = ChatUtil.buildMessage(rawMessage, Map.of(
                    "player", player.getUsername(),
                    "from", event.getPreviousServer().get().getServerInfo().getName(),
                    "to", event.getServer().getServerInfo().getName()
            ));

            // Notify all other staff members about the server switch.
            Nexus.plugin.getProxy().getAllPlayers()
                    .stream()
                    .filter(x -> x != player && x.hasPermission(NexusConstants.STAFF_PERMISSION))
                    .forEach(x -> x.sendMessage(message));
        });
    }
}