package dungeonmania.entities.powerables.logicstrategies;

import java.util.List;

import dungeonmania.entities.powerables.PowerSource;

public class CoAndStrategy implements LogicStrategy {

    @Override
    public boolean isOn(List<PowerSource> sources) {
        if (sources.size() < 2) return false;
        int justPoweredCount = 0;
        for (PowerSource source: sources) {
            if (source.wasJustPowered() && source.isPowered()) {
                justPoweredCount += 1;
            }
        }
        if (justPoweredCount >= 2) return true;
        return false;
    }

}
