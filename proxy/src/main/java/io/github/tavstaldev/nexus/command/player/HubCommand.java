package io.github.tavstaldev.nexus.command.player;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

/**
 * The HubCommand class handles the "hub" command, which teleports players
 * to the hub or lobby server. It includes checks to ensure the player is not
 * already on the hub server and handles cases where the hub server is not set.
 */
public class HubCommand extends CommandBase {

    /**
     * Constructs a HubCommand instance with predefined command details.
     */
    public HubCommand() {
        super("hub",
                "",
                "Teleports you to the hub/lobby server.",
                "",
                new String[]{"lobby"}
        );
    }

    /**
     * Executes the "hub" command. Validates the command source, checks if the player
     * is already on the hub server, and teleports the player to the hub server if possible.
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

        // Retrieve the lobby manager and check if the player is already on the hub server.
        var lobbyManager = Nexus.plugin.getLobbyServerManager();
        var server = player.getCurrentServer().orElse(null);
        if (server != null && lobbyManager.isLobbyServer(server.getServer())) {
            MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getLobbyAlreadyIn());
            return;
        }

        // Retrieve the hub server and handle cases where it is not set.
        var lobbyServer = lobbyManager.getLobbyServer();
        if (lobbyServer == null) {
            MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getLobbyNotSet());
            return;
        }

        // Notify the player about the teleportation and schedule the server transfer task.
        MessageUtil.sendRichMsg(player, Nexus.plugin.getMessages().getLobbyTeleporting(), Map.of("server", lobbyServer.getServerInfo().getName()));
        Nexus.plugin.getProxy().getScheduler().buildTask(Nexus.plugin, () -> {
            player.createConnectionRequest(lobbyServer).fireAndForget();
        }).schedule();
    }
}