package io.github.tavstaldev.nexus.config.main.pinger;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
public class ServerSpooferConfig {
    @Comment("Should the server spoofer be enabled?")
    private boolean enabled;
    @Comment("Should the player hover list be disabled?")
    private boolean disablePlayerHoverList;
    @Comment("Should the player count be hidden?")
    private boolean hidePlayerCount;
    @Comment("List of servers to calculate the player count from. If empty, all connected servers will be used.")
    private String[] targetServers;

    public ServerSpooferConfig() {
        enabled = false;
        disablePlayerHoverList = false;
        hidePlayerCount = false;
        targetServers = new String[0];
    }

    public ServerSpooferConfig(boolean enabled, boolean disablePlayerHoverList, boolean hidePlayerCount, String[] targetServers) {
        this.enabled = enabled;
        this.disablePlayerHoverList = disablePlayerHoverList;
        this.hidePlayerCount = hidePlayerCount;
        this.targetServers = targetServers;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean disablePlayerHoverList() {
        return disablePlayerHoverList;
    }

    public boolean hidePlayerCount() {
        return hidePlayerCount;
    }

    public String[] getTargetServers() {
        return targetServers;
    }
}
