package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.managers.CustomChatManager;
import io.github.tavstaldev.nexus.util.ChatUtil;
import net.kyori.adventure.text.Component;

import java.util.Map;

/**
 * The ChatEventListener class listens for player chat events and handles
 * custom chat functionality, such as toggling chat channels and formatting messages.
 */
public class ChatEventListener {

    /**
     * Registers the ChatEventListener with the Velocity event manager.
     * This allows the listener to handle player chat events.
     */
    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, new ChatEventListener());
        plugin.getLogger().debug("Registered ChatEventListener");
    }

    /**
     * Handles the PlayerChatEvent. If the player has a toggled custom chat channel,
     * the event is canceled, and the message is sent to the players in the custom chat channel.
     *
     * @param event The PlayerChatEvent containing the player and their message.
     */
    @Subscribe
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        // Retrieve the toggled custom chat channel for the player.
        var toggledChat = CustomChatManager.getToggledChat(player.getUniqueId());
        if (toggledChat == null || !toggledChat.isEnabled()) {
            return;
        }

        // Cancel the default chat event.
        event.setResult(PlayerChatEvent.ChatResult.denied());

        // Build the custom chat message using the specified format.
        Component message = ChatUtil.buildMessage(toggledChat.getFormat(), Map.of(
                "player", player.getUsername(),
                "message", event.getMessage()
        ));

        // Retrieve the permission required to view the custom chat messages.
        var permission = toggledChat.getPermission();

        // Send the custom chat message to all players with the required permission.
        Nexus.plugin.getProxy().getAllPlayers().stream()
                .filter(x -> x.hasPermission(permission))
                .forEach(x -> x.sendMessage(message));
    }
}