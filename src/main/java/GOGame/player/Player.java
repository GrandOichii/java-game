package GOGame.player;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Player {
    private String name;
    private String className;
    public Player(String name, PlayerClass pClass) {
//        TODO
        this.name = name;
        this.className = pClass.getName();
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }
}
