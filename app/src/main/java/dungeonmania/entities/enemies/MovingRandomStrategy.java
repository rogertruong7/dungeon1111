package dungeonmania.entities.enemies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class MovingRandomStrategy implements MovingStrategy {
    @Override
    public void move(Game game, Enemy enemy, Position enemyCurrentPosition) {
        Position nextPos;
        Random randGen = new Random();
        GameMap map = game.getMap();
        List<Position> pos = enemyCurrentPosition.getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> map.canMoveTo(enemy, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = enemyCurrentPosition;
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
        }
        game.moveEnemy(enemy, nextPos);
    }
}
