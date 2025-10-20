package io.github.tavstaldev.nexus.command;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.managers.CustomChatManager;
import io.github.tavstaldev.nexus.util.ChatUtil;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

/**
 * The CustomChatCommand class handles custom chat commands, allowing players to send
 * messages to specific chat channels or toggle their participation in those channels.
 */
public class CustomChatCommand extends CommandBase {
    /**
     * The name of the custom chat channel associated with this command.
     */
    private final String chatName;

    /**
     * Constructs a CustomChatCommand instance with the specified chat name, base command,
     * permission, and aliases.
     *
     * @param chatName    The name of the custom chat channel.
     * @param baseCommand The base name of the command.
     * @param permission  The permission required to execute the command.
     * @param aliases     An array of aliases for the command.
     */
    public CustomChatCommand(String chatName, String baseCommand, String permission, String[] aliases) {
        super(baseCommand,
                " <message>",
                "",
                permission,
                aliases
        );
        this.chatName = chatName;
    }

    /**
     * Executes the custom chat command. Handles sending messages to the custom chat channel
     * or toggling the player's participation in the channel.
     *
     * @param invocation The invocation context of the command, containing the source
     *                   and arguments.
     */
    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();

        // Ensure the command source is a player.
        if (!(source instanceof Player player)) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralPlayerOnly());
            return;
        }

        // Retrieve the custom chat configuration by name.
        var customChat = Nexus.plugin.getConfig().getCustomChatByName(chatName);
        if (customChat == null) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralErrorOccurred());
            return;
        }

        var args = invocation.arguments();

        // Handle the case where no message is provided.
        if (args.length < 1) {
            if (!customChat.isAllowToggle()) {
                MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                        "syntax", this.syntax,
                        "command", this.baseCommand
                ));
                return;
            }
            // Toggle the player's participation in the custom chat channel.
            CustomChatManager.toggleChat(player, chatName);
            return;
        }

        // Retrieve custom chat settings.
        boolean requiredPermission = customChat.isRequirePermission();
        String chatPermission = customChat.getPermission();
        String format = customChat.getFormat();

        // Build the message to be sent to the custom chat channel.
        var message = String.join(" ", args);
        var msg = ChatUtil.buildMessage(format, Map.of(
                "player", player.getUsername(),
                "message", message
        ));

        // Send the message to all players in the custom chat channel.
        Nexus.plugin.getProxy().getAllPlayers().stream()
                .filter(otherPlayer -> (!requiredPermission || otherPlayer.hasPermission(chatPermission)) || otherPlayer.equals(player))
                .forEach(otherPlayer -> otherPlayer.sendMessage(msg));
    }
}