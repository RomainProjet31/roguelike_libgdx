package fr.romainprojet31.my_roguelike.constants;

import com.badlogic.gdx.math.Vector2;
import fr.romainprojet31.my_roguelike.actors.enemies.AEnemy;
import fr.romainprojet31.my_roguelike.actors.enemies.Slime;
import lombok.AllArgsConstructor;

import java.util.Random;
import java.util.function.Function;

@AllArgsConstructor
public enum EnemyTypes {
    SLIME(Slime::new);

    private final Function<Vector2, AEnemy> builder;

    public static AEnemy nextRandomInstance(Vector2 vector2) {
        var enemies = EnemyTypes.values();
        return enemies[new Random().nextInt(enemies.length)].builder.apply(vector2);
    }
}
