package it.unibo.caesena.view.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JPanel;

import it.unibo.caesena.model.Player;
import it.unibo.caesena.model.meeple.Meeple;
import it.unibo.caesena.model.tile.Tile;
import it.unibo.caesena.model.tile.TileSection;
import it.unibo.caesena.utils.Pair;

public class TileButtonImpl extends JButton implements TileButton {
    private final Pair<Integer, Integer> position;
    private final BoardComponent<JPanel> parentBoard;
    //TODO sta roba dovrebbe venire dal controller, capiamo come
    private Optional<Tile> containedTile;
    private Optional<Meeple> placedMeeple;
    private Optional<TileSection> placedMeepleSection;
    private boolean locked = false;
    private Color playerColor;

    public TileButtonImpl(int x, int y, BoardComponent<JPanel> parentBoard) {
        super();
        this.parentBoard = parentBoard;
        this.containedTile = Optional.empty();
        this.placedMeeple = Optional.empty();
        this.position = new Pair<Integer,Integer>(x, y);
        this.addActionListener(getTileButtonActionListener());
        this.setContentAreaFilled(false);
        this.setFocusable(false);
    }

    private ActionListener getTileButtonActionListener() {
        return (e) -> {
            TileButtonImpl selectedTileButton = (TileButtonImpl)e.getSource();
            if (this.parentBoard.getGUI().getController().isValidPositionForCurrentTile(selectedTileButton.getPosition())) {
                if (this.parentBoard.isTileButtonPlaced()){
                    TileButton lastTileButtonPlaced = this.parentBoard.getCurrentlySelectedTileButton();
                    if (!lastTileButtonPlaced.isLocked()) {
                        lastTileButtonPlaced.removeTile();
                    }
                }
                this.parentBoard.setPlacedTileButton(selectedTileButton);
                this.parentBoard.getCurrentlySelectedTileButton().addTile(this.parentBoard.getGUI().getController().getCurrentTile());
                var player = this.parentBoard.getGUI().getController().getCurrentPlayer();
                this.playerColor = this.parentBoard.getGUI().getPlayerColor(player);
                parentBoard.updateComponents();
            }
        };
    }

    @Override
    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    @Override
    public void addTile(Tile tile) {
        this.containedTile = Optional.of(tile);
    }

    @Override
    public void lockTile() {
        if (containedTile.isPresent()) {
            locked = true;
        } else {
            throw new IllegalStateException("Can't lock tile since it's not present");
        }
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public Tile getContainedTile() {
        return containedTile.orElseThrow(() -> new IllegalStateException("tried to get contained tile but there was none"));
    }

    @Override
    public TileSection getPlacedMeepleSection() {
        return placedMeepleSection.orElseThrow(() -> new IllegalStateException("tried to get placed meeple but there was none"));
    }

    @Override
    public void addMeeple(Meeple meeple, TileSection section) {
        this.placedMeeple = Optional.of(meeple);
        this.placedMeepleSection = Optional.of(section);
    }

    @Override
    public void removeTile() {
        this.containedTile = Optional.empty();
    }

    @Override
    public void removeMeeple() {
        this.placedMeeple = Optional.empty();
    }

    @Override
    public boolean containsTile() {
        return this.containedTile.isPresent();
    }

    @Override
    public boolean containsMeeple(){
        if (this.placedMeeple.isPresent()) {
            Player owner = this.placedMeeple.get().getOwner();
            Meeple meeple = this.placedMeeple.get();
            if(parentBoard.getGUI().getController().getNotPlacedPlayerMeeples(owner).contains(meeple)) {
                this.placedMeeple = Optional.empty();
            }
        }
        return placedMeeple.isPresent();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (this.containsTile()) {
            TileImage tileImage = new TileImage(getContainedTile(), playerColor);
            if (this.containsMeeple())  {
                tileImage.addMeeple(this.placedMeeple.get(), getPlacedMeepleSection());
            }
            g.drawImage(tileImage.getAsBufferedImage(), 0, 0, getWidth(), getHeight(), null);
        }
    }
}
