package GOGame.items;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RangedWeapon extends Weapon {
    @JsonProperty("ammoType")
    private AmmoType ammoType;

    @Override
    protected String additionalDescriptionInfo() {
        return String.format("Ammo type: %s\n", ammoType) + super.additionalDescriptionInfo();
    }
}
