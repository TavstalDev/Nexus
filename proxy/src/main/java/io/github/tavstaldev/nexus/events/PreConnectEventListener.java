package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.NexusConstants;
import io.github.tavstaldev.nexus.util.ChatUtil;
import io.github.tavstaldev.nexus.util.MessageUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The PreConnectEventListener class listens for server pre-connect events and handles
 * various functionalities such as maintenance mode checks, player report notifications,
 * and staff join notifications.
 */
public class PreConnectEventListener implements AwaitingEventExecutor<ServerPreConnectEvent> {

    /**
     * Registers the PreConnectEventListener with the Velocity event manager.
     * This allows the listener to handle ServerPreConnectEvent events.
     */
    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, ServerPreConnectEvent.class, this);
        plugin.getLogger().debug("Registered PreConnectEventListener");
    }

    /**
     * Handles the ServerPreConnectEvent asynchronously. Implements maintenance mode checks,
     * sends player report notifications to staff, and notifies other staff members when a staff
     * member joins the server.
     *
     * @param event The ServerPreConnectEvent containing the player and connection details.
     * @return An EventTask that executes the connection logic asynchronously, or null if no action is needed.
     */
    public @Nullable EventTask executeAsync(ServerPreConnectEvent event) {
        return EventTask.async(() -> {
            Player player = event.getPlayer();
            var plugin = Nexus.plugin;
            var maintenance = plugin.getMaintenanceSettings();

            // Check if maintenance mode is enabled and deny access to unauthorized players.
            if (maintenance.isEnabled()) {
                if (!maintenance.isPlayerAllowed(player)) {
                    event.setResult(ServerPreConnectEvent.ServerResult.denied());
                    String serializedFormat = String.join("\n", plugin.getMessages().getMaintenanceKickMessage());
                    player.disconnect(ChatUtil.translateColors(serializedFormat, true));
                    return;
                }
            }

            // Skip further processing if the player is switching servers.
            if (event.getPreviousServer() != null) {
                return;
            }

            // Notify staff about player reports if the feature is enabled.
            var reportConfig = Nexus.plugin.getConfig().getPlayerReport();
            if (reportConfig.isEnabled() && reportConfig.isNotifyOnLogin() && player.hasPermission(reportConfig.getNotifyPermission())) {
                var reports = Nexus.plugin.getReports();
                if (reports.isEmpty()) {
                    MessageUtil.sendRichMsg(player, plugin.getMessages().getPlayerReportEmpty());
                } else {
                    MessageUtil.sendRichMsg(player, plugin.getMessages().getPlayerReportCount(),
                            Map.of("count", String.valueOf(reports.size())));
                }
            }

            // Skip further processing if the player is not a staff member.
            if (!player.hasPermission(NexusConstants.STAFF_PERMISSION)) {
                return;
            }

            // Add the staff member to the staff manager and notify other staff members.
            Nexus.plugin.getStaffManager().addStaff(player.getUniqueId());
            var joinMessage = ChatUtil.buildMessage(plugin.getMessages().getStaffJoinMessage(), Map.of(
                    "player", player.getUsername()
            ));
            plugin.getProxy().getAllPlayers()
                    .stream()
                    .filter(x -> x != player && x.hasPermission(NexusConstants.STAFF_PERMISSION))
                    .forEach(x -> x.sendMessage(joinMessage));
        });
    }
}