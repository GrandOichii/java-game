package GOGame;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class GameInfo {

    @JsonProperty("name")
    private String name;
    public String getName() { return this.name; }

    public static GameInfo load(File file) throws IOException {
        var text = Utility.readFile(file);
        ObjectMapper mapper = new ObjectMapper();
        var result = mapper.readValue(text, new TypeReference<GameInfo>() { });
        return result;
    }
}
