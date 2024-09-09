package fr.romainprojet31.my_roguelike.actors.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.actors.Player;
import lombok.Getter;

@Getter
public class Slime extends AEnemy {
    private static final long SLIME_IDLE_FPS_ANIMATION = 300000000;
    private static final String SPRITE_SHEET = "SlimeA.png";
    private static final int IDLE = 0;
    private static final int ATTACK = 2;

    /**
     * Describes the limits of each slime's animation
     */
    private final int[][] indexesBorderMinMax = {
        {0, 2}, // Idle/Walk
        {3, 8}, // Jump
        {9, 15} // Attack
    };
    private int currentAnimation;

    public Slime(Vector2 pos) {
        super(SPRITE_SHEET, new Rectangle(0, 0, 32, 32), 16, 1, pos);
        currentAnimation = IDLE;
        fpsAnimation = CUSTOM_SPRITE_FPS_ANIMATION * 3;
    }

    @Override
    public void update(Player player) {
        if (player.getBoundingRectangle().overlaps(getBoundingRectangle())) {
            currentAnimation = ATTACK;
            fpsAnimation = CUSTOM_SPRITE_FPS_ANIMATION;
        } else if (currentAnimation == ATTACK) {
            currentAnimation = IDLE;
            fpsAnimation = SLIME_IDLE_FPS_ANIMATION;
        }
        super.update(player);
    }

    @Override
    protected int nextFrame() {
        int minFrame = indexesBorderMinMax[currentAnimation][0];
        int computedIdx = (Math.max(getFrameIndex(), minFrame) + 1) % indexesBorderMinMax[currentAnimation][1];
        return computedIdx == 0 ? minFrame : computedIdx;
    }
}
