package GOGame.items;

import GOGame.Engine;
import GOGame.Utility;
import GOGame.terminal.CCTMessage;
import GOGame.terminal.TWindow;
import GOGame.terminal.TerminalUtility;
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
}

public class ItemDescriptionWindow extends TWindow {
    private static final int MAX_DESCRIPTION_WIDTH = 60;

    private final SideButton[] sideButtons;
    private final String descriptionLines[];

    public ItemDescriptionWindow(Terminal terminal, TextGraphics g, ItemLine line) {
        super(terminal, g, 0, 0, 8, 4);
        var item = line.getItem();
        this.setTitle("${cyan}" + item.getDisplayName());
        this.setBorderColor("cyan");
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
        this.setWidth(2 + MAX_DESCRIPTION_WIDTH + 3 + maxLength + 2);
        this.setHeight(2 + descriptionLines.length + 2);
    }

    @Override
    protected void draw() {
//        draw description
        for (int i = 0; i < descriptionLines.length; i++) {
            TerminalUtility.putAt(terminal, x + 2, y + 2 + i, descriptionLines[i]);
        }
    }

    private final Map<KeyType, Runnable> keyMap = new HashMap<>(){{
        put(KeyType.Escape, () -> close());
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
