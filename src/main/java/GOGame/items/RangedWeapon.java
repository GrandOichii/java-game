package GOGame.items;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RangedWeapon extends Weapon {
    @JsonProperty("ammoType")
    private AmmoType ammoType;

}
