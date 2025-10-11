package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.util.ChatUtil;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ConnectEventListener implements AwaitingEventExecutor<ServerConnectedEvent> {

    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, ServerConnectedEvent.class, this);
        plugin.getLogger().debug("Registered ServerConnectedEvent");
    }

    public @Nullable EventTask executeAsync(ServerConnectedEvent event) {
        return EventTask.async(() -> {
            if (event.getPreviousServer().isEmpty()) {
                return;
            }
            Player player = event.getPlayer();
            if (!player.hasPermission("nexus.staff")) {
                return;
            }
            Nexus.plugin.getStaffManager().addStaff(player.getUniqueId());
            var rawMessage = Nexus.plugin.getMessages().getStaffSwitch();

            Component message = ChatUtil.buildMessage(rawMessage, Map.of(
                    "player", player.getUsername(),
                    "from", event.getPreviousServer().get().getServerInfo().getName(),
                    "to", event.getServer().getServerInfo().getName()
            ));

            Nexus.plugin.getProxy().getAllPlayers()
                    .stream()
                    .filter(x -> x != player && x.hasPermission("nexus.staff"))
                    .forEach(x -> x.sendMessage(message));
        });
    }
}
