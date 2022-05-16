package GOGame.terminal;

import GOGame.Engine;
import GOGame.Utility;
import GOGame.exceptions.ScriptException;
import GOGame.map.WTile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TerminalWindow implements GOGame.GameWindow {
//    CCT labels
    private static final CCTMessage LOG_WINDOW_LABEL = new CCTMessage("${default-red}Logs");
    private static final CCTMessage INFO_WINDOW_LABEL = new CCTMessage("${default-cyan}Player info");
    private static final CCTMessage PLAYER_NAME_LABEL = new CCTMessage("Name:");
    private static final CCTMessage PLAYER_CLASS_LABEL = new CCTMessage("Class:");
    private static final String TILE_CHAR_MAP_FILE = "charmap.json";
    private int tileWindowWidth;
    private int tileWindowHeight;
    private static final int TILE_WINDOW_OFFSET_Y = 1;
    private static final int TILE_WINDOW_OFFSET_X = 1;
    private int logWindowWidth;
    private int logWindowHeight;
    private int infoWindowWidth;
    private int infoWindowHeight;
    private int windowHeight;
    private int windowWidth;

    private Engine game;
    private MultiWindowTextGUI textGUI;
    private TerminalScreen screen;
    private SwingTerminalFrame terminal;
    private TextGraphics graphics;
    private boolean running;
    private boolean gameRunning;
    private final HashMap<String, CCTMessage> tileCCTMap = new HashMap<>();
    private final static CCTMessage PLAYER_CCT = new CCTMessage("${green}@");
    private boolean selectLogList;

    public TerminalWindow() {

    }

    @Override
    public void start(Engine engine, String assetsPath) throws IOException {
        this.loadAssets(assetsPath);
        this.game = engine;
        this.terminal = new DefaultTerminalFactory().createSwingTerminal();
        this.terminal.setResizable(false);
        this.graphics = terminal.newTextGraphics();

        this.terminal.setExtendedState(Frame.MAXIMIZED_BOTH);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();

//        set up bounds for windows
        windowWidth = bounds.width / 20;
        windowHeight = bounds.height / 19;
        tileWindowHeight = windowHeight * 3 / 5;
        tileWindowHeight += (4 - (tileWindowHeight -1)%4);
        tileWindowWidth = windowWidth * 2 / 3;
        tileWindowWidth += (4 - (tileWindowWidth -1)%4);
        logWindowHeight = windowHeight - 3 - tileWindowHeight;
        logWindowWidth = tileWindowWidth + 1;
        infoWindowHeight = windowHeight - 1;
        infoWindowWidth = windowWidth - tileWindowWidth - 1;
        logList = new ListTemplate(new ArrayList<>(), logWindowHeight - 2);
        this.game.setWindowSize(tileWindowWidth, tileWindowHeight);

        terminal.setCursorVisible(false);
        terminal.setVisible(true);

        this.running = true;
        while (this.running) {
//        init the ui
            var w = this.createWindow();
//        start the window
            this.startWindow(w);
        }
        this.terminal.close();
    }

    @Override
    public void sleep(Integer amount) {
        this.draw();
        terminal.flush();
        try {
            Thread.sleep(amount);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private final static HashMap<String, MessageDialogButton> BUTTON_MAP = new HashMap<>(){{
        put("Ok", MessageDialogButton.OK);
        put("Cancel", MessageDialogButton.Cancel);
        put("Yes", MessageDialogButton.Yes);
        put("No", MessageDialogButton.No);
    }};

    @Override
    public String requestChoice(String text, String[] choices) {
        if (choices.length == 0) {
            choices = new String[]{"Ok"};
        }
        var builder = new MessageDialogBuilder()
                .setTitle("")
                .setText(text);
        for (var choice : choices) {
            if (!BUTTON_MAP.containsKey(choice)) {
                throw new RuntimeException("can't parse button " + choice);
            }
            builder.addButton(BUTTON_MAP.get(choice));
        }
        var dialog = builder.build();
        var result = dialog.showDialog(textGUI);
//        dialog.close();
        this.terminal.setCursorVisible(false);
//        the terminal starts with SGR.BOLD turned on by default
        this.terminal.enableSGR(SGR.BOLD);
        for (var key : BUTTON_MAP.keySet()) {
            if (BUTTON_MAP.get(key) == result) {
                return key;
            }
        }
        return "";
    }

    private CCTMessage[] logToCCTs(String message) {
//        TODO
        return new CCTMessage[]{new CCTMessage("- ${white-black}" + message)};
    }

    private ListTemplate logList;

    @Override
    public void onLogUpdate(String message) {
        var lines = logToCCTs(message);
        for (var line : lines) {
            logList.addItem(line);
            logList.scrollDown();
            this.selectLogList = true;
        }
    }

    private void loadAssets(String assetsPath) throws IOException {
        var text = Utility.readFile(Path.of(assetsPath, TILE_CHAR_MAP_FILE).toFile());
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> tileCharMap = mapper.readValue(text, new TypeReference<>() {
        });
        for (var key : tileCharMap.keySet()) {
            tileCCTMap.put(key, new CCTMessage(tileCharMap.get(key)));
        }
    }
    
    private Window createWindow() {
        try {
            this.screen = new TerminalScreen(terminal);
            this.screen.startScreen();

        } catch (IOException e) {
            this.close(e);
        }
        this.textGUI = new MultiWindowTextGUI(this.screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.RED));

//        new character panel
        var createCharacterPanel = new Panel();
        createCharacterPanel.setLayoutManager(new GridLayout(2));
        final var nameEdit = new TextBox();
        nameEdit.setPreferredSize(new TerminalSize(15, 1));

        var window = new BasicWindow();

//        main panel
        var mainPanel = new Panel();

//        back to main menu
        var backToMainButton = new Button("Back", () -> {
            window.setComponent(mainPanel);
            nameEdit.setText("");
        });

//        new game button
        var newGameButton = new Button("New game", () -> {
            window.setComponent(createCharacterPanel);
        });
        mainPanel.addComponent(newGameButton);

//        continue game button
        var continueButton = new Button("Continue", () -> {
//            TODO
            System.out.println("continue button closed");
            try {
                var saves = this.game.listSaveFiles();
                for (int i = 0; i < saves.size(); i++) {
                    TerminalUtility.putAt(this.terminal, 1, 1 + i, saves.get(i), "red");
                }
                this.terminal.flush();
            } catch (IOException e) {
                this.close(e);
            }
        });
        mainPanel.addComponent(continueButton);

//        settings button
        var settingsButton = new Button("Settings", () -> {
            System.out.println("settings button closed");
        });
        mainPanel.addComponent(settingsButton);

//        exit button
        var exitButton = new Button("Exit", () -> {
            this.running = false;
            window.close();
        });
        mainPanel.addComponent(exitButton);

//        name label and text box
        createCharacterPanel.addComponent(new Label("Name:"));
        createCharacterPanel.addComponent(nameEdit);

//        class label and
        createCharacterPanel.addComponent(new Label("Class:"));
        final ComboBox<String> classBox = new ComboBox<>();
        var classes = this.game.getClassTemplates();
        for (int i = classes.length - 1; i >= 0; i--) {
            classBox.addItem(classes[i].getName());
        }
        createCharacterPanel.addComponent(classBox);

//        back to main menu
        createCharacterPanel.addComponent(backToMainButton);
//        create button
        createCharacterPanel.addComponent(new Button("Create", () -> {
//            check the correctness
            var name = nameEdit.getText();
            if (name.equals("")) {
//                TODO
                return;
            }
            var index = classBox.getSelectedIndex();
            var pClass = classes[index];
            try {
                this.game.createAndLoadCharacter(name, pClass);
            }
            catch (Exception ex) {
//                TODO
                return;
            }
            this.startGame();
            window.close();
        }));

        window.setComponent(mainPanel);

        window.setTitle(this.game.getGameInfo().getName());
        return window;
    }
    
    private void startWindow(Window w) {
        this.textGUI.addWindowAndWait(w);
        this.textGUI.waitForWindowToClose(w);
    }

    private void close(Exception e) {
        terminal.close();
        throw new RuntimeException(e);
    }

    private void startGame() {
//        prepare the screen
        this.terminal.setCursorVisible(false);

        this.gameRunning = true;
        while (gameRunning) {
//            draw
            this.terminal.clearScreen();
            this.draw();
            this.terminal.flush();

//            handle input
            var key = this.terminal.readInput();
            var update = this.handleInput(key);
            if (update) {
                try {
                    this.game.update();
                } catch (ScriptException e) {
                    this.close(e);
                    return;
                }
            }
        }

//        clear the screen
        this.terminal.clearScreen();
        this.terminal.flush();
    }

    private static final KeyType ARROW_UP = KeyType.ArrowUp;
    private static final KeyType ARROW_DOWN = KeyType.ArrowDown;
    private static final KeyType ARROW_LEFT = KeyType.ArrowLeft;
    private static final KeyType ARROW_RIGHT = KeyType.ArrowRight;
    private static final HashMap<KeyType, String> ARROW_KEY_MAP = new HashMap<>() {{
        put(ARROW_DOWN, "ArrowDown");
        put(ARROW_UP, "ArrowUp");
        put(ARROW_LEFT, "ArrowLeft");
        put(ARROW_RIGHT, "ArrowRight");
    }};
    private static final HashMap<String, String> DIRECTION_MAP = new HashMap<>() {{
        put("ArrowUp", "N");
        put("ArrowDown", "S");
        put("ArrowLeft", "W");
        put("ArrowRight", "E");
        put("y", "NW");
        put("b", "SW");
        put("u", "NE");
        put("n", "SE");
    }};

    private static String stringKey(KeyStroke key) {
        Character ch = key.getCharacter();
        if (ch != null) {
            return ch.toString();
        }
        var kt = key.getKeyType();
        if (ARROW_KEY_MAP.containsKey(kt)) {
            return ARROW_KEY_MAP.get(kt);
        }
        return null;
    }

    private static final String OPEN_INVENTORY_KEY = "i";
    private static final String INTERACT_KEY = "e";

    private boolean handleInput(KeyStroke key) {
        var k = stringKey(key);
        if (k == null) {
            return false;
        }
//        check if is movement key
        if (DIRECTION_MAP.containsKey(k)) {
            var moved = this.game.movePlayer(DIRECTION_MAP.get(k));
            return moved;
        }
        switch (k) {
            case OPEN_INVENTORY_KEY:
                this.openInventory();
                return false;
            case INTERACT_KEY:
                this.interact();
                return false;
            case "q":
//                quit (DEBUG)
                gameRunning = false;
                return false;
        }
        return false;
    }

    private void draw() {
        this.clearScreen();

//        graphics.setBackgroundColor(TextColor.ANSI.GREEN);
//        graphics.drawRectangle(new TerminalPosition(0, 0), new TerminalSize(windowWidth * 2, windowHeight), '*');
//        graphics.setBackgroundColor(TextColor.ANSI.DEFAULT);

        this.drawTileWindow();
        this.drawLogWindow();
        this.drawInfoWindow();
        this.drawPlayer();
    }

    private void drawInfoWindow() {
        var y = TILE_WINDOW_OFFSET_Y - 1;
        var x = 2 * tileWindowWidth + 2;
        this.graphics.setForegroundColor(TextColor.ANSI.CYAN);
        this.graphics.drawRectangle(new TerminalPosition(x, y), new TerminalSize(infoWindowWidth * 2, infoWindowHeight), '*');
        this.graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
        INFO_WINDOW_LABEL.draw(terminal, x + 3, y);
        PLAYER_NAME_LABEL.draw(terminal, x + 3, y + 2);
        TerminalUtility.putAt(terminal, x + 10, y + 2, game.getPlayer().getName(), "green");
        PLAYER_CLASS_LABEL.draw(terminal, x + 3, y + 3);
        TerminalUtility.putAt(terminal, x + 10, y + 3, game.getPlayer().getClassName(), "cyan");
    }

    private void drawPlayer() {
        PLAYER_CCT.draw(terminal, TILE_WINDOW_OFFSET_X + game.getCenterX() * 2, TILE_WINDOW_OFFSET_Y + game.getCenterY());
    }

    private void clearScreen() {
        terminal.clearScreen();
    }

    private void drawTileWindow() {

        var x = TILE_WINDOW_OFFSET_X - 1;
        var y = TILE_WINDOW_OFFSET_Y - 1;
        this.graphics.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
        this.graphics.drawRectangle( new TerminalPosition(x, y), new TerminalSize(2 * tileWindowWidth + 2, tileWindowHeight + 2), '*');
        this.graphics.setForegroundColor(TextColor.ANSI.DEFAULT);

        var visibleTiles = this.game.getVisibleTiles();
        this.drawTiles(visibleTiles, false);
    }

    private void drawLogWindow() {
        var y = tileWindowHeight + 2;
        var x = TILE_WINDOW_OFFSET_X - 1;
        this.graphics.setForegroundColor(TextColor.ANSI.RED);
        this.graphics.drawRectangle(new TerminalPosition(x, y), new TerminalSize(logWindowWidth * 2, logWindowHeight), '*');
        this.graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
        LOG_WINDOW_LABEL.draw(terminal, x + 1, y);
        logList.draw(terminal, x + 1, y + 1, this.selectLogList);
        this.selectLogList = false;
    }

    private void drawTiles(List<WTile> tiles, boolean reverseColor) {
        var roomName = this.game.getMap().getCurrentRoom().getName();
        for (var tile : tiles) {
            var tileName = roomName + ":" + tile.getName();
            if (!tileCCTMap.containsKey(tileName)) {
                TerminalUtility.putAt(terminal, tile.getX(), tile.getY(), "?", "black-white");
                continue;
            }
            var cct = tileCCTMap.get(tileName);
            var y = TILE_WINDOW_OFFSET_Y + tile.getY();
            var x = TILE_WINDOW_OFFSET_X + 2 * tile.getX();
            cct.drawLine(terminal, x, y, reverseColor);
        }
    }

    private void openInventory() {
//        TODO
    }

    private void interact() {
        var tiles = this.game.getAdjacentInteractableTiles();
        if (tiles.size() == 0) {
//            TODO
            return;
        }
        this.drawTiles(tiles, true);
        this.drawPrompt("Interact where?");
        terminal.flush();
        var key = terminal.readInput();
        var k = stringKey(key);
        if (k == null) {
            return;
        }
//        check if is movement key
        if (DIRECTION_MAP.containsKey(k)) {
            try {
                this.game.interactAt(DIRECTION_MAP.get(k));
            } catch (ScriptException e) {
                this.close(e);
                return;
            }
        }
    }

    private void drawPrompt(String prompt) {
        var cct = new CCTMessage("[ ${cyan}" + prompt + "${normal} ]");
        var x = tileWindowWidth - cct.length() / 2;
        cct.draw(terminal, TILE_WINDOW_OFFSET_X + x, TILE_WINDOW_OFFSET_Y);
    }
}
