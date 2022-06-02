package GOGame.items;

import GOGame.Engine;
import GOGame.exceptions.SpellException;

import java.util.List;

public abstract class UsableItem extends Item {
    public UsableItem(boolean stackable) {
        super(stackable);
    }

    abstract public boolean use(Engine game) throws SpellException;
    @Override
    public String getCategory() {
        return "usable";
    }

    @Override
    protected String additionalDescriptionInfo() {
        return "";
    }

    @Override
    public List<String> getAllowedActions() {
        var result = super.getAllowedActions();
        result.add(0, "use");
        return result;
    }
}
