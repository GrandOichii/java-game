package GOGame.terminal;

import GOGame.Utility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TileManager {
    private final HashMap<String, CCTMessage> tiles = new HashMap<>();

    public boolean has(String tileName) {
        return this.tiles.containsKey(tileName);
    }

    public CCTMessage get(String tileName) {
        return this.tiles.get(tileName);
    }

    public static TileManager load(File file) throws IOException {
        var result = new TileManager();
        var text = Utility.readFile(file);
        ObjectMapper mapper = new ObjectMapper();
        var data = mapper.readValue(text, new TypeReference<HashMap<String, HashMap<String, String>>>() {
        });

        var templates = data.get("templates");
        var actualTemplates = new HashMap<String, CCTMessage>();
        for (var key : templates.keySet()) {
            var cct = new CCTMessage(templates.get(key));
            actualTemplates.put(key, cct);
        }

        var tiles = data.get("tiles");
        for (var tile : tiles.keySet()) {
            var cct = actualTemplates.get(tiles.get(tile));
            result.tiles.put(tile, cct);
        }
        return result;
    }
}
