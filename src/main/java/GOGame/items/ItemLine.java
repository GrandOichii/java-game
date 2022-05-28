package GOGame.items;

public class ItemLine {
    private final String name;
    private final String displayName;

    public String getName() {
        return name;
    }

    private final int amount;

    public ItemLine(String name, String displayName) {
        this(name, displayName, -1);
    }

    public ItemLine(String name, String displayName, int amount) {
        this.name = name;
        this.displayName = displayName;
        this.amount = amount;
    }

    @Override
    public String toString() {
        if (amount == -1) {
            return displayName;
        }
        return String.format("%s x %d", displayName, amount);
    }
}
