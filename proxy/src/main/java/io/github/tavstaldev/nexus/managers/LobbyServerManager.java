package io.github.tavstaldev.nexus.managers;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import io.github.tavstaldev.nexus.Nexus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The LobbyServerManager class is responsible for managing and tracking the status of lobby servers.
 * It periodically checks the availability of servers and provides methods to retrieve the best available lobby server.
 */
public class LobbyServerManager {
    // A set of server names to track.
    private final Set<String> serversToTrack;

    // A map to store the tracked lobby servers and their availability status.
    private final HashMap<RegisteredServer, Boolean> lobbyServers = new HashMap<>();

    /**
     * Constructs a LobbyServerManager with the specified set of server names to track.
     * Initializes the lobby servers and starts a scheduled task to check their status.
     *
     * @param serversToTrack A set of server names to track.
     */
    public LobbyServerManager(Set<String> serversToTrack) {
        this.serversToTrack = serversToTrack;
        for (String serverName : serversToTrack) {
            Nexus.plugin.getProxy().getServer(serverName).ifPresent(server -> lobbyServers.put(server, true));
        }
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(this::checkServerStatus, 0L, 500L, TimeUnit.MILLISECONDS);
    }

    /**
     * Periodically checks the status of the tracked lobby servers.
     * Updates the availability status of each server and adds new servers to the tracking map if necessary.
     */
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

    /**
     * Retrieves the best available lobby server based on the number of connected players.
     * If multiple servers are available, the one with the fewest players is selected.
     *
     * @return The best available lobby server, or null if no servers are available.
     */
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
        return bestServer;
    }

    /**
     * Checks if the specified server is being tracked as a lobby server.
     *
     * @param server The server to check.
     * @return True if the server is a tracked lobby server, false otherwise.
     */
    public boolean isLobbyServer(RegisteredServer server) {
        return lobbyServers.containsKey(server);
    }
}