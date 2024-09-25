package fr.romainprojet31.my_roguelike;

import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.actors.Block;
import fr.romainprojet31.my_roguelike.models.Node;

import java.util.ArrayList;
import java.util.List;

public class MapUtils {

    public static List<Vector2> getPath(Node[][] grid, Vector2 start, Vector2 end) {
        Node goal = addPath(grid, (int) start.y, (int) start.x, end);
        assert goal != null : "The end should have been found";
        List<Vector2> way = new ArrayList<>();
        Node currentNode = goal;
        while (currentNode != null) {
            way.add(currentNode.getIndexPosition());
            currentNode = currentNode.getPrevious();
        }
        return way;
    }

    private static Node addPath(Node[][] grid, int i, int j, Vector2 end) {
        System.out.println(i + " " + j);
        Node currentNode = grid[i][j];
        currentNode.setVisited(true); // In case of start
        if (currentNode.getIndexPosition().equals(end)){
            System.out.println("HERE WE ARE");
            return currentNode;
        }

        currentNode.addNodeIfNotVisited(i > 0 ? grid[i - 1][j] : null);
        currentNode.addNodeIfNotVisited(j > 0 ? grid[i][j - 1] : null);
        currentNode.addNodeIfNotVisited(i < grid.length - 1 ? grid[i + 1][j] : null);
        currentNode.addNodeIfNotVisited(j < grid[i].length - 1 ? grid[i][j + 1] : null);

        for (Node node : currentNode.getNexts()) {
            if (node.getIndexPosition().equals(end)) return node;
        }

        for (Node node : currentNode.getNexts()) {
            Node foundEnd = addPath(grid, node.getI(), node.getJ(), end);
            if (foundEnd != null) return foundEnd;
        }
        return null;
    }
}
