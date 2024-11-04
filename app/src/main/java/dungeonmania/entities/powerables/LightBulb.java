package dungeonmania.entities.powerables;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.powerables.logicstrategies.LogicStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.NameConverter;
import dungeonmania.util.Position;

public class LightBulb extends Entity implements Switchable {
    private boolean powered = false;
    private List<PowerSource> sources = new ArrayList<>();
    private LogicStrategy logicStrategy;

    public LightBulb(Position position, LogicStrategy logicStrategy) {
        super(position);
        this.logicStrategy = logicStrategy;
    }

    /*
     * logical entities will also remain
     * activated as long as there is a current
     * from a switch running through them
     *
     */
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

    // In case of explosion of source
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
        } else {
            setPowered(true);
        }
        NameConverter.toSnakeCase(this);
    }

    @Override
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    @Override
    public boolean isPowered() {
        return powered;
    }
}
