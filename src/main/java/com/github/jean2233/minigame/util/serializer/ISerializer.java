package com.github.jean2233.minigame.util.serializer;

public interface ISerializer<T> {
    String serialize(T t);
    T deserialize(String data);
}