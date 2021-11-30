package com.github.jean2233.minigame.util.serializer.impl;

import com.github.jean2233.minigame.util.serializer.ISerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer implements ISerializer<Location> {
    @Override
    public String serialize(Location location) {
        final String worldName = location.getWorld().getName();

        final double
          locationX = location.getX(),
          locationY = location.getY(),
          locationZ = location.getZ(),
          locationYaw = location.getYaw(),
          locationPitch = location.getPitch();

        return new String(
          worldName + ";" +
            locationX + ";" +
            locationY + ";" +
            locationZ + ";" +
            locationYaw + ";" +
            locationPitch + ";"
        );
    }

    @Override
    public Location deserialize(String data) {
        final String[] split = data.split(";");

        final String worldName = split[0];

        final double
          locationX = Double.parseDouble(split[1]),
          locationY = Double.parseDouble(split[2]),
          locationZ = Double.parseDouble(split[3]);

        final float
          locationYaw = Float.parseFloat(split[4]),
          locationPitch = Float.parseFloat(split[5]);

        return new Location(
          Bukkit.getWorld(worldName),
          locationX,
          locationY,
          locationZ,
          locationYaw,
          locationPitch
        );
    }
}