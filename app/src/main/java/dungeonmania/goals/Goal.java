package dungeonmania.goals;

import dungeonmania.Game;

public interface Goal {
    public abstract boolean achieved(Game game);

    public String toString(Game game);
}
