package io.github.tavstaldev.nexus.command.admin;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.ChatUtil;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

public class AlertCommand extends CommandBase {
    public  AlertCommand(String permission, String[] aliases) {
        super("alert",
                "<message>",
                "Broadcasts a message velocity wide.",
                permission,
                aliases
        );
    }

    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();
        if (invocation.arguments().length == 0) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                    "syntax", this.syntax,
                    "command", this.baseCommand
            ));
            return;
        }

        var config = Nexus.plugin.getConfig().getAlert();
        String message = String.join(" ", invocation.arguments());
        String format = config.getFormat();
        var msg = ChatUtil.buildMessage(format, Map.of(
                "message", message
        ));
        Nexus.plugin.getProxy().getAllPlayers()
                .forEach(otherPlayer -> otherPlayer.sendMessage(msg));
    }
}
