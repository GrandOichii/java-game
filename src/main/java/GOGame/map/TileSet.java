package GOGame.map;

import GOGame.Utility;
import GOGame.scripting.Script;
import GOGame.scripting.ScriptOverseer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TileSet {
    private HashMap<String, Tile> tileMap;

    public Tile getTile(String ch) {
        return tileMap.get(ch);
    }

    public static TileSet load(String filePath, ScriptOverseer so) throws Exception {
        var result = new TileSet();
        var parentFolder = new File(filePath).getParent();
        var text = Utility.readFile(new File(filePath));
        ObjectMapper mapper = new ObjectMapper();
        result.tileMap = mapper.readValue(text, new TypeReference<HashMap<String, Tile>>() { });
        for (var roomChar : result.tileMap.keySet()) {
            var tile = result.tileMap.get(roomChar);
            tile.correctPaths(parentFolder);
            Script ss = null;
            Script is = null;
            if (!tile.getInteractScriptPath().equals("")) {
                is = so.loadScript(tile.getInteractScriptPath());
            }
            if (!tile.getStepScriptPath().equals("")) {
                ss = so.loadScript(tile.getStepScriptPath());
            }
            tile.setScripts(ss, is);
//            System.out.println(tile.getName() + " " + ss);
        }
        return result;
    }

    public Tile getTileByName(String tileName) {
        for (var t : tileMap.values()) {
            if (t.getName().equals(tileName)) {
                return t;
            }
        }
        return null;
    }
}
