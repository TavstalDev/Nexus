package io.github.tavstaldev.nexus.models;

import com.velocitypowered.api.proxy.Player;

public interface IPrefixHelper {
    String getPrefix(Player player);

    String getSuffix(Player player);
}
