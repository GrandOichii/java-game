package GOGame.map;

import GOGame.Utility;
import GOGame.exceptions.ScriptException;
import GOGame.scripting.Script;
import GOGame.scripting.ScriptOverseer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Room {
    private String name;
    private int height;
    public int getHeight() { return this.height; }
    private int width;
    public int getWidth() { return this.width; }
    private int visibleRange;
    private Script loadScript;
    private TileSet tileset;
    private Tile[][] tiles;
    private HashMap<String, RoomLocation> warpCodes;

    public HashMap<String, RoomLocation> getWarpCodes() { return this.warpCodes; }

    public static Room from(RoomData rd, ScriptOverseer so) throws Exception {
        var result = new Room();
        result.name = rd.getName();
        result.visibleRange = rd.getVisibleRange();
        result.warpCodes = new HashMap<>();
        result.tileset = TileSet.load(rd.getTileSetPath(), so);
        var text = Utility.readFile(new File(rd.getPath()));
        var lines = text.split("\n");
        result.height = lines.length;
        result.width = lines[0].length();
        result.tiles = new Tile[result.height][];
        var tileSet = result.getTileSet();
        for (int i = 0; i < result.height; i++) {
            result.tiles[i] = new Tile[result.width];
            var line = lines[i].split("");
            for (int ii = 0; ii < result.width; ii++) {
                var ch = line[ii];
                var tile = tileSet.getTile(ch);
                result.tiles[i][ii] = tile;
                var warpCode = tile.getWarpCode();
                if (!warpCode.equals("")) {
                    if (result.warpCodes.containsKey(warpCode)) {
                        throw new IOException(String.format("warp code %s repeated twice", warpCode));
                    }
                    result.warpCodes.put(warpCode, new RoomLocation(rd.getName(), ii, i));
                }
            }
        }
        if (!rd.getLoadScriptPath().equals("")) {
            var p = rd.getLoadScriptPath();
            result.loadScript = so.loadScript(p);
        }
        return result;
    }

    public TileSet getTileSet() {
        return tileset;
    }

    public int getVisibleRange() {
        return visibleRange;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public String getName() {
        return name;
    }

    public boolean hasLoadScript() { return this.loadScript != null; }

    public void executeLoadScript(ScriptOverseer so) throws ScriptException {
        this.loadScript.exec();
    }
}
