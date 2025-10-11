package io.github.tavstaldev.nexus.managers;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class LobbyServerManager {
    private final Set<RegisteredServer> lobbyServers = new HashSet<>();

    public void registerLobbyServer(RegisteredServer server) {
        lobbyServers.add(server);
    }

    public void unregisterLobbyServer(RegisteredServer server) {
        lobbyServers.removeIf(x -> x.equals(server));
    }

    public @Nullable RegisteredServer getLobbyServer() {
        if (lobbyServers.isEmpty()) {
            return null;
        }
        int currentPlayers = Integer.MAX_VALUE;
        RegisteredServer bestServer = null;
        for (RegisteredServer server : lobbyServers) {
            int serverPlayers = server.getPlayersConnected().size();
            if (serverPlayers < currentPlayers) {
                currentPlayers = serverPlayers;
                bestServer = server;
            }
        }
        return  bestServer;
    }
}
