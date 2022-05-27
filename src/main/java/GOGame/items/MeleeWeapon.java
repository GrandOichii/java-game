package GOGame.items;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeleeWeapon extends Weapon {
    @JsonProperty("damageType")
    private DamageType damageType;
}
