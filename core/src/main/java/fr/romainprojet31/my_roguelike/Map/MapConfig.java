package fr.romainprojet31.my_roguelike.Map;

import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.actors.Block;
import fr.romainprojet31.my_roguelike.actors.Door;
import fr.romainprojet31.my_roguelike.actors.Road;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MapConfig {
    List<Block> blocks;
    Vector2 playerPos;
    List<Road> road;
    Door start;
    Door end;
}
