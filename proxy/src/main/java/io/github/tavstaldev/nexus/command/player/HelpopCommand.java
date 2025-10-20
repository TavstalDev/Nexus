package io.github.tavstaldev.nexus.command.player;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.ChatUtil;
import io.github.tavstaldev.nexus.util.MessageUtil;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The HelpopCommand class handles the "helpop" command, which allows players
 * to send help requests to online staff members. It includes a cooldown mechanism
 * to prevent spamming.
 */
public class HelpopCommand extends CommandBase {

    /**
     * A cache to store player cooldowns, mapping player UUIDs to the next allowed usage time.
     */
    private final Cache<@NotNull UUID, @NotNull LocalDateTime> cooldownCache;

    /**
     * Constructs a HelpopCommand instance with the specified permission and aliases.
     * Initializes the cooldown cache based on the configuration.
     *
     * @param permission The permission required to execute the command.
     * @param aliases    An array of aliases for the command.
     */
    public HelpopCommand(String permission, String[] aliases) {
        super("helpop",
                "<message>",
                "Sends a help request to online staff members.",
                permission,
                aliases
        );
        long cooldownSeconds = Nexus.plugin.getConfig().getHelpop().getCooldown();
        this.cooldownCache = CacheBuilder.newBuilder()
                .expireAfterWrite(cooldownSeconds, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Executes the "helpop" command. Validates the command source, checks cooldowns,
     * and sends the help request to staff members.
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

        // Ensure the command has arguments.
        if (invocation.arguments().length == 0) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                    "syntax", this.syntax,
                    "command", this.baseCommand
            ));
            return;
        }

        // Retrieve the Helpop configuration.
        var config = Nexus.plugin.getConfig().getHelpop();

        // Check if the player is on cooldown.
        if (config.getCooldown() > 0) {
            var nextAllowedUseTime = cooldownCache.getIfPresent(player.getUniqueId());
            if (nextAllowedUseTime != null) {
                if (LocalDateTime.now().isBefore(nextAllowedUseTime)) {
                    long timeLeft = Duration.between(LocalDateTime.now(), nextAllowedUseTime).getSeconds();
                    MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCooldown(), Map.of(
                            "time", String.valueOf(timeLeft)
                    ));
                    return;
                }
            }
            // Update the cooldown cache with the next allowed usage time.
            cooldownCache.put(player.getUniqueId(), LocalDateTime.now().plusSeconds(config.getCooldown()));
        }

        // Build the help request message.
        String message = String.join(" ", invocation.arguments());
        String format = config.getFormat();
        var msg = ChatUtil.buildMessage(format, Map.of(
                "player", player.getUsername(),
                "server", player.getCurrentServer().isPresent() ? player.getCurrentServer().get().getServerInfo().getName() : "?????",
                "message", message
        ));

        // Send the message to staff members and the player.
        String staffPermission = config.getStaffPermission();
        Nexus.plugin.getProxy().getAllPlayers().stream()
                .filter(otherPlayer -> otherPlayer.hasPermission(staffPermission) || otherPlayer.equals(player))
                .forEach(otherPlayer -> otherPlayer.sendMessage(msg));
    }
}