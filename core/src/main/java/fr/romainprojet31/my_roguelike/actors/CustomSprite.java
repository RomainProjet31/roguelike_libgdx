package fr.romainprojet31.my_roguelike.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class CustomSprite extends Sprite {
    protected TextureRegion[] animationFrames;
    protected TextureRegion currentFrame;  // Frame courante à afficher
    private float stateTime;  // Temps écoulé pour l'animation
    private int frameIndex;  // Index de la frame courante
    private long lastFrameTime;  // Temps de la dernière mise à jour de la frame
    private boolean paused;

    public CustomSprite(String filePath, Rectangle srcRect, int cols, int rows, Vector2 destPos) {
        super(new Texture(Gdx.files.internal(filePath)), (int) srcRect.x, (int) srcRect.y, (int) srcRect.width, (int) srcRect.height);
        TextureRegion[][] tmpFrames = TextureRegion.split(getTexture(), getTexture().getWidth() / cols, getTexture().getHeight() / rows);
        animationFrames = new TextureRegion[cols * rows];
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
        super(new Texture(Gdx.files.internal(filePath)));
    }

    public void update() {
        if (!paused) {
            stateTime += Gdx.graphics.getDeltaTime();
            if (TimeUtils.nanoTime() - lastFrameTime > 100000000) {  // 100ms entre les frames
                frameIndex = (frameIndex + 1) % animationFrames.length;
                currentFrame = animationFrames[frameIndex];
                lastFrameTime = TimeUtils.nanoTime();
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
