package fr.romainprojet31.my_roguelike.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Road extends CustomSprite {

    public Road(int x, int y) {
        super("road.png", new Rectangle(0, 0, 64, 64), 1, 1, new Vector2(x, y));
    }
}
