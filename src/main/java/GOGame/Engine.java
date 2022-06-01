package GOGame;

import GOGame.exceptions.ScriptException;
import GOGame.items.ContainerManager;
import GOGame.items.Item;
import GOGame.items.ItemManager;
import GOGame.map.GameMap;
import GOGame.map.RoomLocation;
import GOGame.map.WTile;
import GOGame.player.Player;
import GOGame.player.PlayerClass;
import GOGame.scripting.ScriptOverseer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Engine {
    private static final int[][] ALL_DIRS = new int[][]{
            {0, -1},
            {0, 1},
            {-1, 0},
            {1, 0},
            {-1, -1},
            {1, 1},
            {1, -1},
            {-1, 1},
    };
    public static final HashMap<String, int[]> DIRECTION_MAP = new HashMap<>(){{
        put("N", new int[]{0, -1});
        put("S", new int[]{0, 1});
        put("W", new int[]{-1, 0});
        put("E", new int[]{1, 0});
        put("NW", new int[]{-1, -1});
        put("NE", new int[]{1, -1});
        put("SW", new int[]{-1, 1});
        put("SE", new int[]{1, 1});
    }};
    private static final String SAVE_FILE_FORMAT = "json";
    private static final String MAP_DATA_FILE = "mapdata.json"; // Name of the file where the map data should be stored
    private static final String CLASSES_FILE = "classes.json";    // Name of the file where all the class templates are described
    private static final String GAME_INFO_FILE = "game_info.json";  // Name of the file where all the game info is described
    private static final String ITEMS_FILE = "items.json";      // Name of the file where all the items are described
    private static final String CONTAINERS_FILE = "containers.json"; // Name of the file where all containers are described
    private static final String ENEMIES_FILES = "enemies.json"; // Name of the file where all enemies are described
    private static final String SAVES_DIR = "saves"; // Directory of the saves folder
    private static final int RAY_AMOUNT = 200;
    private static final Double[] RAY_RADS = new ArrayList<Double>(){{
        for (int i = 0; i < RAY_AMOUNT; i++) {
            this.add(2 * Math.PI * i / RAY_AMOUNT);
        }
    }}.toArray(new Double[RAY_AMOUNT]);
    private static final String[] REQUIRED_GAME_FILES = {
            MAP_DATA_FILE,
            CLASSES_FILE,
            GAME_INFO_FILE,
            ITEMS_FILE,
            CONTAINERS_FILE,
//            ENEMIES_FILE
    };
    private final GameMap map;

    public GameMap getMap() {
        return map;
    }

    private final GameInfo gi;
    private final List<String> logMessages = new ArrayList<>();
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public GameInfo getGameInfo() { return this.gi; }
    private final PlayerClass[] classTemplates;
    private final ItemManager itemManager;

    public ItemManager getItemManager() {
        return itemManager;
    }

    private final ContainerManager containerManager;

    public ContainerManager getContainerManager() {
        return containerManager;
    }

    public boolean isContainerLooted(String containerKey) {
        return this.so.isContainerLooted(containerKey);
    }

    private final String path;
    private IGameWindow w;

    private ScriptOverseer so;

    public ScriptOverseer getSO() {
        return so;
    }

    public void setGameWindow(IGameWindow w) { this.w = w; }
    public IGameWindow getGameWindow() { return this.w; }

    public Engine(String path) throws Exception {
        this.path = path;
//        check for required files
        var fileMap = Utility.getFilesFrom(path, REQUIRED_GAME_FILES);
//        create the script overseer
        this.so = new ScriptOverseer(this);
//        load the map
        this.map = GameMap.load(path, fileMap.get(MAP_DATA_FILE), this.so);
//        load the class templates
        this.classTemplates = PlayerClass.loadClasses(fileMap.get(CLASSES_FILE));
//        load the game info
        this.gi = GameInfo.load(fileMap.get(GAME_INFO_FILE));
//        load the items
        this.itemManager = Item.loadItems(fileMap.get(ITEMS_FILE));
//        load the containers
        this.containerManager = ContainerManager.load(this, fileMap.get(CONTAINERS_FILE));
    }

    public Path getSavesDir() {
        return Path.of(this.path, SAVES_DIR);
    }

    public List<String> listSaveFiles() throws IOException {
        var result = new ArrayList<String>();
        var savesDir = getSavesDir();
        if (!savesDir.toFile().exists()) {
            return result;
        }
        Files.list(savesDir).forEach( dir -> {
            var file = dir.toFile();
            if (file.isFile()) {
                var split =  Utility.splitFile(file);
                if (split[2].equals(SAVE_FILE_FORMAT)) {
                    result.add(split[1]);
                }
            }
        });
        return result;
    }

    public PlayerClass[] getClassTemplates() { return this.classTemplates; }

    public void createAndLoadCharacter(String name, PlayerClass pClass) {
        this.player = new Player(this, name, pClass);
//        REMOVE
        so.injectPlayerData(this.player);
        this.save();
        var saveFile = getCurrentSaveFile();
        loadCharacter(saveFile);
    }

    public void loadCharacter(File characterFile) {
//        TODO
    }

    public void save() {
//        TODO
    }

    public File getCurrentSaveFile() {
        var fileName = this.player.getName() + "." + SAVE_FILE_FORMAT;
        return Path.of(this.getSavesDir().toString(), fileName).toFile();
    }

    public void update() throws ScriptException {
        var map = this.getMap();
        var pY = map.getPlayerY();
        var pX = map.getPlayerX();
        var tile = map.getCurrentRoom().getTiles()[pY][pX];
        if (tile.hasStepScript()) {
            tile.getSS().exec();
        }
    }

    public boolean movePlayer(String direction) throws ScriptException {
        var moved = this.map.movePlayer(direction);
        return moved;
//        if (!moved) {
//            return false;
//        }
//        var tile = map.getCurrentRoom().getTiles()[map.getPlayerX()][map.getPlayerX()];
////        if (tile.hasStepScript()) {
////            tile.getSS().exec(so);
////        }
//        return false;
    }

    private int windowWidth = -1;
    private int windowHeight = -1;
    private int centerY = -1;

    public int getCenterY() {
        return centerY;
    }
    private int centerX = -1;

    public int getCenterX() {
        return centerX;
    }

    public void setWindowSize(int width, int height) {
        this.windowWidth = width;
        this.windowHeight = height;
        this.centerX = width / 2;
        this.centerY = height / 2;
    }

    public List<WTile> getVisibleTiles() {
        var y = this.map.getPlayerY();
        var x = this.map.getPlayerX();
        var room = this.map.getCurrentRoom();
        var height = room.getHeight();
        var width = room.getWidth();
        var tiles = room.getTiles();
        var visibleRange = (float)room.getVisibleRange();
        var result = new ArrayList<WTile>();
        for (int i = 0; i < RAY_AMOUNT; i++) {
            var r = RAY_RADS[i];
            for (float n = 0; n < visibleRange; n++) {
                var newY = this.centerY + (int)(Math.sin(r) * n);
                var newX = this.centerX + (int)(Math.cos(r) * n);
                if (newY > windowHeight || newX > windowWidth){
                    break;
                }

                var mi = newY - centerY + y;
                var mj = newX - centerX + x;

                var tileName = "unseen";
                var transparent = true;
                if (newY < 0 || newX < 0) {
                    continue;
                }
                if (mi >= 0 && mj >= 0 && mi < height && mj < width) {
                    var tile = tiles[mi][mj];
                    tileName = tile.getName();
                    transparent = tile.isTransparent();
                }
                result.add(new WTile(newX, newY, tileName));
                if (!transparent) {
                    break;
                }
            }
        }
        return result;
    }

    public List<WTile> getAdjacentInteractableTiles() {
        var result = new ArrayList<WTile>();
        var map = this.getMap();
        var room = map.getCurrentRoom();
        var tiles = room.getTiles();
        for (var dir : ALL_DIRS) {
            var x = dir[0] + map.getPlayerX();
            var y = dir[1] + map.getPlayerY();
            if (y < 0 || x < 0 || y >= room.getHeight() || x >= room.getWidth()) {
                continue;
            }
            var tile = tiles[y][x];
            if (tile.hasInteractScript()) {
                result.add(new WTile(dir[0] + centerX, dir[1] + centerY, tile.getName()));
            }
        }
        return result;
    }

    public void travelTo(RoomLocation loc) throws ScriptException {
        var prevRoom = map.getCurrentRoom();
        map.travelTo(loc);
        var room = map.getCurrentRoom();
        if (prevRoom != room && room.hasLoadScript()) {
            room.executeLoadScript(so);
        }
    }

    public void interactAt(String direction) throws ScriptException {
        var dirs = DIRECTION_MAP.get(direction);
        var tile = map.getAt(dirs);
        if (tile.hasInteractScript()) {
            tile.getIS().exec();
        }
    }

    public void addToLog(String message) {
        logMessages.add(message);
        this.w.onLogUpdate(message);
    }

    public void start() throws ScriptException {
        var room = this.map.getCurrentRoom();
        if (room.hasLoadScript()) {
            room.executeLoadScript(so);
        }
    }

    public void openContainer(String containerName, String containerTop) throws IOException {
        this.w.openContainer(containerName, containerTop);
    }

    public void lootContainer(String containerName) {
        this.so.lootContainer(containerName);
        var container = containerManager.get(containerName);
        player.addItemsFromContainer(container);
    }

    public void updatePlayerLook() {
        this.w.updatePlayerLook();
    }
}
