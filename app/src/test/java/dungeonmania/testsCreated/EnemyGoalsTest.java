package dungeonmania.testsCreated;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class EnemyGoalsTest {

    @Test
    @Tag("11-10")
    @DisplayName("Test if win from 3 enemies dead and no spawners")
    public void testPlayerBattlingEnemiesConsecutivelyDefeatsThem() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_enemyGoals1", "c_enemyGoalsTest_exit");
        // 3 enemies
        DungeonResponse res = controller.tick(Direction.RIGHT);
        // 2 enemies left
        List<EntityResponse> entities = res.getEntities();
        int spiderCount = TestUtils.countEntityOfType(entities, "spider");
        int zombieCount = TestUtils.countEntityOfType(entities, "zombie_toast");
        int mercCount = TestUtils.countEntityOfType(entities, "mercenary");
        assertEquals(1, spiderCount);
        assertEquals(0, zombieCount);
        assertEquals(1, mercCount);

        // kill one enemy
        res = controller.tick(Direction.RIGHT);
        entities = res.getEntities();
        spiderCount = TestUtils.countEntityOfType(entities, "spider");
        zombieCount = TestUtils.countEntityOfType(entities, "zombie_toast");
        mercCount = TestUtils.countEntityOfType(entities, "mercenary");
        assertEquals(1, spiderCount);
        assertEquals(0, zombieCount);
        assertEquals(0, mercCount);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = controller.tick(Direction.RIGHT);
        entities = res.getEntities();
        spiderCount = TestUtils.countEntityOfType(entities, "spider");
        zombieCount = TestUtils.countEntityOfType(entities, "zombie_toast");
        mercCount = TestUtils.countEntityOfType(entities, "mercenary");
        assertEquals(0, spiderCount);
        assertEquals(0, zombieCount);
        assertEquals(0, mercCount);
        assertEquals(1, TestUtils.countEntityOfType(entities, "player"));

        // No spawners
        assertEquals("", TestUtils.getGoals(res));
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
    }

    @Test
    @Tag("10-7")
    @DisplayName("Testing destroying a zombie toast spawner and 0 enemies in goal")
    public void toastDestruction() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spawner_enemies", "c_enemyGoalsTest_zero");
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // cardinally adjacent: true, has sword: false
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // pick up sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // cardinally adjacent: false, has sword: true
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // move right
        res = dmc.tick(Direction.RIGHT);

        // cardinally adjacent: true, has sword: true, but invalid_id
        assertThrows(IllegalArgumentException.class, () -> dmc.interact("random_invalid_id"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        // cardinally adjacent: true, has sword: true
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        List<EntityResponse> entities = res.getEntities();
        System.out.println(entities);
        int spawnerCount = TestUtils.countEntityOfType(entities, "zombie_toast_spawner");
        assertEquals(0, spawnerCount);
        assertEquals("", TestUtils.getGoals(res));
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
    }

    @Test
    @Tag("10-7")
    @DisplayName("Testing 2 zombie toast spawner and 3 enemies in goal")
    public void enemyFullGoal() {
        //  PLA  ZTS
        //  SWO
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("2a_enemyGoals", "c_enemy2");
        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId1 = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        String spawnerId2 = TestUtils.getEntities(res, "zombie_toast_spawner").get(1).getId();
        res = controller.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        // 2 enemies left
        List<EntityResponse> entities = res.getEntities();
        int spiderCount = TestUtils.countEntityOfType(entities, "spider");
        int zombieCount = TestUtils.countEntityOfType(entities, "zombie_toast");
        int mercCount = TestUtils.countEntityOfType(entities, "mercenary");
        assertEquals(1, spiderCount);
        assertEquals(0, zombieCount);
        assertEquals(1, mercCount);

        // kill one enemy
        res = controller.tick(Direction.RIGHT);
        entities = res.getEntities();
        spiderCount = TestUtils.countEntityOfType(entities, "spider");
        zombieCount = TestUtils.countEntityOfType(entities, "zombie_toast");
        mercCount = TestUtils.countEntityOfType(entities, "mercenary");
        assertEquals(1, spiderCount);
        assertEquals(0, zombieCount);
        assertEquals(0, mercCount);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = controller.tick(Direction.RIGHT);
        entities = res.getEntities();
        spiderCount = TestUtils.countEntityOfType(entities, "spider");
        zombieCount = TestUtils.countEntityOfType(entities, "zombie_toast");
        mercCount = TestUtils.countEntityOfType(entities, "mercenary");
        assertEquals(0, spiderCount);
        assertEquals(0, zombieCount);
        assertEquals(0, mercCount);
        assertEquals(1, TestUtils.countEntityOfType(entities, "player"));

        // 2 spawners
        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = controller.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> controller.interact(spawnerId2));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        res = controller.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> controller.interact(spawnerId1));
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("10-7")
    @DisplayName("Testing it works with or goals")
    public void worksWithOrGoal() {
        //  PLA  ZTS
        //  SWO
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("2a_enemyOrGoals", "c_enemy2");
        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        res = controller.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        // 2 enemies left
        List<EntityResponse> entities = res.getEntities();
        int spiderCount = TestUtils.countEntityOfType(entities, "spider");
        int zombieCount = TestUtils.countEntityOfType(entities, "zombie_toast");
        int mercCount = TestUtils.countEntityOfType(entities, "mercenary");
        assertEquals(1, spiderCount);
        assertEquals(0, zombieCount);
        assertEquals(1, mercCount);

        // kill one enemy
        res = controller.tick(Direction.RIGHT);
        entities = res.getEntities();
        spiderCount = TestUtils.countEntityOfType(entities, "spider");
        zombieCount = TestUtils.countEntityOfType(entities, "zombie_toast");
        mercCount = TestUtils.countEntityOfType(entities, "mercenary");
        assertEquals(1, spiderCount);
        assertEquals(0, zombieCount);
        assertEquals(0, mercCount);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = controller.tick(Direction.RIGHT);
        entities = res.getEntities();
        assertEquals(1, TestUtils.countEntityOfType(entities, "player"));

        // 2 spawners
        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = controller.tick(Direction.RIGHT);
        res = controller.tick(Direction.DOWN);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("10-7")
    @DisplayName("Testing it works with and exit is last")
    public void worksWithAndGoal() {
         DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("2a_enemyAndGoals", "c_enemy2");
        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId1 = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        String spawnerId2 = TestUtils.getEntities(res, "zombie_toast_spawner").get(1).getId();
        res = controller.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        // 2 enemies left
        List<EntityResponse> entities = res.getEntities();
        int spiderCount = TestUtils.countEntityOfType(entities, "spider");
        int zombieCount = TestUtils.countEntityOfType(entities, "zombie_toast");
        int mercCount = TestUtils.countEntityOfType(entities, "mercenary");
        assertEquals(1, spiderCount);
        assertEquals(0, zombieCount);
        assertEquals(1, mercCount);

        // kill one enemy
        res = controller.tick(Direction.RIGHT);
        entities = res.getEntities();
        spiderCount = TestUtils.countEntityOfType(entities, "spider");
        zombieCount = TestUtils.countEntityOfType(entities, "zombie_toast");
        mercCount = TestUtils.countEntityOfType(entities, "mercenary");
        assertEquals(1, spiderCount);
        assertEquals(0, zombieCount);
        assertEquals(0, mercCount);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = controller.tick(Direction.RIGHT);
        entities = res.getEntities();
        spiderCount = TestUtils.countEntityOfType(entities, "spider");
        zombieCount = TestUtils.countEntityOfType(entities, "zombie_toast");
        mercCount = TestUtils.countEntityOfType(entities, "mercenary");
        assertEquals(0, spiderCount);
        assertEquals(0, zombieCount);
        assertEquals(0, mercCount);
        assertEquals(1, TestUtils.countEntityOfType(entities, "player"));

        // 2 spawners
        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = controller.tick(Direction.RIGHT);
        // 4 1
        res = assertDoesNotThrow(() -> controller.interact(spawnerId2));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        res = controller.tick(Direction.RIGHT);
        // 5 1
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = assertDoesNotThrow(() -> controller.interact(spawnerId1));
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        res = controller.tick(Direction.LEFT);
        assertEquals("", TestUtils.getGoals(res));
    }
}
