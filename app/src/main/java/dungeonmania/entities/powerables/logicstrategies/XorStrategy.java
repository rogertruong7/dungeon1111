package dungeonmania.entities.powerables.logicstrategies;

import java.util.List;

import dungeonmania.entities.powerables.PowerSource;

public class XorStrategy implements LogicStrategy {

    @Override
    public boolean isOn(List<PowerSource> sources) {
        boolean on = false;
        for (PowerSource source: sources) {
            if (source.isPowered()) {
                if (on) {
                    return false;
                }
                on = true;
            }
        }
        return on;
    }

}
