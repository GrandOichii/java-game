package GOGame.map;

import GOGame.exceptions.ScriptException;
import GOGame.scripting.ScriptOverseer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static GOGame.Engine.DIRECTION_MAP;

public class GameMap {

    private HashMap<String, RoomLocation> warpMap;

    public HashMap<String, RoomLocation> getWarpMap() {
        return warpMap;
    }

    private HashMap<String, Room> roomMap;

    public HashMap<String, Room> getRoomMap() {
        return roomMap;
    }

    private int playerY;
    private int playerX;
    private Room currentRoom;

    public GameMap() {
//        TODO
    }

    public static GameMap load(String gamePath, File mapFile, ScriptOverseer so) throws Exception {
//        TODO
        var md = MapData.load(gamePath, mapFile);
        var result = new GameMap();
        result.warpMap = new HashMap<>();
        result.roomMap = new HashMap<>();
        var roomMap = md.getRooms();
        for (var roomName : roomMap.keySet()) {
            var room = Room.from(roomMap.get(roomName), so);
//            so.bindRoom(room);
            result.roomMap.put(roomName, room);
//            add warp codes
            var warps = room.getWarpCodes();
            for (var warpCode : warps.keySet()) {
                if (result.warpMap.containsKey(warpCode)) {
                    throw new IOException(String.format("warp code %s is declared twice in different rooms"));
                }
                var loc = warps.get(warpCode);
                result.warpMap.put(warpCode, loc);
            }
        }
        result.playerY = md.getStartY();
        result.playerX = md.getStartX();
        var irn = md.getIndexRoomName();
        if (!result.roomMap.containsKey(irn)) {
            throw new IOException(String.format("index room %s doesn't exist", irn));
        }
        result.currentRoom = result.roomMap.get(irn);
        return result;
    }

    public String extractStringData() {
        final var indent = "\t\t";
        var result = "";
        for (var roomName : this.roomMap.keySet()) {
            result += indent + "- " + roomName + "\n";
            var room = this.roomMap.get(roomName);
            result += indent + "\tHeight: " + room.getHeight() + "\n";
            result += indent + "\tWidth: " + room.getWidth() + "\n";
            result += indent + "\tBase visible range: " + room.getVisibleRange() + "\n";
        }
        return result;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getPlayerX() {
        return playerX;
    }

    public boolean movePlayer(String direction) {
        var dirs = DIRECTION_MAP.get(direction);
        var newX = playerX + dirs[0];
        var newY = playerY + dirs[1];
        var tile = getCurrentRoom().getTiles()[newY][newX];
        if (!tile.isPassable()) {
            return false;
        }
        playerX = newX;
        playerY = newY;
        return true;
    }

    public void travelTo(RoomLocation loc) throws ScriptException {
        var roomName = loc.getRoomName();
        if (!roomMap.containsKey(roomName)) {
            throw new ScriptException("no room with name " + roomName);
        }
        var room = roomMap.get(roomName);
        this.currentRoom = room;
        this.playerX = loc.getX();
        this.playerY = loc.getY();
    }

    public Tile getAt(int[] dirs) {
        var x = playerX + dirs[0];
        var y = playerY + dirs[1];
        return getCurrentRoom().getTiles()[y][x];
    }
}
