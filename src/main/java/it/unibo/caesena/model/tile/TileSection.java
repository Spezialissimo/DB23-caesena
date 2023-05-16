package it.unibo.caesena.model.tile;

import it.unibo.caesena.model.gameset.GameSet;
import it.unibo.caesena.model.meeple.MutableMeeple;

/**
 * Class representing the different possible portions of the sides of a Tile.
 */
public class TileSection {

    private final TileSectionType type;

    private MutableTile tile;
    private GameSet gameSet;
    private MutableMeeple meeple;
    private boolean closed;

    public TileSection(final TileSectionType type) {
        this.type = type;
        this.closed = false;
    }

    public TileSectionType getType() {
        return this.type;
    }

    public MutableTile getTile() {
        return this.tile;
    }

    public GameSet getGameSet() {
        return this.gameSet;
    }

    public void close() {
        this.closed = true;
    }

    public boolean isClosed() {
        return this.closed;
    }

}
