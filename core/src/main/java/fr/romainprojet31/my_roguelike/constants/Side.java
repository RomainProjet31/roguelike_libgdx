package fr.romainprojet31.my_roguelike.constants;

import java.util.Random;

public enum Side {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT;

    private static final Random random = new Random();

    public static Side pickOne() {
        final Side[] sides = Side.values();
        return sides[random.nextInt(sides.length)];
    }
}
