package GOGame.items;

public class BasicItem extends Item {

    public BasicItem() {
        super(false);
    }

    @Override
    public String getCategory() {
         return "Other";
    }
}
