package GOGame.player;

import GOGame.Engine;
import GOGame.items.Container;
import GOGame.items.Inventory;
import GOGame.items.Item;

public class Player {
    private final String name;
    private String className;
    private final Inventory inventory;
    private final AttributeManager attributes;

    public AttributeManager getAttributes() {
        return attributes;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void addItemsFromContainer(Container container) {
        inventory.addItemsFromContainer(container);
    }

    public Player(Engine engine, String name, PlayerClass pClass) {
        this.name = name;
        this.inventory = new Inventory(engine);
        this.attributes = new AttributeManager();

        this.injectClassData(engine, pClass);
    }

    private void injectClassData(Engine engine, PlayerClass pClass) {
        this.className = pClass.getName();
        var items = pClass.getItems();
        var itemManager = engine.getItemManager();
        for (var itemName : items.keySet()) {
            var amount = items.get(itemName);
            var item = itemManager.get(itemName);
            this.inventory.addItem(item, amount);
        }
        attributes.setBase(pClass.getAttributes());
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
