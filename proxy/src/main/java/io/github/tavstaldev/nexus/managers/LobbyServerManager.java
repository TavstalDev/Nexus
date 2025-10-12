package io.github.tavstaldev.nexus.managers;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import io.github.tavstaldev.nexus.Nexus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LobbyServerManager {
    private final Set<String> serversToTrack;
    private final HashMap<RegisteredServer, Boolean> lobbyServers = new HashMap<>();

    public LobbyServerManager(Set<String> serversToTrack) {
        this.serversToTrack = serversToTrack;
        for (String serverName : serversToTrack) {
            Nexus.plugin.getProxy().getServer(serverName).ifPresent(server -> lobbyServers.put(server, true));
        }
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(this::checkServerStatus, 0L, 500L, TimeUnit.MILLISECONDS);
    }

    private void checkServerStatus() {
        lobbyServers.keySet().forEach(server ->
                server.ping().thenAcceptAsync(result ->
                        lobbyServers.put(server, result != null)
                )
        );
        serversToTrack.forEach(serverName ->
                Nexus.plugin.getProxy().getServer(serverName).ifPresent(server ->
                        lobbyServers.putIfAbsent(server, true)
                )
        );
    }

    public @Nullable RegisteredServer getLobbyServer() {
        if (lobbyServers.isEmpty()) {
            return null;
        }
        int currentPlayers = Integer.MAX_VALUE;
        RegisteredServer bestServer = null;
        for (RegisteredServer server : lobbyServers.keySet()) {
            if (!lobbyServers.get(server)) {
                continue;
            }

            int serverPlayers = server.getPlayersConnected().size();
            if (serverPlayers < currentPlayers) {
                currentPlayers = serverPlayers;
                bestServer = server;
            }
        }
        return  bestServer;
    }

    public boolean isLobbyServer(RegisteredServer server) {
        return lobbyServers.containsKey(server);
    }
}
