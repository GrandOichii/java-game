package GOGame.map;

import GOGame.scripting.Script;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.file.Path;

public class Tile {
    @JsonProperty("passable")
    private boolean passable;
    @JsonProperty("transparent")
    private boolean transparent;
    @JsonProperty("name")
    private String name;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("warpcode")
    private String warpCode;
    @JsonProperty("interactScript")
    private String interactScriptPath;
    @JsonProperty("stepScript")
    private String stepScriptPath;

    public void correctPaths(String gamePath) {
        if (!this.interactScriptPath.equals("")) {
            this.interactScriptPath = Path.of(gamePath, this.interactScriptPath).toString();
        }
        if (!this.stepScriptPath.equals("")) {
            this.stepScriptPath = Path.of(gamePath, this.stepScriptPath).toString();
        }
    }

    Tile() {
        this.interactScriptPath = "";
        this.stepScriptPath = "";
        this.warpCode = "";
    }

    private Script ss; // Step script
    public boolean hasStepScript() { return ss != null; }
    private Script is; // Interact script
    public boolean hasInteractScript() { return is != null; }

    public String getName() {
        return name;
    }

    public String getInteractScriptPath() {
        return interactScriptPath;
    }

    public String getStepScriptPath() {
        return stepScriptPath;
    }

    public void setScripts(Script ss, Script is) {
        this.ss = ss;
        this.is = is;
    }

    public String getWarpCode() {
        return warpCode;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public boolean isPassable() {
        return passable;
    }

    public Script getIS() {
        return this.is;
    }

    public Script getSS() {
        return this.ss;
    }
}
