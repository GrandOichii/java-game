package GOGame.player;

import java.util.EnumMap;
import java.util.Map;

public class AttributeManager {
    private final Map<Attribute, Integer> baseValues = new EnumMap<>(Attribute.class){{
        for (var value : Attribute.values()) {
            put(value, 0);
        }
    }};

    public int getBase(Attribute name) { return baseValues.get(name); }
}
