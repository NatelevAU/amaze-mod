package au.natelev.amaze.game;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final int width;
    private final int height;
    private Tile startTile;
    private final List<Tile> unpaintedTiles = new ArrayList<>();

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Tile getStartTile() { return startTile; }
}
