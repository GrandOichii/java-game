package GrandOichii;

import GOGame.Engine;
import GOGame.terminal.TerminalWindow;

public class Main {
    private static Engine getWindow() {
        var dialog = new LauncherDialog();
        dialog.setModal(true);
        dialog.setVisible(true);
//        dialog.domo
        return dialog.getEngine();
    }

    public static void main(String[] args) throws Exception {
//        var dialog = new LauncherDialog();
//        dialog.setModal(true);
//        dialog.setVisible(true);
//
//        if (!dialog.isOk()) {
//            return;
//        }
//        var assetsPath = dialog.getAssetsPath();
//        var engine = dialog.getEngine();
//        var window = engine.getGameWindow();
//        window.start(engine, assetsPath);

//        for testing
        var game = new Engine("/Users/oichii/Desktop/code/java/java-game/game/demo");
        var window = new TerminalWindow();
//        var window = new TileWindow();
        game.setGameWindow(window);
        window.start(game, "/Users/oichii/Desktop/code/java/java-game/assets");
//        JFrame f = new JFrame("Swing Paint Demo");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.add(new TilePanel(1, 1));
//        f.pack();
//        f.setVisible(true);


//        /Users/oichii/Desktop/code/go/go-game/games/items-tester
//        /Users/oichii/Desktop/games/assets
    }
}
