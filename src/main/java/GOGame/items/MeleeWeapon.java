package GOGame.items;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeleeWeapon extends Weapon {
    @JsonProperty("damageType")
    private DamageType damageType;

    @Override
    protected String additionalDescriptionInfo() {
        var result = String.format("Damage type: %s\n", damageType) + super.additionalDescriptionInfo();
        return result;
    }
}
