package io.github.tavstaldev.nexus.command.staff;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * The ReportListCommand class handles the "reportlist" command, which allows staff members
 * to view a paginated list of active player reports.
 */
public class ReportListCommand extends CommandBase {

    /**
     * Constructs a ReportListCommand instance with predefined command details.
     */
    public ReportListCommand() {
        super("reportlist",
                "",
                "Shows the list of active reports.",
                "nexus.command.reportlist",
                new String[]{"rl", "reports"}
        );
    }

    /**
     * Executes the "reportlist" command. Displays a paginated list of active reports
     * to the command source. Handles invalid page numbers and empty report lists.
     *
     * @param invocation The invocation context of the command, containing the source
     *                   and arguments.
     */
    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();
        int page = 1;

        // Parse the page number from the arguments, defaulting to 1 if not provided or invalid.
        if (invocation.arguments().length >= 1) {
            try {
                page = Integer.parseInt(invocation.arguments()[0]);
                if (page < 1)
                    page = 1;
            } catch (NumberFormatException e) {
                MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralInvalidNumber(), Map.of(
                        "number", invocation.arguments()[0]
                ));
                return;
            }
        }

        // Retrieve the list of active reports.
        var reports = new ArrayList<>(Nexus.plugin.getReports());
        if (reports.isEmpty()) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getPlayerReportEmpty());
            return;
        }

        // Calculate pagination details.
        int itemsPerPage = 10;
        int totalPages = (int) Math.ceil((double) reports.size() / itemsPerPage);
        if (page > totalPages)
            page = totalPages;

        // Send the header message.
        MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getPlayerReportHeader());

        // Display the reports for the current page.
        for (int i = 0; i < itemsPerPage; i++) {
            int index = (page - 1) * itemsPerPage + i;
            if (index >= reports.size())
                break;
            var report = reports.get(index);
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getPlayerReportFormat(), Map.of(
                    "player", report.getReporterName(),
                    "reason", report.getReason(),
                    "reported", report.getTargetName()
            ));
        }

        // Send the footer message with pagination details.
        MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getPlayerReportFooter(), Map.of(
                "current", page,
                "max", totalPages
        ));
    }
}