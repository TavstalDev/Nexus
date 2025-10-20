package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.NexusConstants;
import io.github.tavstaldev.nexus.util.ChatUtil;

import java.util.Map;

/**
 * The DisconnectEventListener class listens for player disconnection events
 * and handles staff-related functionality, such as removing staff members
 * from the staff manager and notifying other staff members when a staff
 * member disconnects.
 */
public class DisconnectEventListener {

    /**
     * Registers the DisconnectEventListener with the Velocity event manager.
     * This allows the listener to handle DisconnectEvent events.
     */
    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, new DisconnectEventListener());
        plugin.getLogger().debug("Registered DisconnectEventListener");
    }

    /**
     * Handles the DisconnectEvent. If the player is a staff member, they are
     * removed from the staff manager, and a notification is sent to other staff
     * members about their disconnection.
     *
     * @param event The DisconnectEvent containing the player and disconnection details.
     */
    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        // Ignore disconnections that occur before the player joins a server.
        if (event.getLoginStatus() == DisconnectEvent.LoginStatus.PRE_SERVER_JOIN)
            return;

        Player player = event.getPlayer();

        // Remove the player from the staff manager.
        Nexus.plugin.getStaffManager().removeStaff(player.getUniqueId());

        // Only proceed if the player has the NexusConstants.STAFF_PERMISSION permission.
        if (!player.hasPermission(NexusConstants.STAFF_PERMISSION)) {
            return;
        }

        var plugin = Nexus.plugin;

        // Build the staff leave notification message.
        var quitMessage = ChatUtil.buildMessage(plugin.getMessages().getStaffLeaveMessage(), Map.of(
                "player", player.getUsername()
        ));

        // Notify all other staff members about the disconnection.
        plugin.getProxy().getAllPlayers()
                .stream()
                .filter(x -> x != player && x.hasPermission(NexusConstants.STAFF_PERMISSION))
                .forEach(x -> x.sendMessage(quitMessage));
    }
}