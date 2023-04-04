package it.unibo.caesena.view.scene;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Optional;

import javax.swing.JPanel;

import it.unibo.caesena.model.tile.Tile;
import it.unibo.caesena.utils.Direction;
import it.unibo.caesena.utils.Pair;
import it.unibo.caesena.view.GUI;
import it.unibo.caesena.view.components.FooterComponent;
import it.unibo.caesena.view.components.FooterComponentImpl;
import it.unibo.caesena.view.components.SideBarComponent;
import it.unibo.caesena.view.components.SideBarComponentImpl;
import it.unibo.caesena.view.components.board.BoardManager;
import it.unibo.caesena.view.components.board.BoardManagerImpl;
import it.unibo.caesena.view.components.tile.TileImage;

public class GameScene extends JPanel implements Scene<JPanel> {
    private static final long serialVersionUID = -4620026742191171535L;
    private static final float MAIN_COMPONENT_RATIO = 0.75f;
    private final GUI userInterface;
    private final BoardManager<JPanel> mainComponent;
    private final FooterComponent<JPanel> footer;
    private final SideBarComponent<JPanel> sidebar;
    private Optional<TileImage> currentTileImage;

    public GameScene(final GUI userInterface) {
        super();
        this.userInterface = userInterface;
        this.currentTileImage = Optional.empty();
        this.mainComponent = new BoardManagerImpl(this);
        this.footer = new FooterComponentImpl(this);
        this.sidebar = new SideBarComponentImpl(this);
        this.setLayout(new GridBagLayout());
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = MAIN_COMPONENT_RATIO;
        gridBagConstraints.weighty = MAIN_COMPONENT_RATIO;
        mainComponent.getComponent().setPreferredSize(new Dimension((int) Math.round(10 * MAIN_COMPONENT_RATIO),
                (int) Math.round(10 * MAIN_COMPONENT_RATIO)));
        this.add(mainComponent.getComponent(), gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1 - MAIN_COMPONENT_RATIO;
        gridBagConstraints.weighty = MAIN_COMPONENT_RATIO;
        sidebar.getComponent().setPreferredSize(new Dimension((int) Math.round(10 * (1 - MAIN_COMPONENT_RATIO)),
                (int) Math.round(10 * MAIN_COMPONENT_RATIO)));
        this.add(sidebar.getComponent(), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1 - MAIN_COMPONENT_RATIO;
        footer.getComponent().setPreferredSize(
                new Dimension((int) Math.round(10 * 1.0), (int) Math.round(10 * (1 - MAIN_COMPONENT_RATIO))));
        this.add(footer.getComponent(), gridBagConstraints);
        super.setVisible(false);
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            this.generateCurrentTileImage();
            this.mainComponent.getBoard().getComponent().setVisible(true);
            this.footer.getComponent().setVisible(true);
            this.sidebar.getComponent().setVisible(true);
        }

        super.setVisible(visible);
    }

    private void generateCurrentTileImage() {
        final Tile currentTile = userInterface.getController().getCurrentTile();
        if (currentTileImage.isEmpty() || !currentTile.equals(currentTileImage.get().getTile())) {
            this.currentTileImage = Optional.of(new TileImage(currentTile));
        }
    }

    public final void updateHUD() {
        this.footer.update();
    }

    public TileImage getCurrentTileImage() {
        return this.currentTileImage.get();
    }

    public void placeMeeple() {
        mainComponent.toggleComponents();
    }

    public boolean placeTile() {
        final Optional<Pair<Integer, Integer>> placedTilePosition = mainComponent.getBoard()
                .getUnlockedTileButtonPosition();
        if (placedTilePosition.isPresent()
                && this.userInterface.getController().placeCurrentTile(placedTilePosition.get())) {
            mainComponent.getBoard().placeTile();
            return true;
        }
        return false;
    }

    public void endTurn() {
        this.mainComponent.endTurn();
    }

    public void zoomIn() {
        this.mainComponent.getBoard().zoomIn();
    }

    public void zoomOut() {
        this.mainComponent.getBoard().zoomOut();
    }

    public void move(final Direction direction) {
        this.mainComponent.getBoard().move(direction);
    }

    public boolean canZoomIn() {
        return this.mainComponent.getBoard().canZoomIn();
    }

    public boolean canZoomOut() {
        return this.mainComponent.getBoard().canZoomOut();
    }

    public boolean canMove(final Direction direction) {
        return this.mainComponent.getBoard().canMove(direction);
    }

    public void removePlacedTile() {
        this.mainComponent.getBoard().removePlacedTile();
    }

    public void updateComponents() {
        this.mainComponent.getBoard().draw();
    }

    @Override
    public final JPanel getComponent() {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final GUI getUserInterface() {
        return this.userInterface;
    }

    @Override
    public void update() {
        this.generateCurrentTileImage();
        this.updateComponents();
        this.updateHUD();
    }
}