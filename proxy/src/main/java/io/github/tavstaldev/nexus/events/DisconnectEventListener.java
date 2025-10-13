package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.util.ChatUtil;
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
        if (event.getLoginStatus() == DisconnectEvent.LoginStatus.PRE_SERVER_JOIN)
            return;

        Player player = event.getPlayer();
        Nexus.plugin.getStaffManager().removeStaff(player.getUniqueId());
        if (!player.hasPermission("nexus.staff")) {
            return;
        }

        var plugin = Nexus.plugin;
        var quitMessage = ChatUtil.buildMessage(plugin.getMessages().getStaffLeaveMessage(), Map.of(
                "player", player.getUsername()
        ));

        plugin.getProxy().getAllPlayers()
                .stream()
                .filter(x -> x != player && x.hasPermission("nexus.staff"))
                .forEach(x -> x.sendMessage(quitMessage));
    }
}
