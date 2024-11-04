package dungeonmania.entities;

import java.util.LinkedList;
import java.util.Queue;

import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.playerState.BaseState;

public class PotionManager {
    private Player player;
    private Queue<Potion> queue = new LinkedList<>();
    private Potion inEffective = null;
    private int nextTrigger = 0;

    public PotionManager(Player player) {
        this.player = player;
    }

    public Potion getEffectivePotion() {
        return inEffective;
    }

public void usePotion(Potion potion, int tick) {
        player.getInventory().remove(potion);
        queue.add(potion);
        if (inEffective == null) {
            triggerNext(tick);
        }
    }

    public void onTick(int tick) {
        if (inEffective != null && tick >= nextTrigger) {
            inEffective.removeEffect(player);
            triggerNext(tick);
        }
    }

    private void triggerNext(int currentTick) {
        if (queue.isEmpty()) {
            inEffective = null;
            player.changeState(new BaseState(player));
            return;
        }
        inEffective = queue.remove();
        inEffective.applyEffect(player);
        nextTrigger = currentTick + inEffective.getDuration();
    }

    public boolean isPlayerInvincible() {
        return inEffective instanceof InvincibilityPotion;
    }

    public boolean isPlayerInvisible() {
        return inEffective instanceof InvisibilityPotion;
    }
}
