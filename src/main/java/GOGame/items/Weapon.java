package GOGame.items;

import com.fasterxml.jackson.annotation.JsonProperty;

enum DamageType {
    PHYSICAL("PHYSICAL");
    private String title;
    DamageType(String title) {
        this.title = title;
    }
}

public abstract class Weapon extends EquipableItem {
    @JsonProperty("minDamage")
    private int minDamage;
    @JsonProperty("maxDamage")
    private int maxDamage;
    @JsonProperty("range")
    private int range;

    @Override
    public String getCategory() {
        return "Weapons";
    }

    @Override
    protected String additionalDescriptionInfo() {
        return super.additionalDescriptionInfo() + String.format("Damage: %d-%d  Range:%d\n\n", minDamage, maxDamage, range);
    }
}
