package GOGame.items;

import GOGame.Engine;
import GOGame.Utility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class ContainerManager {
    private final HashMap<String, Container> containerMap = new HashMap<>();
    public ContainerManager() {}

    public static ContainerManager load(Engine e, File file) throws IOException {
        var itemManager = e.getItemManager();
        var text = Utility.readFile(file);
        ObjectMapper mapper = new ObjectMapper();
        var result = new ContainerManager();
        var m = mapper.readValue(text, new TypeReference<HashMap<String, HashMap<String, Integer>>>() {});
        for (var containerKey : m.keySet()) {
            var container = new Container();
            var itemMap = m.get(containerKey);
            for (var itemName : itemMap.keySet()) {
                var item = itemManager.get(itemName);
                container.add(item, itemMap.get(itemName));
            }
            result.containerMap.put(containerKey, container);
        }
        return result;
    }

    public Container get(String key) {
        return containerMap.get(key);
    }
}
