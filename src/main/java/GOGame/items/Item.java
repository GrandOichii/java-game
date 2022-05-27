package GOGame.items;

import GOGame.Utility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public abstract class Item {
    private final boolean stackable;

    public boolean isStackable() {
        return stackable;
    }

    @JsonProperty("name")
    protected String name;

    public Item(boolean stackable) {
        this.stackable = stackable;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("displayName")
    protected String displayName;

    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("description")
    protected String description;

    public String getDescription() {
        return description;
    }

    public static ItemManager loadItems(File file) throws IOException {
        var text = Utility.readFile(file);
        ObjectMapper mapper = new ObjectMapper();
        var result = mapper.readValue(text, new TypeReference<ItemManager>() {});
        result.setUp();
        return result;
    }
}

