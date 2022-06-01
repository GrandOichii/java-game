package GOGame.items;

public enum EquipSlot {
    HEAD("HEAD"),
    TORSO("TORSO"),
    LEGS("LEGS"),
    ARM("ARM"),
    ARMS("ARMS");
    private String title;

    EquipSlot(String title) {
        this.title = title;
    }
}
