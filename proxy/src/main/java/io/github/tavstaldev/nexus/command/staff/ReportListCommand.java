package io.github.tavstaldev.nexus.command.staff;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.ArrayList;
import java.util.Map;

public class ReportListCommand extends CommandBase {
    public ReportListCommand() {
        super("reportlist",
                "",
                "Shows the list of active reports.",
                "nexus.command.reportlist",
                new String[]{"rl", "reports"}
        );
    }

    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();
        int page = 1;
        if (invocation.arguments().length >= 1) {
            try {
                page = Integer.parseInt(invocation.arguments()[1]);
                if (page < 1)
                    page = 1;
            } catch (NumberFormatException e) {
                MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralInvalidNumber(), Map.of(
                        "number", invocation.arguments()[1]
                ));
                return;
            }
        }

        var reports = new ArrayList<>(Nexus.plugin.getReports());
        if (reports.isEmpty()) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getPlayerReportEmpty());
            return;
        }

        int itemsPerPage = 10;
        int totalPages = (int) Math.ceil((double) reports.size() / itemsPerPage);
        if (page > totalPages)
            page = totalPages;

        MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getPlayerReportHeader());
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
        MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getPlayerReportFooter(), Map.of(
                "current", page,
                "max", totalPages
        ));
    }
}
