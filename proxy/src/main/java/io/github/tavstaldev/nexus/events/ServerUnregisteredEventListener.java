package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.proxy.server.ServerUnregisteredEvent;
import io.github.tavstaldev.nexus.Nexus;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ServerUnregisteredEventListener implements AwaitingEventExecutor<ServerUnregisteredEvent> {
    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, ServerUnregisteredEvent.class, this);
        plugin.getLogger().debug("Registered ServerUnregisteredEventListener");
    }


    @Override
    public @Nullable EventTask executeAsync(ServerUnregisteredEvent event) {
        return EventTask.async(() -> {
            Nexus.plugin.getLobbyServerManager().unregisterLobbyServer(event.unregisteredServer());
        });
    }
}