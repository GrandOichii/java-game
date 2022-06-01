package GOGame.items;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    @JsonProperty("basic")
    private BasicItem[] basicItems;
    @JsonProperty("ammo")
    private AmmoItem[] ammoItems;
    @JsonProperty("weapons")
    private WeaponLoadInfo weapons;
    @JsonProperty("armor")
    private ArmorItem[] armorItems;

    private final List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public void setUp() {
        items.addAll(List.of(basicItems));
        items.addAll(List.of(ammoItems));
        items.addAll(List.of(armorItems));
        items.addAll(weapons.getItems());
    }

    public Item get(String itemName) {
        for (var item : items) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        throw new RuntimeException("unrecognized item name: " + itemName);
    }
}

class WeaponLoadInfo {
    @JsonProperty("melee")
    private MeleeWeapon[] meleeWeapons;
    @JsonProperty("ranged")
    private RangedWeapon[] rangedWeapons;
    public List<Item> getItems() {
        var result = new ArrayList<Item>();
        result.addAll(List.of(meleeWeapons));
        result.addAll(List.of(rangedWeapons));
        return result;
    }
}