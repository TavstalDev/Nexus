package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

public class DisconnectEventListener {

    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, new DisconnectEventListener());
        plugin.getLogger().debug("Registered DisconnectEventListener");
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("nexus.staff")) {
            return;
        }

        var plugin = Nexus.plugin;
        var quitMessage = plugin.getMessages().getStaffLeaveMessage();
        for (Player otherPlayer : plugin.getProxy().getAllPlayers()) {
            if (otherPlayer == player || !otherPlayer.hasPermission("nexus.staff"))
                continue;

            MessageUtil.sendRichMsg(otherPlayer, quitMessage, Map.of(
                    "player", player.getUsername()
            ));
        }
    }
}
