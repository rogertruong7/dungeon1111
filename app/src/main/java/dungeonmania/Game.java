package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

import dungeonmania.battles.BattleFacade;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.Removable;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.powerables.PowerSource;
import dungeonmania.entities.powerables.Switch;
import dungeonmania.entities.powerables.Switchable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.Goal;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Game {
    private String id;
    private String name;
    private Goal goals;
    private GameMap map;
    private Player player;
    private BattleFacade battleFacade;
    private EntityFactory entityFactory;
    private List<Switch> switches = new ArrayList<>();
    private List<Switchable> switchables = new ArrayList<>();
    private List<PowerSource> sources = new ArrayList<>();
    private int spawnerCount = 0;
    private boolean isInTick = false;
    public static final int PLAYER_MOVEMENT = 0;
    public static final int PLAYER_MOVEMENT_CALLBACK = 1;
    public static final int AI_MOVEMENT = 2;
    public static final int AI_MOVEMENT_CALLBACK = 3;
    public static final int ITEM_LONGEVITY_UPDATE = 4;

    private ComparableCallback currentAction = null;

    private int tickCount = 0;
    private PriorityQueue<ComparableCallback> sub = new PriorityQueue<>();
    private PriorityQueue<ComparableCallback> addingSub = new PriorityQueue<>();

    public Game(String dungeonName) {
        this.name = dungeonName;
        this.map = new GameMap();
        this.battleFacade = new BattleFacade();
    }

    public void init() {
        this.id = UUID.randomUUID().toString();
        map.init();
        this.tickCount = 0;
        player = map.getPlayer();
        switches = map.getSwitches();
        switchables = map.getSwitchables();
        sources = map.getSources();
        register(() -> player.onTick(tickCount), PLAYER_MOVEMENT, "potionQueue");
    }

    public Game tick(Direction movementDirection) {
        registerOnce(() -> player.move(this.getMap(), movementDirection), PLAYER_MOVEMENT, "playerMoves");
        sources.stream().forEach(s -> s.update());
        tick();
        switchables.stream().forEach(s -> s.activate(map));
        return this;
    }

    public void removeSwitch(Switch s) {
        if (switches.contains(s)) {
            switches.remove(s);
            sources.remove(s);
        }
    }

    public Position getPlayerPosition() {
        return player.getPosition();
    }

    public void refreshSwitchPaths() {
        switches.stream().filter(s -> s.isPowered()).forEach(s -> s.turnOn());
    }

    public void refreshSwitchables() {
        switchables.stream().forEach(s -> s.activate(map));
    }

    public Game tick(String itemUsedId) throws InvalidActionException {
        Entity item = player.getEntity(itemUsedId);
        if (item == null)
            throw new InvalidActionException(String.format("Item with id %s doesn't exist", itemUsedId));
        if (!(item instanceof Bomb) && !(item instanceof Potion))
            throw new IllegalArgumentException(String.format("%s cannot be used", item.getClass()));

        registerOnce(() -> {
            if (item instanceof Bomb)
                player.use((Bomb) item, map);
            if (item instanceof Potion)
                player.use((Potion) item, tickCount);
        }, PLAYER_MOVEMENT, "playerUsesItem");
        tick();
        return this;
    }

    public void battle(Player player, Enemy enemy) {
        battleFacade.battle(this, player, enemy);
        if (player.getBattleStatistics().getHealth() <= 0) {
            map.destroyEntity(player);
        }
        if (enemy.getBattleStatistics().getHealth() <= 0) {
            map.destroyEntity(enemy);
        }
    }

    public Game build(String buildable) throws InvalidActionException {
        List<String> buildables = player.getBuildables();
        if (buildables.contains("midnight_armour") && map.getEntities(ZombieToast.class).size() > 0) {
            buildables.remove("midnight_armour");
        }
        if (!buildables.contains(buildable)) {
            throw new InvalidActionException(String.format("%s cannot be built", buildable));
        }
        registerOnce(() -> player.build(buildable, entityFactory, map), PLAYER_MOVEMENT, "playerBuildsItem");
        tick();
        return this;
    }

    public Game interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity e = map.getEntity(entityId);
        if (e == null || !(e instanceof Interactable))
            throw new IllegalArgumentException("Entity cannot be interacted");
        if (!((Interactable) e).isInteractable(player)) {
            throw new InvalidActionException("Entity cannot be interacted");
        }
        registerOnce(() -> ((Interactable) e).interact(player, this, getMap()), PLAYER_MOVEMENT, "playerInteracts");
        tick();
        return this;
    }

    public void register(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id));
        else
            sub.add(new ComparableCallback(r, priority, id));
    }

    public void registerOnce(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id, true));
        else
            sub.add(new ComparableCallback(r, priority, id, true));
    }

    public void unsubscribe(String id) {
        if (this.currentAction != null && id.equals(this.currentAction.getId())) {
            this.currentAction.invalidate();
        }
        for (ComparableCallback c : sub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
        for (ComparableCallback c : addingSub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
    }

    public int tick() {
        PriorityQueue<ComparableCallback> nextTickSub = new PriorityQueue<>();
        isInTick = true;
        while (!sub.isEmpty()) {
            currentAction = sub.poll();
            currentAction.run();
            if (currentAction.isValid()) {
                nextTickSub.add(currentAction);
            }
        }
        isInTick = false;
        nextTickSub.addAll(addingSub);
        addingSub = new PriorityQueue<>();
        sub = nextTickSub;
        tickCount++;
        return tickCount;
    }

    public int getTick() {
        return this.tickCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Goal getGoals() {
        return goals;
    }

    public void setGoals(Goal goals) {
        this.goals = goals;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public void setEntityFactory(EntityFactory factory) {
        entityFactory = factory;
    }

    public int getCollectedTreasureCount() {
        return player.getCollectedTreasureCount();
    }

     public int getSpawnersDestroyedCount() {
        return player.getSpawnersDestroyedCount();
    }

    public int getDefeatedEnemiesCount() {
        return player.getDefeatedEnemiesCount();
    }

    public Player getPlayer() {
        return player;
    }

    public BattleFacade getBattleFacade() {
        return battleFacade;
    }

    public void removeItem(Removable item) {
        player.remove(item);
    }

    public void moveEnemy(Enemy enemy, Position pos) {
        map.moveEnemy(enemy, pos);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setBattleFacade(BattleFacade battleFacade) {
        this.battleFacade = battleFacade;
    }

    public int getSpawnerCount() {
        return spawnerCount;
    }

    public void setSpawnerCount(int count) {
        spawnerCount = count;
    }

    public boolean isInTick() {
        return isInTick;
    }

    public void setInTick(boolean isInTick) {
        this.isInTick = isInTick;
    }

    public static int getPlayerMovement() {
        return PLAYER_MOVEMENT;
    }

    public static int getPlayerMovementCallback() {
        return PLAYER_MOVEMENT_CALLBACK;
    }

    public static int getAiMovement() {
        return AI_MOVEMENT;
    }

    public static int getAiMovementCallback() {
        return AI_MOVEMENT_CALLBACK;
    }

    public static int getItemLongevityUpdate() {
        return ITEM_LONGEVITY_UPDATE;
    }

    public ComparableCallback getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(ComparableCallback currentAction) {
        this.currentAction = currentAction;
    }

    public int getTickCount() {
        return tickCount;
    }

    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }

    public PriorityQueue<ComparableCallback> getSub() {
        return sub;
    }

    public void setSub(PriorityQueue<ComparableCallback> sub) {
        this.sub = sub;
    }

    public PriorityQueue<ComparableCallback> getAddingSub() {
        return addingSub;
    }

    public void setAddingSub(PriorityQueue<ComparableCallback> addingSub) {
        this.addingSub = addingSub;
    }
}
