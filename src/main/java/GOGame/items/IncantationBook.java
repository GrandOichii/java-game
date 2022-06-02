package GOGame.items;

import GOGame.Engine;
import GOGame.exceptions.SpellException;
import GOGame.player.Attribute;
import GOGame.spells.IntensityIncantation;
import GOGame.spells.TargetIncantation;
import GOGame.spells.TypeIncantation;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IncantationBook extends UsableItem {
    @JsonProperty("targets")
    private String[] targets;
    @JsonProperty("types")
    private String[] types;
    @JsonProperty("intensities")
    private String[] intensities;
    @JsonProperty("intRequirement")
    private int intRequirement;

    public IncantationBook() {
        super(false);
    }

    @Override
    public boolean use(Engine game) throws SpellException {
        var player = game.getPlayer();
        var INT = player.getAttributes().getBase(Attribute.INT);
        if (INT < intRequirement) {
            game.addToLog(String.format("Can't read %s - require INT %d (you have %s)", displayName, intRequirement, INT));
            return false;
        }
        for (var target : targets) player.learn(TargetIncantation.fromActualTitle(target));
        for (var type : types) player.learn(TypeIncantation.fromActualTitle(type));
        for (var intensity : intensities) player.learn(IntensityIncantation.fromActualTitle(intensity));
        player.takeItem(this.name);
        return true;
    }

    @Override
    protected String additionalDescriptionInfo() {
        var result = super.additionalDescriptionInfo();
        result += "INT requirement: " + intRequirement + "\n";
        try {
            if (targets.length != 0) {
                result += "Targets: ";
                var array = new String[targets.length];
                for (int i = 0; i < targets.length; i++) {
                    var incantation = TargetIncantation.fromActualTitle(targets[i]);
                    array[i] = incantation.getTitle();
                }
                result += String.join(", ", array) + "\n";
            }
            if (types.length != 0) {
                result += "Types: ";
                var array = new String[types.length];
                for (int i = 0; i < types.length; i++) {
                    var incantation = TypeIncantation.fromActualTitle(types[i]);
                    array[i] = incantation.getTitle();
                }
                result += String.join(", ", array) + "\n";
            }
            if (intensities.length != 0) {
                result += "Intensities: ";
                var array = new String[intensities.length];
                for (int i = 0; i < intensities.length; i++) {
                    var incantation = IntensityIncantation.fromActualTitle(intensities[i]);
                    array[i] = incantation.getTitle();
                }
                result += String.join(", ", array) + "\n";
            }
        } catch (SpellException e) {
            throw new RuntimeException(e);
        }

//        var arrayNames = new String[]{"Targets: ", "Types: ", "Intensities: "};
//        var arrays = new String[][]{targets, types, intensities};
//        for (int i = 0; i < arrayNames.length; i++) {
//            var array = arrays[i];
//            if (array.length == 0) continue;
//            result += arrayNames[i];
//            result += String.join(", ", array) + "\n";
//        }
        return result + "\n";
    }

    @Override
    public String getCategory() {
        return "Books";
    }
}
