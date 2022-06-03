package GOGame.terminal;

import GOGame.Engine;
import GOGame.IDescribable;
import GOGame.Utility;
import GOGame.exceptions.SpellException;
import GOGame.items.EquipableItem;
import GOGame.items.Item;
import GOGame.items.ItemLine;
import GOGame.items.UsableItem;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class SideButton {
//    private final ItemDescriptionWindow parent;
    private CCTMessage text;

    public CCTMessage getText() {
        return text;
    }

    public SideButton(String text) {
        this.text = new CCTMessage(text);
//        this.parent = parent;
    }

    public abstract void press() throws IOException, SpellException;

    public void draw(Terminal terminal, int x, int y, boolean reverseColor) {
        this.text.draw(terminal, x, y, reverseColor);
    }
}

public class DescriptionWindow extends TWindow {
    private static final int MAX_DESCRIPTION_WIDTH = 60;
    private static final String BORDER_COLOR = "cyan";
    private final IDescribable item;
    private final InventoryWindow inventoryWindow;
    private Engine game;

    public IDescribable getItem() {
        return item;
    }

    private SideButton[] sideButtons;
    private final int separatorX;
    private int buttonI = 0;
    private final String descriptionLines[];

    private final Map<String, SideButton> sideButtonMap = new HashMap<>(){{
        put("use", new SideButton("Use") {
            @Override
            public void press() throws SpellException {
                var uItem = (UsableItem)item;
                var used = uItem.use(game);
                if (!used) return;
                close();
                inventoryWindow.close();
            }
        });
        put("equip", new SideButton("Equip") {
            @Override
            public void press() throws IOException {
                var item = (EquipableItem)getItem();
                var player = game.getPlayer();
                var pairs = player.getLimbLines(item.getSlot());
                var size = pairs.size();
                var CCTs = new CCTMessage[size];
                AtomicInteger cctI = new AtomicInteger();
                var width = 0;
                for (int i = 0; i < size; i++) {
                    var line = pairs.get(i).getFirst();
                    CCTs[i] = new CCTMessage("${white-black}" + line);
                    var length = line.length();
                    if (length > width) width = length;
                }
                width += 2;
                var height = size + 2;
                var boxY = y + 1;
                var boxX = x + getWidth() - 1;
                AtomicBoolean running = new AtomicBoolean(true);
                var keyMap = new HashMap<KeyType, Runnable>(){{
                    put(KeyType.Enter, () -> {
                        var pair = pairs.get(cctI.get());
                        player.equip(item, pair.getSecond());
                        running.set(false);
                    });
                    put(KeyType.ArrowUp, () -> {
                        cctI.getAndIncrement();
                        if (cctI.get() >= CCTs.length) cctI.set(0);
                    });
                    put(KeyType.ArrowDown, () -> {
                        cctI.getAndDecrement();
                        if ((cctI.get() <= 0)) cctI.set(CCTs.length - 1);
                    });
                    put(KeyType.Escape, () -> running.set(false));
                }};
                while (running.get()) {
//                    draw
                    g.drawRectangle(new TerminalPosition(boxX, boxY), new TerminalSize(width, height), '*');
                    for (int i = 0; i < CCTs.length; i++) {
                        CCTs[i].draw(terminal, boxX + 1, boxY + 1 + i, i == cctI.get());
                    }
                    terminal.flush();
//                    input
                    var key = terminal.readInput();
                    var kt = key.getKeyType();
                    if (!keyMap.containsKey(kt)) continue;
                    keyMap.get(kt).run();
                }
//                clear
                var l = " ".repeat(width);
                for (int i = 0; i < height; i++) {
                    TerminalUtility.putAt(terminal, boxX, boxY + i, l);
                }
            }
        });
        put("close", new SideButton("Close") {
            @Override
            public void press() {
                close();
            }
        });
    }};

    public DescriptionWindow(Terminal terminal, TextGraphics g, InventoryWindow parent, Engine game, IDescribable item, int amount) {
        super(terminal, g, 0, 0, 2, 3);
        this.item = item;
        this.inventoryWindow = parent;
        this.game = game;
        this.setTitle("${cyan}" + item.getDisplayName());
        this.setBorderColor(BORDER_COLOR);
        this.descriptionLines = Utility.StringWidthSplit(item.getBigDescription(amount), MAX_DESCRIPTION_WIDTH);
        var actions = item.getAllowedActions();
        var size = actions.size();
        this.sideButtons = new SideButton[size];
        for (int i = 0; i < size; i++) {
            var action = actions.get(i);
            if (!sideButtonMap.containsKey(action)) throw new RuntimeException("unknown item action: " + action);
            this.sideButtons[i] = sideButtonMap.get(action);
        }
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
        put(KeyType.Enter, () -> {
            try {
                sideButtons[buttonI].press( );
            } catch (IOException | SpellException e) {
                throw new RuntimeException(e);
            }
        });
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
