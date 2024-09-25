package fr.romainprojet31.my_roguelike.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.romainprojet31.my_roguelike.Main;
import fr.romainprojet31.my_roguelike.actors.CustomSprite;
import fr.romainprojet31.my_roguelike.actors.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerLife implements IUI {
    private static final String heartPath = "heart.png";
    private Player player;
    private CustomSprite[] hpSprites;

    public PlayerLife(Player player) {
        this.player = player;
        hpSprites = new CustomSprite[3];
        float middle = (float) (Main.SCREEN_SIZE.x / 2.0);
        for (int i = 0; i < hpSprites.length; i++) {
            var hpSprite = new CustomSprite(heartPath);
            hpSprite.setPosition(middle + i * 64, 10);
            hpSprites[i] = hpSprite;
        }
    }

    @Override
    public void update() {
        for (int i = player.getHp() - 1; i < hpSprites.length; i++) {
            if (hpSprites[i].isAlive()) hpSprites[i].kill();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        for (CustomSprite hp : hpSprites) {
            if (hp.isAlive()) {
                hp.render(batch);
            }
        }
    }
}
