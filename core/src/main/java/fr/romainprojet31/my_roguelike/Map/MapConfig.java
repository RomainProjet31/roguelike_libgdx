package fr.romainprojet31.my_roguelike.Map;

import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.actors.Block;
import fr.romainprojet31.my_roguelike.actors.Door;
import fr.romainprojet31.my_roguelike.actors.Player;
import fr.romainprojet31.my_roguelike.actors.Road;
import fr.romainprojet31.my_roguelike.actors.enemies.AEnemy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MapConfig {
    // Tiles
    List<Road> road;
    List<Block> blocks;
    // Know thy way
    Door start;
    Door end;
    // Living sprites
    Player player;
    List<AEnemy> enemies;

    public MapConfig(List<Road> road, List<Block> blocks, Door start, Door end, Player player, List<AEnemy> enemies) {
        this.road = road;
        this.blocks = blocks;
        this.start = start;
        this.end = end;
        this.player = player;
        this.enemies = enemies;
    }
}
