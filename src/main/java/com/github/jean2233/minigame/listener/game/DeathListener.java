package com.github.jean2233.minigame.listener.game;

import com.github.jean2233.minigame.MinigamePlugin;
import com.github.jean2233.minigame.data.account.Account;
import com.github.jean2233.minigame.data.account.AccountRegistry;
import com.github.jean2233.minigame.data.arena.Arena;
import com.github.jean2233.minigame.data.game.Game;
import com.github.jean2233.minigame.data.game.GameController;
import com.github.jean2233.minigame.util.WorldGuardUtil;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

@RequiredArgsConstructor
public class DeathListener implements Listener {
    private final MinigamePlugin plugin;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        final GameController controller = plugin.getGameController();
        final Game game = plugin.getGame();

        final AccountRegistry accountRegistry = plugin.getAccountRegistry();

        final Player player = event.getEntity();
        final UUID uniqueId = player.getUniqueId();

        final Arena arena = game.getArena();

        final String regionName = arena.getRegionName();
        final ProtectedRegion region = WorldGuardUtil.getRegionByLocation(player.getLocation());
        if(region == null || !region.getId().equalsIgnoreCase(regionName)) return;

        final Account account = accountRegistry.getById(uniqueId);
        if(account == null) return;

        event.getDrops().clear();

        final Player killer = player.getKiller();
        if(killer != null) {
            final UUID killerId = killer.getUniqueId();

            final Account killerAccount = accountRegistry.getById(killerId);
            if(killerAccount == null) return;

            killerAccount.incrementScore();
        }

        Bukkit.broadcastMessage("§a" + player.getName() + " §chas been eliminated.");

        game.getPlayers().remove(account);
        account.setSpectating(true);

        respawn(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        final Game game = plugin.getGame();
        final Arena arena = game.getArena();

        event.setRespawnLocation(arena.getSpawnLocation());

        final Player player = event.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
    }

    private void respawn(Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.spigot().respawn();
        }, 1L);
    }
}