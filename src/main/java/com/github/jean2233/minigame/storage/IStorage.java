package com.github.jean2233.minigame.storage;

import java.sql.ResultSet;

public interface IStorage<T> {
    void createTable();

    void insert(T t);
    void update(T t);

    T find(String id);
    T adapt(ResultSet set);
}