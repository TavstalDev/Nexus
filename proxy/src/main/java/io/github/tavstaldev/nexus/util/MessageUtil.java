package io.github.tavstaldev.nexus.util;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import org.intellij.lang.annotations.RegExp;

import java.util.Map;

public class MessageUtil {

    public static void sendRichMsg(Player player, String message) {
        message = message.replace("%prefix%", Nexus.plugin.getConfig().getPrefix());

        player.sendMessage(ChatUtil.translateColors(message, true));
    }

    public static void sendRichMsg(Player player, String message, Map<String, Object> parameters) {
        String rawMessage = message;

        // Get the keys
        var keys = parameters.keySet();
        for (@RegExp var dirKey : keys) {
            @RegExp String finalKey;
            if (dirKey.startsWith("%"))
                finalKey = dirKey;
            else
                finalKey = "%" + dirKey + "%";
            rawMessage = rawMessage.replace(finalKey, parameters.get(dirKey).toString());
        }

        sendRichMsg(player, rawMessage);
    }
}
