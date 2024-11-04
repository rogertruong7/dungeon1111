package dungeonmania.entities;

import dungeonmania.Game;
import dungeonmania.map.GameMap;


public interface Interactable {
    public void interact(Player player, Game game, GameMap map);
    public boolean isInteractable(Player player);
}
