package dungeonmania.entities.powerables;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Boulder;
import dungeonmania.entities.Destroyable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.MovedAway;
import dungeonmania.entities.Overlappable;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Entity implements Overlappable, MovedAway, PowerSource, Destroyable {
    private boolean powered = false;
    private boolean justPowered = false;
    private List<Bomb> bombs = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();
    private List<Switchable> switchables = new ArrayList<>();

    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
    }

    @Override
    public void turnOff() {
        setPowered(false);
        turnOffAdjacentWires();
        return;
    }

    @Override
    public void turnOn() {
        if (!powered) {
            justPowered = true;
        }
        setPowered(true);
        turnOnAdjacentWires();
        return;
    }

    public void turnOnAdjacentWires() {
        wires.stream().forEach(wire -> wire.subscribe(this));
    }

    public void turnOffAdjacentWires() {
        wires.stream().forEach(wire -> wire.unsubscribe(this));
    }

    @Override
    public void subscribeSwitchable(Switchable s) {
        switchables.add(s);
    }

    public void subscribe(Wire w) {
        wires.add(w);
    }

    public void subscribe(Bomb b) {
        bombs.add(b);
    }

    public void subscribe(Bomb bomb, GameMap map) {
        bombs.add(bomb);
        if (powered) {
            bombs.stream().forEach(b -> b.activate(map));
        }
    }

    public void unsubscribe(Bomb b) {
        bombs.remove(b);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            turnOn();
            bombs.stream().forEach(b -> b.notify(map));
        }
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            turnOff();
        }
    }

    @Override
    public boolean isPowered() {
        return powered;
    }

    @Override
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    @Override
    public void update() {
        if (justPowered) {
            justPowered = false;
        }
    }

    @Override
    public boolean wasJustPowered() {
        return justPowered;
    }

    @Override
    public void onDestroy(Game g, Player player) {
        // Unsubscribe this switch from all wires
        g.removeSwitch(this);
        wires.stream().forEach(x -> x.unsubscribe(this));
        switchables.stream().forEach(s -> s.removeSource(this));
        turnOff();
    }
}
