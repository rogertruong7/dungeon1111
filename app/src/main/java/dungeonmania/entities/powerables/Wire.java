package dungeonmania.entities.powerables;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Destroyable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Wire extends Entity implements PowerSource, Destroyable {
    private boolean powered = false;
    private boolean justPowered = false;
    private List<Wire> wires = new ArrayList<>();
    private List<LogicBomb> bombs = new ArrayList<>();
    private List<Switch> poweredSwitches = new ArrayList<>();
    private List<Switchable> switchables = new ArrayList<>();

    public Wire(Position position) {
        super(position);
    }

    // After a switch is turned on, dfs is run, and each wire connected
    // to the switch has it subscribe to it
    // When the switch is turned off we unsubscribe all wires connected through dfs
    // If the wire still has a subscription to 1 or more switches, it remain on

    @Override
    public void subscribeSwitchable(Switchable s) {
        switchables.add(s);
    }

    @Override
    public void update() {
        if (justPowered) {
            justPowered = false;
        }
    }

    public void subscribe(LogicBomb bomb, GameMap map) {
        bombs.add(bomb);
        if (powered) {
            bombs.stream().forEach(b -> b.activate(map));
        }
    }

    public void subscribe(Switch s) {
        if (!(poweredSwitches.contains(s))) {
            poweredSwitches.add(s);
            turnOn();
            subscribeAdjacentWires(s);
        }
    }

    public void unsubscribe(Switch s) {
        if (poweredSwitches.contains(s)) {
            poweredSwitches.remove(s);
            checkStillOn();
            unsubscribeAdjacentWires(s);
        }
    }

    public void subscribeAdjacentWires(Switch s) {
        wires.stream().forEach(wire -> wire.subscribe(s));
    }

    public void unsubscribeAdjacentWires(Switch s) {
        wires.stream().forEach(wire -> wire.unsubscribe(s));
    }

    /*
     * as long as theres one switch powering a wire it will be on
     */
    public void checkStillOn() {
        if (poweredSwitches.size() == 0) {
            turnOff();
        }
    }

    public void connectWire(Wire w) {
        if (!(wires.contains(w))) {
            wires.add(w);
        }
    }

    @Override
    public void turnOn() {
        if (!powered) {
            justPowered = true;
        }
        setPowered(true);
    }

    @Override
    public void turnOff() {
        setPowered(false);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
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
    public boolean wasJustPowered() {
        return justPowered;
    }

    public void removeWire(Wire wire) {
        wires.remove(wire);
    }

    public void turnOffMyselfAndAdjacentWires() {
        turnOff();
        for (Wire wire: wires) {
            if (wire.isPowered()) {
                wire.turnOffMyselfAndAdjacentWires();
            }
        }
    }

    @Override
    public void onDestroy(Game g, Player player) {
        turnOffMyselfAndAdjacentWires();
        wires.stream().forEach(wire -> wire.removeWire(this));
        switchables.stream().forEach(s -> s.removeSource(this));
        g.refreshSwitchPaths();
        g.refreshSwitchables();
    }
}
