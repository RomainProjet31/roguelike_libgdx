package fr.romainprojet31.my_roguelike.actors.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.actors.CustomSprite;
import fr.romainprojet31.my_roguelike.actors.Player;
import lombok.Getter;


@Getter
public abstract class AEnemy extends CustomSprite {

    public AEnemy(String filePath, Rectangle srcRect, int cols, int rows, Vector2 destPos) {
        super(filePath, srcRect, cols, rows, destPos);
    }


    public void update(Player player) {
        super.update();
    }
}
