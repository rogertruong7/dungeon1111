package dungeonmania.entities;

import dungeonmania.Game;

public interface Destroyable {
    void onDestroy(Game g, Player player);
}
