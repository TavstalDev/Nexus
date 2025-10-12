package io.github.tavstaldev.nexus.command.admin;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

public class FindPlayerCommand extends CommandBase {
    public FindPlayerCommand() {
        super("findplayer",
                "<player>",
                "Finds which server a player is on.",
                "nexus.command.findplayer",
                new String[]{"whereis", "where"}
        );
    }

    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();
        if (invocation.arguments().length != 1) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                    "syntax", this.syntax,
                    "command", this.baseCommand
            ));
            return;
        }

        var playerName = invocation.arguments()[0];
        var player = Nexus.plugin.getProxy().getPlayer(playerName).orElse(null);
        if (player == null) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralPlayerNotFound());
            return;
        }

        var server = player.getCurrentServer().orElse(null);
        if (server == null) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getFindPlayerUnknown(), Map.of(
                    "player", player.getUsername()
            ));
        } else {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getFindPlayerFormat(), Map.of(
                    "player", player.getUsername(),
                    "server", server.getServer().getServerInfo().getName()
            ));
        }
    }
}
