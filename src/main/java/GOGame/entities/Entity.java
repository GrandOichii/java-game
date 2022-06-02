package GOGame.entities;

import GOGame.items.DamageType;

public abstract class Entity {
    protected int curHealth;
    protected int maxHealth;
    public int getHealth() {
        return curHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
    protected int curMana;
    protected int maxMana;

    public int getMana() {
        return curMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void heal(int amount) {
        curHealth += amount;
        if (curHealth > maxHealth) curHealth = maxHealth;
    }

    public void spendMana(int amount) {
        curMana -= amount;
        if (curMana < 0) curMana = 0;
    }

    public int dealDamage(int amount, DamageType type) {
//        TODO
        curHealth -= amount;
        if (curHealth < 0) curHealth = 0;
        return amount;
    }
}
