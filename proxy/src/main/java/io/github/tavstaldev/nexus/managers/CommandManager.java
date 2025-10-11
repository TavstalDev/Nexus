package io.github.tavstaldev.nexus.managers;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.command.player.HelpopCommand;
import io.github.tavstaldev.nexus.command.player.HubCommand;
import io.github.tavstaldev.nexus.command.player.NexusCommand;
import io.github.tavstaldev.nexus.command.player.ReportCommand;
import io.github.tavstaldev.nexus.command.staff.StaffListCommand;

import java.util.HashSet;
import java.util.Set;

public class CommandManager {
    private final Set<CommandBase> registeredCommands = new HashSet<>();

    // TODO: Implement command registration and unregistration
    // Make commands extend CommandBase and register them here
    // Commands: nexus, alert, helpop, report, hub/lobby, stafflist customChat, maintenance
    // findplayer, sendplayer, helpmsg/helpreply

    public void registerCommands() {
        registeredCommands.clear();
        // Main command
        registeredCommands.add(new NexusCommand());
        var config = Nexus.plugin.getConfig();
        // Helpop command
        var helpopConfig = config.getHelpop();
        if (helpopConfig.isEnabled()) {
            registeredCommands.add(new HelpopCommand(helpopConfig.getPermission(), helpopConfig.getAliases().toArray(new String[0])));
        }
        // Report command
        var reportConfig = config.getPlayerReport();
        if (reportConfig.isEnabled()) {
            registeredCommands.add(new ReportCommand(reportConfig.getPermission(), reportConfig.getAliases().toArray(new String[0])));
        }
        // Hub command
        registeredCommands.add(new HubCommand());
        // Staff list command
        registeredCommands.add(new StaffListCommand());
        for (CommandBase command : registeredCommands) {
            command.register();
        }
    }

    public void unregisterCommands() {
        for (CommandBase command : registeredCommands) {
            command.unregister();
        }
        registeredCommands.clear();
    }
}
