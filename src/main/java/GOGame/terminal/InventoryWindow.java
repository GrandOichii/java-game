package GOGame.terminal;

import GOGame.Engine;
import GOGame.items.ItemDescriptionWindow;
import GOGame.items.ItemLine;
import GOGame.player.Player;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.*;

abstract class Menu {
    private final CCTMessage title;
    protected final TWindow parent;
    public CCTMessage getTitle() {
        return title;
    }
    public Menu(TWindow parent, String title) {
        this.parent = parent;
        this.title = new CCTMessage(title);
    }

    abstract void draw();

    abstract void handleInput(KeyStroke key);
}

class ItemsMenu extends Menu {
    private ItemLine[] itemNames;
    private ListTemplate itemList;
    private final Set<String> viewedItemNames = new HashSet<>();


    public ItemsMenu(TWindow parent, ItemLine[] itemNames, ListTemplate itemList) {
        super(parent, "Items");
        this.itemNames = itemNames;
        this.itemList = itemList;
    }

    public Set<String> getViewedItemNames() {
        return this.viewedItemNames;
    }

    @Override
    void draw() {
        itemList.draw(parent.terminal, parent.x + 2, parent.y + 3, true);
    }

    private Map<KeyType, Runnable> keyMap = new EnumMap<>(KeyType.class){{
        put(KeyType.ArrowUp, () -> {
            rememberSelected();
            itemList.moveUp();
        });
        put(KeyType.ArrowDown, () -> {
            rememberSelected();
            itemList.moveDown();
        });
    }};

    @Override
    void handleInput(KeyStroke key) {
        var kt = key.getKeyType();
        if (keyMap.containsKey(kt)) {
            keyMap.get(kt).run();
        }
    }

    private void rememberSelected() {
        var i = itemList.getChoice();
        var itemName = itemNames[i].getName();
        viewedItemNames.add(itemName);
    }
}

public class InventoryWindow extends TWindow {
    private static final int WINDOW_WIDTH = 60;
    private static final int WINDOW_HEIGHT = 20;

//    private Engine game;
    private Menu[] menus;
    private int menuI = 0;

    public InventoryWindow(Terminal terminal, TextGraphics g, Engine game) {
        super(terminal, g, WINDOW_WIDTH, WINDOW_HEIGHT, 2, 2);
        this.setTitle("${orange}Inventory");
//        this.game = game;

        var lines = new ArrayList<IDrawableAsLine>();
        var itemNames = game.getPlayer().getInventory().getAsPrettyList();
        for (var itemName : itemNames) {
            lines.add(new CCTMessage("${white-black}" + itemName));
        }
        var itemList = new ListTemplate(lines, WINDOW_HEIGHT - 4);

        menus = new Menu[]{
            new ItemsMenu(this, itemNames, itemList)
        };
    }

    public Set<String> getViewedItemNames() {
        return ((ItemsMenu)this.menus[menuI]).getViewedItemNames();
    }

    @Override
    protected void draw() {
        g.enableModifiers(SGR.BOLD);
//        draw head
        var y = this.y + 1;
        var x = this.x + 1;
        g.drawLine(x, y, x + this.getWidth() - 3, y, '-');
        for (int i = 0; i < menus.length; i++) {
            var menuTitle = menus[i].getTitle();
            var line = "-<" + "-".repeat(menuTitle.length()) + ">-";
            TerminalUtility.putAt(terminal, x, y, line);
            menuTitle.draw(terminal, x + 2, y, i == menuI);
            x += line.length();
        }
//        draw menu
        this.menus[menuI].draw();
        g.disableModifiers(SGR.BOLD);
    }

    final Map<KeyType, Runnable> keyMap = new HashMap<>(){{
        put(KeyType.Escape, () -> close());
        put(KeyType.Tab, () -> {
            menuI++;
            if (menuI >= menus.length) {
                menuI = 0;
            }
        });
        put(KeyType.ReverseTab, () -> {
            menuI--;
            if (menuI < 0) {
                menuI = menus.length;
            }
        });
    }};

    @Override
    protected void handleInput(KeyStroke key) {
        var kt = key.getKeyType();
        if (keyMap.containsKey(kt)) {
            keyMap.get(kt).run();
            return;
        }
        this.menus[menuI].handleInput(key);
    }
}