package dungeonmania.testsCreated;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicTest {

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }

    @Test
    @Tag("100-1")
    @DisplayName("Test or lightbulb and switch can power lightbulb")
    public void orLightBulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("!2f_or_xor", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_co_and", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_bomb", "2fTesting");
        List<EntityResponse> entities = res.getEntities();

        // Player moves boulder
        int bulbOffCount = TestUtils.countEntityOfType(entities, "light_bulb_off");
        assertEquals(2, bulbOffCount);
        res = dmc.tick(Direction.RIGHT);
        int bulbOnCount = TestUtils.countEntityOfType(res.getEntities(), "light_bulb_on");
        assertEquals(1, bulbOnCount);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        bulbOnCount = TestUtils.countEntityOfType(res.getEntities(), "light_bulb_on");
        assertEquals(2, bulbOnCount);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        bulbOnCount = TestUtils.countEntityOfType(res.getEntities(), "light_bulb_on");
        assertEquals(0, bulbOnCount);

    }

    @Test
    @Tag("100-2")
    @DisplayName("Test xorDoorSwitch")
    public void xorDoorSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("!2f_or_xor", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_co_and", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_bomb", "2fTesting");
        List<EntityResponse> entities = res.getEntities();

        // Player moves boulder
        int switchDoorCount = TestUtils.countEntityOfType(entities, "switch_door");
        int doorOpenCount = TestUtils.countEntityOfType(res.getEntities(), "door_open");

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        switchDoorCount = TestUtils.countEntityOfType(entities, "switch_door");
        assertEquals(1, switchDoorCount);
        // the switch powering two sides of the door so its still closed
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        doorOpenCount = TestUtils.countEntityOfType(res.getEntities(), "door_open");
        assertEquals(1, doorOpenCount);

        //testing can walk through
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(7, -3), getPlayerPos(res));
    }

    @Test
    @Tag("100-3")
    @DisplayName("Test andBomb")
    public void andBomb() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("!2f_or_xor", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_co_and", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_bomb", "2fTesting");
        // List<EntityResponse> entities = res.getEntities();

        // the switch powering two sides of the door so its still closed
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        // pick up logic bomb

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());
        assertEquals(0, TestUtils.getInventory(res, "bomb").size());

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        // it blew up when we got all wires powering it
        assertEquals(1, TestUtils.getEntities(res, "bomb").size());
    }

    @Test
    @Tag("100-4")
    @DisplayName("Test andBombOneWireAlreadyPowered")
    public void andBombOneWireAlreadyPowered() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("!2f_or_xor", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_co_and", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_bomb", "2fTesting");
        // List<EntityResponse> entities = res.getEntities();

        // the switch powering two sides of the door so its still closed
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        // pick up logic bomb
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());
        assertEquals(0, TestUtils.getInventory(res, "bomb").size());
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "bomb").size());
    }

    @Test
    @Tag("100-5")
    @DisplayName("Test andBombOneBlowsUpSwitchAndWire")
    public void andBombOneBlowsUpSwitchAndWire() throws IllegalArgumentException,
    InvalidActionException, InterruptedException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("!2f_or_xor", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_co_and", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_bomb", "2fTesting");
        // List<EntityResponse> entities = res.getEntities();

        // the switch powering two sides of the door so its still closed
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        // pick up logic bomb
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());
        assertEquals(0, TestUtils.getInventory(res, "bomb").size());
        Thread.sleep(1000);
        assertEquals(1, TestUtils.getEntities(res, "bomb").size());

        // THEN BEAUTIFUL HERE TEST IF THE WIRES ARE DISCONNECTED
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
        // wire got broken
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "door_open").size());
    }

    @Test
    @Tag("100-7")
    @DisplayName("Test coAndAsWellAsTwoWiresPoweringOneAndThenTurnedOff")
    public void coAndAsWellAsTwoWiresPoweringOneAndThenTurnedOff() throws
    IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        // DungeonResponse res = dmc.newGame("!2f_or_xor", "2fTesting");
        DungeonResponse res = dmc.newGame("!2f_and_co_and", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_bomb", "2fTesting");
        // List<EntityResponse> entities = res.getEntities();

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getEntities(res, "door_open").size());
        // powered by 2 wires at the same time

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getEntities(res, "door_open").size());
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);

        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
        // proving it can only get turned on when powered by 2 things at the instant same TIME
    }
    @Test
    @Tag("100-8")
    @DisplayName("Test door cant use key")
    public void testDoorKeyNoWork() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("!2f_or_xor", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_co_and", "2fTesting");
        // DungeonResponse res = dmc.newGame("!2f_and_bomb", "2fTesting");
        // List<EntityResponse> entities = res.getEntities();
        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
        // powered by 2 wires at the same time

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
        assertEquals(new Position(7, -1), getPlayerPos(res));
        // proving it can only get turned on when powered by 2 things at the instant same TIME
    }
}
