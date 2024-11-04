package dungeonmania.testsCreated;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

public class MidnightArmourTest {
    @Test
    @Tag("dd-1")
    @DisplayName("Testing crafting midnight armour")
    public void buildMidnightArmour() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_MidnightArmourTest_build", "c_midnightArmourTest_build");

        // Pick up the required items
        res = dmc.tick(Direction.RIGHT); // sword
        res = dmc.tick(Direction.RIGHT); // sunstone
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Craft midnight armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        // Verify the midnight armour is crafted and items are consumed accordingly
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
        assertEquals(0, TestUtils.getInventory(res, "armour").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("d-20")
    @DisplayName("Testing midnight armour cannot be crafted with zombies in dungeon")
    public void cannotCraftMidnightArmourWithZombies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_MidnightArmourTest_zombies", "c_midnightArmourTest_build");

        // Pick up the required items
        res = dmc.tick(Direction.RIGHT); // sword
        res = dmc.tick(Direction.RIGHT); // sunstone
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Attempt to craft midnight armour
        Exception exception = null;

        try {
            res = dmc.build("midnight_armour");
        } catch (InvalidActionException e) {
            exception = e;
        }


        // Verify the midnight armour is not crafted and items are not consumed
        assertTrue(exception != null);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("d-21")
    @DisplayName("Testing midnight armour buffs")
    public void midnightArmourBuffs() {
        DungeonManiaController dmc = new DungeonManiaController();
        String config = "c_midnightArmourTest_buffs";
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_buffs", config);

        // Pick up the required items
        res = dmc.tick(Direction.RIGHT); // sword
        res = dmc.tick(Direction.RIGHT); // sunstone
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Craft midnight armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        BattleResponse battle = res.getBattles().get(0);

        RoundResponse firstRound = battle.getRounds().get(0);
        // Verify the player's stats are buffed
        int enemyAttack = Integer.parseInt(TestUtils.getValueFromConfigFile("spider_attack", config));
        int midnightArmourDefence = Integer.parseInt(TestUtils.
                                    getValueFromConfigFile("midnight_armour_defence", config));

        int expectedDamage = (enemyAttack - midnightArmourDefence) / 10;
        assertEquals(expectedDamage, -firstRound.getDeltaCharacterHealth(), 0.001);

    }
    // @Test
    // @Tag("d-22")
    // @DisplayName("Testing midnight armour lasts forever")
    // public void midnightArmourLastsForever() {
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     DungeonResponse res = dmc.newGame("d_MidnightArmourTest_lastForever", "c_midnightArmourTest_lastForever");

    //     // Pick up the required items
    //     res = dmc.tick(Direction.RIGHT); // armour
    //     res = dmc.tick(Direction.RIGHT); // sunstone
    //     assertEquals(1, TestUtils.getInventory(res, "armour").size());
    //     assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    //     // Craft midnight armour
    //     res = dmc.build("midnight_armour");
    //     assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

    //     // Verify the midnight armour lasts forever (doesn't get consumed)
    //     for (int i = 0; i < 100; i++) {
    //         res = dmc.tick(Direction.NONE);
    //         assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
    //     }
    // }
}
