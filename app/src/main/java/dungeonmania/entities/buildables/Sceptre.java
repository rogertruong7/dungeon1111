package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class Sceptre extends Buildable {
    private int mindControlDuration;

    public Sceptre(int mindControlDuration) {
        super(null);
        this.mindControlDuration = mindControlDuration;
    }

    public int getMindControlDuration() {
        return mindControlDuration;
    }

    public static boolean isBuildable(Inventory inventory) {
        int sunstones = inventory.count(SunStone.class);
        int wood = inventory.count(Wood.class);
        int treasure = inventory.count(Treasure.class);
        int keys = inventory.count(Key.class);
        int arrows = inventory.count(Arrow.class);
        return ((wood >= 1 || arrows >= 2) && (keys >= 1 || treasure >= 1 || sunstones >= 2) && sunstones >= 1);
    }
    @Override
    public InventoryItem buildItem(Inventory inventory, EntityFactory factory) {
        List<SunStone> sunstone = inventory.getEntities(SunStone.class);
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);
        List<Arrow> arrow = inventory.getEntities(Arrow.class);

        if (!(isBuildable(inventory))) {
            return null;
        }

        if (wood.size() >= 1) {
            inventory.remove(wood.get(0));
        } else if (arrow.size() >= 2) {
            inventory.remove(arrow.get(0));
            inventory.remove(arrow.get(1));
        }
        if (treasure.size() >= 1) {
            inventory.remove(treasure.get(0));
        } else if (keys.size() >= 1) {
            inventory.remove(keys.get(0));
        }
        inventory.remove(sunstone.get(0));

        return factory.buildSceptre();
    }
}
