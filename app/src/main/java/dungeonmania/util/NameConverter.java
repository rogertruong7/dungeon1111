package dungeonmania.util;

import java.util.Arrays;
import java.util.Iterator;

import dungeonmania.entities.Door;
import dungeonmania.entities.Entity;
import dungeonmania.entities.KeyDoor;
import dungeonmania.entities.Portal;
import dungeonmania.entities.powerables.LightBulb;
import dungeonmania.entities.powerables.LogicBomb;
import dungeonmania.entities.powerables.SwitchDoor;

public class NameConverter {
    public static String toSnakeCase(Entity entity) {
        String nameBasic = toSnakeCase(entity.getClass().getSimpleName());
        if (entity instanceof Portal) {
            String color = "_" + ((Portal) entity).getColor().toLowerCase();
            return nameBasic + color;
        }
        if (entity instanceof KeyDoor) {
            String open = ((Door) entity).isOpen() ? "_open" : "";
            return "door" + open;
        }
        if (entity instanceof SwitchDoor) {
            if (((Door) entity).isOpen()) {
                return "door_open";
            }
            return "switch_door";
        }
        if (entity instanceof LightBulb) {
            String on = ((LightBulb) entity).isPowered() ? "_on" : "_off";
            return "light_bulb" + on;
        }
        if (entity instanceof LogicBomb) {
            return "bomb";
        }
        return nameBasic;
    }

    public static String toSnakeCase(String name) {
        String[] words = name.split("(?=[A-Z])");
        if (words.length == 1)
            return words[0].toLowerCase();

        StringBuilder builder = new StringBuilder();
        Iterator<String> iter = Arrays.stream(words).iterator();
        builder.append(iter.next().toLowerCase());

        while (iter.hasNext())
            builder.append("_").append(iter.next().toLowerCase());

        return builder.toString();
    }

    public static String toSnakeCase(Class<?> clazz) {
        return toSnakeCase(clazz.getSimpleName());
    }
}
