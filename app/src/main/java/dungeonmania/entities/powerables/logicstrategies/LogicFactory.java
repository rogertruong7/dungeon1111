package dungeonmania.entities.powerables.logicstrategies;

public class LogicFactory {
    public LogicStrategy createLogicStrategy(String type) {
        switch (type) {
            case "and":
                return new AndStrategy();
            case "or":
                return new OrStrategy();
            case "xor":
                return new XorStrategy();
            case "co_and":
                return new CoAndStrategy();
            default:
                throw new IllegalArgumentException("Unknown logic type: " + type);
        }
    }
}
