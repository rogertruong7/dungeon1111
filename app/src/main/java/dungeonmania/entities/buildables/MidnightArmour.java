package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class MidnightArmour extends Buildable implements BattleItem {
    private int durability;
    private int attack;
    private double defence;

    public MidnightArmour(double defence, int attack) {
        super(null);
        this.attack = attack;
        this.defence = defence;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, attack, defence, 0, 0));
    }

    @Override
    public int getDurability() {
        return this.durability = durability;
    }

    @Override
    public void use(Game game) {
        return;
    }

    public static boolean isBuildable(Inventory inventory) {
        int swords = inventory.count(Sword.class);
        int sunstones = inventory.count(SunStone.class);
        return (swords >= 1 && sunstones >= 1);
    }
    @Override
    public InventoryItem buildItem(Inventory inventory, EntityFactory factory) {
        List<Sword> sword = inventory.getEntities(Sword.class);
        List<SunStone> sunstone = inventory.getEntities(SunStone.class);

        if (!(isBuildable(inventory))) {
            return null;
        }

        inventory.remove(sword.get(0));
        inventory.remove(sunstone.get(0));

        return factory.buildMidnightArmour();
    }
}
