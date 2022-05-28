package GOGame.items;

import GOGame.Engine;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.Terminal;

public class ItemDescriptionWindow {

    private static final int WINDOW_OFFSET_X = 3;
    private static final int WINDOW_OFFSET_Y = 3;

    private final Item item;
    private final Terminal terminal;
    private final TextGraphics g;

    public ItemDescriptionWindow(Terminal terminal, TextGraphics g, Engine engine, String itemName) {
        this.terminal = terminal;
        this.g = g;
        this.item = engine.getItemManager().get(itemName);
        System.out.println(this.item.getName());
        System.out.println(this.item.getDescription());
    }

    public void show() {

    }
}
