package GOGame.items;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArmorItem extends EquipableItem{
    @JsonProperty("armorRating")
    private int armorRating;

    @Override
    public String getCategory() {
        return "Armor";
    }

    @Override
    protected String additionalDescriptionInfo() {
        return super.additionalDescriptionInfo() + String.format("Armor rating: %d\n\n", armorRating);
    }
}
