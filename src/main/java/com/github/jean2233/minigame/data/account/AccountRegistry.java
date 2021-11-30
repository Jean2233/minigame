package com.github.jean2233.minigame.data.account;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AccountRegistry {

    @Getter
    private final Set<Account> accounts = new HashSet<>();

    public void register(Account account) {
        accounts.add(account);
    }

    public void unregister(Account account) {
        accounts.remove(account);
    }

    public Account getById(UUID id) {
        return accounts.stream()
          .filter(account -> account.getId().equals(id))
          .findFirst()
          .orElse(null);
    }
}
