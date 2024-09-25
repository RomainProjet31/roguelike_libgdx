package fr.romainprojet31.my_roguelike.models;

import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.actors.Block;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Node {
    private Vector2 position;
    private int i;
    private int j;
    private List<Node> nexts;
    private Node previous;
    private boolean visited;
    private int size;

    public Node(int i, int j, int size) {
        this.i = i;
        this.j = j;
        this.size = size;
        this.visited = false;
        int x = j * Block.BLOCK_SIZE;
        int y = i * Block.BLOCK_SIZE;
        this.nexts = new ArrayList<>();
        this.position = new Vector2(x, y);
    }

    public void addNodeIfNotVisited(@Nullable Node node) {
        if (node != null && !node.isVisited()) {
            node.setVisited(true);
            nexts.add(node);
            node.setPrevious(this);
        }
    }

    public Vector2 getIndexPosition() {
        return new Vector2(j, i);
    }
}
