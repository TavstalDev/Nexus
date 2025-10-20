package io.github.tavstaldev.nexus.command.player;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.NexusConstants;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

/**
 * The NexusCommand class handles the "nexus" command, which serves as the main
 * command for the Nexus plugin. It displays plugin information such as the version
 * and other details to the command source.
 */
public class NexusCommand extends CommandBase {

    /**
     * Constructs a NexusCommand instance with predefined command details.
     */
    public NexusCommand() {
        super("nexus",
                "",
                "Main command for Nexus plugin.",
                "",
                new String[]{"nex"}
        );
    }

    /**
     * Executes the "nexus" command. Sends the plugin information, including the
     * header, body (with version), and footer, to the command source.
     *
     * @param invocation The invocation context of the command, containing the source
     *                   and arguments.
     */
    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();

        // Retrieve the plugin information messages.
        var header = Nexus.plugin.getMessages().getNexusInfoHeader();
        var body = Nexus.plugin.getMessages().getNexusInfoContent();
        var footer = Nexus.plugin.getMessages().getNexusInfoFooter();

        // Send the header, body (with version), and footer to the command source.
        MessageUtil.sendRichMsg(source, header);
        MessageUtil.sendRichMsg(source, body, Map.of(
                "version", NexusConstants.VERSION));
        MessageUtil.sendRichMsg(source, footer);
    }
}