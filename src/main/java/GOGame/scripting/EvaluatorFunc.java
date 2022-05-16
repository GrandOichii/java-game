package GOGame.scripting;

import GOGame.exceptions.ScriptArgumentsException;
import GOGame.exceptions.ScriptException;

public abstract class EvaluatorFunc {
    private final ScriptOverseer so;
    public ScriptOverseer getSO() { return this.so; }

    public EvaluatorFunc(ScriptOverseer so) {
        this.so = so;
    }
    abstract public boolean eval(Object[] args) throws ScriptException;
}
