package dungeonmania.testsCreated;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
// import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class SceptreTest {
    @Test
    @Tag("ddd-1")
    @DisplayName("Testing building a sceptre with wood and key")
 public void buildSceptreWithWoodAndKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SceptreTest_BuildWood", "c_sceptreTest_build");

        // Pick up the required items
        res = dmc.tick(Direction.UP); // wood
        res = dmc.tick(Direction.UP); // key
        res = dmc.tick(Direction.UP); // sunstone
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Craft sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // Verify the sceptre is crafted and items are consumed accordingly
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("ddd-2")
    @DisplayName("Testing building a sceptre with wood and treasure")
    public void buildSceptreWithWoodAndTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SceptreTest_BuildWood", "c_sceptreTest_build");

        // Pick up the required items
        res = dmc.tick(Direction.RIGHT); // wood
        res = dmc.tick(Direction.RIGHT); // treasure
        res = dmc.tick(Direction.RIGHT); // sunstone
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Craft sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // Verify the sceptre is crafted and items are consumed accordingly
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("ddd-3")
    @DisplayName("Testing building a sceptre with wood and sunstone")
    public void buildSceptreWithWoodAndSunstone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SceptreTest_BuildWood", "c_sceptreTest_build");

        // Pick up the required items
        res = dmc.tick(Direction.DOWN); // wood
        res = dmc.tick(Direction.DOWN); // sunstone
        res = dmc.tick(Direction.DOWN); // sunstone
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // Craft sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // Verify the sceptre is crafted and items are consumed accordingly
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("ddd-4")
    @DisplayName("Testing building a sceptre with arrow and key")
    public void buildSceptreWithArrowAndKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SceptreTest_buildarrow", "c_sceptreTest_build");

        // Pick up the required items
        res = dmc.tick(Direction.UP); // arrow
        res = dmc.tick(Direction.UP); // arrow
        res = dmc.tick(Direction.UP); // key
        res = dmc.tick(Direction.UP); // sunstone
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Craft sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // Verify the sceptre is crafted and items are consumed accordingly
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("ddd-5")
    @DisplayName("Testing building a sceptre with arrow and treasure")
    public void buildSceptreWithArrowAndTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SceptreTest_buildarrow", "c_sceptreTest_build");

        // Pick up the required items
        res = dmc.tick(Direction.RIGHT); // arrow
        res = dmc.tick(Direction.RIGHT); // arrow
        res = dmc.tick(Direction.RIGHT); // treasure
        res = dmc.tick(Direction.RIGHT); // sunstone
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Craft sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // Verify the sceptre is crafted and items are consumed accordingly
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("ddd-6")
    @DisplayName("Testing building a sceptre with arrow and sunstone")
    public void buildSceptreWithArrowAndSunstone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SceptreTest_buildarrow", "c_sceptreTest_build");

        // Pick up the required items
        res = dmc.tick(Direction.DOWN); // arrow
        res = dmc.tick(Direction.DOWN); // arrow
        res = dmc.tick(Direction.DOWN); // sunstone
        res = dmc.tick(Direction.DOWN); // sunstone
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // Craft sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // Verify the sceptre is crafted and items are consumed accordingly
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("ddd-7")
    @DisplayName("Testing sceptre allies enemy")
    public void sceptreAlliesEnemy() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SceptreTest_alliesEnemy", "c_sceptreTest_build");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        // Pick up the required items
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Use sceptre to ally an enemy
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        // Check that no treasure was used
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        // Check that sceptre has been used
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
    }

    // @Test
    // @Tag("d-16")
    // @DisplayName("Testing sceptre allies enemy from any distance")
    // public void sceptreAlliesEnemyFromAnyDistance() {
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     DungeonResponse res = dmc.newGame("d_SceptreTest_alliesEnemyDistance", "c_sceptreTest_alliesEnemyDistance");

    //     // Pick up the required items
    //     res = dmc.tick(Direction.RIGHT); // sceptre
    //     assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

    //     // Move away from enemy
    //     res = dmc.tick(Direction.DOWN);
    //     res = dmc.tick(Direction.DOWN);
    //     res = dmc.tick(Direction.DOWN);

    //     // Use sceptre to ally an enemy
    //     res = dmc.interact(TestUtils.getEntities(res, "enemy").get(0).getId());

    //     // Verify the enemy is now an ally despite the distance
    //     assertTrue(TestUtils.getEntities(res, "enemy").get(0).isAllied());
    // }

    // @Test
    // @Tag("d-17")
    // @DisplayName("Testing enemy is no longer allied on tick 3")
    // public void enemyNoLongerAlliedOnTick3() {
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     DungeonResponse res = dmc.newGame("d_SceptreTest_alliesEnemy", "c_sceptreTest_build");
    //     String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
    //     // Pick up the required items
    //     dmc.tick(Direction.RIGHT);
    //     dmc.tick(Direction.RIGHT);
    //     res = dmc.tick(Direction.RIGHT);
    //     List<EntityResponse> entities = res.getEntities();
    //     res = assertDoesNotThrow(() -> dmc.build("sceptre"));
    //     assertEquals(1, TestUtils.getInventory(res, "sceptre").size());


    //     // Use sceptre to ally an enemy
    //     res = assertDoesNotThrow(() -> dmc.interact(mercId));
    //     // Check that sceptre has been used
    //     assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
    //     assertEquals(1, TestUtils.countEntityOfType(entities, "mercenary"));
    //     dmc.tick(Direction.RIGHT);
    //     dmc.tick(Direction.RIGHT);
    //     res = dmc.tick(Direction.RIGHT);
    //     List<EntityResponse> entities2 = res.getEntities();
    //     assertEquals(0, TestUtils.countEntityOfType(entities2, "mercenary"));
    // }

    // @Test
    // @Tag("d-18")
    // @DisplayName("Testing correct values of mindControlDuration")
    // public void correctValuesOfMindControlDuration() {
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     DungeonResponse res = dmc.newGame("d_SceptreTest_mindControlDuration", "c_sceptreTest_mindControlDuration");

    //     // Pick up the required items
    //     res = dmc.tick(Direction.RIGHT); // sceptre
    //     assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

    //     // Use sceptre to ally an enemy
    //     res = dmc.interact(TestUtils.getEntities(res, "enemy").get(0).getId());

    //     // Verify the enemy is now an ally
    //     assertTrue(TestUtils.getEntities(res, "enemy").get(0).isAllied());

    //     // Advance time and check the mindControlDuration at each tick
    //     for (int i = 1; i <= 3; i++) {
    //         res = dmc.tick(Direction.NONE);
    //         if (i < 3) {
    //             assertTrue(TestUtils.getEntities(res, "enemy").get(0).isAllied());
    //         } else {
    //             assertTrue(!TestUtils.getEntities(res, "enemy").get(0).isAllied());
    //         }
    //     }
    // }
}
