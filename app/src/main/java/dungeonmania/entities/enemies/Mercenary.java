package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;
    private MovingStrategy movingStrategy = new MovingRandomStrategy();

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;
    private boolean isAdjacentToPlayer = false;

    private int mindControlDuration;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        Position distance = Position.calculatePositionBetween(player.getPosition(), getPosition());
        int magnitude = distance.magnitude();
        return (bribeRadius >= magnitude && player.countEntityOfType(Treasure.class) >= bribeAmount);
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }

    }

    @Override
    public void interact(Player player, Game game, GameMap map) {
        Sceptre sceptre = player.getInventory().getFirst(Sceptre.class);
        if (canBeBribed(player)) {
            allied = true;
            bribe(player);
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
                isAdjacentToPlayer = true;
        } else if (sceptre != null) {
            allied = true;
            mindControlDuration = sceptre.getMindControlDuration();
            player.getInventory().remove(sceptre);
        }
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        Player player = game.getPlayer();
        if (allied) {
            nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                    : map.dijkstraPathFind(getPosition(), player.getPosition(), this);
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos))
                isAdjacentToPlayer = true;
            map.moveTo(this, nextPos);
        } else if (map.isPlayerInvisible()) {
            movingStrategy = new MovingRandomStrategy();
            movingStrategy.move(game, this, this.getPosition());
        } else if (map.isPlayerInvincible()) {
            movingStrategy = new MovingFromInvincibleStrategy();
            movingStrategy.move(game, this, this.getPosition());
        } else {
            movingStrategy = new MovingMercenaryStrategy();
            movingStrategy.move(game, this, this.getPosition());
        }
    }

    @Override
    public boolean isInteractable(Player player) {
        if (!allied && canBeBribed(player)) {
            return true;
        } else if (player.getInventory().getFirst(Sceptre.class) != null) {
            return true;
        }
        return false;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!allied)
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }
}
