package GOGame.items;

import GOGame.Pair;

import java.util.ArrayList;
import java.util.List;

public class Container {
    private final ArrayList<Item> items = new ArrayList<>();
    private final ArrayList<Integer> amounts = new ArrayList<>();

    public List<Pair<Item, Integer>> getAsPairs() {
        var size = items.size();
        var result = new ArrayList<Pair<Item, Integer>>();
        for (int i = 0; i < size; i++) {
            var item = items.get(i);
            result.add(new Pair<>(item, amounts.get(i)));
        }
        return result;
    }

    public void add(Item item, int amount) {
        items.add(item);
        amounts.add(amount);
    }

    public ItemLine[] getLines() {
        var pairs = getAsPairs();
        var size = pairs.size();
        var result = new ItemLine[size];
        for (int i = 0; i < size; i++) {
            var pair = pairs.get(i);
            var item = pair.getFirst();
            result[i] = new ItemLine(item, item.displayName, pair.getSecond());
        }
        return result;
    }
}
