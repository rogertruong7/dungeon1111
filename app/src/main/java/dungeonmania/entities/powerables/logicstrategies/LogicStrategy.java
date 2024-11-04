package dungeonmania.entities.powerables.logicstrategies;

import java.util.List;

import dungeonmania.entities.powerables.PowerSource;

public interface LogicStrategy {
    public boolean isOn(List<PowerSource> sources);
}
