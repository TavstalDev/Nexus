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

public class HelpopCommand extends CommandBase {
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

    private final Cache<@NotNull UUID, @NotNull LocalDateTime> cooldownCache;

    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();
        if (!(source instanceof Player player)) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralPlayerOnly());
            return;
        }

        if (invocation.arguments().length == 0) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                    "syntax", this.syntax,
                    "command", this.baseCommand
            ));
            return;
        }

        var config = Nexus.plugin.getConfig().getHelpop();
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
            cooldownCache.put(player.getUniqueId(), LocalDateTime.now().plusSeconds(config.getCooldown()));
        }

        String message = String.join(" ", invocation.arguments());
        String format = config.getFormat();
        var msg = ChatUtil.buildMessage(format, Map.of(
                "player", player.getUsername(),
                "server", player.getCurrentServer().isPresent() ? player.getCurrentServer().get().getServerInfo().getName() : "?????",
                "message", message
        ));
        String staffPermission = config.getStaffPermission();
        Nexus.plugin.getProxy().getAllPlayers().stream()
                .filter(otherPlayer -> otherPlayer.hasPermission(staffPermission) || otherPlayer.equals(player))
                .forEach(otherPlayer -> otherPlayer.sendMessage(msg));
    }
}
