package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    private MovingStrategy movingStrategy = new MovingRandomStrategy();

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }

    @Override
    public void move(Game game) {
        GameMap map = game.getMap();
        if (map.isPlayerInvincible()) {
            movingStrategy = new MovingFromInvincibleStrategy();
        } else if (map.isPlayerInvisible()) {
            movingStrategy = new MovingRandomStrategy();
        }
        movingStrategy.move(game, this, this.getPosition());
    }

}

