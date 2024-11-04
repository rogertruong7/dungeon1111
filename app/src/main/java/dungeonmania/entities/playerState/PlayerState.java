package dungeonmania.entities.playerState;

import dungeonmania.entities.Player;

public abstract class PlayerState {
    private Player player;

    PlayerState(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract boolean isInvincible();

    public abstract boolean isInvisible();

    public abstract void transitionInvisible();

    public abstract void transitionInvincible();

    public abstract void transitionBase();
}
