package io.github.tavstaldev.nexus.command.player;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

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

        var lobbyManager = Nexus.plugin.getLobbyServerManager();
        var server = player.getCurrentServer().orElse(null);
        if (server != null && lobbyManager.isLobbyServer(server.getServer())) {
            MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getLobbyAlreadyIn());
            return;
        }

        var lobbyServer = lobbyManager.getLobbyServer();
        if (lobbyServer == null) {
            MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getLobbyNotSet());
            return;
        }

        MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getLobbyTeleporting(), Map.of("server", lobbyServer.getServerInfo().getName()));
        Nexus.plugin.getProxy().getScheduler().buildTask(Nexus.plugin, () -> {
            player.createConnectionRequest(lobbyServer).fireAndForget();
        }).schedule();
    }
}
