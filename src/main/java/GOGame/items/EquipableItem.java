package GOGame.items;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

enum EquipSlot {
    ARM("ARM"),
    ARMS("ARMS");
    private String title;
    EquipSlot(String title) {
        this.title = title;
    }
}

public abstract class EquipableItem extends Item {
    @JsonProperty("slot")
    private EquipSlot slot;
    @JsonProperty("requirements")
    private HashMap<String, Integer> requirements;

    public EquipableItem() {
        super(false);
    }
}
