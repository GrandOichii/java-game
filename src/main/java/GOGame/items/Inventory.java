package GOGame.items;

import GOGame.Engine;
import GOGame.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Inventory {

    private static final String NEW_ITEM_PREFIX = "${green}[NEW]${normal} ";
    private final Engine e;
    private Engine engine;

    private final List<Pair<String, Integer>> items = new ArrayList<>();
    private final Set<String> knownItemNames = new HashSet<>();
    public void addViewedItemNames(Set<String> names) {
        knownItemNames.addAll(names);
    }

    public Inventory(Engine engine) {
        this.e = engine;
    }

    public void takeItem(String itemName) {
        for (var pair : items) {
            if (pair.getFirst().equals(itemName)) {
                pair.setSecond(pair.getSecond() - 1);
                if (pair.getSecond() <= 0) {
                    items.remove(pair);
                    return;
                }
            }
        }
    }

    public int count(String itemName) {
        for (var pair : items) {
            if (pair.getFirst().equals(itemName)) {
                return pair.getSecond();
            }
        }
        return 0;
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

    public SortedItemLines getAsPrettyList() {
        var lines = new ArrayList<ItemLine>();
        var itemManager = e.getItemManager();
        for (var pair : items) {
            var item = itemManager.get(pair.getFirst());
            var amount = pair.getSecond();
            var prefix = "";
            if (!knownItemNames.contains(item.name)) {
                prefix = NEW_ITEM_PREFIX;
            }
            var displayName = prefix + item.displayName;

            if (item.isStackable()) {
                lines.add(new ItemLine(item, displayName, amount));
                continue;
            }
            for (int i = 0; i < amount; i++) {
                lines.add(new ItemLine(item, displayName));
            }
        }
        return new SortedItemLines(lines.toArray(new ItemLine[0]));
    }
}
