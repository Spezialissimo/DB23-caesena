package it.unibo.caesena.view.components.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import it.unibo.caesena.controller.Controller;

import it.unibo.caesena.model.gameset.GameSet;
import it.unibo.caesena.model.tile.TileSection;
import it.unibo.caesena.view.scene.GameScene;

/**
 * This class rappresents a section button, used to pick which section to
 * select. Every SectionButton object rappresent a single section.
 */
public class SectionButton extends JButton {
    private static final long serialVersionUID = 3246088701705856082L;
    private static final Color UNSELECTED_COLOR = Color.WHITE;
    private static final Color SELECTED_COLOR = Color.GREEN;
    private final TileSection section;
    private final GameScene gameScene;
    private boolean selected;
    private final boolean toBeDrawn;

    /**
     * Class constructor.
     *
     * @param section that is rappresented by the SectionButton object
     */
    SectionButton(final TileSection section, GameScene gameScene, ActionListener onClickActionListener) {
        super();
        this.gameScene = gameScene;
        this.section = section;
        final Controller controller = gameScene.getUserInterface().getController();
        final GameSet gameSet = controller.getCurrentTileGameSetInSection(section);
        this.toBeDrawn = gameSet.isMeepleFree() && !gameSet.isClosed();
        if (this.toBeDrawn) {
            selected = false;
            final String buttonLabel = getLabelFromSection(section);
            this.setText(buttonLabel);
            this.addActionListener(onClickActionListener);
            this.setOpaque(true);
            this.setBackground(UNSELECTED_COLOR);
        } else {
            this.setContentAreaFilled(false);
            this.setBorderPainted(false);
            this.setBackground(new Color(0, 0, 0, 0));
            this.setOpaque(false);
        }
    }

    /**
     * Specifies if the SectionButton should be drawn.
     * Namely if a Section is selectable.
     *
     * @return true if the SectionButton should be drawn
     */
    public boolean shouldBeDrawn() {
        return this.toBeDrawn;
    }

    /**
     * Speficied if the section has been selected.
     *
     * @return true if the section has been selected, false otherwise
     */
    public boolean hasBeenSelected() {
        return selected;
    }

    /**
     * Selects the SectionButton.
     */
    public void select() {
        selected = true;
        this.setBackground(SELECTED_COLOR);
        this.setOpaque(true);
        this.repaint();
    }

    /**
     * Deselects the SectionButton.
     */
    public void deselect() {
        selected = false;
        this.setBackground(UNSELECTED_COLOR);
        this.setOpaque(true);
        this.repaint();
    }

    /**
     * Gets the section that has been selected.
     *
     * @return the section that has been selected
     */
    public TileSection getSection() {
        return section;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize() {
        final Dimension d = super.getPreferredSize();
        final int s = (int) (d.getWidth() < d.getHeight() ? d.getHeight() : d.getWidth());
        return new Dimension(s, s);
    }

    /**
     * Gets the label to place inside a TileButton based on a given section.
     *
     * @param section from which extrapolate the label
     * @return the label rappresenting the given section
     */
    private String getLabelFromSection(final TileSection section) {
        final Controller controller = this.gameScene.getUserInterface().getController();
        final GameSet gameSet = controller.getCurrentTileGameSetInSection(section);
        return String.valueOf(gameSet.getType().name().toCharArray()[0]);
    }

}