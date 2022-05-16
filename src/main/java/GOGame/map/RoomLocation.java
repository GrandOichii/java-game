package GOGame.map;

public class RoomLocation {

    public RoomLocation(String roomName, int x, int y) {
        this.roomName = roomName;
        this.x = x;
        this.y = y;
    }
    private String roomName;
    private int x;
    private int y;

    public String getRoomName() {
        return roomName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
