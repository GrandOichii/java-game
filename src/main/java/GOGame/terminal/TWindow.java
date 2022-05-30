package GOGame.terminal;

import GOGame.Pair;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public abstract class TWindow {
    protected Terminal terminal;
    protected TextGraphics g;
    private int width;

    public int getWidth() {
        return width;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    private int height;

    public int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    protected final int x;
    protected final int y;
    private boolean running;
    protected Pair<TextColor, TextColor> borderColor = TerminalUtility.parseColor("white-black");
    private CCTMessage title = null;

    public void setBorderColor(String color) {
        this.borderColor = TerminalUtility.parseColor(color);
    }

    public void setTitle(String title) {
        this.title = new CCTMessage(title);
    }

    public TWindow(Terminal terminal, TextGraphics g, int width, int height, int x, int y) {
        this.terminal = terminal;
        this.g = g;
        this.height = height;
        this.width = width;
        this.y = y;
        this.x = x;
    }

    protected void close() {
        this.running = false;
    }

    private void clear() {
        var line = " ".repeat(width);
        for (int i = 0; i < height; i++) {
            TerminalUtility.putAt(terminal, this.x, this.y + i, line);
        }
    }

    private void drawBorder() {
        g.enableModifiers(SGR.BOLD);
        g.setForegroundColor(borderColor.getFirst());
        g.setBackgroundColor(borderColor.getSecond());
        g.drawRectangle(new TerminalPosition(x, y), new TerminalSize(width, height), '*');
        g.setForegroundColor(TextColor.ANSI.DEFAULT);
        g.setBackgroundColor(TextColor.ANSI.DEFAULT);
        if (this.title != null) {
            title.draw(terminal, x + 1, y);
        }
    }

    public void show() throws IOException {
        this.running = true;
        g.enableModifiers(SGR.BOLD);
        while (this.running) {
            this.clear();
            this.drawBorder();
            this.draw();
            terminal.flush();
            var key = terminal.readInput();
            this.handleInput(key);
        }
        this.clear();
        g.disableModifiers(SGR.BOLD);
    }

    protected abstract void draw();

    protected abstract void handleInput(KeyStroke key);
}
