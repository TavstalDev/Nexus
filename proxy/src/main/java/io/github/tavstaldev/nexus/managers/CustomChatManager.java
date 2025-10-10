package io.github.tavstaldev.nexus.managers;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.config.Messages;
import io.github.tavstaldev.nexus.config.main.CustomChatConfig;
import io.github.tavstaldev.nexus.util.MessageUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomChatManager {
    private static final HashMap<UUID, String> toggledChats = new HashMap<>();

    public static void toggleChat(Player player, String chatName) {
        final UUID playerUUID = player.getUniqueId();
        final boolean hasToggled = toggledChats.containsKey(playerUUID);

        if (hasToggled && toggledChats.get(playerUUID).equals(chatName)) {
            toggledChats.remove(playerUUID);
            MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getCustomChatToggleOff(), Map.of("chat", chatName));
        } else {
            toggledChats.put(playerUUID, chatName);
            if (hasToggled)
                MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getCustomChatSwitchedTo(), Map.of("chat", chatName));
            else
                MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getCustomChatToggleOn(), Map.of("chat", chatName));
        }
    }

    public static @Nullable CustomChatConfig getToggledChat(UUID playerUUID) {
        String chatName = toggledChats.get(playerUUID);
        if (chatName == null)
            return null;

        return Nexus.plugin.getConfig().getCustomChatByName(chatName);
    }
}
