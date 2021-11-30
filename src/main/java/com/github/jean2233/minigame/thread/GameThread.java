package com.github.jean2233.minigame.thread;

import com.github.jean2233.minigame.Constants;
import com.github.jean2233.minigame.MinigamePlugin;
import com.github.jean2233.minigame.data.account.Account;
import com.github.jean2233.minigame.data.game.Game;
import com.github.jean2233.minigame.data.game.GameController;
import com.github.jean2233.minigame.data.game.GameState;
import com.github.jean2233.minigame.util.ActionBar;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class GameThread extends Thread {
    private final MinigamePlugin plugin;

    @Override
    public void run() {
        final Game game = plugin.getGame();
        final GameController controller = plugin.getGameController();

        final int ticks = game.getTicks();
        game.setTicks(ticks + 1);

        final GameState state = game.getState();
        switch(state) {
            case AWAITING: {
                final List<Account> players = game.getPlayers();
                if(players.size() < Constants.getMinPlayers()) {
                    sendAwaitingMessage(game);
                    return;
                }

                controller.prepare(game, false);
                break;
            }

            case STARTING: {
                final List<Account> players = game.getPlayers();
                if(players.isEmpty()) {
                    game.setState(GameState.FINISHING);
                    game.setTicks(0);
                    return;
                }

                if(players.size() < Constants.getMinPlayers()) {
                    players.forEach(player -> player.toPlayer().sendMessage(
                      "§cWe don't have enough players! Start cancelled."
                    ));
                    return;
                }

                if(game.getTicks() < 10) {
                    sendStartingMessage(game);
                    return;
                }

                controller.start(game);
                break;
            }

            case OCCURRING: {
                final List<Account> players = game.getPlayers();
                if(players.isEmpty()) {
                    game.setState(GameState.FINISHING);
                    game.setTicks(0);
                    return;
                }

                if(players.size() == 1) {
                    game.setState(GameState.FINISHING);
                    game.setTicks(0);

                    players.forEach(player -> {
                        if(player.isSpectating()) return;

                        final Player toPlayer = player.toPlayer();
                        toPlayer.sendTitle("§a§lYOU WON!", "");

                        toPlayer.setAllowFlight(true);
                        toPlayer.setFlying(true);

                        player.incrementWin();
                    });
                }
                break;
            }

            case FINISHING: {
                final List<Account> players = game.getPlayers();
                players.forEach(player -> {
                    if(player.isSpectating()) return;

                    final Player toPlayer = player.toPlayer();
                    final World world = toPlayer.getWorld();

                    world.spawn(toPlayer.getLocation(), Firework.class);
                });

                if(game.getTicks() == 20) {
                    final FileConfiguration config = plugin.getConfig();
                    for (String command : config.getStringList("rewards")) {
                        Bukkit.dispatchCommand(
                          Bukkit.getConsoleSender(), command
                            .replace("@player", players.get(0).getName())
                        );
                    }
                    controller.finish(game);
                }

                break;
            }
        }
    }

    private void sendAwaitingMessage(Game game) {
        final List<Account> players = game.getPlayers();
        players.forEach(player -> ActionBar.sendActionText(
          player.toPlayer(), "§eAwaiting players..."
        ));
    }

    private void sendStartingMessage(Game game) {
        final List<Account> players = game.getPlayers();
        players.forEach(player -> player.toPlayer().sendTitle(
          "§6Minigame", "§eStarting in §c" + (10 - game.getTicks())
        ));
    }
}