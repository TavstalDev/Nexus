package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.util.ChatUtil;
import io.github.tavstaldev.nexus.util.MessageUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PreConnectEventListener implements AwaitingEventExecutor<ServerPreConnectEvent> {

    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, ServerPreConnectEvent.class, this);
        plugin.getLogger().debug("Registered PreConnectEventListener");
    }

    public @Nullable EventTask executeAsync(ServerPreConnectEvent event) {
        return EventTask.async(() -> {
            Player player = event.getPlayer();
            var plugin = Nexus.plugin;
            var maintenance = plugin.getMaintenanceSettings();

            if (maintenance.isEnabled())
            {
                if (!maintenance.isPlayerAllowed(player)) {
                    event.setResult(ServerPreConnectEvent.ServerResult.denied());
                    String serializedFormat = String.join("\n", plugin.getMessages().getMaintenanceKickMessage());
                    player.disconnect(ChatUtil.translateColors(serializedFormat, true));
                    return;
                }
            }

            if (event.getPreviousServer() != null) {
                return;
            }

            var reportConfig = Nexus.plugin.getConfig().getPlayerReport();
            if (reportConfig.isEnabled() && reportConfig.isNotifyOnLogin() && player.hasPermission(reportConfig.getNotifyPermission())) {
                var reports = Nexus.plugin.getReports();
                if (reports.isEmpty()) {
                    MessageUtil.sendRichMsg(player, plugin.getMessages().getPlayerReportEmpty());
                }
                else {
                    MessageUtil.sendRichMsg(player, plugin.getMessages().getPlayerReportCount(),
                            Map.of("count", String.valueOf(reports.size())));
                }
            }

            if (!player.hasPermission("nexus.staff")) {
                return;
            }

            Nexus.plugin.getStaffManager().addStaff(player.getUniqueId());
            var joinMessage = ChatUtil.buildMessage(plugin.getMessages().getStaffJoinMessage(), Map.of(
                    "player", player.getUsername()
            ));
            plugin.getProxy().getAllPlayers()
                    .stream()
                    .filter(x -> x != player && x.hasPermission("nexus.staff"))
                    .forEach(x -> x.sendMessage(joinMessage));
        });
    }
}
