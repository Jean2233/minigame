package com.github.jean2233.minigame.data.game;

import com.github.jean2233.minigame.Constants;
import com.github.jean2233.minigame.data.account.Account;
import me.saiintbrisson.minecraft.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class GameController {
    public void prepare(Game game, boolean force) {
        final List<Account> players = game.getPlayers();
        if(players.size() < Constants.getMinPlayers() && !force) return;

        players.forEach($ -> {
            final Player player = $.toPlayer();
            player.teleport(game.getArena().getSpawnLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 20F, 1.3f);

            prepareInventory(player);
        });

        game.setState(GameState.STARTING);
        game.setTicks(0);
    }

    public void start(Game game) {
        final List<Account> players = game.getPlayers();
        players.forEach($ -> {
            final Player player = $.toPlayer();
            player.sendTitle("", "§a§lLet's go!");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 20F, 1.3f);
        });

        game.setTicks(0);
        game.setState(GameState.OCCURRING);
    }

    public void finish(Game game) {
        final List<Account> players = game.getPlayers();
        players.forEach($ -> {
            final Player player = $.toPlayer();
            player.kickPlayer("");
        });

        Bukkit.getServer().shutdown();
    }

    private void prepareInventory(Player player) {
        final PlayerInventory playerInventory = player.getInventory();
        playerInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        playerInventory.setItem(0, new ItemBuilder(Material.BONE).name("§4§lGun").build());
    }
}