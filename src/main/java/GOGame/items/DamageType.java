package GOGame.items;

public enum DamageType {
    FIRE("FIRE"),
    PHYSICAL("PHYSICAL");
    private String title;

    DamageType(String title) {
        this.title = title;
    }
}
