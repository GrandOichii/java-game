package GOGame.terminal;

import GOGame.Engine;
import GOGame.IGameWindow;
import GOGame.exceptions.ScriptException;
import GOGame.exceptions.SpellException;
import GOGame.map.WTile;
import GOGame.scripting.Script;
import GOGame.spells.Spell;
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
import java.util.*;
import java.util.List;

public class TerminalWindow implements IGameWindow {
//    CCT labels
    private static final CCTMessage LOG_WINDOW_LABEL = new CCTMessage("${white-red}Logs");
    private static final CCTMessage INFO_WINDOW_LABEL = new CCTMessage("${white-cyan}Player info");
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
    private TileManager tileManager;
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
        return new CCTMessage[]{new CCTMessage("- ${white-black}" + message)};
    }

    private ListTemplate logList;

    @Override
    public void onLogUpdate(String message) {
        var lines = logToCCTs(message);
        for (var line : lines) {
            logList.addItem(line);
            logList.moveDown();
            this.selectLogList = true;
        }
        this.drawLogWindow(false);
//        this.terminal.flush();
    }

    @Override
    public void openContainer(String containerName, String containerTop) throws IOException {
        if (game.isContainerLooted(containerName)) {
            this.requestChoice(containerTop + " is empty", new String[]{"Ok"});
            return;
        }
        var w = new ContainerWindow(terminal, graphics, game, containerName, containerTop);
        w.show();
        if (w.lootConfirmed()) {
            game.lootContainer(containerName);
        }
    }

    @Override
    public void updatePlayerLook() {
//        TODO
        var player = game.getPlayer();
        var equipment = player.getEquipmentMap();
        for (var key : equipment.keySet()) {
            var item = equipment.get(key);
            var second = "none";
            if (item != null) {
                second = item.getDisplayName();
            }
        }
    }

    private void loadAssets(String assetsPath) throws IOException {
        this.tileManager = TileManager.load(Path.of(assetsPath, TILE_CHAR_MAP_FILE).toFile());
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
        for (var pClass : classes) {
            classBox.addItem(pClass.getName());
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
                this.startGame();
            }
            catch (Exception ex) {
//                TODO
                throw new RuntimeException(ex);
            }
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

    private void startGame() throws ScriptException, IOException {
//        prepare the screen
        this.terminal.setCursorVisible(false);

        this.gameRunning = true;
        try {
            this.game.start();
        } catch (ScriptException e) {
            this.close(e);
            return;
        }

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
    private static final String COMMANDS_KEY = "~";
    private static final String CAST_SPELL_KEY = "c";

    private boolean handleInput(KeyStroke key) throws ScriptException, IOException {
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
            case COMMANDS_KEY:
                this.enterConsoleCommandMode();
                return false;
            case CAST_SPELL_KEY:
                var cast = this.castMode();
                return cast;
            case "q":
//                quit (DEBUG)
                gameRunning = false;
                return false;
        }
        return false;
    }

    private boolean castMode() throws IOException {
        var x = 1;
        var y = tileWindowHeight;
        var label = "Enter the spell: ";
        TerminalUtility.putAt(terminal, x, y, label, "cyan");
        x += label.length();
        var edit = new LineEdit("", tileWindowWidth - label.length() - 2);
        while (true) {
            edit.draw(terminal, x, y);
            terminal.flush();
            var key = terminal.readInput();
            if (key.getKeyType() == KeyType.Escape) return false;
            if (key.getKeyType() == KeyType.Enter) break;
            edit.handleInput(key);
        }
        var line = edit.getText();
        try {
            var spell = new Spell(line, game);
            game.getPlayer().castSpell(spell);
            return true;
        } catch (SpellException e) {
            e.printStackTrace();
            game.addToLog(String.format("You say %s, but nothing happens.", line));
        }
        return false;
//        var edit = new LineEdit("", 20);
//        if (line.startsWith("$")) {
//            try {
//                var spell = new Spell(line.substring(1));
//                System.out.println(spell);
//            } catch (SpellException e) {
//                throw new RuntimeException(e);
//            }
//            continue;
//        }
    }

    private void enterConsoleCommandMode() throws IOException {
        final int wHeight = infoWindowHeight / 2;
        final int wWidth = infoWindowWidth;
        final var y = wHeight + 1;
        final var x = 2 * tileWindowWidth + 2;
        final var commandY = infoWindowHeight - 2;
        final var commandX = x + 3;

//        var line = new ArrayList<String>();
//        int cursor = 0;
        final int maxLength = wWidth * 2 - 5;
        var edit = new LineEdit("", maxLength);
        final var prevLines = new ArrayList<String>();
        final var maxPrevSize = wHeight - 3;

        while (true) {
//            draw
            graphics.drawRectangle(new TerminalPosition(x, y), new TerminalSize(wWidth * 2, wHeight), '*');
            TerminalUtility.putAt(terminal, x + 1, y, "Command window y:" + game.getMap().getPlayerY() + " x: " + game.getMap().getPlayerX(), "red");
            TerminalUtility.putAt(terminal, commandX - 2, commandY, "> ", "cyan");
            edit.draw(terminal, commandX, commandY);
            var size = prevLines.size();
            for (int i = 0; i < maxPrevSize; i++) {
                if (i >= size) {
                    break;
                }
                var prevLine = prevLines.get(size - i - 1);
                TerminalUtility.putAt(terminal, commandX - 2, commandY - i - 1, "> " + prevLine, "green");
            }
            terminal.flush();
//            handle input
            var key = this.terminal.readInput();
            if (key.getKeyType() == KeyType.Enter) {
                var line = edit.getText();
                try {
                    new Script(line, game.getSO()).exec();
                } catch (Exception e) {
                    TerminalUtility.putAt(terminal, commandX-2, commandY+1, "Failed to execute line", "white-red");
                    terminal.flush();
                    terminal.readInput();
                    continue;
                }
//                executed the command
                prevLines.add(String.join("", line));
                edit.setText("");
                this.draw();
                continue;
            }
//            check if escape
            if (key.getKeyType() == KeyType.Escape) {
                break;
            }
            edit.handleInput(key);
        }
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

    private static final int BAR_WIDTH = 13 + 2;

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
        y += 4;
        var player = game.getPlayer();
//        draw health bar
        var tX = x + 2;
        var line = "Health: ";
        TerminalUtility.putAt(terminal, tX, y, line);
        tX += line.length();
        TerminalUtility.drawBar(terminal, tX, y, BAR_WIDTH, player.getHealth(), player.getMaxHealth(), "red", true);
//        draw mana
        y += 1;
        tX = x + 2;
        line = "Mana:   ";
        TerminalUtility.putAt(terminal, tX, y, line);
        tX += line.length();
        TerminalUtility.drawBar(terminal, tX, y, BAR_WIDTH, player.getMana(), player.getMaxMana(), "blue", true);
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
        this.drawLogWindow(true);
    }

    private void drawLogWindow(boolean updateTiming) {
        var y = tileWindowHeight + 2;
        var x = TILE_WINDOW_OFFSET_X - 1;
        this.graphics.setForegroundColor(TextColor.ANSI.RED);
        this.graphics.drawRectangle(new TerminalPosition(x, y), new TerminalSize(logWindowWidth * 2, logWindowHeight), '*');
        this.graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
        LOG_WINDOW_LABEL.draw(terminal, x + 1, y);
        logList.draw(terminal, x + 1, y + 1, this.selectLogList);
        if (updateTiming) {
            this.selectLogList = false;
        }
    }

    private final HashSet<String> unknownTiles = new HashSet<>();

    private void drawTiles(List<WTile> tiles, boolean reverseColor) {
        var roomName = this.game.getMap().getCurrentRoom().getName();
        for (var tile : tiles) {
            var tileName = roomName + ":" + tile.getName();
            var y = TILE_WINDOW_OFFSET_Y + tile.getY();
            var x = TILE_WINDOW_OFFSET_X + 2 * tile.getX();
            if (!tileManager.has(tileName)) {
                TerminalUtility.putAt(terminal, x, y, "?", "black-white");
                if (!unknownTiles.contains(tileName)) {
                    unknownTiles.add(tileName);
                    System.out.println("Unknown tile: " + tileName);
                }
                continue;
            }
            var cct = tileManager.get(tileName);
            cct.drawLine(terminal, x, y, reverseColor);
        }
    }

    private void openInventory() throws IOException {
        var w = new InventoryWindow(terminal, graphics, game);
        w.show();
        var viewItemNames = w.getViewedItemNames();
        game.getPlayer().getInventory().addViewedItemNames(viewItemNames);
    }

    private void interact() {
        var tiles = this.game.getAdjacentInteractableTiles();
        if (tiles.size() == 0) {
            this.requestChoice("No interactable tiles nearby!", new String[]{"Ok"});
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
