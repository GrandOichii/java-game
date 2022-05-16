package GOGame.map;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.file.Path;

public class RoomData {
    private String name;

    @JsonProperty("loadScript")
    private String loadScriptPath = "";

    @JsonProperty("path")
    private String path;

    @JsonProperty("tileset")
    private String tileSetPath;
    public String getTileSetPath() { return this.tileSetPath; }

    @JsonProperty("visibleRange")
    private int visibleRange;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void correctPaths(String gamePath) {
        this.path = Path.of(gamePath, this.path).toString();
        this.tileSetPath = Path.of(gamePath, this.tileSetPath).toString();
        if (!this.loadScriptPath.equals("")) {
            this.loadScriptPath = Path.of(gamePath, this.loadScriptPath).toString();
        }
    }

    public int getVisibleRange() {
        return visibleRange;
    }

    public String getPath() {
        return this.path;
    }

    public String getLoadScriptPath() { return this.loadScriptPath; }
}
