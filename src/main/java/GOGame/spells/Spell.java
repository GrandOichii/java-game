package GOGame.spells;

import GOGame.Engine;
import GOGame.entities.Entity;
import GOGame.exceptions.SpellException;
import GOGame.exceptions.SpellParseException;

public class Spell {
    private Entity target;

    public Entity getTarget() {
        return target;
    }

    private final Entity caster;

    public Entity getCaster() {
        return caster;
    }

    private final TargetIncantation targetIncantation;

    public TargetIncantation getTargetIncantation() {
        return targetIncantation;
    }

    private final TypeIncantation typeIncantation;

    public TypeIncantation getTypeIncantation() {
        return typeIncantation;
    }

    private final IntensityIncantation intensityIncantation;

    public IntensityIncantation getSpellIntensity() {
        return intensityIncantation;
    }

    private int cost;

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void addCost(int cost) {
        this.cost += cost;
    }

    public void factorCost(int factor) {
        this.cost *= factor;
    }

    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void factorAmount(int factor) {
        this.amount *= factor;
    }

    public Spell(String line, Engine engine) throws SpellException {
        var split = line.split(" ");
        if (split.length != 3) {
            throw new SpellParseException(line, "too few incantation words");
        }
        targetIncantation = TargetIncantation.fromTitle(split[0]);
        typeIncantation = TypeIncantation.fromTitle(split[1]);
        intensityIncantation = IntensityIncantation.fromTitle(split[2]);

        caster = engine.getPlayer();
        cost = 0;
        amount = 0;

//        edit spell
        var array = new Incantation[]{targetIncantation, typeIncantation, intensityIncantation};
        for (var incantation : array) incantation.editSpell(this, engine);
    }

    @Override
    public String toString() {
        return targetIncantation + " " + typeIncantation + " " + intensityIncantation;
    }

    public void cast() {
        caster.spendMana(cost);
        this.typeIncantation.cast();
    }

    public void setTargetIncantation(Entity entity) {
        this.target = entity;
    }
}