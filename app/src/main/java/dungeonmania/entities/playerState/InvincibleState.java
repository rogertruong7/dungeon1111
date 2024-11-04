package dungeonmania.entities.playerState;

import dungeonmania.entities.Player;

public class InvincibleState extends PlayerState {
    public InvincibleState(Player player) {
        super(player);
    }

    @Override
    public boolean isInvincible() {
        return true;
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public void transitionBase() {
        Player player = getPlayer();
        player.changeState(new BaseState(player));
    }

    @Override
    public void transitionInvincible() {
        Player player = getPlayer();
        player.changeState(new InvincibleState(player));
    }

    @Override
    public void transitionInvisible() {
        Player player = getPlayer();
        player.changeState(new InvisibleState(player));
    }
}
