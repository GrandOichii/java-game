package GOGame.terminal;

import GOGame.Engine;
import GOGame.items.ItemDescriptionWindow;
import GOGame.items.ItemLine;
import GOGame.player.Player;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class InventoryWindow {
    private Engine game;
    private ItemLine[] itemNames;
    private Terminal terminal;
    private final Player player;
    private ListTemplate itemList;

    private static final int WINDOW_OFFSET_X = 2;
    private static final int WINDOW_OFFSET_Y = 2;
    private static final CCTMessage WINDOW_TOP_MESSAGE = new CCTMessage("${orange}Inventory");
    private static final String[] TAB_NAMES = new String[]{"Items", "Equipment", "Spells"};
//    private static final int[] MENU_INDEXES = new int[] {0, 0, 0};
    private Runnable[] tabDrawMap = new Runnable[]{
        () -> {
            itemList.draw(terminal, WINDOW_OFFSET_X + 2, WINDOW_OFFSET_Y + 3, true);
        },
        () -> {

        },
        () -> {

        }
    };
    private static final int WINDOW_WIDTH = 60;
    private static final int WINDOW_HEIGHT = 20;
    private TextGraphics g;
    private boolean running;
    private int tabI = 0;

    public InventoryWindow(Terminal terminal, TextGraphics g, Engine game) {
        this.game = game;
        this.terminal = terminal;
        this.player = game.getPlayer();
        this.g = g;
        this.itemNames = this.player.getInventory().getAsPrettyList();

        var lines = new ArrayList<DrawableAsLine>();
        for (var itemName : itemNames) {
            lines.add(new CCTMessage("${white-black}" + itemName));
        }
        this.itemList = new ListTemplate(lines, WINDOW_HEIGHT - 4);
    }

    public void show() throws IOException {
        this.running = true;
        while (this.running) {
            this.draw();
            this.handleInput();
        }
    }

    private Runnable[] arrowUpMap = new Runnable[]{
            () -> {
                itemList.moveUp();
            },
            () -> {

            },
            () -> {

            }
    };

    private Runnable[] arrowDownMap = new Runnable[]{
            () -> {
                itemList.moveDown();
            },
            () -> {

            },
            () -> {

            }
    };

    private Runnable[] enterMap = new Runnable[] {
            () -> {
                var i = itemList.getChoice();
                new ItemDescriptionWindow(terminal, g, game, itemNames[i].getName()).show();
            },
            () -> {

            },
            () -> {

            }
    };

    private HashMap<KeyType, Runnable> keyMap = new HashMap<>(){{
        put(KeyType.ReverseTab, () -> {
            tabI--;
            if (tabI < 0) {
                tabI = TAB_NAMES.length - 1;
            }
        });
        put(KeyType.Tab, () -> {
            tabI++;
            if (tabI >= TAB_NAMES.length) {
                tabI = 0;
            }
        });
        put(KeyType.Escape, () -> running = false);
        put(KeyType.ArrowDown, () -> arrowDownMap[tabI].run());
        put(KeyType.ArrowUp, () -> arrowUpMap[tabI].run());
        put(KeyType.Enter, () -> enterMap[tabI].run());
    }};

    private void handleInput() throws IOException {
        var key = terminal.readInput();
        var keyType = key.getKeyType();
        if (!keyMap.containsKey(keyType)) {
            return;
        }
        keyMap.get(keyType).run();
    }

    private void clear() {
        var line = " ".repeat(WINDOW_WIDTH);
        for (int i = 0; i < WINDOW_HEIGHT; i++) {
            TerminalUtility.putAt(terminal, WINDOW_OFFSET_X, WINDOW_OFFSET_Y + i, line);
        }
    }

    private void draw() throws IOException {
        clear();
        g.enableModifiers(SGR.BOLD);
        g.drawRectangle(new TerminalPosition(WINDOW_OFFSET_X, WINDOW_OFFSET_Y), new TerminalSize(WINDOW_WIDTH, WINDOW_HEIGHT), '*');
        WINDOW_TOP_MESSAGE.draw(terminal, WINDOW_OFFSET_X + 1, WINDOW_OFFSET_Y);
        this.drawTabs();
        this.tabDrawMap[tabI].run();
        this.terminal.flush();
        g.disableModifiers(SGR.BOLD);
    }

    private void drawTabs() {
        var y = WINDOW_OFFSET_Y + 1;
        var x = WINDOW_OFFSET_X + 1;
        g.drawLine(x, y, x + WINDOW_WIDTH - 3, y, '-');
        for (int i = 0; i < TAB_NAMES.length; i++) {
            var tabName = TAB_NAMES[i];
            var line = "-<" + "-".repeat(tabName.length()) + ">-";
            TerminalUtility.putAt(terminal, x, y, line);
            TerminalUtility.putAt(terminal, x + 2, y, tabName, i == tabI ? "blue" : "normal");
            x += line.length();
        }
    }
}
