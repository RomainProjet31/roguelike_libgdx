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
        float middle = (float) (Main.SCREEN_SIZE.x / 2.0) - 64f - 5;
        for (int i = 0; i < hpSprites.length; i++) {
            var hpSprite = new CustomSprite(heartPath);
            double posX = i * 64.0;
            double offsetX = i * 5;
            double posY = Main.SCREEN_SIZE.y + Main.HEADER_SIZE / 2.0;
            hpSprite.setPosition((int) (middle + posX + offsetX), (int) posY);
            hpSprites[i] = hpSprite;
        }
    }

    @Override
    public void update() {
        for (int i = player.getHp(); i < hpSprites.length; i++) {
            if (hpSprites[i].isAlive()) hpSprites[i].kill();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        for (CustomSprite hp : hpSprites) {
            hp.render(batch);
        }
    }
}
