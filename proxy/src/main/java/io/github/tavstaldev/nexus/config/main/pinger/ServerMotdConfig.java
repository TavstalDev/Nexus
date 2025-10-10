package io.github.tavstaldev.nexus.config.main.pinger;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class ServerMotdConfig {
    private final ServerFavIconConfig icon;
    private final String line1;
    private final String line2;

    public ServerMotdConfig() {
        this.icon = new ServerFavIconConfig();
        this.line1 = "A Velocity Server";
        this.line2 = "Powered by Nexus";
    }

    public ServerMotdConfig(ServerFavIconConfig icon, String line1, String line2) {
        this.icon = icon;
        this.line1 = line1;
        this.line2 = line2;
    }

    public ServerFavIconConfig getIcon() {
        return icon;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }
}
