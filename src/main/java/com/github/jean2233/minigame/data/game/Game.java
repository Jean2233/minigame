package com.github.jean2233.minigame.data.game;

import com.github.jean2233.minigame.data.account.Account;
import com.github.jean2233.minigame.data.arena.Arena;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class Game {
    private final String gameId;

    private final Arena arena;

    private GameState state;
    private int ticks;

    private final List<Account> players = new ArrayList<>();
    private final Map<Account, Integer> scoreMap = new HashMap<>();
}