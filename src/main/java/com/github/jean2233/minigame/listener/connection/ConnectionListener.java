package com.github.jean2233.minigame.listener.connection;

import com.github.jean2233.minigame.MinigamePlugin;
import com.github.jean2233.minigame.data.account.Account;
import com.github.jean2233.minigame.data.account.AccountFactory;
import com.github.jean2233.minigame.data.arena.Arena;
import com.github.jean2233.minigame.data.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public record ConnectionListener(MinigamePlugin plugin, AccountFactory factory) implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Account account = factory.create(player);

        final Game game = plugin.getGame();
        game.getPlayers().add(account);

        final Arena arena = game.getArena();
        player.teleport(arena.getSpawnLocation());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final Account account = factory.remove(player);

        final Game game = plugin.getGame();
        game.getPlayers().remove(account);
    }
}