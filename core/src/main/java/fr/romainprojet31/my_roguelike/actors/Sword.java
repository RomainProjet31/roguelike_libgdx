package fr.romainprojet31.my_roguelike.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Sword extends CustomSprite {
    private static final float ASSET_OFFSET = 40f;
    private int deg;
    private boolean attack;


    public Sword() {
        super("WoodSword.png");
        deg = 0;
        attack = false;
        setSize(16, 16);
        getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void update(Vector2 center, int offset) {
        if (attack && deg < 360) {
            double rad = Math.toRadians(deg);
            float x = (float) (center.x + offset * Math.cos(rad));
            float y = (float) (center.y + offset * Math.sin(rad));
            setCenter(x, y);
            setOriginCenter();
            setColor(1, 1, 1, 1); // Réinitialiser la couleur (blanc opaque)
            setRotation(deg - offset);
            deg += 15;
        } else if (attack) {
            attack = false;
            deg = 0;
        }
    }

    public boolean canAttack() {
        return !attack && deg == 0;
    }

    public void attack() {
        this.attack = true;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (attack) {
            draw(batch);
            //super.render(batch);
        }
    }

    @Override
    public String toString() {
        return "Sword{" +
            "deg=" + deg +
            ", attack=" + attack +
            '}';
    }
}
