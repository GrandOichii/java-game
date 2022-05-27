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
}
