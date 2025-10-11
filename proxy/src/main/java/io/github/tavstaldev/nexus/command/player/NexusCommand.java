package io.github.tavstaldev.nexus.command.player;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.NexusConstants;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

public class NexusCommand extends CommandBase {
    public NexusCommand() {
        super("nexus",
                "",
                "Main command for Nexus plugin.",
                "",
                new String[]{"nex"}
        );
    }

    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();
        var header = Nexus.plugin.getMessages().getNexusInfoHeader();
        var body = Nexus.plugin.getMessages().getNexusInfoContent();
        var footer = Nexus.plugin.getMessages().getNexusInfoFooter();
        MessageUtil.sendRichMsg(source, header);
        MessageUtil.sendRichMsg(source, body, Map.of(
                "version", NexusConstants.VERSION));
        MessageUtil.sendRichMsg(source, footer);
    }
}
