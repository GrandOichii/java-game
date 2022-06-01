package GOGame.scripting;

import GOGame.exceptions.ScriptArgumentsException;
import GOGame.exceptions.ScriptException;

public abstract class EvaluatorFunc {

    public EvaluatorFunc() {}
    abstract public boolean eval(Object[] args) throws ScriptException;
}
