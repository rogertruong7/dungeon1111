package dungeonmania.entities.powerables;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.powerables.logicstrategies.LogicStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class LogicBomb extends Bomb implements Switchable {
    private boolean powered = false;
    private List<PowerSource> sources = new ArrayList<>();
    private LogicStrategy logicStrategy;

    public LogicBomb(Position position, int radius, LogicStrategy logicStrategy) {
        super(position, radius);
        this.logicStrategy = logicStrategy;
    }

    @Override
    public boolean checkPoweredNow() {
        return logicStrategy.isOn(sources);
    }

    // In the case a source is destroyed by explosion
    @Override
    public void removeSource(PowerSource source) {
        if (sources.contains(source)) {
            sources.remove(source);
        }
    }

    @Override
    public void activate(GameMap map) {
        if (checkPoweredNow() && getState() == State.PLACED) {
            powered = true;
            super.notify(map);
        }
    }

    @Override
    public void onPutDown(GameMap map, Position p) {
        translate(Position.calculatePositionBetween(getPosition(), p));
        map.addEntity(this);
        setState(State.PLACED);
        List<Position> adjPosList = getPosition().getCardinallyAdjacentPositions();
        adjPosList.stream().forEach(node -> {
            List<Entity> entities = map.getEntities(node).stream().filter(e -> (e instanceof Switch))
                    .collect(Collectors.toList());
            entities.stream().map(Switch.class::cast).forEach(s -> s.subscribe(this, map));
            entities.stream().map(Switch.class::cast).forEach(s -> s.subscribeSwitchable(this));
            entities.stream().map(Switch.class::cast).forEach(s -> sources.add(s));
        });
        adjPosList.stream().forEach(node -> {
            List<Entity> entities = map.getEntities(node).stream().filter(e -> (e instanceof Wire))
                    .collect(Collectors.toList());
            entities.stream().map(Wire.class::cast).forEach(s -> s.subscribe(this, map));
            entities.stream().map(Wire.class::cast).forEach(s -> s.subscribeSwitchable(this));
            entities.stream().map(Wire.class::cast).forEach(s -> sources.add(s));
        });
    }

    @Override
    public void addSource(PowerSource powerSource) {
        if (powerSource != null && !(sources.contains(powerSource))) {
            sources.add(powerSource);
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
}
