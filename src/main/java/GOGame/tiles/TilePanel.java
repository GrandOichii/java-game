package GOGame.tiles;

import GOGame.Engine;
import GOGame.map.WTile;
import com.googlecode.lanterna.TextColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TilePanel extends JPanel {
    public static int TILE_HEIGHT = 64;
    public static int TILE_WIDTH = 64;
    private final int tileCountX;
    private final int tileCountY;
    private final int centerY;
    private final int centerX;
    private TileSet tileSet;
    private List<WTile> bufferedHighlight;
    private Color bufferedHighlightColor;

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
        this.playerImage = this.tileSet.get("player");
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

    private Set<String> unknownTiles = new HashSet<>();

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
        for ( var tile : tiles ) {
            var x = tile.getX();
            var y = tile.getY();
            var tileName = roomName + ":" + tile.getName();
            if (tileSet.containsKey(tileName)) {
                g.drawImage(tileSet.get(tileName), x * TILE_WIDTH, y * TILE_HEIGHT, null);
            }
            else {
                if (!unknownTiles.contains(tileName)) {
                    System.out.println("unknown tile: " + tileName);
                    unknownTiles.add(tileName);
                }
                g.setColor(Color.red);
                g.drawRect(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            }
        }
        g.drawImage(this.playerImage, centerX, centerY, null);

        if (this.bufferedHighlight != null) {
            g.setColor(this.bufferedHighlightColor);
            for (var tile : this.bufferedHighlight) {
                var x = tile.getX() * TILE_WIDTH;
                var y = tile.getY() * TILE_HEIGHT;
                var amount = 2;
                for (int i = 0; i < amount; i++) {
                    g.drawRect(x + i, y + i, TILE_WIDTH - 2 * i, TILE_HEIGHT - 2 * i);
                }
//            g.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);
            }
            this.bufferedHighlight = null;
            this.bufferedHighlightColor = null;
        }
    }

    public void highlightTiles(List<WTile> tiles, Color color) {
        this.bufferedHighlight = tiles;
        this.bufferedHighlightColor = color;
    }
}
