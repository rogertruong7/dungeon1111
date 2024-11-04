package dungeonmania.testsCreated;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SunstoneTest {
    @Test
    @Tag("d-1")
    @DisplayName("Testing sunstone can be picked up and added to inventory")
    public void pickupSunstone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunstoneTest_pickUpSunstone", "c_sunstoneTest_pickUpSunstone");

        assertEquals(1, TestUtils.getEntities(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "sun_stone").size());
    }

    @Test
    @Tag("d-2")
    @DisplayName("Testing sunstone can open doors and is retained after use")
    public void sunstoneOpensDoors() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunstoneTest_pickUpSunstone", "c_sunstoneTest_pickUpSunstone");

        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        assertEquals(1, TestUtils.getEntities(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "sun_stone").size());

        // walk through door
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "sun_stone").size());

    }

    @Test
    @Tag("d-3")
    @DisplayName("Test using sunstone to hit a treasure goal")
    public void treasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunstoneTest_treasureandcrafting", "c_sunstoneTest_pickUpSunstone");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        // collect item that is not treasure or sunstone
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // move to right
        res = dmc.tick(Direction.RIGHT);
        // pick up sunstone
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // assert treasure goal met
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));
    }

    @Test
    @Tag("d-4")
    @DisplayName("Testing sunstone cannot be used to bribe")
    public void sunstoneCannotBeUsedToBribe() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunstoneTest_cannotBribe", "c_sunstoneTest_pickUpSunstone");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // Pick up the sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "sun_stone").size());
        // Attempt to bribe a mercenary
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        // Verify the player still has the sunstone and mercenary is not bribed
        assertEquals(3, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("d-5")
    @DisplayName("Sunstone can replace keys and treasure for crafting if insufficient materials, player keeps sunstone")
    public void sunstoneInterchangeableForCrafting() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunstoneTest_crafting", "c_sunstoneTest_pickUpSunstone");

        // Pick up the required items
        res = dmc.tick(Direction.RIGHT); // wood
        res = dmc.tick(Direction.RIGHT); // wood
        res = dmc.tick(Direction.RIGHT); // key
        res = dmc.tick(Direction.RIGHT); // sunstone
        assertEquals(2, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        // Craft shield using sunstone instead of key or treasure
        res = assertDoesNotThrow(() -> dmc.build("shield"));

        // Verify the shield is crafted and sunstone is retained
        assertEquals(1, TestUtils.getInventory(res, "shield").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("d-6")
    @DisplayName("Testing sunstone will be consumed if used as listed ingredient")
    public void sunstoneConsumedIfListedIngredient() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunstoneTest_crafting", "c_sunstoneTest_pickUpSunstone");

        // Pick up the required items
        res = dmc.tick(Direction.RIGHT); // wood
        res = dmc.tick(Direction.RIGHT); // wood
        res = dmc.tick(Direction.RIGHT); // key
        res = dmc.tick(Direction.RIGHT); // sunstone
        assertEquals(2, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        // Craft sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // Verify the shield is crafted and sunstone is retained
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("d-7")
    @DisplayName("Testing player can hold multiple sunstones in inventory")
    public void multipleSunstonesInInventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunstoneTest_crafting", "c_sunstoneTest_pickUpSunstone");

        // Pick up multiple sunstones
        res = dmc.tick(Direction.RIGHT); // first sunstone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // Verify player has multiple sunstones in inventory
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());
    }
}
