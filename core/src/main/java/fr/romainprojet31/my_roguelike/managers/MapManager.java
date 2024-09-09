package fr.romainprojet31.my_roguelike.managers;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.Map.MapConfig;
import fr.romainprojet31.my_roguelike.actors.Block;
import fr.romainprojet31.my_roguelike.actors.Door;
import fr.romainprojet31.my_roguelike.actors.Player;
import fr.romainprojet31.my_roguelike.actors.Road;
import fr.romainprojet31.my_roguelike.actors.enemies.AEnemy;
import fr.romainprojet31.my_roguelike.constants.EnemyTypes;
import fr.romainprojet31.my_roguelike.constants.Side;
import fr.romainprojet31.my_roguelike.exceptions.NoMoreSolutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapManager {

    private static final Random random = new Random();

    public static MapConfig generateMap(int width, int height, Side start) {
        GridCell[][] grid = initGrid(width, height);
        Vector2 startPos = pickOnePos(start, grid);
        Vector2 endPos = pickOnePos(Side.pickOne(), grid);

        List<Vector2> path = getPath(grid, startPos, endPos);
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
        Vector2 playerPos = grid[(int) startPos.y][(int) startPos.x].getPosition(new Vector2());
        Vector2 startMapPos = grid[(int) startPos.y][(int) startPos.x].getPosition(new Vector2());
        Vector2 endMapPos = grid[(int) endPos.y][(int) endPos.x].getPosition(new Vector2());
        return new MapConfig(road, blocks, new Door(startMapPos), new Door(endMapPos), new Player(playerPos), generateEnemies(path, grid));
    }

    private static List<AEnemy> generateEnemies(List<Vector2> path, GridCell[][] grid) {
        List<AEnemy> enemies = new ArrayList<>();
        for (int i = 0; i < path.size() / 3; i++) {
            int randIdx = -1;
            Vector2 pos = null;
            while (randIdx == -1) {
                randIdx = random.nextInt(path.size() - 1) + 1;
                int x = (int) path.get(randIdx).x;
                int y = (int) path.get(randIdx).y;
                pos = grid[y][x].getPosition(new Vector2());
                if (enemies.stream().anyMatch(e -> e.getX() == x && e.getY() == y)) {
                    randIdx = -1;
                }
            }
            enemies.add(EnemyTypes.nextRandomInstance(pos));
        }
        return enemies;
    }

    private static Vector2 pickOnePos(Side side, GridCell[][] grid) {
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

    private static GridCell[][] initGrid(int width, int height) {
        final int maxI = height / Block.BLOCK_SIZE;
        final int maxJ = width / Block.BLOCK_SIZE;
        GridCell[][] grid = new GridCell[maxI][maxJ];

        for (int i = 0; i < maxI; i++) {
            for (int j = 0; j < maxJ; j++) {
                int x = j * Block.BLOCK_SIZE;
                int y = i * Block.BLOCK_SIZE;
                grid[i][j] = new GridCell(x, y, Block.BLOCK_SIZE);
            }
        }
        return grid;
    }

    /**
     * @param grid     The projection of the map
     * @param startPos The start corresponding to grid[y][x]
     * @param endPos   The goal corresponding to grid[y][x]
     * @return A list of all coordinates grid[y][x] from the start to the end
     */
    private static List<Vector2> getPath(GridCell[][] grid, Vector2 startPos, Vector2 endPos) {
        List<Vector2> path = null;
        int nbTries = 0;
        while (path == null) {
            try {
                path = singleTryToGetPath(grid, startPos, endPos);
            } catch (NoMoreSolutionException e) {
                nbTries++;
                System.out.printf("Failure at the try n°%d%n. Reinitializing the grid...\n", nbTries);
                for (GridCell[] gridCells : grid) {
                    for (GridCell gridCell : gridCells) {
                        gridCell.setVisited(false);
                    }
                }
            }
        }
        return path;
    }

    private static List<Vector2> singleTryToGetPath(GridCell[][] grid, Vector2 startPos, Vector2 endPos) throws NoMoreSolutionException {
        List<Vector2> path = new ArrayList<>();
        Vector2 current = startPos;
        path.add(current);
        do {
            current = next(grid, current, endPos);
            if (current != null) path.add(current);
        } while (current != null);
        path.add(endPos);
        return path.size() > 2 ? path : null;
    }

    private static Vector2 next(GridCell[][] grid, Vector2 currentCell, Vector2 goalPos) throws NoMoreSolutionException {
        List<Vector2> possibilities = new ArrayList<>();

        if (currentCell.x > 0) {
            var left = new Vector2(currentCell.x - 1, currentCell.y);
            GridCell goal = addOrGetGoal(left, goalPos, grid, possibilities);
            if (goal != null) return null;
        }
        if (currentCell.y > 0) {
            var top = new Vector2(currentCell.x, currentCell.y - 1);
            GridCell goal = addOrGetGoal(top, goalPos, grid, possibilities);
            if (goal != null) return null;
        }
        if (currentCell.x < grid[(int) currentCell.y].length - 1) {
            var right = new Vector2(currentCell.x + 1, currentCell.y);
            GridCell goal = addOrGetGoal(right, goalPos, grid, possibilities);
            if (goal != null) return null;
        }
        if (currentCell.y < grid.length - 1) {
            var bottom = new Vector2(currentCell.x, currentCell.y + 1);
            GridCell goal = addOrGetGoal(bottom, goalPos, grid, possibilities);
            if (goal != null) return null;
        }

        if (!possibilities.isEmpty()) {
            int index = possibilities.size() == 1 ? 0 : new Random().nextInt(possibilities.size() - 1);
            Vector2 chosenCell = possibilities.get(index);
            grid[(int) chosenCell.y][(int) chosenCell.x].setVisited(true);
            return chosenCell;
        } else {
            throw new NoMoreSolutionException(currentCell);
        }
    }

    private static GridCell addOrGetGoal(Vector2 pos, Vector2 goalPos, GridCell[][] grid, final List<Vector2> possibilities) {
        GridCell cell = grid[(int) pos.y][(int) pos.x];
        if (goalPos.x == pos.x && goalPos.y == pos.y) {
            return cell;
        } else if (!cell.isVisited()) {
            possibilities.add(pos);
        }
        return null;
    }
}

class GridCell extends Rectangle {
    private boolean visited;

    public GridCell(int x, int y, int size) {
        super(x, y, size, size);
        this.visited = false;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }
}
