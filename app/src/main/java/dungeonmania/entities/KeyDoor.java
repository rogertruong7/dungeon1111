package dungeonmania.entities;

import dungeonmania.map.GameMap;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.util.Position;

public class KeyDoor extends Door {
    private int number;

    public KeyDoor(Position position, int number) {
        super(position.asLayer(Entity.DOOR_LAYER));
        this.number = number;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (isOpen() || entity instanceof Spider) {
            return true;
        }
        return (entity instanceof Player && hasKeyOrSunstone((Player) entity));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player))
            return;

        Player player = (Player) entity;
        Inventory inventory = player.getInventory();
        Key key = inventory.getFirst(Key.class);

        if (hasKeyOrSunstone(player)) {
            inventory.remove(key);
            open();
        }
    }

    private boolean hasKeyOrSunstone(Player player) {
        Inventory inventory = player.getInventory();
        Key key = inventory.getFirst(Key.class);
        SunStone sunstone = inventory.getFirst(SunStone.class);

        return (key != null && key.getnumber() == number || (sunstone != null));
    }

}
