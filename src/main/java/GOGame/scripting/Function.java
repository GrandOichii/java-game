package GOGame.scripting;


import GOGame.exceptions.ScriptException;

public abstract class Function {
    public abstract void exec(Object[] args, ScriptOverseer so) throws ScriptException;
}
