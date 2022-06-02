package GOGame.player;

import GOGame.Engine;
import GOGame.Pair;
import GOGame.entities.Entity;
import GOGame.items.*;
import GOGame.spells.*;

import java.util.*;

public class Player extends Entity {
    private final String name;
    private final Engine engine;
    private String className;
    private final Inventory inventory;
    private final AttributeManager attributes;

    private final Set<TargetIncantation> knownSpellTargets = new HashSet<>();
    private final Set<TypeIncantation> knownSpellTypes = new HashSet<>();
    private final Set<IntensityIncantation> knownSpellIntensities = new HashSet<>();

    public void learn(TargetIncantation t) { knownSpellTargets.add(t); }
    public void learn(TypeIncantation t) { knownSpellTypes.add(t); }
    public void learn(IntensityIncantation t) { knownSpellIntensities.add(t); }

    public AttributeManager getAttributes() {
        return attributes;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void takeItem(String itemName) {
        inventory.takeItem(itemName);
    }

    public void addItemsFromContainer(Container container) {
        inventory.addItemsFromContainer(container);
    }

    public Player(Engine engine, String name, PlayerClass pClass) {
        this.name = name;
        this.maxHealth = 10;
        this.curHealth = this.maxHealth;
        this.maxMana = 100;
        this.curMana = this.maxMana;
        this.inventory = new Inventory(engine);
        this.attributes = new AttributeManager();
        this.engine = engine;

        this.injectClassData(engine, pClass);
    }

    private static final int MANA_REGEN = 5;

    public void update() {
        curMana += MANA_REGEN;
        if (curMana > maxMana) curMana = maxMana;
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

    public static final String[] ORDERED_LIMBS = new String[]{
            "ARM1",
            "ARM2",
            "HEAD",
            "TORSO",
            "LEGS"
    };

    private final Map<String, EquipableItem> limbMap = new HashMap<>(){{
        for (var limb : ORDERED_LIMBS) {
            put(limb, null);
        }
    }};

    public Map<String, EquipableItem> getEquipmentMap() {
        return limbMap;
    }

    private final static Map<EquipSlot, String[]> LIMB_EQUIP_MAP = new EnumMap<>(EquipSlot.class){{
        put(EquipSlot.ARM, new String[]{"ARM1", "ARM2"});
        put(EquipSlot.ARMS, new String[]{"ARMS"});
        put(EquipSlot.HEAD, new String[]{"HEAD"});
        put(EquipSlot.TORSO, new String[]{"TORSO"});
        put(EquipSlot.LEGS, new String[]{"LEGS"});
    }};

    public Item[] getEquippedItems(String limb) {
        if (limb.equals("ARMS")) {
            return new Item[]{limbMap.get("ARM1"), limbMap.get("ARM2")};
        }
        return new Item[]{limbMap.get(limb)};
    }


    public List<Pair<String, String>> getLimbLines(EquipSlot slot) {
        if (!LIMB_EQUIP_MAP.containsKey(slot)) throw new RuntimeException("can't recognize slot " + slot + " for limbs");
        var lines = LIMB_EQUIP_MAP.get(slot).clone();
        var result = new ArrayList<Pair<String, String>>();
        for (int i = 0; i < lines.length; i++){
            var items = getEquippedItems(lines[i]);
            var names = new ArrayList<String>();
            for (var item : items) {
                if (item != null) names.add(item.getDisplayName());
            }
            var pair = new Pair<>("", lines[i]);
            var j = String.join(", ", names);
            if (j.length() != 0) {
                lines[i] += " (" + j + ")";
            }
            pair.setFirst(lines[i]);
            result.add(pair);
        }
        return result;
    }

    public void equip(EquipableItem item, String limb) {
        if (limb.equals("ARMS")) {
            limbMap.put("ARM1", item);
            limbMap.put("ARM2", item);
        } else {
            if (limb.startsWith("ARM")){
                var i = limbMap.get("ARM1");
                if (i != null && i.getSlot().equals(EquipSlot.ARMS)) {
                    limbMap.put("ARM1", null);
                    limbMap.put("ARM2", null);
                }
            }
            limbMap.put(limb, item);
//            if item is equipped to other limb, unequip it
            var count = inventory.count(item.getName());
            for (var otherLimb : limbMap.keySet()) {
                if (otherLimb.equals(limb)) {
                    continue;
                }
                var otherItem = limbMap.get(otherLimb);
                if (otherItem == item) {
                    count--;
                    if (count > 0) continue;
                    limbMap.put(otherLimb, null);
                }
            }
        }
        engine.updatePlayerLook();
    }

    public void castSpell(Spell spell) {
//        check if player can cast the spell
        var map = new HashMap<Incantation, Set>(){{
            put(spell.getTargetIncantation(), knownSpellTargets);
            put(spell.getTypeIncantation(), knownSpellTypes);
            put(spell.getSpellIntensity(), knownSpellIntensities);
        }};
        for (var incantation : map.keySet()) {
            var arr = map.get(incantation);
            if (!arr.contains(incantation)) {
                engine.addToLog("Can't cast spell: don't know incantation " + incantation.getTitle());
                return;
            }
        }
        var cost = spell.getCost();
        if (this.curMana < cost) {
            engine.addToLog(String.format("Failed to cast spell, not enough mana (requires: %d)", cost));
            return;
        }

//        cast the actual spell
        this.curMana -= cost;
//        pick target
        spell.cast();
    }
}
