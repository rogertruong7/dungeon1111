package dungeonmania.entities;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.Buildable;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.playerState.BaseState;
import dungeonmania.entities.playerState.PlayerState;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements Battleable, Overlappable {
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 5.0;
    private BattleStatistics battleStatistics;
    private Inventory inventory;
    private PotionManager potionManager;
    private BattleItem currentWeapon;

    private int collectedTreasureCount = 0;
    private int spawnersDestroyedCount = 0;
    private int defeatedEnemiesCount = 0;

    private PlayerState state;

    public Player(Position position, double health, double attack) {
        super(position);
        battleStatistics = new BattleStatistics(health, attack, 0, BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER);
        inventory = new Inventory();
        potionManager = new PotionManager(this);
        state = new BaseState(this);
    }

    public int getCollectedTreasureCount() {
        return collectedTreasureCount;
    }

    public int getSpawnersDestroyedCount() {
        return spawnersDestroyedCount;
    }

    public void destroyedSpawner() {
        this.spawnersDestroyedCount += 1;
    }

    public int getDefeatedEnemiesCount() {
        return defeatedEnemiesCount;
    }

    public void destroyedEnemy() {
        this.defeatedEnemiesCount += 1;
    }

    public boolean hasWeapon() {
        return inventory.hasWeapon();
    }

    public BattleItem getWeapon() {
        currentWeapon = inventory.getWeapon();
        return currentWeapon;
    }

    public void useWeapon(Game game) {
        if (currentWeapon == null) {
            getWeapon();
        }
        currentWeapon.use(game);
    }

    public List<String> getBuildables() {
        return inventory.getBuildables();
    }

public boolean build(String entity, EntityFactory factory, GameMap map) {
    Buildable buildable = createBuildable(entity, factory);
    if (buildable == null) return false;

    InventoryItem item = inventory.checkBuildCriteria(map, true, factory, buildable);
    if (item == null) return false;

    return inventory.add(item);
}

private Buildable createBuildable(String entity, EntityFactory factory) {
    switch (entity) {
        case "bow":
            return new Bow(0); // Example parameters
        case "shield":
            return new Shield(0, 0); // Example: add constructor parameters if needed
        case "midnight_armour":
            return new MidnightArmour(0, 0); // Example parameters
        case "sceptre":
            return new Sceptre(0); // Example: add constructor parameters if needed
        default:
            return null;
    }
}

    public void move(GameMap map, Direction direction) {
        this.setFacing(direction);
        map.moveTo(this, Position.translateBy(this.getPosition(), direction));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Enemy) {
            if (entity instanceof Mercenary) {
                if (((Mercenary) entity).isAllied())
                    return;
            }
            map.handleBattle(this, (Enemy) entity);
        } else if (entity instanceof InventoryItem) {
            pickUp((InventoryItem) entity, map);
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public Entity getEntity(String itemUsedId) {
        return inventory.getEntity(itemUsedId);
    }

    public void pickUp(InventoryItem item, GameMap map) {
        // Do not pick up the key if the player already has one
        if (item instanceof Key && inventory.getEntities(Key.class).size() >= 1) {
            return;
        }
        if (item instanceof Treasure || item instanceof SunStone)
            collectedTreasureCount++;
        boolean added = inventory.add(item);
        if (added) {
            map.destroyEntity((Entity) item);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public <T extends InventoryItem> void use(Class<T> itemType) {
        T item = inventory.getFirst(itemType);
        if (item != null)
            inventory.remove(item);
    }

    public void use(Bomb bomb, GameMap map) {
        inventory.remove(bomb);
        bomb.onPutDown(map, getPosition());
    }

    public void changeState(PlayerState playerState) {
        state = playerState;
    }

    public void use(Potion potion, int tick) {
        potionManager.usePotion(potion, tick);
    }

    public void onTick(int tick) {
        potionManager.onTick(tick);
    }

    public void remove(Removable item) {
        inventory.remove((InventoryItem) item);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public <T extends InventoryItem> int countEntityOfType(Class<T> itemType) {
        return inventory.count(itemType);
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        if (state.isInvincible()) {
            return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 1, 1, true, true));
        } else if (state.isInvisible()) {
            return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 1, 1, false, false));
        }
        return origin;
    }

    public boolean isInvincible() {
        return potionManager.isPlayerInvincible();
    }

    public boolean isInvisible() {
        return potionManager.isPlayerInvisible();
    }

    public Potion getEffectivePotion() {
        return potionManager.getEffectivePotion();
    }

}
