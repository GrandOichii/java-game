package GOGame.items;

import GOGame.Engine;
import GOGame.Pair;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final Engine e;
    private Engine engine;

    private final List<Pair<String, Integer>> items = new ArrayList<>();

    public Inventory(Engine engine) {
        this.e = engine;
    }

    public void addItem(Item item) {
        this.addItem(item, 1);
    }

    public void addItem(Item item, int amount) {
        var itemName = item.getName();
        for (var pair : items) {
            if (pair.getFirst().equals(itemName)) {
                pair.setSecond(pair.getSecond() + amount);
                return;
            }
        }
        items.add(new Pair<>(itemName, amount));
    }

    public void addItemsFromContainer(Container container) {
        var pairs = container.getAsPairs();
        for (var pair : pairs) {
            this.addItem(pair.getFirst(), pair.getSecond());
        }
    }

    public ItemLine[] getAsPrettyList() {
        var result = new ArrayList<ItemLine>();
        var itemManager = e.getItemManager();
        for (var pair : items) {
            var item = itemManager.get(pair.getFirst());
            var displayName = item.getDisplayName();
            var name = item.getName();
            var amount = pair.getSecond();
            if (item.isStackable()) {
                result.add(new ItemLine(name, displayName, amount));
                continue;
            }
            for (int i = 0; i < amount; i++) {
                result.add(new ItemLine(name, displayName));
            }
        }
        return result.toArray(new ItemLine[0]);
    }
}
