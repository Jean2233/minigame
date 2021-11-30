package com.github.jean2233.minigame;

import com.github.jean2233.minigame.command.ArenaCommand;
import com.github.jean2233.minigame.command.GameCommand;
import com.github.jean2233.minigame.data.account.Account;
import com.github.jean2233.minigame.data.account.AccountFactory;
import com.github.jean2233.minigame.data.account.AccountRegistry;
import com.github.jean2233.minigame.data.account.AccountStorage;
import com.github.jean2233.minigame.data.arena.ArenaRegistry;
import com.github.jean2233.minigame.data.game.Game;
import com.github.jean2233.minigame.data.game.GameController;
import com.github.jean2233.minigame.data.game.GameState;
import com.github.jean2233.minigame.listener.connection.ConnectionListener;
import com.github.jean2233.minigame.listener.game.DeathListener;
import com.github.jean2233.minigame.listener.game.ProjectileListener;
import com.github.jean2233.minigame.thread.GameThread;
import com.github.jean2233.minigame.util.IDGenerator;
import lombok.Getter;
import lombok.SneakyThrows;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.DriverManager;
import java.util.UUID;

@Getter
public final class MinigamePlugin extends JavaPlugin {

    private Game game;

    private ArenaRegistry arenaRegistry;
    private AccountRegistry accountRegistry;

    private AccountFactory accountFactory;
    private AccountStorage accountStorage;

    private GameController gameController;

    @Override
    public void onLoad() {
        saveDefaultConfig();

        this.accountRegistry = new AccountRegistry();
        this.arenaRegistry = new ArenaRegistry();

        this.gameController = new GameController();
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        this.accountStorage = new AccountStorage(DriverManager.getConnection(
          getConfig().getString("mysql.connection-url"),
          getConfig().getString("mysql.username"),
          getConfig().getString("mysql.password")
        ));

        this.accountFactory = new AccountFactory(accountRegistry, accountStorage);

        createDefaultRegistry();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            final UUID uniqueId = player.getUniqueId();

            final Account account = accountRegistry.getById(uniqueId);
            accountStorage.update(account);
        });
    }

    private void createDefaultRegistry() {
        arenaRegistry.registerArenas(getConfig());

        this.initGame();

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(this, new GameThread(this), 20L, 20L);

        final BukkitFrame frame = new BukkitFrame(this);
        frame.registerCommands(new ArenaCommand(this, arenaRegistry));
        frame.registerCommands(new GameCommand(game, gameController));

        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ConnectionListener(this, accountFactory), this);
        pluginManager.registerEvents(new DeathListener(this), this);
        pluginManager.registerEvents(new ProjectileListener(game), this);
    }

    private void initGame() {
        this.game = Game.builder()
          .gameId(IDGenerator.generateNewId(6))
          .arena(arenaRegistry.getRandomArena())
          .state(GameState.AWAITING)
          .build();
    }
}