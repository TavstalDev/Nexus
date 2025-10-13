package io.github.tavstaldev.nexus.command.admin;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.config.maintenance.MaintenancePlayer;
import io.github.tavstaldev.nexus.util.ChatUtil;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MaintenanceCommand extends CommandBase {
    public MaintenanceCommand() {
        super("maintenance",
                "<add|remove|list|on|off|kickall>",
                "Toggles maintenance mode on or off.",
                "nexus.command.maintenance",
                new String[]{"maintain"}
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

        var subcommand = invocation.arguments()[0].toLowerCase();
        var config = Nexus.plugin.getMaintenanceSettings();
        switch (subcommand) {
            case "add": {
                if (invocation.arguments().length != 2) {
                    MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                            "syntax", " add <player>",
                            "command", this.baseCommand
                    ));
                    return;
                }

                var playerName = invocation.arguments()[1];
                if (config.isPlayerAllowed(playerName)) {
                    MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenancePlayerAlreadyAllowed(), Map.of(
                            "player", playerName
                    ));
                    return;
                }

                if (source instanceof Player adder) {
                    config.addPlayer(playerName, adder);
                } else {
                    config.addPlayer(playerName);
                }
                Nexus.plugin.getConfigurationLoader().saveMaintenanceConfig();
                MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenancePlayerAdded(), Map.of(
                        "player", playerName
                ));
                break;
            }
            case "remove": {
                if (invocation.arguments().length != 2) {
                    MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                            "syntax", " remove <player>",
                            "command", this.baseCommand
                    ));
                    return;
                }

                var playerName = invocation.arguments()[1];
                if (!config.isPlayerAllowed(playerName)) {
                    MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenancePlayerNotAllowed(), Map.of(
                            "player", playerName
                    ));
                    return;
                }

                config.removePlayer(playerName);
                Nexus.plugin.getConfigurationLoader().saveMaintenanceConfig();
                MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenancePlayerRemoved(), Map.of(
                        "player", playerName
                ));
                break;
            }
            case "list": {
                int page = 1;
                if (invocation.arguments().length >= 2) {
                    try {
                        page = Integer.parseInt(invocation.arguments()[1]);
                        if (page < 1)
                            page = 1;
                    } catch (NumberFormatException e) {
                        MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralInvalidNumber(), Map.of(
                                "number", invocation.arguments()[1]
                        ));
                        return;
                    }
                }

                var allowedPlayers = config.getPlayers().toArray(new MaintenancePlayer[0]);
                if (allowedPlayers.length == 0) {
                    MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenanceListEmpty());
                    return;
                }

                int itemsPerPage = 15;
                int totalPages = (int) Math.ceil((double) allowedPlayers.length / itemsPerPage);
                if (page > totalPages)
                    page = totalPages;

                MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenanceListHeader());
                for (int i = 0; i < itemsPerPage; i++) {
                    int index = (page - 1) * itemsPerPage + i;
                    if (index >= allowedPlayers.length)
                        break;
                    String playerName = allowedPlayers[index].getName();
                    MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenanceListFormat(), Map.of(
                            "player", playerName
                    ));
                }
                MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenanceListFooter(), Map.of(
                        "current", page,
                        "max", totalPages
                ));
                break;
            }
            case "on": {
                if (config.isEnabled()) {
                    MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenanceAlreadyEnabled());
                    return;
                }

                config.setEnabled(true);
                if (source instanceof Player player) {
                    if (!config.isPlayerAllowed(player)) {
                        config.addPlayer(player.getUsername());
                    }
                }
                Nexus.plugin.getConfigurationLoader().saveMaintenanceConfig();
                MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenanceEnabled());
                break;
            }
            case "off": {
                if (!config.isEnabled()) {
                    MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenanceAlreadyDisabled());
                    return;
                }

                config.setEnabled(false);
                Nexus.plugin.getConfigurationLoader().saveMaintenanceConfig();
                MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getMaintenanceDisabled());
                break;
            }
            case "kickall": {
                var serializedFormat = ChatUtil.translateColors(String.join("\n", Nexus.plugin.getMessages().getMaintenanceKickMessage()), true);
                for (var player : Nexus.plugin.getProxy().getAllPlayers()) {
                    if (!config.isPlayerAllowed(player)) {
                        player.disconnect(serializedFormat);
                    }
                }
                break;
            }
            default:
            {
                MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                        "syntax", this.syntax,
                        "command", this.baseCommand
                ));
                break;
            }
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        var args = invocation.arguments();
        switch (args.length)
        {
            case 0: {
                List<String> commandList = new ArrayList<>(List.of("add", "remove", "list", "on", "off", "kickall"));
                return CompletableFuture.supplyAsync(() -> commandList);
            }
            case 1: {
                List<String> commandList = new ArrayList<>(List.of("add", "remove", "list", "on", "off", "kickall"));
                commandList.removeIf(cmd -> !cmd.toLowerCase().startsWith(args[0].toLowerCase()));
                return CompletableFuture.supplyAsync(() -> commandList);
            }
            case 2: {
                String subcommand = args[0].toLowerCase();
                switch (subcommand) {
                    case "add": {
                        List<String> commandList = new ArrayList<>();
                        Nexus.plugin.getProxy().getAllPlayers().forEach(x -> {
                            commandList.add(x.getUsername());
                        });
                        return CompletableFuture.supplyAsync(() -> commandList);
                    }
                    case "remove": {
                        List<String> commandList = new ArrayList<>();
                        var config = Nexus.plugin.getMaintenanceSettings();
                        var allowedPlayers = config.getPlayers().toArray(new MaintenancePlayer[0]);
                        for (var p : allowedPlayers) {
                            commandList.add(p.getName());
                        }
                        return CompletableFuture.supplyAsync(() -> commandList);
                    }
                }
            }
        }
        return super.suggestAsync(invocation);
    }
}
