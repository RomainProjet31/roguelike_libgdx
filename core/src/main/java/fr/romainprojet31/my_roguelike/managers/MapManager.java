package fr.romainprojet31.my_roguelike.managers;

import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.Map.MapConfig;
import fr.romainprojet31.my_roguelike.MapUtils;
import fr.romainprojet31.my_roguelike.actors.Block;
import fr.romainprojet31.my_roguelike.actors.Door;
import fr.romainprojet31.my_roguelike.actors.Player;
import fr.romainprojet31.my_roguelike.actors.Road;
import fr.romainprojet31.my_roguelike.actors.enemies.AEnemy;
import fr.romainprojet31.my_roguelike.constants.EnemyTypes;
import fr.romainprojet31.my_roguelike.constants.Side;
import fr.romainprojet31.my_roguelike.models.Node;
import fr.romainprojet31.my_roguelike.ui.IUI;
import fr.romainprojet31.my_roguelike.ui.PlayerLife;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapManager {
    public static MapConfig MAP_CONFIG = null;

    private static final Random random = new Random();

    public static MapConfig generateMap(int width, int height, Side startSide) {
        Node[][] grid = initGrid(width, height);
        Vector2 startPos = pickOnePos(startSide, grid);
        Vector2 endPos = pickOnePos(Side.pickOne(), grid);
        List<Vector2> path = MapUtils.getPath(grid, startPos, endPos);

        final int maxI = height / Block.BLOCK_SIZE;
        final int maxJ = width / Block.BLOCK_SIZE;
        List<Block> blocks = new ArrayList<>();
        List<Road> road = new ArrayList<>();

        for (int i = 0; i < maxI; i++) {
            for (int j = 0; j < maxJ; j++) {
                final var currentPos = new Vector2(j, i);
                int x = Math.multiplyExact(j, Block.BLOCK_SIZE);
                int y = Math.multiplyExact(i, Block.BLOCK_SIZE);
                if (!path.contains(currentPos)) {
                    blocks.add(new Block(x, y));
                } else {
                    road.add(new Road(x, y));
                }
            }
        }

        final Door end = new Door(grid[(int) endPos.y][(int) endPos.x].getPosition());
        final Door start = new Door(grid[(int) startPos.y][(int) startPos.x].getPosition());
        final Player player = new Player(grid[(int) startPos.y][(int) startPos.x].getPosition());

        return MAP_CONFIG = new MapConfig(road, blocks, start, end, player, generateEnemies(path, grid), getUIS(player));
    }

    private static List<IUI> getUIS(Player player) {
        List<IUI> uis = new ArrayList<>();
        uis.add(new PlayerLife(player));
        return uis;
    }

    private static List<AEnemy> generateEnemies(List<Vector2> path, Node[][] grid) {
        List<AEnemy> enemies = new ArrayList<>();
        for (int i = 0; i < path.size() / 3; i++) {
            int randIdx = -1;
            Vector2 pos = null;
            while (randIdx == -1) {
                randIdx = random.nextInt(path.size() - 1) + 1;
                int x = (int) path.get(randIdx).x;
                int y = (int) path.get(randIdx).y;
                pos = grid[y][x].getPosition();
                if (enemies.stream().anyMatch(e -> e.getX() == x && e.getY() == y)) {
                    randIdx = -1;
                }
            }
            enemies.add(EnemyTypes.nextRandomInstance(pos));
        }
        return enemies;
    }

    private static Vector2 pickOnePos(Side side, Node[][] grid) {
        int x, y;
        switch (side) {
            case LEFT:
                x = 0;
                y = random.nextInt(grid.length - 1);
                break;
            case BOTTOM:
                y = 0;
                x = random.nextInt(grid[y].length - 1);
                break;
            case RIGHT:
                x = grid[0].length - 1;
                y = random.nextInt(grid.length - 1);
                break;
            case TOP:
                y = grid.length - 1;
                x = random.nextInt(grid[y].length - 1);
                break;
            default:
                throw new UnsupportedOperationException("Side must be set to LEFT/RIGHT/BOTTOM/TOP");
        }
        return new Vector2(x, y);
    }

    private static Node[][] initGrid(int width, int height) {
        final int maxI = height / Block.BLOCK_SIZE;
        final int maxJ = width / Block.BLOCK_SIZE;
        Node[][] grid = new Node[maxI][maxJ];

        for (int i = 0; i < maxI; i++) {
            for (int j = 0; j < maxJ; j++) {
                grid[i][j] = new Node(i, j, Block.BLOCK_SIZE);
            }
        }
        return grid;
    }
}

