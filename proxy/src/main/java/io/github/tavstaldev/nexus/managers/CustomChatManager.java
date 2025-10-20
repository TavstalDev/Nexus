package io.github.tavstaldev.nexus.managers;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.config.main.CustomChatConfig;
import io.github.tavstaldev.nexus.util.MessageUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The CustomChatManager class manages the toggling of custom chats for players.
 * It allows players to switch between different custom chats and handles the
 * retrieval of the currently toggled chat for a player.
 */
public class CustomChatManager {
    // A map to store the toggled chats for players, where the key is the player's UUID
    // and the value is the name of the toggled chat.
    private static final HashMap<UUID, String> toggledChats = new HashMap<>();

    /**
     * Toggles a custom chat for the specified player. If the player is already
     * toggled into the specified chat, it will be turned off. If the player is
     * toggled into a different chat, they will be switched to the new chat.
     *
     * @param player   The player toggling the chat.
     * @param chatName The name of the chat to toggle.
     */
    public static void toggleChat(Player player, String chatName) {
        final UUID playerUUID = player.getUniqueId();
        final boolean hasToggled = toggledChats.containsKey(playerUUID);

        if (hasToggled && toggledChats.get(playerUUID).equals(chatName)) {
            // Turn off the toggled chat if the player is already in the specified chat.
            toggledChats.remove(playerUUID);
            MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getCustomChatToggleOff(), Map.of("chat", chatName));
        } else {
            // Switch to the new chat or toggle it on if no chat is currently toggled.
            toggledChats.put(playerUUID, chatName);
            if (hasToggled)
                MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getCustomChatSwitchedTo(), Map.of("chat", chatName));
            else
                MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getCustomChatToggleOn(), Map.of("chat", chatName));
        }
    }

    /**
     * Retrieves the currently toggled chat configuration for the specified player.
     *
     * @param playerUUID The UUID of the player whose toggled chat is being retrieved.
     * @return The CustomChatConfig of the toggled chat, or null if no chat is toggled.
     */
    public static @Nullable CustomChatConfig getToggledChat(UUID playerUUID) {
        String chatName = toggledChats.get(playerUUID);
        if (chatName == null)
            return null;

        // Retrieve the chat configuration by its name from the plugin's configuration.
        return Nexus.plugin.getConfig().getCustomChatByName(chatName);
    }
}