package com.github.jean2233.minigame.loader;

import org.bukkit.configuration.ConfigurationSection;

public interface ConfigurationLoader<V> {
    V load(String key, ConfigurationSection section);
}