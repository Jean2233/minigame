package com.github.jean2233.minigame.data.arena;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;

@Data
@Builder
public class Arena {
    private final String name, regionName;

    private final Location spawnLocation;
}