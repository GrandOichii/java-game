package GOGame.items;

import com.fasterxml.jackson.annotation.JsonProperty;

enum DamageType {
    PHYSICAL("PHYSICAL");
    private String title;
    DamageType(String title) {
        this.title = title;
    }
}

public class Weapon extends EquipableItem {
    @JsonProperty("minDamage")
    private int minDamage;
    @JsonProperty("maxDamage")
    private int maxDamage;
    @JsonProperty("range")
    private int range;
}
