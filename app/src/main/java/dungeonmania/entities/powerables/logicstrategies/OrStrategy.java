package dungeonmania.entities.powerables.logicstrategies;

import java.util.List;

import dungeonmania.entities.powerables.PowerSource;

public class OrStrategy implements LogicStrategy {

    @Override
    public boolean isOn(List<PowerSource> sources) {
        return sources.stream().anyMatch(s -> s.isPowered());
    }

}
