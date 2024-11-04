package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class MovingSpiderStrategy implements MovingStrategy {
    @Override
    public void move(Game game, Enemy enemy, Position enemyCurrentPosition) {
        if (!(enemy instanceof Spider) || enemy == null) return;
        Spider spider = (Spider) enemy;
        boolean forward = spider.isForward();
        List<Position> movementTrajectory = spider.getMovementTrajectory();
        int nextPositionElement = spider.getNextPositionElement();
        Position nextPos = movementTrajectory.get(nextPositionElement);
        List<Entity> entities = game.getMap().getEntities(nextPos);
        if (entities != null && entities.size() > 0 && entities.stream().anyMatch(e -> e instanceof Boulder)) {
            forward = !forward;
            spider.setForward(forward);
            nextPositionElement = updateNextPosition(forward, nextPositionElement);
            nextPositionElement = updateNextPosition(forward, nextPositionElement);
            spider.setNextPositionElement(nextPositionElement);
        }
        nextPos = movementTrajectory.get(nextPositionElement);
        entities = game.getMap().getEntities(nextPos);
        if (entities == null || entities.size() == 0
                || entities.stream().allMatch(e -> e.canMoveOnto(game.getMap(), enemy))) {
            game.getMap().moveTo(enemy, nextPos);
            spider.setNextPositionElement(updateNextPosition(forward, nextPositionElement));
        }
    }

    private int updateNextPosition(boolean forward, int nextPositionElement) {
        if (forward) {
            nextPositionElement++;
            if (nextPositionElement == 8) {
                nextPositionElement = 0;
            }
        } else {
            nextPositionElement--;
            if (nextPositionElement == -1) {
                nextPositionElement = 7;
            }
        }
        return nextPositionElement;
    }
}
