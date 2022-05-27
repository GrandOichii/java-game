package GOGame.tiles;

import GOGame.Pair;
import GOGame.Utility;
import GOGame.map.MapData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Random;

public class TileSet {
    private static final String IMAGES_MAP_FILE = "imagesmap.json";
    private final HashMap<String, Image> imageMap = new HashMap<>();

    public Image get(String key) {
        return imageMap.get(key);
    }

    public boolean containsKey(String key) { return imageMap.containsKey(key); }

    public static TileSet load(String path, int width, int height) throws IOException {
        var text = Utility.readFile(Path.of(path, IMAGES_MAP_FILE).toFile());
        ObjectMapper mapper = new ObjectMapper();
        var map = mapper.readValue(text, new TypeReference<HashMap<String, String>>() { });
        var result = new TileSet();
        for (var key : map.keySet()) {
            var imagePath = map.get(key);
            var file = Path.of(path, imagePath).toFile();
            try {
                var im = ImageIO.read(file);
                im = resizeImage(im, width, height);
                result.imageMap.put(key, im);
            }
            catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Can't read file " + file.getPath());
                throw new RuntimeException(ex);
            }
        }
        return result;
    }

    private static BufferedImage resizeImage(Image original, int targetWidth, int targetHeight) {
//        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
//        Graphics2D graphics2D = resizedImage.createGraphics();
//        graphics2D.drawImage(original, 0, 0, targetWidth, targetHeight, null);
//        graphics2D.dispose();
//        return resizedImage;
//
        Image tmp = original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage result = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return result;

    }

    private TileSet() {

    }
}
