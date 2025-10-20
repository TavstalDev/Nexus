package io.github.tavstaldev.nexus.command.admin;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The SendPlayerCommand class handles the "sendplayer" command, which allows
 * administrators to send a player to a specified server.
 */
public class SendPlayerCommand extends CommandBase {

    /**
     * Constructs a SendPlayerCommand instance with predefined command details.
     */
    public SendPlayerCommand() {
        super("sendplayer",
                "<player> <server>",
                "Sends a player to a specified server.",
                "nexus.command.sendplayer",
                new String[]{"moveplayer"}
        );
    }

    /**
     * Executes the "sendplayer" command. Validates the input arguments, retrieves
     * the target player and server, and sends the player to the specified server.
     *
     * @param invocation The invocation context of the command, containing the source
     *                   and arguments.
     */
    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();

        // Check if the correct number of arguments is provided.
        if (invocation.arguments().length != 2) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                    "syntax", this.syntax,
                    "command", this.baseCommand
            ));
            return;
        }

        // Retrieve the target player from the arguments.
        var playerName = invocation.arguments()[0];
        var targetPlayer = Nexus.plugin.getProxy().getPlayer(playerName).orElse(null);
        if (targetPlayer == null) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralPlayerNotFound());
            return;
        }

        // Retrieve the target server from the arguments.
        var serverName = invocation.arguments()[1];
        var server = Nexus.plugin.getProxy().getServer(serverName).orElse(null);
        if (server == null) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralInvalidServer(), Map.of(
                    "server", serverName
            ));
            return;
        }

        // Notify the target player and the command source about the server transfer.
        MessageUtil.sendRichMsg(targetPlayer, Nexus.plugin.getMessages().getSendPlayerTarget(), Map.of(
                "server", server.getServerInfo().getName())
        );
        MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getSendPlayerSender(), Map.of(
                "player", targetPlayer.getUsername(),
                "server", server.getServerInfo().getName()
        ));

        // Schedule the server transfer task.
        Nexus.plugin.getProxy().getScheduler().buildTask(Nexus.plugin, () -> {
            targetPlayer.createConnectionRequest(server).fireAndForget();
        }).schedule();
    }

    /**
     * Provides asynchronous suggestions for the "sendplayer" command.
     * Suggestions include player names for the first argument and server names
     * for the second argument, filtered based on the current input.
     *
     * @param invocation The invocation context of the command, containing the source
     *                   and arguments.
     * @return A CompletableFuture containing a list of suggested player or server names.
     */
    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        var args = invocation.arguments();
        switch (args.length) {
            case 0: {
                // Suggests all online player names.
                List<String> commandList = new ArrayList<>();
                Nexus.plugin.getProxy().getAllPlayers().forEach(x -> {
                    commandList.add(x.getUsername());
                });
                return CompletableFuture.supplyAsync(() -> commandList);
            }
            case 1: {
                // Filters player name suggestions based on input.
                List<String> commandList = new ArrayList<>();
                Nexus.plugin.getProxy().getAllPlayers().forEach(x -> {
                    commandList.add(x.getUsername());
                });
                commandList.removeIf(cmd -> !cmd.toLowerCase().startsWith(args[0].toLowerCase()));
                return CompletableFuture.supplyAsync(() -> commandList);
            }
            case 2: {
                // Suggests server names for the second argument.
                List<String> commandList = new ArrayList<>();
                Nexus.plugin.getProxy().getAllServers().forEach(x -> {
                    commandList.add(x.getServerInfo().getName());
                });
                commandList.removeIf(cmd -> !cmd.toLowerCase().startsWith(args[1].toLowerCase()));
                return CompletableFuture.supplyAsync(() -> commandList);
            }
        }
        return super.suggestAsync(invocation);
    }
}