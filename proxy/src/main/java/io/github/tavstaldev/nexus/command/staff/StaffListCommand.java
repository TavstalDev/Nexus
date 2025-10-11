package io.github.tavstaldev.nexus.command.staff;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class StaffListCommand extends CommandBase {
    public StaffListCommand() {
        super("stafflist",
                "",
                "Shows the list of online staffs.",
                "nexus.command.stafflist",
                new String[]{"sl", "onlinestaff", "onlinestaffs"}
        );
    }

    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();
        if (!source.hasPermission(this.permission)) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralNoPermission());
            return;
        }

        var header = Nexus.plugin.getMessages().getStaffListHeader();
        var footer = Nexus.plugin.getMessages().getStaffListFooter();
        var staffs = Nexus.plugin.getStaffManager().getOnlineStaff();
        if (staffs.isEmpty()) {
            MessageUtil.sendRichMsg(source, header);
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getStaffListNoStaff());
            MessageUtil.sendRichMsg(source, footer);
            return;
        }

        MessageUtil.sendRichMsg(source, header);
        var staffEntry = Nexus.plugin.getMessages().getStaffListFormat();
        for (var staff : staffs) {
            MessageUtil.sendRichMsg(source, staffEntry, Map.of(
                    "player", staff.getUsername(),
                    "server", staff.getCurrentServer().isPresent() ? staff.getCurrentServer().get().getServerInfo().getName() : "?????"
            ));
        }
        MessageUtil.sendRichMsg(source, footer);
    }
}
