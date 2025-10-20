package io.github.tavstaldev.nexus.command.staff;

import io.github.tavstaldev.nexus.Nexus;
import io.github.tavstaldev.nexus.command.CommandBase;
import io.github.tavstaldev.nexus.util.MessageUtil;

import java.util.Map;

/**
 * The StaffListCommand class handles the "stafflist" command, which displays
 * a list of online staff members to the command source. It includes permission
 * checks and handles cases where no staff members are online.
 */
public final class StaffListCommand extends CommandBase {

    /**
     * Constructs a StaffListCommand instance with predefined command details.
     */
    public StaffListCommand() {
        super("stafflist",
                "",
                "Shows the list of online staffs.",
                "nexus.command.stafflist",
                new String[]{"sl", "onlinestaff", "onlinestaffs"}
        );
    }

    /**
     * Executes the "stafflist" command. Checks if the source has the required permission,
     * retrieves the list of online staff members, and sends the formatted list to the source.
     * If no staff members are online, an appropriate message is displayed.
     *
     * @param invocation The invocation context of the command, containing the source
     *                   and arguments.
     */
    @Override
    public void execute(final Invocation invocation) {
        var source = invocation.source();

        // Check if the source has the required permission.
        if (!source.hasPermission(this.permission)) {
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getGeneralNoPermission());
            return;
        }

        // Retrieve the header, footer, and list of online staff members.
        var header = Nexus.plugin.getMessages().getStaffListHeader();
        var footer = Nexus.plugin.getMessages().getStaffListFooter();
        var staffs = Nexus.plugin.getStaffManager().getOnlineStaff();

        // Handle the case where no staff members are online.
        if (staffs.isEmpty()) {
            MessageUtil.sendRichMsg(source, header);
            MessageUtil.sendRichMsg(source, Nexus.plugin.getMessages().getStaffListNoStaff());
            MessageUtil.sendRichMsg(source, footer);
            return;
        }

        // Send the header message.
        MessageUtil.sendRichMsg(source, header);

        // Format and send the list of online staff members.
        var staffEntry = Nexus.plugin.getMessages().getStaffListFormat();
        for (var staff : staffs) {
            MessageUtil.sendRichMsg(source, staffEntry, Map.of(
                    "player", staff.getUsername(),
                    "server", staff.getCurrentServer().isPresent() ? staff.getCurrentServer().get().getServerInfo().getName() : "?????"
            ));
        }

        // Send the footer message.
        MessageUtil.sendRichMsg(source, footer);
    }
}