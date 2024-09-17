package fr.romainprojet31.my_roguelike.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block extends CustomSprite {
    public static final int BLOCK_SIZE = 64;


    public Block(int x, int y) {
        //super("RezIDLE.jpg", new Rectangle(x, y, BLOCK_SIZE, BLOCK_SIZE), 8, 5, new Vector2(x, y));
        super("wall.jpg", new Rectangle(0, 0, BLOCK_SIZE, BLOCK_SIZE), 1, 1, new Vector2(x, y), 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}
