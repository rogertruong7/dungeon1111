package dungeonmania.goals;

import java.util.List;
import java.util.ArrayList;

import dungeonmania.Game;

public class EnemyGoal implements Goal {
    private String type;
    private List<Goal> subgoals = new ArrayList<>();

    public EnemyGoal(String type) {
        this.type = type;
    }

    public void add(Goal subgoal) {
        if (!(subgoal instanceof EnemySubgoal)) {
            return;
        }
        List<Goal> subgoals = getSubgoals();
        // We only want unique EnemySubgoals so we check if the classes are
        // unique through a stream, in case we need to add more enemy goals
        // in the future
        // EnemySubgoal is used for polymorphism and open-closed principle
        boolean alreadyExists = subgoals.stream()
                                    .anyMatch(g -> g.getClass().equals(subgoal.getClass()));
        if (subgoal != null && !alreadyExists) {
            subgoals.add(subgoal);
        }
        return;
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return subgoals.stream().allMatch(x -> x.achieved(game));
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return ":enemies";
    }

    public void remove(Goal subgoal) {
        if (subgoal != null && subgoals.contains(subgoal)) {
            subgoals.remove(subgoal);
        }
        return;
    }

    public Goal getChildren(int index) {
        return subgoals.get(index);
    }

    public int getChildCount() {
        return subgoals.size();
    }

    public List<Goal> getSubgoals() {
        return subgoals;
    }

    public String getType() {
        return type;
    }

}
