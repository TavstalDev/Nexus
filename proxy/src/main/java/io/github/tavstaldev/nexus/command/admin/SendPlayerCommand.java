package io.github.tavstaldev.nexus.command.admin;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

public class SendPlayerCommand extends CommandBase {
    public SendPlayerCommand() {
        super("sendplayer",
                "<player> <server>",
                "Sends a player to a specified server.",
                "nexus.command.sendplayer",
                new String[]{"moveplayer"}
        );
    }

    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();
        if (invocation.arguments().length != 2) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                    "syntax", this.syntax,
                    "command", this.baseCommand
            ));
            return;
        }

        var playerName = invocation.arguments()[0];
        var targetPlayer = Nexus.plugin.getProxy().getPlayer(playerName).orElse(null);
        if (targetPlayer == null) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralPlayerNotFound());
            return;
        }

        var serverName = invocation.arguments()[1];
        var server = Nexus.plugin.getProxy().getServer(serverName).orElse(null);
        if (server == null) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralInvalidServer(), Map.of(
                    "server", serverName
            ));
            return;
        }

        MessageUtil.sendRichMsg(targetPlayer, Nexus.plugin.getMessages().getSendPlayerTarget(), Map.of(
                "server", server.getServerInfo().getName())
        );
        MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getSendPlayerSender(), Map.of(
                "player", targetPlayer.getUsername(),
                "server", server.getServerInfo().getName()
        ));
        Nexus.plugin.getProxy().getScheduler().buildTask(Nexus.plugin, () -> {
            targetPlayer.createConnectionRequest(server).fireAndForget();
        }).schedule();
    }
}
