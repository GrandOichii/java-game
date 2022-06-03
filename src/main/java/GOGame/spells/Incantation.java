package GOGame.spells;

import GOGame.Engine;
import GOGame.IDescribable;
import GOGame.entities.Entity;

import java.util.List;

public abstract class Incantation implements IDescribable {
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


    @Override
    public String getBigDescription(int amount) {
        return "Incantation: " + title + "\n\n" + description;
    }

    @Override
    public List<String> getAllowedActions() {
        return List.of("close");
    }
}
