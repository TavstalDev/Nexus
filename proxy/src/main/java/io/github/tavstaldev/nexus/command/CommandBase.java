package io.github.tavstaldev.nexus.command;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import io.github.tavstaldev.nexus.Nexus;

/**
 * The CommandBase class serves as an abstract base for all commands in the Nexus plugin.
 * It provides common functionality such as command registration, unregistration, and permission checks.
 */
public abstract class CommandBase implements SimpleCommand {
    /**
     * The base name of the command.
     */
    protected String baseCommand;

    /**
     * The syntax of the command, describing its usage.
     */
    protected String syntax;

    /**
     * A brief description of the command.
     */
    protected String description;

    /**
     * The permission required to execute the command.
     */
    protected String permission;

    /**
     * An array of aliases for the command.
     */
    protected String[] aliases;

    /**
     * Constructs a CommandBase instance with the specified command details.
     *
     * @param baseCommand The base name of the command.
     * @param syntax      The syntax of the command.
     * @param description A brief description of the command.
     * @param permission  The permission required to execute the command.
     * @param aliases     An array of aliases for the command.
     */
    public CommandBase(String baseCommand, String syntax, String description, String permission, String[] aliases) {
        this.baseCommand = baseCommand;
        this.syntax = syntax;
        this.description = description;
        this.permission = permission;
        this.aliases = aliases;
    }

    /**
     * Registers the command with the Velocity command manager.
     */
    public void register() {
        CommandManager commandManager = Nexus.plugin.getProxy().getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder(baseCommand)
                .aliases(aliases)
                .plugin(Nexus.plugin)
                .build();
        commandManager.register(commandMeta, this);
        Nexus.plugin.getLogger().info("Registered command: " + baseCommand);
    }

    /**
     * Unregisters the command from the Velocity command manager.
     */
    public void unregister() {
        CommandManager commandManager = Nexus.plugin.getProxy().getCommandManager();
        commandManager.unregister(baseCommand);
        Nexus.plugin.getLogger().info("Unregistered command: " + baseCommand);
    }

    /**
     * Checks if the command source has the required permission to execute the command.
     *
     * @param invocation The invocation context of the command, containing the source and arguments.
     * @return True if the source has the required permission, or if no permission is required; otherwise, false.
     */
    @Override
    public boolean hasPermission(final Invocation invocation) {
        if (permission == null || permission.isEmpty()) {
            return true;
        }
        return invocation.source().hasPermission(permission);
    }
}