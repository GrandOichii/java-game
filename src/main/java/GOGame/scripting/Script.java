package GOGame.scripting;

import GOGame.Utility;
import GOGame.exceptions.ScriptException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Script {
    private String text;
    private List<Command> commands;
    private ScriptOverseer so;
    public Script(String text, ScriptOverseer so) throws Exception {
        this.text = text;
        this.so = so;
        this.commands = new ArrayList<>();

        var lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            var l = lines[i];
            if (l.equals("")) {
                continue;
            }
            var macroName = getMacroDeclaration(l);
            if (macroName != null) {
                var endI = i + 1;
                while (!lines[endI].equals("}")) {
                    endI++;
                }
                var macroLines = Arrays.copyOfRange(lines, i+1, endI);
                var macroText = String.join("\n", macroLines);
                var script = new Script(macroText, so);
                so.addMacro(macroName, script);
                i = endI;
                continue;
            }
            var command = so.parseCommand(l);
            this.commands.add(command);
        }
    }

    private static String getMacroDeclaration(String line) {
        var index = line.indexOf("{");
        if (index == -1) {
            return null;
        }
        return line.substring(0, index).trim();
    }

    public void exec() throws ScriptException {
        for (var command : commands) {
            command.exec(so);
        }
    }
}
