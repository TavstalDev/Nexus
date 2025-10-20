package io.github.tavstaldev.nexus.util;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import org.intellij.lang.annotations.RegExp;

import java.util.Map;

/**
 * The MessageUtil class provides utility methods for sending rich messages to players or command sources.
 * It supports parameterized messages and color code translations.
 */
public class MessageUtil {

    /**
     * Sends a rich message to a player. The message is processed to replace the prefix placeholder
     * with the configured prefix and to translate color codes.
     *
     * @param player  The player to whom the message will be sent.
     * @param message The raw message to send.
     */
    public static void sendRichMsg(Player player, String message) {
        message = message.replace("%prefix%", Nexus.plugin.getConfig().getPrefix());

        player.sendMessage(ChatUtil.translateColors(message, true));
    }

    /**
     * Sends a rich message to a player with parameterized placeholders. Placeholders in the message
     * are replaced with their corresponding values from the parameters map.
     *
     * @param player     The player to whom the message will be sent.
     * @param message    The raw message containing placeholders.
     * @param parameters A map of placeholder keys and their replacement values.
     */
    public static void sendRichMsg(Player player, String message, Map<String, Object> parameters) {
        String rawMessage = message;

        // Replace placeholders with their corresponding values
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

    /**
     * Sends a rich message to a command source. The message is processed to replace the prefix placeholder
     * with the configured prefix and to translate color codes.
     *
     * @param player  The command source to whom the message will be sent.
     * @param message The raw message to send.
     */
    public static void sendRichMsg(CommandSource player, String message) {
        message = message.replace("%prefix%", Nexus.plugin.getConfig().getPrefix());

        player.sendMessage(ChatUtil.translateColors(message, true));
    }

    /**
     * Sends a rich message to a command source with parameterized placeholders. Placeholders in the message
     * are replaced with their corresponding values from the parameters map.
     *
     * @param player     The command source to whom the message will be sent.
     * @param message    The raw message containing placeholders.
     * @param parameters A map of placeholder keys and their replacement values.
     */
    public static void sendRichMsg(CommandSource player, String message, Map<String, Object> parameters) {
        String rawMessage = message;

        // Replace placeholders with their corresponding values
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