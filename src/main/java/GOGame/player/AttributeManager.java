package GOGame.player;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class AttributeManager {
    private final Map<Attribute, Integer> baseValues = new EnumMap<>(Attribute.class){{
        for (var value : Attribute.values()) {
            put(value, 0);
        }
    }};

    public String[] getAttributeNames() {
        return (String[]) baseValues.keySet().stream().map(Enum::name).toArray();
    }

    public int getBase(Attribute name) { return baseValues.get(name); }

    public void setBase(HashMap<Attribute, Integer> attributeMap) {
        for (var key : attributeMap.keySet()) {
            this.baseValues.put(key, attributeMap.get(key));
        }
    }
}
