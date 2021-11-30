package com.github.jean2233.minigame.data.account;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
@Builder
public class Account {

    private final UUID id;
    private final String name;

    private int wins, lastScore, totalScore;

    private boolean spectating;

    public void incrementScore() {
        this.lastScore += 1;
        this.totalScore += 1;
    }

    public void incrementWin() {
        this.wins += 1;
    }

    public Player toPlayer() {
        return Bukkit.getPlayer(id);
    }
}