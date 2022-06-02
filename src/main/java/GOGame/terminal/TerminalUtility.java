package GOGame.terminal;

import GOGame.Pair;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.HashMap;

public abstract class TerminalUtility {
    static final String DEFAULT_BG_COLOR = "black";
    static final String DEFAULT_FG_COLOR = "white";
    static final HashMap<String, TextColor> COLOR_MAP = new HashMap<>() {{
        put("blue", TextColor.ANSI.BLUE);
        put("black", TextColor.ANSI.BLACK);
        put("cyan", TextColor.ANSI.CYAN);
        put("white", TextColor.ANSI.WHITE);
        put("green", TextColor.ANSI.GREEN);
        put("magenta", TextColor.ANSI.MAGENTA);
        put("red", TextColor.ANSI.RED);
        put("yellow", TextColor.ANSI.YELLOW);
        put("blueBright", TextColor.ANSI.BLUE_BRIGHT);
        put("blackBright", TextColor.ANSI.BLACK_BRIGHT);
        put("cyanBright", TextColor.ANSI.CYAN_BRIGHT);
        put("whiteBright", TextColor.ANSI.WHITE_BRIGHT);
        put("greenBright", TextColor.ANSI.GREEN_BRIGHT);
        put("magentaBright", TextColor.ANSI.MAGENTA_BRIGHT);
        put("redBright", TextColor.ANSI.RED_BRIGHT);
        put("yellowBright", TextColor.ANSI.YELLOW_BRIGHT);
        put("orange", new TextColor.RGB(255, 69, 0));
        put("gray", new TextColor.RGB(128, 128, 128));
    }};

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
//            return new Pair<>(TextColor.ANSI.DEFAULT, TextColor.ANSI.DEFAULT);
            return parseColor(DEFAULT_FG_COLOR + "-" + DEFAULT_BG_COLOR);
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

    public static void drawBar(Terminal t, int x, int y, int width, int value, int maxValue, String color, boolean drawValues) {
        String base = "[" + " ".repeat(width - 2) + "]";
        putAt(t, x, y, base);
        x += 1;
        var count = value * (width - 2) / maxValue;
        var pair = parseColor(color);
        try {
            t.setForegroundColor(pair.getSecond());
            t.setBackgroundColor(pair.getFirst());
            putAt(t, x, y, " ".repeat(count));
            if (drawValues) {
                resetColors(t);
                x += base.length();
                base = "(   /   )";
                putAt(t, x, y, base);
                t.setForegroundColor(pair.getFirst());
                t.setBackgroundColor(pair.getSecond());
                putAt(t, x+1, y, String.valueOf(value));
                putAt(t, x+5, y, String.valueOf(maxValue));
            }
            resetColors(t);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
