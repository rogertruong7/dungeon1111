package dungeonmania.goals;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Game;

public class OrGoal implements Goal {
    private String type;
    private List<Goal> subgoals = new ArrayList<>();

    public OrGoal(String type) {
        this.type = type;
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
        return getChildren(0).achieved(game) || getChildren(1).achieved(game);
    }

    @Override
    public String toString(Game game) {
        if (achieved(game))
            return "";
        else
            return "(" + getChildren(0).toString(game) + " OR " + getChildren(1).toString(game) + ")";
    }

    public String getType() {
        return type;
    }
}
