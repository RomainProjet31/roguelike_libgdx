package fr.romainprojet31.my_roguelike.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;

@Getter
public class Door extends CustomSprite {
    private boolean opened;

    public Door(Vector2 pos) {
        super("door.png", new Rectangle(0, 0, 64, 64), 1, 1, pos);
        opened = false;
    }

    public void update(Player player) {
        super.update();
        if (getBoundingRectangle().contains(player.getBoundingRectangle())) {
            opened = true;
        }
    }
}
