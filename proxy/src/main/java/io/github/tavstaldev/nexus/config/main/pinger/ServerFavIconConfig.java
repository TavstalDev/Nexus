package io.github.tavstaldev.nexus.config.main.pinger;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
public class ServerFavIconConfig {
    @Comment("Should the server favicon be enabled?")
    private boolean enabled;
    @Comment("Path to the server favicon. Use 'random' to select a random icon from the 'favicons' folder.")
    private String path;

    public ServerFavIconConfig() {
        this.enabled = true;
        this.path = "random";
    }

    public ServerFavIconConfig(boolean enabled, String path) {
        this.enabled = enabled;
        this.path = path;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPath() {
        return path;
    }
}
