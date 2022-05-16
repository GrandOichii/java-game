package GOGame;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;

public class Utility {


    public static HashMap<String, File> getFilesFrom(String path, String[] fileNames) throws FileNotFoundException {
        var result = new HashMap<String, File>();
        for (int i = 0; i < fileNames.length; i++){
            var filePath = Path.of(path, fileNames[i]);
            var file = filePath.toFile();
            if (!file.exists()) {
                throw new FileNotFoundException(String.format("file %s doesn't exist", filePath));
            }
            result.put(file.getName(), file);
        }
        return result;
    }

    public static String readFile(File file) throws IOException {
        var input = new FileInputStream(file);
        var br = new BufferedReader(new InputStreamReader(input));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            builder.append(line + "\n");
        }
        input.close();
        return builder.toString();
    }

    public static void writeFile(File file, String text) {

    }

    public static String[] splitFile(File file) {
        var fileFullName = file.getName();
        var split = fileFullName.split("\\.");
        var length = split.length;
        var ext = split[length - 1];
        var fileName = String.join(".", Arrays.copyOfRange(split, 0, length - 1));

        return new String[]{file.getPath(), fileName, ext};
    }
}
