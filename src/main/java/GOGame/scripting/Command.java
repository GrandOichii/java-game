package GOGame.scripting;

import GOGame.exceptions.ScriptException;

public class Command {
    private final Function f;
    private final Object[] args;

    public Command(Function f, Object[] args) {
        this.f = f;
        this.args = args;
    }

    public void exec(ScriptOverseer so) throws ScriptException {
        f.exec(args, so);
    }
}
