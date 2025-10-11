package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.proxy.server.ServerRegisteredEvent;
import io.github.tavstaldev.nexus.Nexus;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ServerRegisteredEventListener implements AwaitingEventExecutor<ServerRegisteredEvent> {
    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, ServerRegisteredEvent.class, this);
        plugin.getLogger().debug("Registered ServerRegisteredEventListener");
    }


    @Override
    public @Nullable EventTask executeAsync(ServerRegisteredEvent event) {
        return EventTask.async(() -> {
            var plugin = Nexus.plugin;
            var server = event.registeredServer();
            if (plugin.getConfig().getLobbyServers().contains(server.getServerInfo().getName()))
                plugin.getLobbyServerManager().registerLobbyServer(server);
        });
    }
}