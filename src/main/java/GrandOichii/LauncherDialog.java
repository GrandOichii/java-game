package GrandOichii;

import GOGame.Engine;
import GOGame.IGameWindow;
import GOGame.terminal.TerminalWindow;
import GOGame.tiles.TileWindow;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Properties;

public class LauncherDialog extends JDialog {
    private static final String[] windowTypes = {
            "Tiles",
            "Curses"
    };
    private static final IGameWindow[] windows = {
            new TileWindow(),
            new TerminalWindow()
    };
    private static final String CONFIG_FILE = "launcher.config";
    private static final String GAME_LOC_PROPERTY = "gameloc";
    private static final String WINDOW_TYPE_PROPERTY = "window";
    private static final String ASSETS_PROPERTY = "assets";

    private static final String WINDOW_TYPE_LABEL_TEXT = " Window type";
    static final int BETWEEN_Y = 3;
    static final int BETWEEN_X = 3;
    static final Border LABEL_BORDER = BorderFactory.createLineBorder(Color.BLACK, 1);
    static final int FRAME_WIDTH = 300;
    static String GAME_LOC_LABEL_TEXT = " Game";
    static final int GAME_LOC_LABEL_Y = BETWEEN_Y;
    static final int GAME_LOC_LABEL_X = BETWEEN_X;
    static final int GAME_LOC_LABEL_WIDTH = 90;
    static final int GAME_LOC_LABEL_HEIGHT = 30;
    static final String GAME_LOC_BUTTON_TEXT = "...";
    static final int GAME_LOC_BUTTON_HEIGHT = GAME_LOC_LABEL_HEIGHT;
    static final int GAME_LOC_BUTTON_WIDTH = GAME_LOC_BUTTON_HEIGHT;
    static final int GAME_LOC_FIELD_HEIGHT = GAME_LOC_LABEL_HEIGHT;
    static final int GAME_LOC_FIELD_WIDTH = FRAME_WIDTH - GAME_LOC_LABEL_WIDTH - GAME_LOC_BUTTON_WIDTH - 4 * BETWEEN_X;
    static final int GAME_LOC_FIELD_X = GAME_LOC_LABEL_X + GAME_LOC_LABEL_WIDTH + BETWEEN_X;
    static final int GAME_LOC_FIELD_Y = GAME_LOC_LABEL_Y;
    static final int GAME_LOC_BUTTON_Y = GAME_LOC_LABEL_Y;
    static final int GAME_LOC_BUTTON_X = GAME_LOC_FIELD_X + BETWEEN_X + GAME_LOC_FIELD_WIDTH;
    private static final int WINDOW_TYPE_LABEL_X = GAME_LOC_LABEL_X;
    private static final int WINDOW_TYPE_LABEL_Y = GAME_LOC_LABEL_Y + GAME_LOC_LABEL_HEIGHT + BETWEEN_Y;
    private static final int WINDOW_TYPE_LABEL_WIDTH = GAME_LOC_LABEL_WIDTH;
    private static final int WINDOW_TYPE_LABEL_HEIGHT = GAME_LOC_LABEL_HEIGHT;
    private static final int WINDOW_TYPE_COMBO_BOX_X = WINDOW_TYPE_LABEL_X + WINDOW_TYPE_LABEL_WIDTH + BETWEEN_X;
    private static final int WINDOW_TYPE_COMBO_BOX_Y = WINDOW_TYPE_LABEL_Y;
    private static final int WINDOW_TYPE_COMBO_BOX_HEIGHT = WINDOW_TYPE_LABEL_HEIGHT;
    private static final int WINDOW_TYPE_COMBO_BOX_WIDTH = FRAME_WIDTH - WINDOW_TYPE_LABEL_WIDTH - 2 * BETWEEN_X;
    private static final String ASSETS_LABEL_TEXT = " Assets path";
    private static final int ASSETS_LABEL_HEIGHT = GAME_LOC_LABEL_HEIGHT;
    private static final int ASSETS_LABEL_WIDTH = GAME_LOC_LABEL_WIDTH;
    private static final int ASSETS_LABEL_X = GAME_LOC_LABEL_X;
    private static final int ASSETS_LABEL_Y = WINDOW_TYPE_LABEL_Y + WINDOW_TYPE_LABEL_HEIGHT + BETWEEN_Y;
    private static final int ASSETS_FIELD_HEIGHT = GAME_LOC_FIELD_HEIGHT;
    private static final int ASSETS_FIELD_WIDTH = GAME_LOC_FIELD_WIDTH;
    private static final int ASSETS_FIELD_X = ASSETS_LABEL_X + ASSETS_LABEL_WIDTH + BETWEEN_X;
    private static final int ASSETS_FIELD_Y = ASSETS_LABEL_Y;
    private static final String ASSETS_BUTTON_TEXT = "...";
    private static final int ASSETS_BUTTON_HEIGHT = GAME_LOC_BUTTON_HEIGHT;
    private static final int ASSETS_BUTTON_WIDTH = GAME_LOC_BUTTON_WIDTH;
    private static final int ASSETS_BUTTON_Y = ASSETS_FIELD_Y;
    private static final int ASSETS_BUTTON_X = ASSETS_FIELD_X + ASSETS_FIELD_WIDTH + BETWEEN_X;
    private static final String START_BUTTON_TEXT = "Launch";
    private static final int START_BUTTON_Y = ASSETS_BUTTON_Y + ASSETS_BUTTON_HEIGHT + BETWEEN_Y;
    private static final int START_BUTTON_X = BETWEEN_X;
    private static final int START_BUTTON_HEIGHT = GAME_LOC_BUTTON_HEIGHT;
    private static final int START_BUTTON_WIDTH = FRAME_WIDTH - 2 * BETWEEN_X;
    static final int FRAME_HEIGHT = 160;
    private JTextField gameLocField;
    private JComboBox<String> windowTypeComboBox;
    private JTextField assetsField;
    private int windowIndex = 0;
    private Engine game;
    private boolean ok;

    public boolean isOk() {
        return ok;
    }

    public LauncherDialog() {
        super();
        this.ok = false;
        this.initUI();
        try {
            this.loadConfig();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load config file");
        }
    }

    private void initUI() {
        this.setLayout(null);
//        Frame info
        this.setTitle("Game launcher");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);

//        UI elements
        var gameLocLabel = new JLabel();
        gameLocLabel.setText(GAME_LOC_LABEL_TEXT);
        gameLocLabel.setBounds(GAME_LOC_LABEL_X, GAME_LOC_LABEL_Y, GAME_LOC_LABEL_WIDTH, GAME_LOC_LABEL_HEIGHT);
        gameLocLabel.setBorder(LABEL_BORDER);
        this.add(gameLocLabel);

        this.gameLocField = new JTextField();
        gameLocField.setBounds(GAME_LOC_FIELD_X, GAME_LOC_FIELD_Y, GAME_LOC_FIELD_WIDTH, GAME_LOC_FIELD_HEIGHT);
        this.add(gameLocField);

        var pickGameLocButton = new JButton();
        pickGameLocButton.setText(GAME_LOC_BUTTON_TEXT);
        pickGameLocButton.setBounds(GAME_LOC_BUTTON_X, GAME_LOC_BUTTON_Y, GAME_LOC_BUTTON_WIDTH, GAME_LOC_BUTTON_HEIGHT);
        pickGameLocButton.addActionListener(this::pickGameLoc);
        this.add(pickGameLocButton);

        var windowTypeLabel = new JLabel();
        windowTypeLabel.setText(WINDOW_TYPE_LABEL_TEXT);
        windowTypeLabel.setBounds(WINDOW_TYPE_LABEL_X, WINDOW_TYPE_LABEL_Y, WINDOW_TYPE_LABEL_WIDTH, WINDOW_TYPE_LABEL_HEIGHT);
        windowTypeLabel.setBorder(LABEL_BORDER);
        this.add(windowTypeLabel);

        this.windowTypeComboBox = new JComboBox<>();
        for (var windowType : windowTypes) {
            this.windowTypeComboBox.addItem(windowType);
        }
        this.windowTypeComboBox.setBounds(WINDOW_TYPE_COMBO_BOX_X, WINDOW_TYPE_COMBO_BOX_Y, WINDOW_TYPE_COMBO_BOX_WIDTH, WINDOW_TYPE_COMBO_BOX_HEIGHT);
        this.add(this.windowTypeComboBox);

        var assetsLabel = new JLabel();
        assetsLabel.setText(ASSETS_LABEL_TEXT);
        assetsLabel.setBounds(ASSETS_LABEL_X, ASSETS_LABEL_Y, ASSETS_LABEL_WIDTH, ASSETS_LABEL_HEIGHT);
        assetsLabel.setBorder(LABEL_BORDER);
        this.add(assetsLabel);

        this.assetsField = new JTextField();
        assetsField.setBounds(ASSETS_FIELD_X, ASSETS_FIELD_Y, ASSETS_FIELD_WIDTH, ASSETS_FIELD_HEIGHT);
        assetsField.addActionListener(this::assetLocChanged);
        this.add(assetsField);

        var assetsButton = new JButton();
        assetsButton.setText(ASSETS_BUTTON_TEXT);
        assetsButton.setBounds(ASSETS_BUTTON_X, ASSETS_BUTTON_Y, ASSETS_BUTTON_WIDTH, ASSETS_BUTTON_HEIGHT);
        assetsButton.addActionListener(this::pickAssetsLoc);
        this.add(assetsButton);

        var startButton = new JButton();
        startButton.setText(START_BUTTON_TEXT);
        startButton.setBounds(START_BUTTON_X, START_BUTTON_Y, START_BUTTON_WIDTH, START_BUTTON_HEIGHT);
        startButton.addActionListener(this::startGame);
        this.add(startButton);
    }

    private void saveConfig() {
        Properties prop = new Properties();
        prop.setProperty(GAME_LOC_PROPERTY, this.gameLocField.getText());
        prop.setProperty(WINDOW_TYPE_PROPERTY, (String) this.windowTypeComboBox.getSelectedItem());
        prop.setProperty(ASSETS_PROPERTY, this.assetsField.getText());

        try {
            prop.store(new FileOutputStream(CONFIG_FILE), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadConfig() throws IOException {
        if (!new File(CONFIG_FILE).exists()) {
            return;
        }
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(CONFIG_FILE);
        prop.load(fis);
        fis.close();

        this.gameLocField.setText(prop.getProperty(GAME_LOC_PROPERTY));
        this.windowTypeComboBox.setSelectedItem(prop.getProperty(WINDOW_TYPE_PROPERTY));
        this.assetsField.setText(prop.getProperty(ASSETS_PROPERTY));
    }

//    actions
    private void pickGameLoc(ActionEvent e) {
        var chooser = new JFileChooser();
        chooser.setDialogTitle("Select game directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            var d = chooser.getSelectedFile();
            this.gameLocField.setText(d.getPath());
        }
    }

    private void pickAssetsLoc(ActionEvent e) {
        var chooser = new JFileChooser();
        chooser.setDialogTitle("Select assets path");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            var d = chooser.getSelectedFile();
            this.assetsField.setText(d.getPath());
        }
    }

    private void assetLocChanged(ActionEvent e) {
//        TODO
    }

    private void startGame(ActionEvent e) {
        var gamePath = this.gameLocField.getText();
        if (gamePath.equals("")) {
            JOptionPane.showMessageDialog(this, "Enter the game path");
            return;
        }
        if (!new File(gamePath).exists()) {
            JOptionPane.showMessageDialog(this, String.format("Directory %s doesn't exist", gamePath));
            return;
        }
        var wi = this.windowTypeComboBox.getSelectedIndex();
        var assetsPath = getAssetsPath();
        if (assetsPath.equals("")) {
            JOptionPane.showMessageDialog(this, "Enter the assets path");
            return;
        }
        if (!new File(assetsPath).exists()) {
            JOptionPane.showMessageDialog(this, String.format("Directory %s doesn't exist", assetsPath));
            return;
        }
        try {
            this.game = new Engine(gamePath);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load game");
            ex.printStackTrace();
//            System.out.println(ex);
            return;
        }
//        save the configuration
        this.saveConfig();
//        open the window
        var window = windows[wi];
        game.setGameWindow(window);
        this.ok = true;
        this.dispose();
    }

    public Engine getEngine() { return this.game; }
    public String getAssetsPath() {
        return this.assetsField.getText();
    }
}
