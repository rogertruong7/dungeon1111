package dungeonmania.goals;

import dungeonmania.Game;

public class TreasureGoal implements Goal {
    private String type;
    private int target;

    public TreasureGoal(String type, int target) {
        this.type = type;
        this.target = target;
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return game.getCollectedTreasureCount() >= target;
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return ":treasure";
    }

    public String getType() {
        return type;
    }
}
