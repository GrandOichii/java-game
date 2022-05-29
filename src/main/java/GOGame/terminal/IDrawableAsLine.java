package GOGame.terminal;

import com.googlecode.lanterna.terminal.Terminal;

public interface IDrawableAsLine {
    void drawLine(Terminal terminal, int x, int y, boolean reverseColor);
}
