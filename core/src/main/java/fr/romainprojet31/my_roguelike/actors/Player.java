package fr.romainprojet31.my_roguelike.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.constants.SoundsNames;
import fr.romainprojet31.my_roguelike.managers.MapManager;
import fr.romainprojet31.my_roguelike.managers.SoundManager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Player extends CustomSprite {
    public static final int FULL_HP = 3;
    private static final int PLAYER_SIZE = 32;
    private static final int PLAYER_SPEED = 4;
    private static final String SPRITE_SHEET = "RezIDLE.jpg";

    private float lastFrameSuccessUpdate;
    private float timeSuccessAnimation;
    private boolean successAnimation;
    private float lastFrameHitUpdate;
    private float timeHitAnimation;
    private boolean hitAnimation;
    private final Sword sword;
    private int yOffset;
    private int hp;

    public Player(Vector2 destPos) {
        super(SPRITE_SHEET, new Rectangle(0, 0, PLAYER_SIZE, PLAYER_SIZE), 3, 1, destPos, PLAYER_SPEED);
        successAnimation = false;
        lastFrameSuccessUpdate = 0;
        sword = new Sword();
        hp = FULL_HP;
        yOffset = 5;
    }

    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            SoundManager.play(SoundsNames.WIN);
        }
        velocity.setZero();
        // Player itself
        handleKeyboardAndAnimation();
        handleCollision();
        setPosition(getX() + velocity.x * PLAYER_SPEED, getY() + velocity.y * PLAYER_SPEED);
        animationManagement();

        // Sword
        var pos = getBoundingRectangle().getCenter(new Vector2());
        sword.update(pos, PLAYER_SIZE);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        sword.render(batch);
    }

    public void hit() {
        if (!hitAnimation && alive) {
            hp--;
            if (hp <= 0) {
                alive = false;
            } else {
                hitAnimation = true;
                SoundManager.play(SoundsNames.HIT);
            }
        }
    }

    private void animationManagement() {
        successAnimationManagement();
        hitAnimationManagement();
    }

    private void successAnimationManagement() {
        if (successAnimation && lastFrameSuccessUpdate > 0.5f) {
            lastFrameSuccessUpdate = 0;
            yOffset *= -1;
            setY(getY() + yOffset);
            if (timeSuccessAnimation >= 2f) {
                successAnimation = false;
                timeSuccessAnimation = 0;
                lastFrameSuccessUpdate = 0;
            }
        } else if (successAnimation) {
            lastFrameSuccessUpdate += Gdx.graphics.getDeltaTime();
            timeSuccessAnimation += Gdx.graphics.getDeltaTime();
        }
    }

    private void hitAnimationManagement() {
        if (hitAnimation && lastFrameHitUpdate > 0.25f) {
            setAlpha(getColor().a == 1 ? 0.3f : 1);
            lastFrameHitUpdate = 0;
            if (timeHitAnimation >= 2f) {
                setAlpha(1);
                hitAnimation = false;
                lastFrameHitUpdate = 0;
                timeHitAnimation = 0;
            }
        } else if (hitAnimation) {
            lastFrameHitUpdate += Gdx.graphics.getDeltaTime();
            timeHitAnimation += Gdx.graphics.getDeltaTime();
        }
    }

    private void handleCollision() {
        this.setInScreen();
        if (!velocity.equals(Vector2.Zero)) {
            MapManager.MAP_CONFIG.getBlocks().forEach(block -> handleRectCollision(block.getBoundingRectangle()));
        }
    }

    private void handleRectCollision(Rectangle rectangle) {
        var centerR = rectangle.getCenter(new Vector2());
        var nextRect = getNextRect();
        var myCenter = nextRect.getCenter(new Vector2());

        if (rectangle.overlaps(nextRect) || rectangle.overlaps(getBoundingRectangle())) {
            if (centerR.y >= myCenter.y && velocity.y > 0 || centerR.y <= myCenter.y && velocity.y < 0) {
                velocity.y = 0;
            }
            if (centerR.x <= myCenter.x && velocity.x < 0 || centerR.x >= myCenter.x && velocity.x > 0) {
                velocity.x = 0;
            }
        }
    }

    private Rectangle getNextRect() {
        var nextPos = new Rectangle(getBoundingRectangle());
        nextPos.setPosition(getX() + velocity.x * PLAYER_SPEED, getY() + velocity.y * PLAYER_SPEED);
        return nextPos;
    }

    private void handleKeyboardAndAnimation() {
        boolean animate = false, left = false, right = false;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x = -1;
            animate = true;
            left = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocity.y = 1;
            animate = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x = 1;
            animate = true;
            right = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.y = -1;
            animate = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && sword.canAttack()) {
            sword.attack();
        }

        if (animate) {
            super.update();
        } else {
            currentFrame = animationFrames[0];
        }

        if (left && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (right && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (currentFrame.isFlipX()) {
            currentFrame.flip(false, false);
        }
    }
}
