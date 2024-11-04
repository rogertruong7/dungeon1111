package dungeonmania.goals;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Game;

public class AndGoal implements Goal {
    private String type;
    private List<Goal> subgoals = new ArrayList<>();

    public AndGoal(String type) {
        this.type = type;
    }

    public List<Goal> getSubgoals() {
        return subgoals;
    }

    public void add(Goal subgoal) {
        if (subgoal != null && subgoals.size() < 2) {
            subgoals.add(subgoal);
        }
        return;
    }

    public Goal getChildren(int index) {
        return subgoals.get(index);
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return getChildren(0).achieved(game) && getChildren(1).achieved(game);
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return "(" + getChildren(0).toString(game) + " AND " + getChildren(1).toString(game) + ")";
    }

    public String getType() {
        return type;
    }
}
