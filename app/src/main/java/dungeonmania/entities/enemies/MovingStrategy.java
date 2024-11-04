package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.util.Position;

public interface MovingStrategy {
    public void move(Game game, Enemy enemy, Position enemyCurrentPosition);
}
