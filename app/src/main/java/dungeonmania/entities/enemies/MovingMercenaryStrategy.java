package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class MovingMercenaryStrategy implements MovingStrategy {
    @Override
    public void move(Game game, Enemy enemy, Position enemyCurrentPosition) {
        Position nextPos;
        GameMap map = game.getMap();
        nextPos = map.dijkstraPathFind(enemyCurrentPosition, game.getPlayerPosition(), enemy);
        game.moveEnemy(enemy, nextPos);
    }
}
