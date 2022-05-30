package GOGame.player;

import GOGame.Engine;
import GOGame.items.Container;
import GOGame.items.Inventory;
import GOGame.items.Item;

public class Player {
    private final String name;
    private final String className;
    private final Inventory inventory;
    private final AttributeManager attributes;

    public Inventory getInventory() {
        return inventory;
    }

    public void addItemsFromContainer(Container container) {
        inventory.addItemsFromContainer(container);
    }

    public Player(Engine engine, String name, PlayerClass pClass) {
        this.name = name;
        this.className = pClass.getName();
        this.inventory = new Inventory(engine);
        this.attributes = new AttributeManager();
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public void addItem(Item item) {
        this.inventory.addItem(item);
    }
}
