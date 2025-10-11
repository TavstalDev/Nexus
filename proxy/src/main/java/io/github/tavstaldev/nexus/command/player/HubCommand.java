package io.github.tavstaldev.nexus.command.player;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

public class HubCommand extends CommandBase {
    public HubCommand() {
        super("hub",
                "",
                "Teleports you to the hub/lobby server.",
                "",
                new String[]{"lobby"}
        );
    }

    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();
        if (!(source instanceof Player player)) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralPlayerOnly());
            return;
        }

        var lobbyServer = Nexus.plugin.getLobbyServerManager().getLobbyServer();
        if (lobbyServer == null) {
            MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getNoLobbyServerSet());
            return;
        }

        // TODO: Add lobby check
        // Add Cooldown
        // Add message

        Nexus.plugin.getProxy().getScheduler().buildTask(Nexus.plugin, () -> {
            player.createConnectionRequest(lobbyServer).fireAndForget();
        });
    }
}
