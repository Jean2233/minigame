/*
 * Made by https://github.com/Jean2233
 */

package com.github.jean2233.minigame.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ActionBar {
    public static void sendActionText(Player player, String text) {
        player.spigot().sendMessage(
          ChatMessageType.ACTION_BAR,
          TextComponent.fromLegacyText(text)
        );
    }
}