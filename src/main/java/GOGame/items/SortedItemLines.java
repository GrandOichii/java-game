package GOGame.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SortedItemLines {
    private final Map<String, ItemLine[]> itemTypeMap = new HashMap<>();

    public Map<String, ItemLine[]> getItemTypeMap() {
        return itemTypeMap;
    }

    public static final String[] ORDERED_TYPES = new String[]{"All", "Weapons", "Ammo", "Other"};

    public SortedItemLines(ItemLine[] lines) {
        var types = new HashMap<String, ArrayList<ItemLine>>();
        for (var line : lines) {
            var item = line.getItem();
            var t = item.getCategory();
            if (!types.containsKey(t)) {
                types.put(t, new ArrayList<>());
            }
            types.get(t).add(line);
        }
        itemTypeMap.put("All", lines);
        for (var key : types.keySet()){
            itemTypeMap.put(key, types.get(key).toArray(new ItemLine[0]));
        }
    }
}
