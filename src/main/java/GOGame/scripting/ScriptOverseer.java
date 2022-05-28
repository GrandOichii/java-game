package GOGame.scripting;

import GOGame.Engine;
import GOGame.Utility;
import GOGame.exceptions.ScriptArgumentsException;
import GOGame.exceptions.ScriptException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ScriptOverseer {
    private final HashMap<String, Script> macros;
    private final HashMap<String, Object> vars;
    private final HashMap<String, Object> immutableVars;
    private final HashMap<String, Function> funcMap;
    private Engine e;

    private static final String MESSAGE_BOX_RESULT_NAME = "_mbresult";
    private boolean equal(Object o1, Object o2) throws ScriptException {
        var v1 = parseArg(o1);
        var v2 = parseArg(o2);
        return v1.equals(v2);
    }

    public ScriptOverseer(Engine engine) {
        this.e = engine;
        this.macros = new HashMap<>();
        this.vars = new HashMap<>();
        this.immutableVars = new HashMap<>();
        var ifEvaluators = new HashMap<String, EvaluatorFunc>();
//        set evaluation
        ifEvaluators.put("set", new EvaluatorFunc(this) {
            @Override
            public boolean eval(Object[] args) throws ScriptArgumentsException {
                var reqCount = 2;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("set", reqCount, args.length);
                }
                var vars = this.getSO().vars;
                var key = (String) args[1];
                var contains = vars.containsKey(key);
                return contains;
            }
        });
//        equals evaluation
        ifEvaluators.put("=", new EvaluatorFunc(this) {
            @Override
            public boolean eval(Object[] args) throws ScriptException {
                var reqCount = 3;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("=", reqCount, args.length);
                }
                var so = getSO();
                return so.equal(args[0], args[2]);
            }
        });
        funcMap = new HashMap<>();
        funcMap.put("debug:list_commands", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var reqCount = 0;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("list_commands", reqCount, args.length);
                }
                System.out.println("Commands list:");
                for (var key : funcMap.keySet()) {
                    System.out.println("\t" + key);
                }
            }
        });
        funcMap.put("debug:list_all_items", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var reqCount = 0;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("list_all_items", reqCount, args.length);
                }
                System.out.println("Item names:");
                var items = e.getItemManager().getItems();
                for (var item : items) {
                    System.out.println("\t" + item.getName());
                }
            }
        });
        funcMap.put("run", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var reqCount = 1;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("run", reqCount, args.length);
                }
                var macroName = (String)args[0];
                if (!so.macros.containsKey(macroName)) {
                    throw new ScriptException("no macro with name " + macroName);
                }
                var macro = so.macros.get(macroName);
                macro.exec();
            }
        });
        funcMap.put("log", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var reqCount = 1;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("log", reqCount, args.length);
                }
                var message = so.toString(args[0]);
                so.e.addToLog(message);
                System.out.println(message);
            }
        });
        funcMap.put("warp", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var reqCount = 1;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("warp", reqCount, args.length);
                }
                var warpCode = (String) args[0];
                var warpMap = so.e.getMap().getWarpMap();
                if (!warpMap.containsKey(warpCode)) {
                    throw new ScriptException("no warp code " + warpCode);
                }
                var loc = warpMap.get(warpCode);
                so.e.travelTo(loc);
            }
        });
        funcMap.put("give", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var reqCount = 1;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("give", reqCount, args.length);
                }
                var itemName = so.toString(args[0]);
                var item = e.getItemManager().get(itemName);
                e.getPlayer().addItem(item);
            }
        });
        funcMap.put("opencontainer", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
//                opencontainer chest1 "Small Chest"
                var reqCount = 2;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("opencontainer", reqCount, args.length);
                }
                var containerName = (String)args[0];
                var containerTop = so.toString(args[1]);
                e.openContainer(containerName, containerTop);
            }
        });
        funcMap.put("tset", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var reqCount = 3;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("tset", reqCount, args.length);
                }
                var y = (Integer) args[0];
                var x = (Integer) args[1];
                var tileName = (String) args[2];
                var split = tileName.split(":");
                if (split.length != 2) {
                    throw new ScriptException("can't tset tile " + tileName);
                }
                var roomName = split[0];
                tileName = split[1];
                var roomMap = so.e.getMap().getRoomMap();
                if (!roomMap.containsKey(roomName)) {
                    throw new ScriptException("no room with name " + roomName);
                }
                var room = roomMap.get(roomName);
                var tileSet = room.getTileSet();
                var tile = tileSet.getTileByName(tileName);
                if (tile == null) {
                    throw new ScriptException("no tile with name " + tileName + " in room " + roomName);
                }
                room.getTiles()[y][x] = tile;
            }
        });
        funcMap.put("sleep", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var reqCount = 1;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("sleep", reqCount, args.length);
                }
                var time = (Integer) args[0];
                so.e.getGameWindow().sleep(time);
            }
        });
        funcMap.put("set", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var reqCount = 2;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("set", reqCount, args.length);
                }
                var argName = (String) args[0];
                so.vars.put(argName, parseArg(args[1]));
            }
        });
        funcMap.put("sadd", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var reqCount = 2;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("sadd", reqCount, args.length);
                }
                var add = so.toString(args[1]);
                var key = (String) args[0];
                so.vars.put(key, so.vars.get(key) + add);
            }
        });
        funcMap.put("mb", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var reqCount = 2;
                if (args.length != reqCount) {
                    throw new ScriptArgumentsException("mb", reqCount, args.length);
                }
                var text = so.toString(args[0]);
                var choices = so.toString(args[1]).split("_");
                var result = e.getGameWindow().requestChoice(text, choices);
                so.immutableVars.put(MESSAGE_BOX_RESULT_NAME, result);
            }
        });
        funcMap.put("if", new Function() {
            @Override
            public void exec(Object[] args, ScriptOverseer so) throws ScriptException {
                var thenI = -1;
                for (int i = 0; i < args.length; i++) {
                    var arg = args[i];
                    if (arg.equals("then")) {
                        thenI = i;
                        break;
                    }
                }
                if (thenI == -1) {
                    throw new ScriptException("no then in if statement");
                }
                var reverse = false;
                var doIf = false;
                var ifArgs = Arrays.copyOfRange(args, 0, thenI);
                var actionArgs = Arrays.copyOfRange(args, thenI+1, args.length);
                if (ifArgs[0].equals("not")) {
                    reverse = true;
                    ifArgs = Arrays.copyOfRange(ifArgs, 1, ifArgs.length);
                }
                var ifOp = "";
                for (var key : ifEvaluators.keySet()) {
                    if (key.equals(ifArgs[1])) {
                        ifOp = key;
                        break;
                    }
                }
                if (ifOp.equals("")) {
                    if (ifArgs[0].equals("set")) {
                        ifOp = "set";
                    }
                    else {
                        throw new ScriptException("unknown evaluator " + ifArgs[1]);
                    }
                }
                doIf = ifEvaluators.get(ifOp).eval(ifArgs);
                doIf = (doIf || reverse) && !(doIf && reverse);
                if (doIf) {
                    var funcName = (String) actionArgs[0];
                    if (!so.funcMap.containsKey(funcName)) {
                        throw new ScriptException("no func with name " + funcName);
                    }
                    so.funcMap.get(funcName).exec(Arrays.copyOfRange(actionArgs, 1, actionArgs.length), so);
                }
            }
        });
    }

    private String toString(Object o) throws ScriptException {
        return parseArg(o).toString();
    }

    private Object parseArg(Object arg) throws ScriptException {
        if (arg instanceof Integer) {
            return copyOf(arg);
        }
        if (arg instanceof String) {
            var s = (String) arg;
//            check if is string
            if (s.charAt(0) == '\"' && s.charAt(s.length()-1) == '\"') {
                return s.substring(1, s.length()-1);
            }
//            check if is a variable
            if (this.vars.containsKey(s)) {
                return copyOf(this.vars.get(s));
            }
//            check if is an immutable variable
            if (this.immutableVars.containsKey(s)) {
                return copyOf(this.immutableVars.get(s));
            }
        }
        throw new ScriptException("don't know what " + arg + " is");
    }

    private static Object copyOf(Object o) {
        if (o instanceof Integer) {
            return Integer.valueOf(((Integer) o).intValue());
        }
        if (o instanceof String) {
            return o;
        }
        return null;
    }

    public Script loadScript(String path) throws Exception {
        var text = Utility.readFile(new File(path));
        return new Script(text, this);
    }

    public Command parseCommand(String line) throws Exception {
        var words = line.split(" ");
        var fName = words[0];
        var args = Arrays.copyOfRange(words, 1, words.length);
        args = parseArgs(args);
        if (!this.funcMap.containsKey(fName)) {
            throw new ScriptException("no func with name " + fName);
        }
        var f = this.funcMap.get(fName);
        var arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            try {
                var a = Integer.valueOf(Integer.parseInt(args[i]));
                arguments[i] = a;
            }
            catch (NumberFormatException e) {
//                can't be parsed to an int
                arguments[i] = args[i];
            }
        }
        var result = new Command(f, arguments, line);
        return result;
    }

    public void addMacro(String name, Script script) throws IOException {
        if (this.macros.containsKey(name)) {
            throw new IOException("macro " + name + " redifined");
        }
        this.macros.put(name, script);
    }

    private String[] parseArgs(String[] words) throws Exception {
        var list = new ArrayList<String>();
        var sb = false;
        var line = "";
        for (var word : words) {
            var first = word.charAt(0);
            var last = word.charAt(word.length() - 1);
            if (first == '\"') {
                sb = true;
            }
            if (sb) {
                line += " " + word;
                if (last == '\"') {
                    list.add(line.substring(1));
                    line = "";
                    sb = false;
                }
            }
            else {
                list.add(word);
            }
        }
        if (!line.equals("")) {
            throw new Exception("parsing error: no closing string quote");
        }
        return list.toArray(new String[0]);
    }
}
