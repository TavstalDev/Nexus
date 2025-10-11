package io.github.tavstaldev.nexus.command.player;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.config.Report;
import io.github.tavstaldev.nexus.util.ChatUtil;
import io.github.tavstaldev.nexus.util.MessageUtil;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ReportCommand extends CommandBase {
    public ReportCommand(String permission, String[] aliases) {
        super("report",
                "<player> <reason>",
                "Report a player for misconduct.",
                permission,
                aliases
        );
        long cooldownSeconds = Nexus.plugin.getConfig().getPlayerReport().getCooldown();
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

        var args = invocation.arguments();
        if (args.length < 2) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                    "syntax", this.syntax,
                    "command", this.baseCommand)
            );
            return;
        }

        Player reported = Nexus.plugin.getProxy().getPlayer(args[0]).orElse(null);
        if (reported == null) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralPlayerNotFound(), Map.of(
                    "player", args[0]
            ));
            return;
        }

        if (reported.equals(player)) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getPlayerReportSelf());
            return;
        }

        var config = Nexus.plugin.getConfig().getPlayerReport();
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

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        String format = config.getFormat();

        String reporterName = player.getUsername();
        String reportedName = reported.getUsername();
        String serverName = player.getCurrentServer().isPresent() ? player.getCurrentServer().get().getServerInfo().getName() : "?????";

        Nexus.plugin.getReports().add(new Report(reporterName, player.getUniqueId(), reportedName, reported.getUniqueId(), reason, serverName, System.currentTimeMillis()));
        Nexus.plugin.getConfigurationLoader().saveReports();

        var msg = ChatUtil.buildMessage(format, Map.of(
                "player", reporterName,
                "reported", reportedName,
                "server", serverName,
                "reason", reason
        ));
        String staffPermission = config.getNotifyPermission();
        Nexus.plugin.getProxy().getAllPlayers().stream()
                .filter(otherPlayer -> otherPlayer.hasPermission(staffPermission) || otherPlayer.equals(player))
                .forEach(otherPlayer -> otherPlayer.sendMessage(msg));
    }
}
