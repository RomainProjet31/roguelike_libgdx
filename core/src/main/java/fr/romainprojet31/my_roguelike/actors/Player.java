package fr.romainprojet31.my_roguelike.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.Main;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Player extends CustomSprite {
    private static final String SPRITE_SHEET = "RezIDLE.jpg";
    private static final int PLAYER_SIZE = 32;
    private static final int PLAYER_SPEED = 4;

    private float timeSuccessAnimation;
    private boolean successAnimation;
    private final Vector2 velocity;
    private float lastFrameUpdate;
    private final Sword sword;
    private int yOffset;

    public Player(Vector2 destPos) {
        super(SPRITE_SHEET, new Rectangle(0, 0, PLAYER_SIZE, PLAYER_SIZE), 3, 1, destPos);
        velocity = new Vector2();
        successAnimation = false;
        lastFrameUpdate = 0;
        sword = new Sword();
        yOffset = 5;
    }

    public void update(List<Block> blocks) {
        velocity.setZero();
        // Player itself
        handleKeyboardAndAnimation();
        handleCollision(blocks);
        setPosition(getX() + velocity.x * PLAYER_SPEED, getY() + velocity.y * PLAYER_SPEED);
        // Its success animation
        if (successAnimation && lastFrameUpdate > 0.5f) {
            lastFrameUpdate = 0;
            yOffset *= -1;
            setY(getY() + yOffset);
            System.out.println(timeSuccessAnimation);
            if (timeSuccessAnimation >= 2f) {
                timeSuccessAnimation = 0;
                successAnimation = false;
            }
        } else if (successAnimation) {
            lastFrameUpdate += Gdx.graphics.getDeltaTime();
            timeSuccessAnimation += Gdx.graphics.getDeltaTime();
            System.out.println(lastFrameUpdate + " " + timeSuccessAnimation);
        }
        // Sword
        var pos = getBoundingRectangle().getCenter(new Vector2());
        sword.update(pos, PLAYER_SIZE);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        sword.render(batch);
    }

    private void handleCollision(List<Block> blocks) {
        this.setInScreen();
        if (!velocity.equals(Vector2.Zero)) {
            blocks.forEach(block -> handleRectCollision(block.getBoundingRectangle()));
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

    private void setInScreen() {
        if (velocity.x < 0 && getX() < getWidth() / 2 || velocity.x > 0 && getX() + getWidth() + PLAYER_SPEED / 2.0 > Main.SCREEN_SIZE.x) {
            velocity.x = 0;
        }
        if (velocity.y < 0 && getY() < getHeight() / 2 || velocity.y > 0 && getY() + getHeight() + PLAYER_SPEED / 2.0 > Main.SCREEN_SIZE.y) {
            velocity.y = 0;
        }
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
