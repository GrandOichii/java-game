package GOGame.map;

public class WTile {
    private final int x;
    private final int y;
    private final String name;

    public WTile(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }
}
