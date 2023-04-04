package it.unibo.caesena.view.components.meeple;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import it.unibo.caesena.view.GUI;
import it.unibo.caesena.view.scene.GameScene;
import it.unibo.caesena.controller.Controller;
import it.unibo.caesena.model.Player;

public class RemainingMeeplesComponentImpl extends JPanel implements RemainingMeeplesComponent<JPanel> {
    private static final long serialVersionUID = 5371662486606196479L;
    private final GUI userInterface;
    private final Controller controller;
    private final JPanel allMeeplesPanel;
    private final Map<Player, List<MeepleImage>> meeples;

    public RemainingMeeplesComponentImpl(final GameScene gameScene) {
        super();

        this.userInterface = gameScene.getUserInterface();
        this.controller = userInterface.getController();

        this.meeples = new HashMap<>();

        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.allMeeplesPanel = new JPanel();
        this.allMeeplesPanel.setOpaque(false);

        this.add(allMeeplesPanel);
        this.allMeeplesPanel.setVisible(true);
        super.setVisible(false);
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            for (final var player : controller.getPlayers()) {
                meeples.put(player, controller.getPlayerMeeples(player).stream()
                    .map(m -> new MeepleImage(m, player.getColor().asSwingColor())).toList());
            }
            update();
        }

        super.setVisible(visible);
    }

    @Override
    public void update() {
        this.allMeeplesPanel.removeAll();
        this.allMeeplesPanel.revalidate();
        this.allMeeplesPanel.repaint();


        final Player currentPlayer = controller.getCurrentPlayer();
        this.allMeeplesPanel.setLayout(new GridLayout(1, meeples.get(currentPlayer).size()));
        for (final MeepleImage meeple : meeples.get(currentPlayer)) {
            final JPanel meeplePanel = new JPanel() {
                @Override
                protected void paintComponent(final Graphics graphics) {
                    super.paintComponent(graphics);
                    if (this.getHeight() > this.getWidth()) {
                        graphics.drawImage(meeple.getImage(), 0, 0, this.getWidth(), this.getWidth(), null);
                    } else {
                        graphics.drawImage(meeple.getImage(), 0, 0, this.getHeight(), this.getHeight(), null);
                    }
                }
            };
            meeplePanel.setOpaque(false);
            this.allMeeplesPanel.add(meeplePanel);
        }
    }

    @Override
    public JPanel getComponent() {
        return this;
    }
}