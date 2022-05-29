package GOGame.items;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AmmoItem extends Item {
    @JsonProperty("damage")
    private int damage;
    @JsonProperty("ammoType")
    private AmmoType ammoType;
    @JsonProperty("damageType")
    private DamageType damageType;

    public AmmoItem() {
        super(true);
    }

    @Override
    public String getCategory() {
        return "Ammo";
    }

    @Override
    protected String additionalDescriptionInfo() {
        var result = "";
        result += String.format("Ammo type: %s\nDamage: %d  Type: %s\n\n", this.ammoType, this.damage, this.damageType);
        return result;
    }
}
