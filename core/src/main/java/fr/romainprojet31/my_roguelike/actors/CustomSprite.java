package fr.romainprojet31.my_roguelike.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import fr.romainprojet31.my_roguelike.Main;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CustomSprite extends Sprite {
    protected static final long CUSTOM_SPRITE_FPS_ANIMATION = 100000000;
    protected long fpsAnimation = 100000000;

    protected TextureRegion[] animationFrames;
    protected TextureRegion currentFrame;  // Frame courante à afficher
    protected Vector2 velocity;
    protected int speed;
    protected boolean alive;

    private int frameIndex;  // Index de la frame courante
    private float stateTime;  // Temps écoulé pour l'animation
    private boolean paused;
    private long lastFrameTime;  // Temps de la dernière mise à jour de la frame

    public CustomSprite(String filePath, Rectangle srcRect, int cols, int rows, Vector2 destPos, int speed) {
        super(loadTexture(filePath), (int) srcRect.x, (int) srcRect.y, (int) srcRect.width, (int) srcRect.height);
        TextureRegion[][] tmpFrames = TextureRegion.split(getTexture(), getTexture().getWidth() / cols, getTexture().getHeight() / rows);
        animationFrames = new TextureRegion[cols * rows];
        velocity = new Vector2();
        this.speed = speed;
        alive = true;

        int index = 0;

        // Convertir la matrice 2D en tableau 1D
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                animationFrames[index++] = tmpFrames[i][j];
            }
        }
        currentFrame = animationFrames[0];
        stateTime = 0f;
        frameIndex = 0;
        lastFrameTime = TimeUtils.nanoTime();
        setPosition(destPos.x, destPos.y);
    }

    public CustomSprite(String filePath) {
        super(loadTexture(filePath));
    }

    public void update() {
        if (!paused) {
            stateTime += Gdx.graphics.getDeltaTime();
            if (TimeUtils.nanoTime() - lastFrameTime > fpsAnimation) {  // 100ms between frames
                frameIndex = nextFrame();
                lastFrameTime = TimeUtils.nanoTime();
                currentFrame = animationFrames[frameIndex];
            }
        }
    }

    /**
     * Encapsulated so children of CustomSprite can implement another kind of currentFrame implementation
     */
    protected int nextFrame() {
        return (frameIndex + 1) % animationFrames.length;
    }

    public void render(SpriteBatch batch) {
        if (alive) {
            final Color lastBatchColor = batch.getColor();
            batch.setColor(getColor());
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
            batch.setColor(lastBatchColor);
        }
    }

    public void kill(){
        alive = false;
    }

    public void setInScreen() {
        if (isOutScreenX()) {
            velocity.x = 0;
        }
        if (isOutScreenY()) {
            velocity.y = 0;
        }
    }

    public boolean isOutScreenX() {
        return velocity.x < 0 && getX() < getWidth() / 2 || velocity.x > 0 && getX() + getWidth() + speed / 2.0 > Main.SCREEN_SIZE.x;
    }

    public boolean isOutScreenY() {
        return velocity.y < 0 && getY() < getHeight() / 2 || velocity.y > 0 && getY() + getHeight() + speed / 2.0 > Main.SCREEN_SIZE.y;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private static Texture loadTexture(String spriteName) {
        return new Texture(Gdx.files.internal("sprites/"+spriteName));
    }
}
