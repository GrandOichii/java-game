package GOGame.spells;

import GOGame.Engine;
import GOGame.exceptions.SpellException;

import java.util.ArrayList;
import java.util.List;

public class IntensityIncantation extends Incantation {
    private final int factor;
//    LOW("low", "An incantation to cast a weak spell."),
//    MEDIUM("medium", "An incantation to cast a medium spell."),
//    HIGH("high", "An incantation to cast a strong spell.");


    IntensityIncantation(String title, String actualTitle, String description, int factor) {
        super(title, actualTitle, description);
        this.factor = factor;
    }

    private static final List<IntensityIncantation> INTENSITIES = new ArrayList<>(){{
        add(new IntensityIncantation("low", "LOW", "An incantation to cast a weak spell.", 1));
        add(new IntensityIncantation("medium", "MEDIUM", "An incantation to cast a medium spell.", 2));
        add(new IntensityIncantation("high", "HIGH", "An incantation to cast a strong spell.", 3));
    }};

    public static IntensityIncantation fromTitle(String title) throws SpellException {
        for (var i : INTENSITIES) if (i.getTitle().equals(title)) return i;
        throw new SpellException("don't know type incantation: " + title);
    }

    public static IntensityIncantation fromActualTitle(String title) throws SpellException {
        for (var i : INTENSITIES) if (i.getActualTitle().equals(title)) return i;
        throw new SpellException("don't know type incantation: " + title);
    }

    @Override
    public void editSpell(Spell spell, Engine game) {
        spell.factorAmount(factor);
        spell.factorCost(factor);
    }

    @Override
    public String getDisplayName() {
        return "Intensity: " + getTitle();
    }
}
