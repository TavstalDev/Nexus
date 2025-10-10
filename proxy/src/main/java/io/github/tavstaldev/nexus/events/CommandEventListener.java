package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CommandEventListener implements AwaitingEventExecutor<CommandExecuteEvent> {
    private static final Map<UUID, Integer> executions = new ConcurrentHashMap<>();

    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, CommandExecuteEvent.class, this);
        plugin.getLogger().debug("Registered CommandEventListener");
    }

    public @Nullable EventTask executeAsync(CommandExecuteEvent event) {
        return EventTask.async(() -> {
            CommandSource source = event.getCommandSource();
            if (!(source instanceof Player player)) {
                return;
            }
            var antiCrasher = Nexus.plugin.getConfig().getAntiCrashHandler();
            if (!antiCrasher.isEnabled()) {
                return;
            }
            UUID uuid = player.getUniqueId();
            int count = executions.getOrDefault(uuid, 0) + 1;
            if (count >= 10) {
                executions.remove(uuid);
                player.disconnect(Component.text(antiCrasher.getMessage()));
                return;
            }

            if (!executions.containsKey(uuid)) {
                Nexus.plugin.getProxy().getScheduler().buildTask(Nexus.plugin, () ->
                        executions.remove(uuid)).delay(antiCrasher.getClearTime(), TimeUnit.MILLISECONDS
                ).schedule();
            }
            executions.put(uuid, count);

        });
    }
}