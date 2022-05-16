package GOGame.tiles;

import GOGame.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TilePanel extends JPanel {
    public static final int TILE_HEIGHT = 32;
    public static final int TILE_WIDTH = 32;
    private final int tileCountX;
    private final int tileCountY;
    private final int centerY;
    private final int centerX;
    private TileSet tileSet;

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
        this.playerImage = this.tileSet.getImageMap().get("player");
    }

    private Engine e;
    private Image playerImage;

    public TilePanel(int width, int height) {
        this.tileCountX = width;
        this.tileCountY = height;
        this.centerY = height / 2 * TILE_HEIGHT;
        this.centerX = width / 2 * TILE_WIDTH;

        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void setEngine(Engine e) {
        this.e = e;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(tileCountX * TILE_WIDTH, tileCountY * TILE_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        var size = this.getPreferredSize();
        g.fillRect(0, 0, size.width, size.height);

        for (int i = 0; i < tileCountY; i++) {
            for (int ii = 0; ii < tileCountX; ii++) {
                g.setColor(Color.black);
                g.drawRect(ii * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            }
        }
        var tiles = e.getVisibleTiles();
        var roomName = e.getMap().getCurrentRoom().getName();
        var imageMap = tileSet.getImageMap();
        for ( var tile : tiles ) {
            var x = tile.getX();
            var y = tile.getY();
            var tileName = roomName + ":" + tile.getName();
            if (imageMap.containsKey(tileName)) {
                g.drawImage(imageMap.get(tileName), x * TILE_WIDTH, y * TILE_HEIGHT, null);
            }
            else {
                System.out.println("unknown tile: " + tileName);
                g.setColor(Color.red);
                g.drawRect(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            }
        }
        g.drawImage(this.playerImage, centerX, centerY, null);
    }

}
