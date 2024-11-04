package dungeonmania.goals;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoalFactory {
    public static Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        switch (jsonGoal.getString("goal")) {
        case "AND":
            return createAndGoal(jsonGoal, config);
        case "OR":
            return createOrGoal(jsonGoal, config);
        case "exit":
            return createExitGoal();
        case "boulders":
            return createBoulderGoal();
        case "treasure":
            return createTreasureGoal(config);
        case "enemies":
            return createEnemyGoal(config);
        default:
            return null;
        }
    }

    private static Goal createEnemyGoal(JSONObject config) {
        int enemiesDefeatedCount = config.optInt("enemy_goal", 1);
        EnemyGoal enemyGoal = new EnemyGoal("enemies");
        enemyGoal.add(createEnemiesDefeatedGoal(enemiesDefeatedCount));
        enemyGoal.add(createSpawnersDestroyedGoal());
        return enemyGoal;
    }

    private static Goal createEnemiesDefeatedGoal(int enemiesDefeatedCount) {
        EnemiesDefeatedGoal enemiesDefeatedGoal = new EnemiesDefeatedGoal("enemies_defeated", enemiesDefeatedCount);
        return enemiesDefeatedGoal;
    }

    private static Goal createSpawnersDestroyedGoal() {
        return new SpawnersDestroyedGoal("spawners_destroyed");
    }

    private static Goal createAndGoal(JSONObject jsonGoal, JSONObject config) {
        JSONArray subgoals = jsonGoal.getJSONArray("subgoals");
        AndGoal andGoal = new AndGoal("AND");
        andGoal.add(createGoal(subgoals.getJSONObject(0), config));
        andGoal.add(createGoal(subgoals.getJSONObject(1), config));
        return andGoal;
    }

    private static Goal createOrGoal(JSONObject jsonGoal, JSONObject config) {
        JSONArray subgoals = jsonGoal.getJSONArray("subgoals");
        OrGoal orGoal = new OrGoal("OR");
        orGoal.add(createGoal(subgoals.getJSONObject(0), config));
        orGoal.add(createGoal(subgoals.getJSONObject(1), config));
        return orGoal;
    }

    private static Goal createExitGoal() {
        return new ExitGoal("exit");
    }

    private static Goal createBoulderGoal() {
        return new BouldersGoal("boulders");
    }

    private static Goal createTreasureGoal(JSONObject config) {
        int treasureGoal = config.optInt("treasure_goal", 1);
        return new TreasureGoal("treasure", treasureGoal);
    }
}
