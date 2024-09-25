package fr.romainprojet31.my_roguelike.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AStarPath {
    private List<Node> path;
    private boolean killed;

    public AStarPath() {
        path = new ArrayList<>();
        killed = false;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }
}
