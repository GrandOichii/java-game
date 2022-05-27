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
        var itemName = item.getName();
        for (var pair : items) {
            if (pair.getFirst().equals(itemName)) {
                pair.setSecond(pair.getSecond() + 1);
                return;
            }
        }
        items.add(new Pair<>(itemName, 1));
    }

    public String[] getAsPrettyList() {
        var result = new ArrayList<String>();
        var itemManager = e.getItemManager();
        for (var pair : items) {
            var item = itemManager.get(pair.getFirst());
            var displayName = item.getDisplayName();
            var amount = pair.getSecond();
            if (item.isStackable()) {
                result.add(String.format("%s x %d", displayName, amount));
                continue;
            }
            for (int i = 0; i < amount; i++) {
                result.add(displayName);
            }
        }
        return result.toArray(new String[0]);
    }
}
