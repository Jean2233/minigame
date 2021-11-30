package com.github.jean2233.minigame.data.account;

import org.bukkit.entity.Player;

import java.util.UUID;

public record AccountFactory(AccountRegistry registry, AccountStorage storage) {

    public Account create(Player player) {
        final UUID uniqueId = player.getUniqueId();

        final Account find = storage.find(uniqueId.toString());
        if(find != null) {
            registry.register(find);
            return find;
        }

        final Account account = Account.builder()
          .id(player.getUniqueId())
          .name(player.getName())
          .build();

        registry.register(account);
        storage.insert(account);

        return account;
    }

    public Account remove(Player player) {
        final UUID playerId = player.getUniqueId();

        final Account account = registry.getById(playerId);
        if(account == null) return null;

        registry.unregister(account);
        storage.update(account);

        return account;
    }
}