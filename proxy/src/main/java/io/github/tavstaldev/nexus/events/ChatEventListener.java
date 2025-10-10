package io.github.tavstaldev.nexus.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.managers.CustomChatManager;
import io.github.tavstaldev.nexus.util.ChatUtil;
import net.kyori.adventure.text.Component;

import java.util.Map;

public class ChatEventListener {

    public void register() {
        var plugin = Nexus.plugin;
        plugin.getProxy().getEventManager().register(plugin, new ChatEventListener());
        plugin.getLogger().debug("Registered ChatEventListener");
    }

    @Subscribe
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        var toggledChat = CustomChatManager.getToggledChat(player.getUniqueId());
        if (toggledChat == null || !toggledChat.isEnabled()) {
            return;
        }
        event.setResult(PlayerChatEvent.ChatResult.denied());

        Component message = ChatUtil.buildMessage(toggledChat.getFormat(), Map.of(
                "player", player.getUsername(),
                "message", event.getMessage()
        ));
        for (Player players : Nexus.plugin.getProxy().getAllPlayers()) {
            if (!players.hasPermission(toggledChat.getPermission()))
                continue;
            players.sendMessage(message);
        }
    }
}