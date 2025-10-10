package io.github.tavstaldev.nexus.command;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.SimpleCommand;
import io.github.tavstaldev.nexus.Nexus;

public abstract class CommandBase implements SimpleCommand {
    protected String baseCommand;
    protected String syntax;
    protected String description;
    protected String permission;
    protected String[] aliases;

    public CommandBase(String baseCommand, String syntax, String description, String permission, String... aliases) {
        this.baseCommand = baseCommand;
        this.syntax = syntax;
        this.description = description;
        this.permission = permission;
        this.aliases = aliases;
    }

    public void register() {
        CommandManager commandManager = Nexus.plugin.getProxy().getCommandManager();
        commandManager.register(commandManager.metaBuilder(baseCommand)
                .aliases(aliases)
                .build(), this);
        Nexus.plugin.getLogger().debug("Registered command: " + baseCommand);
    }

    public void unregister() {
        CommandManager commandManager = Nexus.plugin.getProxy().getCommandManager();
        commandManager.unregister(baseCommand);
        Nexus.plugin.getLogger().debug("Unregistered command: " + baseCommand);
    }

    @Override
    public abstract void execute(Invocation invocation);
}
