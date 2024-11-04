package dungeonmania.entities.powerables;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Door;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.powerables.logicstrategies.LogicStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.NameConverter;
import dungeonmania.util.Position;

public class SwitchDoor extends Door implements Switchable {
    private boolean powered = false;
    private List<PowerSource> sources = new ArrayList<>();
    private LogicStrategy logicStrategy;

    public SwitchDoor(Position position, LogicStrategy logicStrategy) {
        super(position);
        this.logicStrategy = logicStrategy;
    }

    @Override
    public boolean checkPoweredNow() {
        if (!powered) {
            return logicStrategy.isOn(sources);
        }
        return sources.stream().anyMatch(s -> s.isPowered());
    }

    @Override
    public void addSource(PowerSource powerSource) {
        if (powerSource != null && !(sources.contains(powerSource))) {
            sources.add(powerSource);
        }
    }

    @Override
    public void removeSource(PowerSource source) {
        if (sources.contains(source)) {
            sources.remove(source);
        }
    }

    @Override
    public void activate(GameMap map) {
        // If its still unpowered, return. If its still powered, return
        if (checkPoweredNow() == isPowered()) return;
        // Changed status so we change powered attribute.
        if (powered) {
            setPowered(false);
            close();
        } else {
            setPowered(true);
            open();
        }
        NameConverter.toSnakeCase(this);
    }

    @Override
    public boolean isPowered() {
        return this.powered;
    }

    @Override
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player)) {
            return;
        } else if ((entity instanceof Player) && !isPowered()) {
            return;
        }
    }

}
