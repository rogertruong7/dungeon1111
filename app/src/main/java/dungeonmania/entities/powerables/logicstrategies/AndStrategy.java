package dungeonmania.entities.powerables.logicstrategies;

import java.util.List;

import dungeonmania.entities.powerables.PowerSource;

public class AndStrategy implements LogicStrategy {

    @Override
    public boolean isOn(List<PowerSource> sources) {
        if (sources.size() < 2) return false;
        return sources.stream().allMatch(s -> s.isPowered());
    }

}
