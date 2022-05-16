package GrandOichii;

import GOGame.Engine;
import GOGame.GameWindow;
import GOGame.Utility;
import GOGame.terminal.TerminalUtility;
import GOGame.terminal.TerminalWindow;
import GOGame.tiles.TilePanel;
import GOGame.tiles.TileWindow;

import javax.swing.*;
import java.io.IOException;

public class Main {
    private static Engine getWindow() {
        var dialog = new LauncherDialog();
        dialog.setModal(true);
        dialog.setVisible(true);
//        dialog.domo
        return dialog.getEngine();
    }

    public static void main(String[] args) throws Exception {
        TerminalUtility.initColorMap();
//        var dialog = new LauncherDialog();
//        dialog.setModal(true);
//        dialog.setVisible(true);
//
//        var assetsPath = dialog.getAssetsPath();
//        var engine = dialog.getEngine();
//        var window = engine.getGameWindow();
//        window.start(engine, assetsPath);

//        for testing
        var game = new Engine("/Users/oichii/Desktop/games/souls-like");
        var window = new TileWindow();
        game.setGameWindow(window);
        window.start(game, "/Users/oichii/Desktop/games/assets");
//        JFrame f = new JFrame("Swing Paint Demo");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.add(new TilePanel(1, 1));
//        f.pack();
//        f.setVisible(true);


//        /Users/oichii/Desktop/code/go/go-game/games/items-tester
//        /Users/oichii/Desktop/games/assets
    }
}
