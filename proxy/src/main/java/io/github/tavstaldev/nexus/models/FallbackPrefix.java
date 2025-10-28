package io.github.tavstaldev.nexus.models;

import com.velocitypowered.api.proxy.Player;

/**
 * Fallback implementation of the IPrefixHelper interface.
 * This class provides methods to retrieve player prefixes and suffixes,
 * which are currently set to return empty strings.
 */
public class FallbackPrefix implements IPrefixHelper {

    /**
     * Retrieves the prefix of the specified player.
     * This implementation always returns an empty string.
     *
     * @param player The player whose prefix is to be retrieved.
     * @return An empty string as the prefix.
     */
    @Override
    public String getPrefix(Player player) {
        return "";
    }

    /**
     * Retrieves the suffix of the specified player.
     * This implementation always returns an empty string.
     *
     * @param player The player whose suffix is to be retrieved.
     * @return An empty string as the suffix.
     */
    @Override
    public String getSuffix(Player player) {
        return "";
    }
}
