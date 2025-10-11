package io.github.tavstaldev.nexus.command;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.managers.CustomChatManager;
import io.github.tavstaldev.nexus.util.ChatUtil;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

public class CustomChatCommand extends CommandBase {
    private final String chatName;
    public CustomChatCommand(String chatName, String baseCommand, String permission, String[] aliases) {
        super(baseCommand,
                " <message>",
                "",
                permission,
                aliases
        );
        this.chatName = chatName;
    }

    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();
        if (!(source instanceof Player player)) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralPlayerOnly());
            return;
        }

        var customChat = Nexus.plugin.getConfig().getCustomChatByName(chatName);
        if (customChat == null) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralErrorOccurred());
            return;
        }

        var args = invocation.arguments();
        if (args.length < 1) {
            if (!customChat.isAllowToggle()) {
                MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralCommandSyntax(), Map.of(
                        "syntax", this.syntax,
                        "command", this.baseCommand
                ));
                return;
            }
            CustomChatManager.toggleChat(player, chatName);
            return;
        }



        boolean requiredPermission = customChat.isRequirePermission();
        String chatPermission = customChat.getPermission();
        String format = customChat.getFormat();
        var message = String.join(" ", args);
        var msg = ChatUtil.buildMessage(format, Map.of(
                "player", player.getUsername(),
                "message", message
        ));


        Nexus.plugin.getProxy().getAllPlayers().stream()
                .filter(otherPlayer -> (!requiredPermission || otherPlayer.hasPermission(chatPermission)) || otherPlayer.equals(player))
                .forEach(otherPlayer -> otherPlayer.sendMessage(msg));
    }
}
