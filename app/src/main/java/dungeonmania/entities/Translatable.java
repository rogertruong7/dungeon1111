package dungeonmania.entities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public interface Translatable {
    public void translate(Direction direction);

    public void translate(Position offset);
}
