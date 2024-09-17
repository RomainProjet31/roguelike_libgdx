package fr.romainprojet31.my_roguelike.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.Main;
import fr.romainprojet31.my_roguelike.constants.Side;
import fr.romainprojet31.my_roguelike.constants.SoundsNames;
import fr.romainprojet31.my_roguelike.managers.SoundManager;
import lombok.Getter;

@Getter
public class Door extends CustomSprite {
    private boolean opened;

    public Door(Vector2 pos) {
        super("door.png", new Rectangle(0, 0, 64, 64), 1, 1, pos, 0);
        opened = false;
    }

    public void update(Player player) {
        super.update();
        if (getBoundingRectangle().contains(player.getBoundingRectangle())) {
            opened = true;
            SoundManager.play(SoundsNames.WIN);
        }
    }

    public Side getSide() {
        if (getX() == 0) {
            return Side.LEFT;
        } else if (getY() == 0) {
            return Side.TOP;
        } else if (getX() == Main.SCREEN_SIZE.x - getWidth()) {
            return Side.RIGHT;
        } else if (getY() == Main.SCREEN_SIZE.y - getHeight()) {
            return Side.BOTTOM;
        } else {
            throw new UnsupportedOperationException("AYO WTF BRO");
        }
    }
}
