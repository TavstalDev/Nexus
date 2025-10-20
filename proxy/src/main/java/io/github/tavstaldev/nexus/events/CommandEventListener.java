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

/**
 * The CommandEventListener class listens for command execution events and implements
 * anti-crash functionality by limiting the number of commands a player can execute
 * within a specified time frame.
 */
public class CommandEventListener implements AwaitingEventExecutor<CommandExecuteEvent> {
    /**
     * A map that tracks the number of commands executed by each player, identified by their UUID.
     */
    private static final Map<UUID, Integer> executions = new ConcurrentHashMap<>();

    /**
     * Registers the CommandEventListener with the Velocity event manager.
     * This allows the listener to handle CommandExecuteEvent events.
     */
    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, CommandExecuteEvent.class, this);
        plugin.getLogger().debug("Registered CommandEventListener");
    }

    /**
     * Handles the CommandExecuteEvent asynchronously. Implements anti-crash functionality
     * by disconnecting players who exceed the allowed number of commands within the configured time frame.
     *
     * @param event The CommandExecuteEvent containing the command source and other details.
     * @return An EventTask that executes the anti-crash logic asynchronously, or null if the source is not a player.
     */
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