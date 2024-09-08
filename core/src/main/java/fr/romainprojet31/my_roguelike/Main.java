package fr.romainprojet31.my_roguelike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.romainprojet31.my_roguelike.Map.MapConfig;
import fr.romainprojet31.my_roguelike.actors.Player;
import fr.romainprojet31.my_roguelike.constants.Side;
import fr.romainprojet31.my_roguelike.managers.MapManager;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    public static final Vector2 SCREEN_SIZE = new Vector2(64 * 10, 64 * 8);
    private ShapeRenderer shapeR;
    private SpriteBatch batch;
    private MapConfig map;
    private Player player;
    private OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeR = new ShapeRenderer();
        map = MapManager.generateMap((int) SCREEN_SIZE.x, (int) SCREEN_SIZE.y, Side.TOP);
        player = new Player((int) map.getPlayerPos().x, (int) map.getPlayerPos().y);
        camera = new OrthographicCamera();
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
        player.render(batch);
        batch.end();
    }

    private void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.R) || map.getEnd().isOpened()) {
            map = MapManager.generateMap(64 * 10, 64 * 8, Side.pickOne());
            player.setPosition(map.getPlayerPos().x, map.getPlayerPos().y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
        }
        player.update(map.getBlocks());
        map.getEnd().update(player);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
