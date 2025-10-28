package io.github.tavstaldev.nexus.models;

import com.velocitypowered.api.proxy.Player;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;

/**
 * Implementation of the IPrefixHelper interface using LuckPerms.
 * This class provides methods to retrieve player prefixes and suffixes
 * based on their LuckPerms group metadata.
 */
public class LuckPrefix implements IPrefixHelper {
    private final LuckPerms luckPerms;

    /**
     * Constructs a LuckPrefix instance with the specified LuckPerms instance.
     *
     * @param luckPerms The LuckPerms instance used to retrieve user and group data.
     */
    public LuckPrefix(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    /**
     * Retrieves the prefix of the specified player based on their LuckPerms group metadata.
     *
     * @param player The player whose prefix is to be retrieved.
     * @return The prefix of the player, or an empty string if no prefix is found.
     */
    @Override
    public String getPrefix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return "";
        }
        var group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) {
            return "";
        }
        var meta = group.getCachedData().getMetaData();
        return meta.getPrefix() != null ? meta.getPrefix() : "";
    }

    /**
     * Retrieves the suffix of the specified player based on their LuckPerms group metadata.
     *
     * @param player The player whose suffix is to be retrieved.
     * @return The suffix of the player, or an empty string if no suffix is found.
     */
    @Override
    public String getSuffix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return "";
        }

        var group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) {
            return "";
        }
        var meta = group.getCachedData().getMetaData();
        return meta.getSuffix() != null ? meta.getSuffix() : "";
    }
}