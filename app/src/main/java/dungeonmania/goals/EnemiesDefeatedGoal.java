package dungeonmania.goals;

import dungeonmania.Game;

public class EnemiesDefeatedGoal implements EnemySubgoal {
    private String type;
    private int target;

    public EnemiesDefeatedGoal(String type, int target) {
        this.type = type;
        this.target = target;
    }

    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return game.getDefeatedEnemiesCount() >= target;
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return ":defeatedEnemies";
    }

    public String getType() {
        return type;
    }
}
