package fr.romainprojet31.my_roguelike.actors.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.Main;
import fr.romainprojet31.my_roguelike.actors.Block;
import fr.romainprojet31.my_roguelike.managers.MapManager;
import lombok.Getter;

import java.util.List;

@Getter
public class Slime extends AEnemy {
    private static final long SLIME_IDLE_FPS_ANIMATION = 300000000;
    private static final String SPRITE_SHEET = "SlimeA.png";
    private static final int IDLE = 0;
    private static final int ATTACK = 2;
    private static final int MAX_MOVEMENT = Block.BLOCK_SIZE * 3;
    /**
     * Describes the limits of each slime's animation
     */
    private final int[][] indexesBorderMinMax = {
        {0, 2}, // Idle/Walk
        {3, 8}, // Jump
        {9, 15} // Attack
    };
    private int currentAnimation;
    private Boolean verticalMovement;

    public Slime(Vector2 pos) {
        super(SPRITE_SHEET, new Rectangle(0, 0, 32, 32), 16, 1, pos, 1);
        currentAnimation = IDLE;
        verticalMovement = null;
        fpsAnimation = CUSTOM_SPRITE_FPS_ANIMATION * 3;
    }

    @Override
    public void update() {
        if (alive) {
            boolean blockCollision = MapManager.MAP_CONFIG.getBlocks()
                .stream()
                .anyMatch(block -> block.getBoundingRectangle().overlaps(getBoundingRectangle()));

            if (verticalMovement == null) {
                verticalMovement = getCptAround(true) > getCptAround(false);
            } else if (verticalMovement) {
                if (velocity.y == 0) {
                    velocity.y = -1;
                } else if (blockCollision || isOutScreenY()) {
                    velocity.y *= -1;
                }
            } else {
                if (velocity.x == 0) {
                    velocity.x = -1;
                } else if (blockCollision || isOutScreenX()) {
                    velocity.x *= -1;
                }
            }

            boolean playerTouched = MapManager.MAP_CONFIG.getPlayer().getBoundingRectangle().overlaps(getBoundingRectangle());
            boolean canAttack = currentAnimation != ATTACK && MapManager.MAP_CONFIG.getPlayer().isAlive();
            if (playerTouched && canAttack) {
                velocity.setZero();
                currentAnimation = ATTACK;
                MapManager.MAP_CONFIG.getPlayer().hit();
                fpsAnimation = CUSTOM_SPRITE_FPS_ANIMATION;
            } else if (currentAnimation == ATTACK) {
                currentAnimation = IDLE;
                fpsAnimation = SLIME_IDLE_FPS_ANIMATION;
            }
        }
        super.update();
    }

    @Override
    public void hit() {
        alive = false;
    }

    private int getCptAround(boolean yAxis) {
        int startIdx = (int) (yAxis ? getY() : getX()) / Block.BLOCK_SIZE;
        List<Block> blocks = MapManager.MAP_CONFIG.getBlocks();
        boolean foundA = false, foundB = false;
        int cpt = 0;
        int first = startIdx, second = startIdx;
        final int max = (int) (yAxis ? Main.SCREEN_SIZE.y : Main.SCREEN_SIZE.x) / Block.BLOCK_SIZE;
        while ((!foundA || !foundB) && startIdx > 0 && startIdx < max) {

            final int ff = first;
            boolean noBlockOnTop = blocks.stream()
                .noneMatch(block -> ((int) block.getY() / Block.BLOCK_SIZE) == ff && block.getX() == getX());

            if (noBlockOnTop && ff > 0) {
                cpt++;
                first--;
            } else {
                foundA = true;
            }

            final int fs = second;
            boolean noBlockOnBot = blocks.stream()
                .noneMatch(block -> ((int) block.getY() / Block.BLOCK_SIZE) == fs && block.getX() == getX());

            if (noBlockOnBot && fs < max) {
                cpt++;
                second++;
            } else {
                foundB = true;
            }
        }
        return cpt;
    }

    @Override
    protected int nextFrame() {
        int minFrame = indexesBorderMinMax[currentAnimation][0];
        int computedIdx = (Math.max(getFrameIndex(), minFrame) + 1) % indexesBorderMinMax[currentAnimation][1];
        return computedIdx == 0 ? minFrame : computedIdx;
    }
}
