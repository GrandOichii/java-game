package GOGame.scripting;

import GOGame.exceptions.ScriptException;

public class Command {
    private final Function f;
    private final Object[] args;

    private final String line;

    public Command(Function f, Object[] args, String line) {
        this.f = f;
        this.args = args;
        this.line = line;
    }

    public void exec(ScriptOverseer so) throws ScriptException {
        f.exec(args, so);
    }
}
