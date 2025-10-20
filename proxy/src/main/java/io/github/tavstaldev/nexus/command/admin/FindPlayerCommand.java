package io.github.tavstaldev.nexus.command.admin;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The FindPlayerCommand class is responsible for locating the server
 * a specific player is currently on. It extends the CommandBase class
 * and provides the implementation for the "findplayer" command.
 */
public class FindPlayerCommand extends CommandBase {

    /**
     * Constructs a FindPlayerCommand instance with predefined command details.
     */
    public FindPlayerCommand() {
        super("findplayer",
                "<player>",
                "Finds which server a player is on.",
                "nexus.command.findplayer",
                new String[]{"whereis", "where"}
        );
    }

    /**
     * Executes the "findplayer" command. If the player is found, it retrieves
     * the server the player is currently on and sends the information to the
     * command source. If the player is not found or no server is associated,
     * appropriate error messages are sent.
     *
     * @param invocation The invocation context of the command, containing the source
     *                   and arguments.
     */
    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();

        // Check if the correct number of arguments is provided.
        if (invocation.arguments().length != 1) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                    "syntax", this.syntax,
                    "command", this.baseCommand
            ));
            return;
        }

        // Retrieve the player name from the arguments.
        var playerName = invocation.arguments()[0];
        var player = Nexus.plugin.getProxy().getPlayer(playerName).orElse(null);

        // Handle the case where the player is not found.
        if (player == null) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralPlayerNotFound());
            return;
        }

        // Retrieve the server the player is currently on.
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

    /**
     * Provides asynchronous suggestions for the "findplayer" command.
     * The suggestions include a list of online player usernames, filtered
     * based on the current input.
     *
     * @param invocation The invocation context of the command, containing the source
     *                   and arguments.
     * @return A CompletableFuture containing a list of suggested player names.
     */
    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        var args = invocation.arguments();

        // Handle suggestions based on the number of arguments provided.
        switch (args.length) {
            case 0: {
                List<String> commandList = new ArrayList<>();
                Nexus.plugin.getProxy().getAllPlayers().forEach(x -> {
                    commandList.add(x.getUsername());
                });
                return CompletableFuture.supplyAsync(() -> commandList);
            }
            case 1: {
                List<String> commandList = new ArrayList<>();
                Nexus.plugin.getProxy().getAllPlayers().forEach(x -> {
                    commandList.add(x.getUsername());
                });
                // Filter suggestions based on the input.
                commandList.removeIf(cmd -> !cmd.toLowerCase().startsWith(args[0].toLowerCase()));
                return CompletableFuture.supplyAsync(() -> commandList);
            }
        }
        return super.suggestAsync(invocation);
    }
}