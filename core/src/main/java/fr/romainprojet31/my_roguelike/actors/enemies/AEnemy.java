package fr.romainprojet31.my_roguelike.actors.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.actors.CustomSprite;
import fr.romainprojet31.my_roguelike.managers.SoundManager;
import lombok.Getter;


@Getter
public abstract class AEnemy extends CustomSprite {

    public AEnemy(String filePath, Rectangle srcRect, int cols, int rows, Vector2 destPos, int speed) {
        super(filePath, srcRect, cols, rows, destPos, speed);
    }

    /**
     * Call setInScreen() before calling super.update() if you want to stop the AEnemy in this case
     */
    public void update() {
        float newX = getX() + velocity.x * speed;
        float newY = getY() + velocity.y * speed;
        setPosition(newX, newY);
        super.update();
    }

    public abstract void hit();
}
