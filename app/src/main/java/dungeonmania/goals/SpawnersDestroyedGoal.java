package dungeonmania.goals;

import dungeonmania.Game;

public class SpawnersDestroyedGoal implements EnemySubgoal {
    private String type;

    public SpawnersDestroyedGoal(String type) {
        this.type = type;
    }

    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return game.getSpawnersDestroyedCount() == game.getSpawnerCount();
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return ":destroyedSpawners";
    }

    public String getType() {
        return type;
    }

}
