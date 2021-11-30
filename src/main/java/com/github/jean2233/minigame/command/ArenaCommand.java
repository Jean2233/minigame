package com.github.jean2233.minigame.command;

import com.github.jean2233.minigame.MinigamePlugin;
import com.github.jean2233.minigame.data.arena.Arena;
import com.github.jean2233.minigame.data.arena.ArenaRegistry;
import com.github.jean2233.minigame.util.WorldGuardUtil;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ArenaCommand {
    private final MinigamePlugin plugin;
    private final ArenaRegistry registry;

    @Command(
      name = "arena",
      permission = "server.administrator"
    )
    public void handleCommand(Context<Player> context) {
        context.sendMessage(new String[] {
          "",
          "§a /arena create <name> <region-name>",
          "§a /arena delete <name>",
          "§a /arena list",
          ""
        });
    }

    @Command(
      name = "arena.create"
    )
    public void handleCreateCommand(Context<Player> context, String name, String regionName) {
        final Player sender = context.getSender();

        final ProtectedRegion region = WorldGuardUtil.getRegionById(sender.getWorld(), regionName);
        if(region == null) {
            context.sendMessage("§cCould't find region named '" + regionName + "'.");
            return;
        }

        registry.registerArena(
          Arena.builder()
            .name(name)
            .regionName(regionName)
            .spawnLocation(sender.getLocation())
            .build(),
          plugin
        );

        context.sendMessage("§aArena named '" + name + "' successfully created!");
    }

    @Command(
      name = "arena.delete"
    )
    public void handleDeleteCommand(Context<Player> context, String name) {
        final Arena arena = registry.getByName(name);
        if(arena == null) {
            context.sendMessage("§cCould't find arena named '" + name + "'.");
            return;
        }

        registry.unregisterArena(arena);

        context.sendMessage("§aArena named '" + name + "' successfully deleted!");
    }

    @Command(
      name = "arena.list"
    )
    public void handleListCommand(Context<Player> context) {
        final List<String> arenas = registry.getArenas()
          .stream()
          .map(arena -> arena.getName())
          .collect(Collectors.toList());

        final String join = String.join(", ", arenas);
        context.sendMessage("§aAvailable arenas: §f" + join);
    }
}
