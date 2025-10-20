package io.github.tavstaldev.nexus.command.admin;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.ChatUtil;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

/**
 * The AlertCommand class is responsible for broadcasting a message to all players
 * across the Velocity proxy. It extends the CommandBase class and provides the
 * implementation for the "alert" command.
 */
public class AlertCommand extends CommandBase {

    /**
     * Constructs an AlertCommand instance.
     *
     * @param permission The permission required to execute the command.
     * @param aliases    An array of aliases for the command.
     */
    public AlertCommand(String permission, String[] aliases) {
        super("alert",
                "<message>",
                "Broadcasts a message velocity wide.",
                permission,
                aliases
        );
    }

    /**
     * Executes the "alert" command. If no arguments are provided, it sends a syntax
     * error message to the command source. Otherwise, it broadcasts the provided
     * message to all players.
     *
     * @param invocation The invocation context of the command, containing the source
     *                   and arguments.
     */
    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();

        // Check if no arguments are provided and send a syntax error message.
        if (invocation.arguments().length == 0) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                    "syntax", this.syntax,
                    "command", this.baseCommand
            ));
            return;
        }

        // Retrieve the alert configuration and format the message.
        var config = Nexus.plugin.getConfig().getAlert();
        String message = String.join(" ", invocation.arguments());
        String format = config.getFormat();
        var msg = ChatUtil.buildMessage(format, Map.of(
                "message", message
        ));

        // Broadcast the formatted message to all players.
        Nexus.plugin.getProxy().getAllPlayers()
                .forEach(otherPlayer -> otherPlayer.sendMessage(msg));
    }
}
