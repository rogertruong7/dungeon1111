package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Removable;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class Bow extends Buildable implements Removable, BattleItem {
    private int durability;

    public Bow(int durability) {
        super(null);
        this.durability = durability;

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
                new BattleStatistics(0, 0, 0, 0, 0));
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

        int arrow = inventory.count(Arrow.class);
        return (wood >= 1 && arrow >= 3);
    }
    @Override
    public InventoryItem buildItem(Inventory inventory, EntityFactory factory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Arrow> arrow = inventory.getEntities(Arrow.class);

        if (!(isBuildable(inventory))) {
            return null;
        }

        inventory.remove(wood.get(0));
        inventory.remove(arrow.get(0));
        inventory.remove(arrow.get(1));
        inventory.remove(arrow.get(2));

        return factory.buildBow();
    }
}
