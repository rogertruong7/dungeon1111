package dungeonmania.entities.buildables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public abstract class Buildable extends Entity implements InventoryItem {
    public Buildable(Position position) {
        super(position);
    }
    public abstract InventoryItem buildItem(Inventory inventory, EntityFactory factory);
}
