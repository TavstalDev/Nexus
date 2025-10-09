package io.github.tavstaldev.nexus;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
    id = "nexusproxy",
    name = "nexusProxy",
    version = "1.0.0"
    ,url = "https://tavstaldev.github.io"
    ,authors = {"Tavstal"}
)
public class Nexus {
    public static Nexus plugin;
    private final ProxyServer proxy;
    private final Path dataFolder;
    private ScheduledTask reportTask;
    private ScheduledTask announcementsTask;
    @Inject private Logger logger;

    public Nexus(ProxyServer proxy, Path dataFolder) {
        plugin = this;
        this.proxy = proxy;
        this.dataFolder = dataFolder;

    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
