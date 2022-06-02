package GOGame.terminal;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.*;

public class LineEdit {
    private final int maxWidth;
    private List<String> text;
    private int cursor ;

    public LineEdit(String text, int maxWidth) {
        this.setText(text);
        this.maxWidth = maxWidth;
    }

    public void draw(Terminal terminal, int x, int y) throws IOException {
        terminal.enableSGR(SGR.UNDERLINE);
        terminal.setCursorPosition(x, y);
        terminal.putString(" ".repeat(maxWidth));
        TerminalUtility.putAt(terminal, x, y, String.join("", text));
        terminal.disableSGR(SGR.UNDERLINE);
        TerminalUtility.putAt(terminal, x + cursor, y, " ", "white-cyan");
    }

    public String getText() {
        return String.join("", text);
    }

    public void setText(String text) {
        this.text = new ArrayList<>();
        var split = text.split("");
        this.text.addAll(Arrays.asList(split));
        this.cursor = text.length();
    }

    private Map<KeyType, Runnable> keyMap = new EnumMap<>(KeyType.class){{
        put(KeyType.ArrowLeft, () -> {
            cursor--;
            if (cursor < 0) {
                cursor = 0;
            }
        });
        put(KeyType.ArrowRight, () -> {
            cursor++;
            var size = text.size();
            if (cursor >= size) {
                cursor = size - 1;
            }
        });
        put(KeyType.Backspace, () -> {
            if (cursor == 0) {
                return;
            }
            text.remove(cursor-1);
            cursor--;
        });
    }};

    public void handleInput(KeyStroke key) {
        var kt = key.getKeyType();
        if (keyMap.containsKey(kt)) {
            keyMap.get(kt).run();
            return;
        }
        if (key.getKeyType() == KeyType.Character) {
            if (text.size() == maxWidth) {
                return;
            }
            var ch = key.getCharacter().toString();
            text.add(cursor, ch);
            cursor++;
        }
    }
}
