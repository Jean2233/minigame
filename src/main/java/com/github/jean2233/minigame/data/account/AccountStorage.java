package com.github.jean2233.minigame.data.account;

import com.github.jean2233.minigame.storage.IStorage;
import com.github.jean2233.minigame.storage.util.SQLReader;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class AccountStorage implements IStorage<Account> {

    private final Connection connection;
    private final SQLReader reader;

    @SneakyThrows
    public AccountStorage(Connection connection) {
        this.connection = connection;

        this.reader = new SQLReader();
        reader.loadFromResources();

        createTable();
    }

    @SneakyThrows
    @Override
    public void createTable() {
        final PreparedStatement statement = prepareStatement("create_table");
        statement.executeUpdate();
    }

    @SneakyThrows
    @Override
    public void insert(Account account) {
        final PreparedStatement statement = prepareStatement("insert");
        statement.setString(1, account.getId().toString());
        statement.setString(2, account.getName());
        statement.setInt(3, account.getLastScore());
        statement.setInt(4, account.getTotalScore());

        statement.executeUpdate();
    }

    @SneakyThrows
    @Override
    public void update(Account account) {
        final PreparedStatement statement = prepareStatement("update");
        statement.setInt(1, account.getLastScore());
        statement.setInt(2, account.getTotalScore());

        statement.executeUpdate();
    }

    @SneakyThrows
    @Override
    public Account find(String id) {
        final PreparedStatement statement = prepareStatement("find");
        statement.setString(1, id);

        final ResultSet set = statement.executeQuery();
        if(!set.next()) return null;

        return adapt(set);
    }

    @SneakyThrows
    @Override
    public Account adapt(ResultSet set) {
        final UUID id = UUID.fromString(set.getString("id"));
        final String name = set.getString("name");

        final int lastScore = set.getInt("last_score");
        final int totalScore = set.getInt("total_score");

        return Account.builder()
          .id(id)
          .name(name)
          .lastScore(lastScore)
          .totalScore(totalScore)
          .build();
    }

    @SneakyThrows
    private PreparedStatement prepareStatement(String sqlFile) {
        return connection.prepareStatement(reader.getSql(sqlFile));
    }
}