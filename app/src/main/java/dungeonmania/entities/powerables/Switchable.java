package dungeonmania.entities.powerables;

import dungeonmania.map.GameMap;

public interface Switchable {
    public boolean isPowered();
    public boolean checkPoweredNow();
    public void activate(GameMap map);
    public void setPowered(boolean powered);
    public void addSource(PowerSource powerSource);
    public void removeSource(PowerSource source);
}
