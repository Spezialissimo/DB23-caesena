package it.unibo.caesena.view.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.unibo.caesena.controller.Controller;
import it.unibo.caesena.model.gameset.GameSet;
import it.unibo.caesena.model.tile.TileSection;
import it.unibo.caesena.utils.Pair;
import it.unibo.caesena.view.GameView;

class SectionSelectorComponentImpl extends JPanel implements SectionSelectorComponent<JPanel> {
    private final GameView gameView;
    private final Map<SectionButton, GridBagConstraints> sectionButtons = new HashMap<>();

    SectionSelectorComponentImpl(final GameView gameView) {
        super();
        this.gameView = gameView;
    }

    @Override
    public final Optional<TileSection> getSelectedSection() {
        return sectionButtons.keySet().stream()
                .filter(SectionButton::hasBeenSelected)
                .map(SectionButton::getSection)
                .findFirst();
    }

    @Override
    public final Dimension getPreferredSize() {
        final Dimension d = this.getParent().getSize();
        int newSize = d.width > d.height ? d.height : d.width;
        newSize = newSize == 0 ? 100 : newSize;
        return new Dimension(newSize, newSize);
    }

    @Override
    public final Boolean isSectionSelected() {
        return getSelectedSection().isPresent();
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        final BufferedImage tileButton = gameView.getCurrentTileImage().getAsBufferedImageWithoutMeeple(this.getWidth(),
                this.getHeight());
        graphics.drawImage(tileButton, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    @Override
    public void draw() {
        this.removeAll();
        this.drawSections();
        this.validate();
        this.repaint();
    }

    @Override
    public void reset() {
        this.sectionButtons.clear();
        this.removeAll();
    }

    @Override
    public JPanel getComponent() {
        return this;
    }

    private void drawSections() {
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        if (sectionButtons.isEmpty()) {
            populateSectionButtons();
        }
        sectionButtons.entrySet().forEach(e -> this.add(e.getKey(), e.getValue()));
    }

    private void populateSectionButtons() {
        for (final var section : TileSection.values()) {
            createButton(section);
        }
    }

    private void createButton(final TileSection section) {
        final var coordinates = this.getCoordinates(section);
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = coordinates.getX();
        constraints.gridy = coordinates.getY();
        final SectionButton sectionButton = new SectionButton(section);
        sectionButtons.put(sectionButton, constraints);
    }

    private Pair<Integer, Integer> getCoordinates(final TileSection section) {
        return switch (section) {
            case CENTER -> new Pair<>(2, 2);
            case DOWN_CENTER -> new Pair<>(2, 4);
            case DOWN_LEFT -> new Pair<>(1, 4);
            case DOWN_RIGHT -> new Pair<>(3, 4);
            case LEFT_CENTER -> new Pair<>(0, 2);
            case LEFT_DOWN -> new Pair<>(0, 3);
            case LEFT_UP -> new Pair<>(0, 1);
            case RIGHT_CENTER -> new Pair<>(4, 2);
            case RIGHT_DOWN -> new Pair<>(4, 3);
            case RIGHT_UP -> new Pair<>(4, 1);
            case UP_CENTER -> new Pair<>(2, 0);
            case UP_LEFT -> new Pair<>(1, 0);
            case UP_RIGHT -> new Pair<>(3, 0);
            default -> throw new IllegalStateException("Section is a known section or is null");
        };
    }

    private String getLabelFromSection(final TileSection section) {
        final Controller controller = gameView.getUserInterface().getController();
        final GameSet gameSet = controller.getCurrentTileGameSetInSection(section);
        return String.valueOf(gameSet.getType().name().toCharArray()[0]);
    }

    private ActionListener getSectionButtonListener() {
        return (e) -> {
            final SectionButton newSectionButton = (SectionButton) e.getSource();
            Boolean wasSelected = newSectionButton.hasBeenSelected();
            sectionButtons.keySet().stream()
                .filter(s -> s.shouldBeDrawn())
                .filter(s -> s.hasBeenSelected())
                .forEach(s -> s.deselect());
            if (!wasSelected) {
                newSectionButton.select();
            }
        };
    }

    private class SectionButton extends JButton {
        private final TileSection section;
        private static final Color UNSELECTED_COLOR = Color.WHITE;
        private static final Color SELECTED_COLOR = Color.GREEN;
        private boolean selected;
        private boolean toBeDrawn;

        SectionButton(final TileSection section) {
            super();
            this.section = section;
            final Controller controller = gameView.getUserInterface().getController();
            final GameSet gameSet = controller.getCurrentTileGameSetInSection(section);
            this.toBeDrawn = gameSet.isMeepleFree() && !gameSet.isClosed();
            if (this.toBeDrawn) {
                final String buttonLabel = getLabelFromSection(section);
                this.setText(buttonLabel);
                this.addActionListener(getSectionButtonListener());
                this.deselect();
            } else {
                this.setContentAreaFilled(false);
                this.setBorderPainted(false);
                this.setBackground(new Color(0, 0, 0, 0));
                this.setOpaque(false);
            }
        }

        public boolean shouldBeDrawn() {
            return this.toBeDrawn;
        }

        public boolean hasBeenSelected() {
            return selected;
        }

        public void select() {
            selected = true;
            this.setBackground(SELECTED_COLOR);
            this.setOpaque(true);
            this.repaint();
        }

        public void deselect() {
            selected = false;
            this.setBackground(UNSELECTED_COLOR);
            this.setOpaque(true);
            this.repaint();
        }

        public TileSection getSection() {
            return section;
        }

        @Override
        public Dimension getPreferredSize() {
            final Dimension d = super.getPreferredSize();
            final int s = (int) (d.getWidth() < d.getHeight() ? d.getHeight() : d.getWidth());
            return new Dimension(s, s);
        }
    }
}