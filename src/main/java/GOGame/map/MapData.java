package GOGame.map;

import GOGame.Utility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MapData {

    @JsonProperty("index")
    private String indexRoomName;

    @JsonProperty("startX")
    private int startX;

    @JsonProperty("startY")
    private int startY;

    @JsonProperty("rooms")
    private HashMap<String, RoomData> rooms;

    public HashMap<String, RoomData> getRooms() { return this.rooms; }

    public MapData() {

    }

    public static MapData load(String gamePath, File file) throws IOException {
        var text = Utility.readFile(file);
        ObjectMapper mapper = new ObjectMapper();
        var result = mapper.readValue(text, new TypeReference<MapData>() { });
        for (var roomName : result.rooms.keySet()) {
            var room = result.rooms.get(roomName);
            room.setName(roomName);
            room.correctPaths(gamePath);
        }
        return result;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public String getIndexRoomName() {
        return indexRoomName;
    }
}
