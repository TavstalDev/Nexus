package io.github.tavstaldev.nexus.config.main;

import io.github.tavstaldev.nexus.config.main.pinger.ServerFavIconConfig;
import io.github.tavstaldev.nexus.config.main.pinger.ServerMotdConfig;
import io.github.tavstaldev.nexus.config.main.pinger.ServerSpooferConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.List;

@ConfigSerializable
public class ServerPingerConfig {
    @Comment("Should the server pinger be enabled?")
    private boolean enabled;
    @Comment("Should the server pinger use a random MOTD from the list below?")
    private List<ServerMotdConfig> motds;

    @Comment("Spoofer settings for the server pinger.")
    private ServerSpooferConfig spoofer;

    @Comment("FavIcon settings for the server pinger.")
    private ServerFavIconConfig favIcon;

    public ServerPingerConfig() {
        this.enabled = true;
        this.motds = List.of(new ServerMotdConfig());
        this.spoofer = new ServerSpooferConfig();
        this.favIcon = new ServerFavIconConfig();
    }

    public ServerPingerConfig(boolean enabled, List<ServerMotdConfig> motds, ServerSpooferConfig spoofer, ServerFavIconConfig favIcon) {
        this.enabled = enabled;
        this.motds = motds;
        this.spoofer = spoofer;
        this.favIcon = favIcon;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<ServerMotdConfig> getMotds() {
        return motds;
    }

    public ServerSpooferConfig getSpoofer() {
        return spoofer;
    }

    public ServerFavIconConfig getFavIcon() {
        return favIcon;
    }
}
