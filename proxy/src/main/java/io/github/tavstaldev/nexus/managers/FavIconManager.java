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

public class FavIconManager {
    private final Path ICONS_DIR;
    private final Map<String, Favicon> icons = new HashMap<>();

    public FavIconManager() {
        ICONS_DIR = Nexus.plugin.getDataFolder().resolve("icons");
        if (!ICONS_DIR.toFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            ICONS_DIR.toFile().mkdirs();
        }
    }

    public void loadIcons() {
        icons.clear();

        try (var paths = Files.list(ICONS_DIR)) {
            paths.filter(path -> {
                String fileName = path.getFileName().toString().toLowerCase();
                return fileName.endsWith(".png");
            }).forEach(this::loadIcon);
        } catch (Exception e) {
            Nexus.plugin.getLogger().error("Failed to load icons: " + e.getMessage());
        }
    }

    public @Nullable Favicon getIcon(String path) {
        if (icons.isEmpty()) {
            return null;
        }

        if (path == null || path.isEmpty() || path.equalsIgnoreCase("random")) {
            int randomIndex = ThreadLocalRandom.current().nextInt(icons.size());
            return (Favicon)icons.values().toArray()[randomIndex];
        }
        return icons.get(path);
    }

    private void loadIcon(Path path) {
        try(InputStream inputStream = Files.newInputStream(path)) {
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
