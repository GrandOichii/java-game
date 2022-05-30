package GOGame.player;

import GOGame.GameInfo;
import GOGame.Utility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PlayerClass {
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("items")
    private HashMap<String, Integer> items;

    public HashMap<String, Integer> getItems() {
        return items;
    }

    @JsonProperty("attributes")
    private HashMap<Attribute, Integer> attributes;

    public HashMap<Attribute, Integer> getAttributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static PlayerClass[] loadClasses(File file) throws IOException {
        var text = Utility.readFile(file);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(text, new TypeReference<>() { });
    }
}
