package io.github.tavstaldev.nexus.command.player;

import com.velocitypowered.api.proxy.Player;
import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

public class HubCommand extends CommandBase {
    public HubCommand() {
        super("hub",
                "",
                "Teleports you to the hub/lobby server.",
                "",
                new String[]{"lobby"}
        );
    }

    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();
        if (!(source instanceof Player player)) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralPlayerOnly());
            return;
        }

        // TODO
    }
}
