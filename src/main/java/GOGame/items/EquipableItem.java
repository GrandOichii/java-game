package GOGame.items;

import GOGame.player.Attribute;
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
    private HashMap<Attribute, Integer> requirements;

    public EquipableItem() {
        super(false);
    }

    @Override
    protected String additionalDescriptionInfo() {
        StringBuilder result = new StringBuilder("Requirements:\n");
        for (var key : requirements.keySet()) {
            var value = requirements.get(key);
            if (value <= 0) {
                continue;
            }
            result.append(String.format(" %s: [%d]", key, value));
        }
        return result.toString() + "\n";
    }
}
