package fr.romainprojet31.my_roguelike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.romainprojet31.my_roguelike.Map.MapConfig;
import fr.romainprojet31.my_roguelike.actors.enemies.AEnemy;
import fr.romainprojet31.my_roguelike.constants.Side;
import fr.romainprojet31.my_roguelike.managers.MapManager;
import fr.romainprojet31.my_roguelike.managers.SoundManager;
import fr.romainprojet31.my_roguelike.ui.IUI;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    public static final Vector2 SCREEN_SIZE = new Vector2(64 * 10, 64 * 8);
    private SpriteBatch batch;
    private MapConfig map;
    private OrthographicCamera camera;

    @Override
    public void create() {
        map = MapManager.generateMap((int) SCREEN_SIZE.x, (int) SCREEN_SIZE.y, Side.TOP);
        camera = new OrthographicCamera();
        batch = new SpriteBatch();

        camera.setToOrtho(false, SCREEN_SIZE.x, SCREEN_SIZE.y);

    }

    @Override
    public void render() {
        update();
        ScreenUtils.clear(0, 0, 0, 0.5f);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        map.getBlocks().forEach(block -> block.render(batch));
        map.getRoad().forEach(road -> road.render(batch));
        map.getStart().render(batch);
        map.getEnd().render(batch);
        map.getPlayer().render(batch);
        map.getEnemies().forEach(mob -> mob.render(batch));
        map.getUis().forEach(ui -> ui.render(batch));
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        SoundManager.disposeInstance();
    }

    private void update() {
        handleNewMap();
        map.getPlayer().update(map.getBlocks());

        boolean wasOpened = map.getEnd().isOpened();
        map.getEnd().update(map.getPlayer());
        if (map.getEnd().isOpened() && !wasOpened) {
            map.getPlayer().setSuccessAnimation(true);
        } else if (!map.getPlayer().isSuccessAnimation()) {
            map.getEnemies().forEach(AEnemy::update);
        }
        map.getUis().forEach(IUI::update);
    }

    private void handleNewMap() {
        boolean isMapEnded = map.getEnd().isOpened() && !map.getPlayer().isSuccessAnimation();
        if (Gdx.input.isKeyPressed(Input.Keys.R) || isMapEnded) {
            map = MapManager.generateMap(64 * 10, 64 * 8, map.getEnd().getSide());
        } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
            Gdx.app.exit();
        }
    }
}
