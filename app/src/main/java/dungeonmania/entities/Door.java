package dungeonmania.entities;

import dungeonmania.map.GameMap;

import dungeonmania.entities.enemies.Spider;
import dungeonmania.util.Position;

public abstract class Door extends Entity implements Overlappable {
    private boolean open = false;

    public Door(Position position) {
        super(position.asLayer(Entity.DOOR_LAYER));
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (open || entity instanceof Spider) {
            return true;
        }
        return (entity instanceof Player && isOpen());
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player))
            return;
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

    public void close() {
        open = false;
    }
}
