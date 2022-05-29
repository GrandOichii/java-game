package GOGame.items;

public class ItemLine {
    private Item item;
    private int amount;

    public int getAmount() {
        return amount;
    }

    private String displayName;
    public Item getItem() {
        return item;
    }

    public ItemLine(Item item, String displayName) {
        this(item, displayName, -1);
    }

    public ItemLine(Item item, String displayName, int amount) {
        this.item = item;
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
