package GOGame.player;

import GOGame.GameInfo;
import GOGame.Utility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class PlayerClass {
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static PlayerClass[] loadClasses(File file) throws IOException {
        var text = Utility.readFile(file);
        ObjectMapper mapper = new ObjectMapper();
        var result = mapper.readValue(text, new TypeReference<PlayerClass[]>() { });
        return result;
    }
}
