package com.github.jean2233.minigame.util;

public class IDGenerator {
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int CHARACTER_LENGTH = CHARACTERS.length();

	public static String generateNewId(int size) {
		final StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < size; i++) {
			final double index = (Math.random() * CHARACTER_LENGTH);
			buffer.append(CHARACTERS.charAt((int) index));
		}

		return buffer.toString();
	}
}