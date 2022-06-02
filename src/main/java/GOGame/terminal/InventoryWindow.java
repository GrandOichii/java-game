package GOGame.terminal;

import GOGame.Engine;
import GOGame.items.EquipSlot;
import GOGame.items.EquipableItem;
import GOGame.items.ItemLine;
import GOGame.items.SortedItemLines;
import GOGame.player.Player;
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

//class MenuItemLine implements IDrawableAsLine {
//    private final ItemLine itemLine;
//
//    public MenuItemLine(ItemLine itemLine) {
//        this.itemLine = itemLine;
//    }
//
//    @Override
//    public void drawLine(Terminal terminal, int x, int y, boolean reverseColor) {
//
//    }
//}

class ItemsSubMenu extends Menu {
    private Engine game;
    private ItemLine[] itemNames;
    private ListTemplate itemList;

    public ItemsSubMenu(TWindow parent, Engine game, String title, ItemLine[] items) {
        super(parent, title);
        this.game = game;
        this.itemNames = items;
        var lines = new ArrayList<IDrawableAsLine>();
        for (var itemName : itemNames) {
            lines.add(new CCTMessage("${white-black}" + itemName));
        }
        this.itemList = new ListTemplate(lines, parent.getHeight() - 4);
    }

    @Override
    void draw() {
        itemList.draw(parent.terminal, parent.x + 2, parent.y + 3, true);
    }

    private final Map<KeyType, Runnable> keyMap = new EnumMap<>(KeyType.class){{
        put(KeyType.ArrowUp, () -> itemList.moveUp());
        put(KeyType.ArrowDown, () -> itemList.moveDown());
        put(KeyType.Enter, () -> {
            rememberSelected();
            var i = itemList.getChoice();
            var line = itemNames[i];
            try {
                new ItemDescriptionWindow(parent.terminal, parent.g, (InventoryWindow) parent, game, line).show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }};

    @Override
    void handleInput(KeyStroke key) {
        var kt = key.getKeyType();
        if (!keyMap.containsKey(kt)) {
            return;
        }
        keyMap.get(kt).run();
    }

    public void rememberSelected() {
        var i = itemList.getChoice();
        var line = itemNames[i];
        var itemName = line.getItem().getName();
        ((InventoryWindow)(parent)).getViewedItemNames().add(itemName);
    }
}

class ItemsMenu extends Menu {
    private final Set<String> viewedItemNames = new HashSet<>();
    private ItemsSubMenu[] menus;
    private int menuI;

    public ItemsMenu(TWindow parent, SortedItemLines itemNames, Engine game) {
        super(parent, "Items");
        var menus = new ArrayList<ItemsSubMenu>();
        var tm = itemNames.getItemTypeMap();
        for (var key : tm.keySet()) {
            menus.add(new ItemsSubMenu(parent, game, key, tm.get(key)));
        }
        this.menus = menus.toArray(new ItemsSubMenu[0]);
    }

    public Set<String> getViewedItemNames() {
        return this.viewedItemNames;
    }

    @Override
    void draw() {
        var y = this.parent.y + 2;
        var x = this.parent.x + 1;
        parent.g.drawLine(x, y, x + this.parent.getWidth() - 3, y, '-');
        for (int i = 0; i < menus.length; i++) {
            var menuTitle = menus[i].getTitle();
            var line = "-<" + "-".repeat(menuTitle.length()) + ">-";
            TerminalUtility.putAt(parent.terminal, x, y, line);
            menuTitle.draw(parent.terminal, x + 2, y, i == menuI);
            x += line.length();
        }
        this.menus[menuI].draw();
    }

    private final Map<KeyType, Runnable> keyMap = new EnumMap<>(KeyType.class){{
        put(KeyType.ArrowLeft, () -> {
            menuI--;
            if (menuI < 0) {
                menuI = menus.length - 1;
            }
        });
        put(KeyType.ArrowRight, () -> {
            menuI++;
            if (menuI >= menus.length) {
                menuI = 0;
            }
        });
    }};

    @Override
    void handleInput(KeyStroke key) {
        var kt = key.getKeyType();
        if (keyMap.containsKey(kt)) {
            keyMap.get(kt).run();
            return;
        }
        this.menus[menuI].handleInput(key);
    }
}

class EquipmentMenu extends Menu {
    private int limbI = 0;
    private Player player;

    public EquipmentMenu(TWindow parent, Engine game) {
        super(parent, "Equipment");
        this.player = game.getPlayer();
    }

    @Override
    void draw() {
        var equipment = player.getEquipmentMap();
        var i = 0;
        var y = parent.y + 3;
        var x = parent.x + 3;
        for (var limb : Player.ORDERED_LIMBS) {
            TerminalUtility.putAt(parent.terminal, x, y + i, "- " + limb);
            var itemName = "none";
            var item = equipment.get(limb);
            if (item != null) {
                itemName = item.getExtendedDisplayName();
                if (item.getSlot().equals(EquipSlot.ARMS)) itemName += " (ARMS)";
            }
            TerminalUtility.putAt(parent.terminal, x + 9, y + i, itemName, i / 2 == limbI ? "black-cyan" : "cyan-black");
            i += 2;
        }
    }

    private final Map<KeyType, Runnable> keyMap = new EnumMap<>(KeyType.class){{
        put(KeyType.ArrowDown, () -> {
            var size = player.getEquipmentMap().size();
            limbI++;
            if (limbI >= size) limbI = 0;
        });
        put(KeyType.ArrowUp, () -> {
            var size = player.getEquipmentMap().size();
            limbI--;
            if (limbI < 0) limbI = size - 1;
        });
    }};

    @Override
    void handleInput(KeyStroke key) {
        var kt = key.getKeyType();
        if (!keyMap.containsKey(kt)) return;
        keyMap.get(kt).run();
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
        this.setBorderColor("orange");
        var items = game.getPlayer().getInventory().getAsPrettyList();

        menus = new Menu[]{
            new ItemsMenu(this, items, game),
            new EquipmentMenu(this, game)
        };
    }

    public Set<String> getViewedItemNames() {
        for (var menu : this.menus) {
            if (menu instanceof ItemsMenu) {
                return ((ItemsMenu) menu).getViewedItemNames();
            }
        }
        throw new RuntimeException("can't find items menu");
    }

    @Override
    protected void draw() {
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