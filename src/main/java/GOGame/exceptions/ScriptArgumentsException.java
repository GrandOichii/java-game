package GOGame.exceptions;

public class ScriptArgumentsException extends ScriptException{
    public ScriptArgumentsException(String name, int requiredCount, int observedCount) {
//        sup
        super(String.format("scripting error: %s requires %d operands (observed amount: %d)", name, requiredCount, observedCount));
    }
}
