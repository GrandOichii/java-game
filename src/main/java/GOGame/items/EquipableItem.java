package GOGame.items;

import GOGame.player.Attribute;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class EquipableItem extends Item {
    @JsonProperty("slot")
    private EquipSlot slot;

    public EquipSlot getSlot() {
        return slot;
    }

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
        return result + "\n";
    }

    @Override
    public List<String> getAllowedActions() {
        var result = super.getAllowedActions();
        result.add(0, "equip");
        return result;
    }
}
