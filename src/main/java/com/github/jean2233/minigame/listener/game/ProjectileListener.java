package com.github.jean2233.minigame.listener.game;

import com.github.jean2233.minigame.data.game.Game;
import com.github.jean2233.minigame.data.game.GameState;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@RequiredArgsConstructor
public class ProjectileListener implements Listener {
    private final Game game;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        final Action action = event.getAction();
        if(action != Action.RIGHT_CLICK_AIR) return;

        final ItemStack item = event.getItem();
        if(item == null || item.getType() != Material.BONE) return;

        final GameState state = game.getState();
        if(state != GameState.OCCURRING) return;

        final Snowball projectile = player.launchProjectile(Snowball.class);

        final Vector vector = player.getLocation().getDirection().multiply(1.5);
        projectile.setVelocity(vector);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 1.5f);
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        final Projectile entity = event.getEntity();
        if(entity.getType() != EntityType.SNOWBALL) return;

        final Entity hitEntity = event.getHitEntity();
        if(!(hitEntity instanceof Player)) return;

        final Player player = (Player) hitEntity;
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1F, 1F);

        player.setHealth(0D);
    }
}