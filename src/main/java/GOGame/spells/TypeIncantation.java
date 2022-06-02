package GOGame.spells;

import GOGame.Engine;
import GOGame.exceptions.SpellException;
import GOGame.items.DamageType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TypeIncantation extends Incantation {
//    FIRE("fire", "An incantation to cast a spell that deals fire damage."),
//    ICE("ice", "An incantation to cast a spell that deals ice damage."),
//    HEAL("heal", "An incantation to heal an entity");

    TypeIncantation(String title, String actualTitle, String description) {
        super(title, actualTitle, description);
    }

    private Spell parent;

    public Spell getParent() {
        return parent;
    }

    public void setParent(Spell parent) {
        this.parent = parent;
    }

    private static final List<TypeIncantation> TYPES = new ArrayList<>(){{
        add(new TypeIncantation("heal", "HEAL", "An incantation to cast a spell that deals fire damage.") {
            @Override
            public void cast() {
                var parent = this.getParent();
                var target = parent.getTarget();
                var amount = parent.getAmount();
                target.heal(amount);
            }

            @Override
            public void editSpell(Spell spell, Engine game) {
                super.editSpell(spell, game);
                spell.addAmount(10);
                spell.addCost(10);
            }
        });
        add(new TypeIncantation("fire", "FIRE", "An incantation to cast a spell that deals fire damage.") {
            @Override
            public void cast() {
                var spell = getParent();
                var target = spell.getTarget();
                target.dealDamage(spell.getAmount(), DamageType.FIRE);
            }

            @Override
            public void editSpell(Spell spell, Engine game) {
                super.editSpell(spell, game);
                spell.addAmount(5);
                spell.addCost(10);
            }
        });
    }};

    public static TypeIncantation fromTitle(String title) throws SpellException {
        for (var t : TYPES) if (t.getTitle().equals(title)) return t;
        throw new SpellException("don't know type incantation: " + title);
    }

    public static TypeIncantation fromActualTitle(String title) throws SpellException {
        for (var t : TYPES) if (t.getActualTitle().equals(title)) return t;
        throw new SpellException("don't know type incantation: " + title);
    }

    public abstract void cast();

    @Override
    public void editSpell(Spell spell, Engine game) {
        setParent(spell);
    }
}
