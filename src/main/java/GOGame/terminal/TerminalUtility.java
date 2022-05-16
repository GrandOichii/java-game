package GOGame.terminal;

import GOGame.Pair;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.HashMap;

public class TerminalUtility {
    static final String DEFAULT_BG_COLOR = "default";
    static final String DEFAULT_FG_COLOR = "default";
    static final HashMap<String, TextColor> COLOR_MAP = new HashMap<>();
    public static void initColorMap() {
        COLOR_MAP.put("default", TextColor.ANSI.DEFAULT);
        COLOR_MAP.put("blue", TextColor.ANSI.BLUE);
        COLOR_MAP.put("black", TextColor.ANSI.BLACK);
        COLOR_MAP.put("cyan", TextColor.ANSI.CYAN);
        COLOR_MAP.put("white", TextColor.ANSI.WHITE);
        COLOR_MAP.put("green", TextColor.ANSI.GREEN);
        COLOR_MAP.put("magenta", TextColor.ANSI.MAGENTA);
        COLOR_MAP.put("red", TextColor.ANSI.RED);
        COLOR_MAP.put("yellow", TextColor.ANSI.YELLOW);
        COLOR_MAP.put("blueBright", TextColor.ANSI.BLUE_BRIGHT);
        COLOR_MAP.put("blackBright", TextColor.ANSI.BLACK_BRIGHT);
        COLOR_MAP.put("cyanBright", TextColor.ANSI.CYAN_BRIGHT);
        COLOR_MAP.put("whiteBright", TextColor.ANSI.WHITE_BRIGHT);
        COLOR_MAP.put("greenBright", TextColor.ANSI.GREEN_BRIGHT);
        COLOR_MAP.put("magentaBright", TextColor.ANSI.MAGENTA_BRIGHT);
        COLOR_MAP.put("redBright", TextColor.ANSI.RED_BRIGHT);
        COLOR_MAP.put("yellowBright", TextColor.ANSI.YELLOW_BRIGHT);
        COLOR_MAP.put("orange", new TextColor.RGB(255, 69, 0));
        COLOR_MAP.put("gray", new TextColor.RGB(128, 128, 128));
    }

    public static void putAt(Terminal terminal, int x, int y, String line) {
        try {
            terminal.setCursorPosition(x, y);
            terminal.putString(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Pair<TextColor, TextColor> parseColor(String color) {
        if (color.equals("normal")) {
            return new Pair<>(TextColor.ANSI.DEFAULT, TextColor.ANSI.DEFAULT);
//            return parseColor(DEFAULT_FG_COLOR + "-" + DEFAULT_BG_COLOR);
        }
        var split = color.split("-");
        if (split.length == 0 || split.length > 3) {
            throw new RuntimeException(String.format("can't parse color %s", color));
        }
        var fgc = split[0];
        String bgc;
        if (split.length == 1) {
            bgc = DEFAULT_BG_COLOR;
        }
        else {
            bgc = split[1];
        }
        if (!COLOR_MAP.containsKey(fgc)) {
            throw new RuntimeException(String.format("can't recognize color %s", fgc));
        }
        if (!COLOR_MAP.containsKey(bgc)) {
            throw new RuntimeException(String.format("can't recognize color %s", bgc));
        }
        return new Pair<>(COLOR_MAP.get(fgc), COLOR_MAP.get(bgc));
    }

    public static void putAt(Terminal terminal, int x, int y, String line, String color) {
        var pair = parseColor(color);
        var fg = pair.getFirst();
        var bg = pair.getSecond();
        try {
            terminal.setBackgroundColor(bg);
            terminal.setForegroundColor(fg);
            putAt(terminal, x, y, line);
            resetColors(terminal);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetColors(Terminal t) {
        try {
            t.setBackgroundColor(COLOR_MAP.get(DEFAULT_BG_COLOR));
            t.setForegroundColor(COLOR_MAP.get(DEFAULT_FG_COLOR));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
