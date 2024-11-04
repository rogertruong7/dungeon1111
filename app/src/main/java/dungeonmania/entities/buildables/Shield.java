package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Removable;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class Shield extends Buildable implements Removable, BattleItem {
    private int durability;
    private double defence;

    public Shield(int durability, double defence) {
        super(null);
        this.durability = durability;
        this.defence = defence;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            remove(game);
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin,
                new BattleStatistics(0, 0, defence, 0, 0));
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public void remove(Game game) {
        game.removeItem(this);
    }

    public static boolean isBuildable(Inventory inventory) {
        int wood = inventory.count(Wood.class);
        int treasure = inventory.count(Treasure.class);
        int keys = inventory.count(Key.class);
        int sunstones = inventory.count(SunStone.class);
        return (wood >= 2 && (treasure >= 1 || keys >= 1 || sunstones >= 1));
    }
    @Override
    public InventoryItem buildItem(Inventory inventory, EntityFactory factory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);

        if (!(isBuildable(inventory))) {
            return null;
        }

        inventory.remove(wood.get(0));
        inventory.remove(wood.get(1));
        if (treasure.size() >= 1) {
            inventory.remove(treasure.get(0));
        } else if (keys.size() >= 1) {
            inventory.remove(keys.get(0));
        }

        return factory.buildShield();
    }
}
