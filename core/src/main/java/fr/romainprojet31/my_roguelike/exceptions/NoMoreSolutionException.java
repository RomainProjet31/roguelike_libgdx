package fr.romainprojet31.my_roguelike.exceptions;

import com.badlogic.gdx.math.Vector2;

public class NoMoreSolutionException extends Exception {

    public NoMoreSolutionException(Vector2 lastIndex) {
        super(String.format("No more solution with the given index [%s]", lastIndex));
    }
}
