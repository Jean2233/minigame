package com.github.jean2233.minigame;

import org.bukkit.configuration.file.FileConfiguration;

public class Constants {
    private static final MinigamePlugin PLUGIN = MinigamePlugin.getPlugin(MinigamePlugin.class);

    public static int getMinPlayers() {
        final FileConfiguration config = PLUGIN.getConfig();
        return config.getInt("settings.min-players");
    }
}