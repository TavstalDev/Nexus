package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.util.ChatUtil;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The ProxyPingEventListener class listens for ProxyPingEvent events and modifies
 * the server ping response based on the server's configuration. This includes
 * customizing the MOTD, player count, and server favicon.
 */
public class ProxyPingEventListener implements AwaitingEventExecutor<ProxyPingEvent> {

    /**
     * Registers the ProxyPingEventListener with the Velocity event manager.
     * This allows the listener to handle ProxyPingEvent events.
     */
    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, ProxyPingEvent.class, this);
        plugin.getLogger().debug("Registered ProxyPingEventListener");
    }

    /**
     * Handles the ProxyPingEvent asynchronously. Modifies the server ping response
     * based on the configuration, including player count spoofing, MOTD selection,
     * and favicon customization.
     *
     * @param event The ProxyPingEvent containing the ping request and response details.
     * @return An EventTask that executes the ping modification logic asynchronously, or null if no action is needed.
     */
    public @Nullable EventTask executeAsync(ProxyPingEvent event) {
        return EventTask.async(() -> {
            var config = Nexus.plugin.getConfig();
            var serverPinger = config.getServerPinger();
            if (!serverPinger.isEnabled()) {
                return;
            }

            ServerPing.Builder pong = event.getPing().asBuilder();
            int playerCount = 0;
            var targetServers = serverPinger.getSpoofer().getTargetServers();
            if (targetServers.length == 0) {
                playerCount = pong.getOnlinePlayers();
            } else {
                var proxy = Nexus.plugin.getProxy();
                HashSet<ServerPing.SamplePlayer> players = new HashSet<>();
                for (String predefinedServer : targetServers) {
                    RegisteredServer server = proxy.getServer(predefinedServer).orElse(null);
                    if (server == null)
                        continue;
                    playerCount += server.getPlayersConnected().size();
                    for (Player connectedPlayer : server.getPlayersConnected()) {
                        players.add(new ServerPing.SamplePlayer(connectedPlayer.getGameProfile().getName(), connectedPlayer.getUniqueId()));
                    }
                }
                pong.clearSamplePlayers();
                pong.samplePlayers(players);
            }

            if (serverPinger.getFavIcon().isEnabled()) {
                var favIcon = Nexus.plugin.getFavIconManager().getIcon(serverPinger.getFavIcon().getPath());
                if (favIcon != null)
                    pong.favicon(favIcon);
            }
            pong.description(this.selectMotd());
            if (serverPinger.getSpoofer().disablePlayerHoverList()) {
                pong.clearSamplePlayers();
            }
            if (serverPinger.getSpoofer().hidePlayerCount()) {
                pong.nullPlayers();
            } else {
                pong.onlinePlayers(playerCount);
            }
            event.setPing(pong.build());
        });
    }

    /**
     * Selects a random MOTD (Message of the Day) from the configured list of MOTDs.
     * If only one MOTD is available, it is returned directly. If no MOTDs are found,
     * a default message is returned.
     *
     * @return A Component representing the selected MOTD.
     */
    private Component selectMotd() {
        var motds = Nexus.plugin.getConfig().getServerPinger().getMotds();
        if (motds.isEmpty()) {
            Nexus.plugin.getLogger().warn("No motds found in configuration!");
            return Component.text("No motds found!");
        }
        // If only one motd is found, return it directly
        if (motds.size() == 1) {
            var motd = motds.getFirst();
            Component motdComponent = Component.empty();
            motdComponent = motdComponent.append(ChatUtil.translateColors(motd.getLine1(), true));
            motdComponent = motdComponent.append(Component.newline());
            motdComponent = motdComponent.append(ChatUtil.translateColors(motd.getLine2(), true));
            return motdComponent;
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(motds.size());
        var motd = motds.get(randomIndex);
        Component motdComponent = Component.empty();
        motdComponent = motdComponent.append(ChatUtil.translateColors(motd.getLine1(), true));
        motdComponent = motdComponent.append(Component.newline());
        motdComponent = motdComponent.append(ChatUtil.translateColors(motd.getLine2(), true));
        return motdComponent;
    }
}