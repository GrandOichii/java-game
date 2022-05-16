package GOGame.tiles;

import GOGame.Utility;
import GOGame.map.MapData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class TileSet {
    private static final String IMAGES_MAP_FILE = "imagesmap.json";
    private HashMap<String, Image> imageMap = new HashMap<>();

    public HashMap<String, Image> getImageMap() {
        return imageMap;
    }

    public static TileSet load(String path) throws IOException {
        var text = Utility.readFile(Path.of(path, IMAGES_MAP_FILE).toFile());
        ObjectMapper mapper = new ObjectMapper();
        var map = mapper.readValue(text, new TypeReference<HashMap<String, String>>() { });
        var result = new TileSet();
        for (var key : map.keySet()) {
            var im = ImageIO.read(Path.of(path, map.get(key)).toFile());
            result.imageMap.put(key, im);
        }
        return result;
    }

    private TileSet() {

    }
}
