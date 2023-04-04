package it.unibo.caesena.view.components.board;

import java.util.Optional;

import it.unibo.caesena.model.tile.TileSection;

/**
 * This class rappresents a section selector.
 * A section selector is a component used when placing a meeple onto a tile
 * where there needs to be a way to select which specific section to place it.
 *
 * @param <X> is the type of the component
 */
public interface SectionSelectorComponent<X> {

    /**
     * Gets the section that has been selected.
     *
     * @return the section that has been selected
     */
    Optional<TileSection> getSelectedSection();

    /**
     * Specifies if any section has been selected.
     *
     * @return true if any section has been selected, false otherwise
     */
    Boolean isSectionSelected();

    /**
     * Draws the section selector.
     */
    void draw();

    /**
     * Resets the selection.
     */
    void reset();

    /**
     * Gets the section selector as a specific component of a GUI framework, such as JPanel.
     *
     * @return the section selector as a specific component of a GUI framework.
     */
    X getComponent();
}
