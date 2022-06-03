package GOGame.spells;

import GOGame.Engine;
import GOGame.exceptions.SpellException;

import java.util.ArrayList;
import java.util.List;

public abstract class TargetIncantation extends Incantation {
    TargetIncantation(String title, String actualTitle, String description) {
        super(title, actualTitle, description);
    }

    private static final List<TargetIncantation> TARGETS = new ArrayList<>(){{
        add(new TargetIncantation("self", "SELF", "Incantation for casting the spell on self.") {
            @Override
            public void editSpell(Spell spell, Engine game) {
                var target = game.getPlayer();
                spell.setTargetIncantation(target);
                spell.setAmount(0);
                spell.setCost(5);
            }
        });
        add(new TargetIncantation("other", "OTHER", "Incantation for casting the spell on another being") {
            @Override
            public void editSpell(Spell spell, Engine game) {
                var target = game.getPlayer();
                spell.setTargetIncantation(target);
                spell.setAmount(0);
                spell.setCost(10);
            }
        });
    }};

    public static TargetIncantation fromTitle(String title) throws SpellException {
        for (var t : TARGETS) {
            if (t.getTitle().equals(title)) return t;
        }
        throw new SpellException("don't know target incantation: " + title);
    }

    public static TargetIncantation fromActualTitle(String title) throws SpellException {
        for (var t : TARGETS) if (t.getActualTitle().equals(title)) return t;
        throw new SpellException("don't know target incantation: " + title);
    }

    @Override
    public String getDisplayName() {
        return "Target: " + getTitle();
    }
}