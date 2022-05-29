package GOGame.items;

import GOGame.terminal.TerminalUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SortedItemLines {
    public static final String[] TYPE_NAMES = new String[]{"All", "Weapons", "Ammo", "Other"};
    public static final int TYPES_COUNT = TYPE_NAMES.length;
    private ItemLine[] allItems;

    public ItemLine[] getAllItems() {
        return allItems;
    }
    private ItemLine[] weapons;

    public ItemLine[] getWeapons() {
        return weapons;
    }
    private ItemLine[] ammo;

    public ItemLine[] getAmmo() {
        return ammo;
    }
    private ItemLine[] other;

    public ItemLine[] getOther() {
        return other;
    }
    public SortedItemLines(ItemLine[] lines) {
        allItems = lines;
        var weapons = new ArrayList<ItemLine>();
        var ammo = new ArrayList<ItemLine>();
        var other = new ArrayList<ItemLine>();
        for (var line : allItems) {
            var item = line.getItem();
            if (item instanceof Weapon) {
                weapons.add(line);
                continue;
            }
            if (item instanceof AmmoItem) {
                ammo.add(line);
                continue;
            }
            other.add(line);
        }
        this.weapons = weapons.toArray(new ItemLine[0]);
        this.ammo = ammo.toArray(new ItemLine[0]);
        this.other = other.toArray(new ItemLine[0]);
    }
}
