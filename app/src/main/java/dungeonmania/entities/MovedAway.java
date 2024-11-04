package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface MovedAway {
    void onMovedAway(GameMap map, Entity entity);
}
