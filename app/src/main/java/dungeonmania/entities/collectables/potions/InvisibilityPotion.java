package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.entities.playerState.BaseState;
import dungeonmania.entities.playerState.InvisibleState;
import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion {
    public static final int DEFAULT_DURATION = 8;

    public InvisibilityPotion(Position position, int duration) {
        super(position, duration);
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 1, 1, false, false));
    }

    @Override
    public void applyEffect(Player player) {
        player.changeState(new InvisibleState(player));
    }

    @Override
    public void removeEffect(Player player) {
        player.changeState(new BaseState(player));
    }
}
