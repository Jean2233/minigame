package com.github.jean2233.minigame.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;

public class WorldGuardUtil {
    private static final WorldGuard WORLD_GUARD = WorldGuard.getInstance();
    private static final WorldGuardPlatform PLATFORM = WORLD_GUARD.getPlatform();

    public static ProtectedRegion getRegionById(World world, String id) {
        final RegionContainer container = PLATFORM.getRegionContainer();
        final RegionManager manager = container.get(adapt(world));

        return manager.getRegion(id);
    }

    public static ProtectedRegion getRegionByLocation(Location location) {
        final RegionContainer container = PLATFORM.getRegionContainer();
        final RegionManager manager = container.get(adapt(location.getWorld()));

        final Map<String, ProtectedRegion> regions = manager.getRegions();
        return regions.values().stream()
          .filter(region -> region.contains(
            location.getBlockY(), location.getBlockY(), location.getBlockZ()
          ))
          .findFirst()
          .orElse(null);
    }

    private static com.sk89q.worldedit.world.World adapt(World world) {
        return BukkitAdapter.adapt(world);
    }
}