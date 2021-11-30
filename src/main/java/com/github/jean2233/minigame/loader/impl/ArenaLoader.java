package com.github.jean2233.minigame.loader.impl;

import com.github.jean2233.minigame.data.arena.Arena;
import com.github.jean2233.minigame.loader.ConfigurationLoader;
import com.github.jean2233.minigame.util.serializer.impl.LocationSerializer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

@RequiredArgsConstructor
public class ArenaLoader extends LocationSerializer implements ConfigurationLoader<Arena> {
    @Override
    public Arena load(String key, ConfigurationSection section) {
        final String regionName = section.getString("region-name");

        final Location location = deserialize(section.getString("spawn-location"));

        return Arena.builder()
          .name(key)
          .regionName(regionName)
          .spawnLocation(location)
          .build();
    }
}
