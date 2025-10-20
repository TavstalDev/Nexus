package io.github.tavstaldev.nexus.managers;

import com.velocitypowered.api.util.Favicon;
import io.github.tavstaldev.nexus.Nexus;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The FavIconManager class is responsible for managing server icons (favicons) in the Nexus plugin.
 * It handles loading icons from a directory, validating their dimensions, and providing methods
 * to retrieve specific or random icons.
 */
public class FavIconManager {
    // The directory where the icons are stored.
    private final Path iconsDir;

    // A map to store loaded icons, where the key is the file name and the value is the Favicon object.
    private final Map<String, Favicon> icons = new HashMap<>();

    /**
     * Constructs a FavIconManager and initializes the icons directory.
     * If the directory does not exist, it is created.
     */
    public FavIconManager() {
        iconsDir = Nexus.plugin.getDataFolder().resolve("icons");
        if (!iconsDir.toFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            iconsDir.toFile().mkdirs();
        }
    }

    /**
     * Loads all icons from the icons directory. Only files with a .png extension are considered.
     * Icons are cleared before loading to ensure no duplicates exist.
     * Logs an error message if the loading process fails.
     */
    public void loadIcons() {
        icons.clear();

        try (var paths = Files.list(iconsDir)) {
            paths.filter(path -> {
                String fileName = path.getFileName().toString().toLowerCase();
                return fileName.endsWith(".png");
            }).forEach(this::loadIcon);
        } catch (Exception e) {
            Nexus.plugin.getLogger().error("Failed to load icons: " + e.getMessage());
        }
    }

    /**
     * Retrieves a Favicon object based on the specified path.
     * If the path is null, empty, or "random", a random icon is returned.
     * If no icons are loaded, null is returned.
     *
     * @param path The name of the icon file or "random" to get a random icon.
     * @return The Favicon object, or null if no icons are available.
     */
    public @Nullable Favicon getIcon(String path) {
        if (icons.isEmpty()) {
            return null;
        }

        if (path == null || path.isEmpty() || path.equalsIgnoreCase("random")) {
            int randomIndex = ThreadLocalRandom.current().nextInt(icons.size());
            return (Favicon) icons.values().toArray()[randomIndex];
        }
        return icons.get(path);
    }

    /**
     * Loads a single icon from the specified path. Validates that the image dimensions are 64x64 pixels.
     * If the dimensions are invalid or the loading process fails, a warning or error is logged.
     *
     * @param path The path to the icon file.
     */
    private void loadIcon(Path path) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            var bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage.getHeight() != 64 || bufferedImage.getWidth() != 64) {
                Nexus.plugin.getLogger().warn("Failed to load icon, the image size must be 64x64: " + path.getFileName().toString());
                return;
            }

            Favicon icon = Favicon.create(bufferedImage);
            icons.put(path.getFileName().toString(), icon);
        } catch (Exception e) {
            Nexus.plugin.getLogger().error("Failed to load icon: " + path.getFileName().toString() + " - " + e.getMessage());
        }
    }
}