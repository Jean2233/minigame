package com.github.jean2233.minigame.data.arena;

import com.github.jean2233.minigame.MinigamePlugin;
import com.github.jean2233.minigame.loader.impl.ArenaLoader;
import com.github.jean2233.minigame.util.serializer.impl.LocationSerializer;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ArenaRegistry extends LocationSerializer {

    @Getter
    private final List<Arena> arenas = new ArrayList<>();

    public void registerArenas(FileConfiguration configuration) {
        final ArenaLoader loader = new ArenaLoader();

        final ConfigurationSection section = configuration.getConfigurationSection("arenas");
        if(section == null) return;

        for (String key : section.getKeys(false)) {
            final Arena arena = loader.load(key, section.getConfigurationSection(key));
            if(arena == null) continue;

            arenas.add(arena);
        }
    }

    public Arena getByName(String name) {
        return arenas.stream()
          .filter(arena -> arena.getName().equalsIgnoreCase(name))
          .findFirst()
          .orElse(null);
    }

    public Arena getByRegion(String regionName) {
        return arenas.stream()
          .filter(arena -> arena.getRegionName().equalsIgnoreCase(regionName))
          .findFirst()
          .orElse(null);
    }

    public void registerArena(Arena arena, MinigamePlugin plugin) {
        this.arenas.add(arena);

        final FileConfiguration config = plugin.getConfig();
        config.set("arenas." + arena.getName() + ".region-name", arena.getRegionName());
        config.set("arenas." + arena.getName() + ".spawn-location", serialize(arena.getSpawnLocation()));

        plugin.saveConfig();
    }

    public void unregisterArena(Arena arena) {
        this.arenas.add(arena);
    }

    public Arena getRandomArena() {
        if(arenas.isEmpty()) return null;

        final int random = new Random().nextInt(arenas.size());
        return arenas.get(random);
    }
}
