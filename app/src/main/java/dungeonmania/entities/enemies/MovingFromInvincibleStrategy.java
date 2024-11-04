package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovingFromInvincibleStrategy implements MovingStrategy {
    @Override
    public void move(Game game, Enemy enemy, Position enemyCurrentPosition) {
        Position nextPos;
        GameMap map = game.getMap();
        Position plrDiff = Position.calculatePositionBetween(game.getPlayerPosition(), enemyCurrentPosition);

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(enemyCurrentPosition, Direction.RIGHT)
                : Position.translateBy(enemy.getPosition(), Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(enemyCurrentPosition, Direction.UP)
                : Position.translateBy(enemy.getPosition(), Direction.DOWN);
        Position offset = enemy.getPosition();
        if (plrDiff.getY() == 0 && map.canMoveTo(enemy, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && map.canMoveTo(enemy, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else
                offset = enemy.getPosition();
        } else {
            if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else
                offset = enemy.getPosition();
        }
        nextPos = offset;
        game.moveEnemy(enemy, nextPos);
    }
}
