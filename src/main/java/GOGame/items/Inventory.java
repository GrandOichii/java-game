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
            var name = item.name;
            var amount = pair.getSecond();
            var prefix = "";
            if (!knownItemNames.contains(item.name)) {
                prefix = NEW_ITEM_PREFIX;
            }
            var displayName = prefix + item.displayName;

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
