package GOGame.terminal;

import com.googlecode.lanterna.terminal.Terminal;

public interface DrawableAsLine {
    void drawLine(Terminal terminal, int x, int y, boolean reverseColor);
}
