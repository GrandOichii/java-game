package GOGame.terminal;

import GOGame.Utility;
import GOGame.items.ItemLine;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.HashMap;
import java.util.Map;

abstract class SideButton {
    private CCTMessage text;

    public CCTMessage getText() {
        return text;
    }

    public SideButton(String text) {
        this.text = new CCTMessage(text);
    }

    public abstract void press();

    public void draw(Terminal terminal, int x, int y, boolean reverseColor) {
        this.text.draw(terminal, x, y, reverseColor);
    }
}

public class ItemDescriptionWindow extends TWindow {
    private static final int MAX_DESCRIPTION_WIDTH = 60;
    private static final String BORDER_COLOR = "cyan";

    private SideButton[] sideButtons;
    private final int separatorX;
    private int buttonI = 0;
    private final String descriptionLines[];

    public ItemDescriptionWindow(Terminal terminal, TextGraphics g, ItemLine line) {
        super(terminal, g, 0, 0, 2, 3);
        var item = line.getItem();
        this.setTitle("${cyan}" + item.getDisplayName());
        this.setBorderColor(BORDER_COLOR);
        this.descriptionLines = Utility.StringWidthSplit(item.getBigDescription(line.getAmount()), MAX_DESCRIPTION_WIDTH);
        sideButtons = new SideButton[]{
                new SideButton("Close") {
                    @Override
                    public void press() {
                        close();
                    }
                }
        };
        var maxLength = -1;
        for (var b : sideButtons) {
            var l = b.getText().length();
            if (l > maxLength) {
                maxLength = l;
            }
        }
        this.setWidth(2 + MAX_DESCRIPTION_WIDTH + 3 + maxLength + 3);
        this.separatorX = 2 + MAX_DESCRIPTION_WIDTH + 2;
        this.setHeight(2 + descriptionLines.length + 2);
    }

    @Override
    protected void draw() {
//        draw description
        for (int i = 0; i < descriptionLines.length; i++) {
            TerminalUtility.putAt(terminal, x + 2, y + 2 + i, descriptionLines[i]);
        }
//        draw separator
        var c = TerminalUtility.parseColor(BORDER_COLOR);
        g.setForegroundColor(c.getFirst());
        g.setBackgroundColor(c.getSecond());
        g.drawLine(new TerminalPosition(x + separatorX, y), new TerminalPosition(x + separatorX, y + getHeight() - 1), '*');
        g.setForegroundColor(TextColor.ANSI.DEFAULT);
        g.setBackgroundColor(TextColor.ANSI.DEFAULT);
//        draw buttons
        for (int i = 0; i < sideButtons.length; i++) {
            var b = sideButtons[i];
            b.draw(terminal, x + separatorX + 2, y + 2 + i, i == buttonI);
        }
    }

    private final Map<KeyType, Runnable> keyMap = new HashMap<>(){{
        put(KeyType.Escape, () -> close());
        put(KeyType.Enter, () -> sideButtons[buttonI].press());
        put(KeyType.ArrowDown, () -> {
            buttonI--;
            if (buttonI < 0) buttonI = sideButtons.length - 1;
        });
        put(KeyType.ArrowUp, () -> {
            buttonI++;
            if (buttonI >= sideButtons.length) buttonI = 0;
        });
    }};

    @Override
    protected void handleInput(KeyStroke key) {
        var kt = key.getKeyType();
        if (!keyMap.containsKey(kt)) {
            return;
        }
        keyMap.get(kt).run();
    }
}
