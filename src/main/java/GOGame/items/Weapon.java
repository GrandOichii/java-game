package GOGame.items;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @Override
    public String getExtendedDisplayName() {
        return String.format("%s (%d - %d, %d)", displayName, minDamage, maxDamage, range);
    }
}
