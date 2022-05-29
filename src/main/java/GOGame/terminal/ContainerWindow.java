package GOGame.terminal;

import GOGame.Engine;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.HashMap;


public class ContainerWindow extends TWindow {
    private static final int MIN_HEIGHT = 5;
    private static final String TITLE_COLOR = "red";
    private static final String BORDER_COLOR = "cyan";
    private static final CCTMessage LOOT_BUTTON = new CCTMessage("${red}Loot");
    private static final CCTMessage CANCEL_BUTTON = new CCTMessage("${yellow}Cancel");
    private final String[] itemLines;

    public boolean lootConfirmed() {
        return lootFocused;
    }

    private boolean lootFocused = true;
    public ContainerWindow(Terminal terminal, TextGraphics g, Engine e, String containerName, String containerTop) {
        super(terminal, g, 0, 0, 3, 3);
        this.setTitle("${" + TITLE_COLOR + "}" + containerTop);
        this.setBorderColor(BORDER_COLOR);

        var lines = e.getContainerManager().get(containerName).getLines();
        this.itemLines = new String[lines.length];
        var maxLength = -1;
        for (int i = 0; i < lines.length; i++) {
            var s = lines[i].toString();
            this.itemLines[i] = s;
            var length = s.length();
            if (length > maxLength){
                maxLength = length;
            }
        }
        this.setWidth(maxLength + 5);
        this.setHeight(Math.max(itemLines.length, MIN_HEIGHT) + 6);
    }

    @Override
    protected void draw() {
        g.enableModifiers(SGR.BOLD);
//        draw item lines
        for (int i = 0; i < itemLines.length; i++) {
            TerminalUtility.putAt(terminal, x + 2, y + 2 + i, itemLines[i]);
        }
//        draw separator
        g.setForegroundColor(borderColor.getFirst());
        g.setBackgroundColor(borderColor.getSecond());
        var y = this.y + this.getHeight() - 3;
        g.drawLine(new TerminalPosition(x, y), new TerminalPosition(x + getWidth() - 1, y), '*');
        g.setForegroundColor(TextColor.ANSI.DEFAULT);
        g.setBackgroundColor(TextColor.ANSI.DEFAULT);
//        draw buttons
        y++;
        var x = this.x + 1;
        LOOT_BUTTON.draw(terminal, x, y, lootFocused);
        x += LOOT_BUTTON.length() + 1;
        CANCEL_BUTTON.draw(terminal, x, y, !lootFocused);
        g.disableModifiers(SGR.BOLD);

    }

    private final HashMap<KeyType, Runnable> keyMap = new HashMap<>(){{
        put(KeyType.Escape, () -> {
            lootFocused = false;
            close();
        });
        put(KeyType.ArrowLeft, () -> lootFocused = !lootFocused);
        put(KeyType.ArrowRight, () -> lootFocused = !lootFocused);
        put(KeyType.Enter, () -> {
            close();
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
