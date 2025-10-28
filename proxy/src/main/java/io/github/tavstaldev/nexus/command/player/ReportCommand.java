package io.github.tavstaldev.nexus.command.player;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.config.reporting.Report;
import io.github.tavstaldev.nexus.util.ChatUtil;
import io.github.tavstaldev.nexus.util.MessageUtil;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * The ReportCommand class handles the "report" command, which allows players
 * to report other players for misconduct. It includes a cooldown mechanism
 * to prevent spamming and notifies staff members of the report.
 */
public class ReportCommand extends CommandBase {

    /**
     * A cache to store player cooldowns, mapping player UUIDs to the next allowed usage time.
     */
    private final Cache<@NotNull UUID, @NotNull LocalDateTime> cooldownCache;

    /**
     * Constructs a ReportCommand instance with the specified permission and aliases.
     * Initializes the cooldown cache based on the configuration.
     *
     * @param permission The permission required to execute the command.
     * @param aliases    An array of aliases for the command.
     */
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

    /**
     * Executes the "report" command. Validates the input arguments, checks cooldowns,
     * and sends the report to staff members while saving it in the configuration.
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

        var args = invocation.arguments();

        // Ensure the correct number of arguments is provided.
        if (args.length < 2) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                    "syntax", this.syntax,
                    "command", this.baseCommand)
            );
            return;
        }

        // Retrieve the reported player from the arguments.
        Player reported = Nexus.plugin.getProxy().getPlayer(args[0]).orElse(null);
        if (reported == null) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralPlayerNotFound(), Map.of(
                    "player", args[0]
            ));
            return;
        }

        // Prevent players from reporting themselves.
        if (reported.equals(player)) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getPlayerReportSelf());
            return;
        }

        // Retrieve the report configuration.
        var config = Nexus.plugin.getConfig().getPlayerReport();

        // Prevent reporting players with bypass permission.
        if (reported.hasPermission(config.getBypassPermission())) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getPlayerReportBypass());
            return;
        }

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

        // Build the report reason and format the message.
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        String format = config.getFormat();

        String reporterName = player.getUsername();
        String reportedName = reported.getUsername();
        String serverName = player.getCurrentServer().isPresent() ? player.getCurrentServer().get().getServerInfo().getName() : "?????";

        // Save the report in the configuration.
        Nexus.plugin.getReportData().add(new Report(reporterName, player.getUniqueId(), reportedName, reported.getUniqueId(), reason, serverName, System.currentTimeMillis()));
        Nexus.plugin.getConfigurationLoader().saveReports();

        // Notify staff members and the reporting player about the report.
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

    /**
     * Provides asynchronous suggestions for the "report" command.
     * Suggestions include predefined report templates filtered based on the current input.
     *
     * @param invocation The invocation context of the command, containing the source
     *                   and arguments.
     * @return A CompletableFuture containing a list of suggested report templates.
     */
    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        var args = invocation.arguments();
        if (args.length != 2) {
            return super.suggestAsync(invocation);
        }

        // Suggest report templates based on the current input.
        List<String> commandList = new ArrayList<>(Nexus.plugin.getConfig().getPlayerReport().getReportTemplates());
        commandList.removeIf(cmd -> !cmd.toLowerCase().startsWith(args[1].toLowerCase()));
        return CompletableFuture.supplyAsync(() -> commandList);
    }
}