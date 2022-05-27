package GOGame.tiles;

import GOGame.Engine;
import GOGame.GameWindow;
import GOGame.exceptions.ScriptException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;


public class TileWindow extends JFrame implements GameWindow, KeyListener {
    private static final int TILE_COUNT_X = 21;
    private static final int TILE_COUNT_Y = 11;
    private Engine game;
    private TilePanel tilesPanel;
    private boolean interactState = false;

    public TileWindow() {
        super("TEST");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.initUI();
    }

    void initUI() {
        this.tilesPanel = new TilePanel(TILE_COUNT_X, TILE_COUNT_Y);
        this.add(this.tilesPanel);
        this.pack();
    }

    @Override
    public void start(Engine engine, String assetsPath) throws IOException, ScriptException {
        addKeyListener(this);

        this.game = engine;
        var size = this.tilesPanel.getPreferredSize();
        game.setWindowSize(TILE_COUNT_X, TILE_COUNT_Y);
        this.tilesPanel.setEngine(game);
        var tileSet = TileSet.load(assetsPath, TilePanel.TILE_WIDTH, TilePanel.TILE_HEIGHT);
        this.tilesPanel.setTileSet(tileSet);
        this.setIconImage(tileSet.get("icon"));

        this.game.start();

        this.setVisible(true);
    }

    @Override
    public void sleep(Integer amount) {
//        this.tilesPanel.setIgnoreRepaint(false);
        this.draw();
//        this.repaint();
//        this.revalidate();
        try {
            Thread.sleep(amount);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String requestChoice(String text, String[] choices) {
//        TODO
        return null;
    }

    @Override
    public void onLogUpdate(String message) {
//        TODO
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    private static final HashMap<Integer, String> ARROW_KEY_MAP = new HashMap<>() {{
        put(KeyEvent.VK_UP, "ArrowUp");
        put(KeyEvent.VK_DOWN, "ArrowDown");
        put(KeyEvent.VK_LEFT, "ArrowLeft");
        put(KeyEvent.VK_RIGHT, "ArrowRight");
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

    private static String stringKey(KeyEvent key) {
        var kt = key.getKeyCode();
        if (ARROW_KEY_MAP.containsKey(kt)) {
            return ARROW_KEY_MAP.get(kt);
        }
        return Character.toString(key.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        var key = stringKey(e);
        if (interactState) {
            interactState = false;
            if (DIRECTION_MAP.containsKey(key)) {
                try {
                    this.game.interactAt(DIRECTION_MAP.get(key));
                } catch (ScriptException ex) {
                    throw new RuntimeException(ex);
                }
            }
            this.draw();
            return;
        }
        if (DIRECTION_MAP.containsKey(key)) {
            try {
                var moved = this.game.movePlayer(DIRECTION_MAP.get(key));
                if (!moved) {
                    return;
                }
                this.game.update();
            } catch (ScriptException ex) {
                throw new RuntimeException(ex);
            }
            this.tilesPanel.repaint();
            return;
        }
        switch (key) {
        case "e":
            this.interact();
        case "+":

        }
    }

    private void drawTiles() {
        var size = this.tilesPanel.getPreferredSize();
        this.tilesPanel.paintImmediately(0, 0, size.width, size.height);
    }

    private void draw() {
        this.drawTiles();
    }

    private void interact() {
        var tiles = this.game.getAdjacentInteractableTiles();
        if (tiles.size() == 0) {
//            TODO
            return;
        }
        tilesPanel.highlightTiles(tiles, Color.lightGray);
        this.drawTiles();
        this.interactState = true;
    }
}
