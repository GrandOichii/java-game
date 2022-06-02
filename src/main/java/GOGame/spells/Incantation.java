package GOGame.spells;

import GOGame.Engine;
import GOGame.entities.Entity;

public abstract class Incantation {
    private final String title;
    private final String description;
    private final String actualTitle;
    protected Incantation(String title, String actualTitle, String description) {
        this.title = title;
        this.actualTitle = actualTitle;
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getActualTitle() {
        return actualTitle;
    }

    abstract public void editSpell(Spell spell, Engine game);

    @Override
    public String toString() {
        return getActualTitle();
    }
}
