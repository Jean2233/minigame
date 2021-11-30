package com.github.jean2233.minigame.command;

import com.github.jean2233.minigame.data.game.Game;
import com.github.jean2233.minigame.data.game.GameController;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GameCommand {
    private final Game game;
    private final GameController gameController;

    @Command(
      name = "game",
      permission = "server.administrator"
    )
    public void handleCommand(Context<Player> context) {
        context.sendMessage(new String[] {
          "",
          "§a /game forcestart",
          ""
        });
    }

    @Command(
      name = "game.forcestart"
    )
    public void handleForceStartCommand(Context<Player> context) {
        gameController.prepare(game, true);
    }
}
