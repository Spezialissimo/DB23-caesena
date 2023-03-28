package it.unibo.caesena.view;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import it.unibo.caesena.controller.Controller;
import it.unibo.caesena.model.Player;
import it.unibo.caesena.utils.ImageUtil;

public class GUI extends JFrame implements UserInterface {
    // TODO rimuovere
    // RAGA È SOLO PER DEBUG, SE ATTIVO UNO DISATTIVATE GLI ALTRI!!
    private static boolean DEBUG_GAME_VIEW = true;
    private static boolean DEBUG_GAME_OVER_VIEW = false;
    private static float MINIMUM_SIZE_RATIO = 0.35f;
    private static final String SEP = File.separator;
    private static final String ROOT = "it" + SEP + "unibo" + SEP + "caesena" + SEP + "images" + SEP;
    private Controller controller;
    private View<JPanel> startView;
    private View<JPanel> gameView;
    private View<JPanel> pauseView;
    private View<JPanel> gameOverView;
    private JPanel gamePanel;
    private Map<Player, Color> players;

    public GUI(final Controller controller) {
        super();
        this.controller = controller;
        this.players = new HashMap<>();

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                showExitDialog();
            }
        });

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setMinimumSize(new Dimension(Math.round(screenSize.width * MINIMUM_SIZE_RATIO), 
            Math.round(screenSize.height * MINIMUM_SIZE_RATIO)));
        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setLocationByPlatform(true);

        this.setIconImage(ImageUtil.getImageFromRelativePath(ROOT + "tiles" + SEP + "TILE_BACK.png"));
        this.setVisible(true);

        //TODO rimuovere
        if (DEBUG_GAME_VIEW || DEBUG_GAME_OVER_VIEW) {
            this.addPlayer("Giocatore1", Color.RED);
            this.addPlayer("Giocatore2", Color.GREEN);
            this.startGame();
            if (DEBUG_GAME_OVER_VIEW) {
                this.showGameOverView();
            }
        } else {
            this.showStartView();
        }
    }

    public void showStartView() {
        this.setTitle("Caesena | Start menu");
        this.startView = new StartView(this);
        this.pauseView = null;
        this.gameView = null;
        this.gameOverView = null;
        this.gamePanel = null;

        this.startView.setVisible(true);
        this.setContentPane(startView.getComponent());
        this.validate();
        this.repaint();
    }

    public void startGame() {
        this.setTitle("Caesena | Playing a game");
        this.startView = null;
        this.gameView = new GameView(this);
        this.pauseView = new PauseView(this);
        this.gamePanel = new JPanel();
        this.gamePanel.setLayout(new OverlayLayout(this.gamePanel));

        this.gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "togglePauseView");
        this.gamePanel.getActionMap().put("togglePauseView", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                togglePauseView();
            }
        });

        this.gameView.setVisible(true);
        this.pauseView.setVisible(false);
        this.gamePanel.add(this.pauseView.getComponent());
        this.gamePanel.add(this.gameView.getComponent());

        this.setContentPane(gamePanel);
        this.validate();
        this.repaint();
    }

    public void togglePauseView() {
        this.pauseView.setVisible(!this.pauseView.isVisible());
        setEnabledAllComponents(gameView.getComponent(), !this.pauseView.isVisible());
        setEnabledAllComponents(pauseView.getComponent(), this.pauseView.isVisible());
    }

    public void showGameOverView() {
        this.setTitle("Caesena | Game ended");
        this.gameOverView = new GameOverView(this);

        this.gameView.setVisible(false);
        this.pauseView.setVisible(false);
        this.gameOverView.setVisible(true);

        this.setContentPane(gameOverView.getComponent());
        this.validate();
        this.repaint();
    }

    public void showExitDialog() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?",
            "Exit Caesena", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            exit();
        }
    }

    public void exit() {
        this.controller.exitGame();
        System.exit(0);
    }

    public Controller getController() {
        return this.controller;
    }

    private void setEnabledAllComponents(Container container, boolean enabled) {
        for (var component : container.getComponents()) {
            component.setEnabled(enabled);
            if (component instanceof Container) {
                setEnabledAllComponents(((Container)component), enabled);
            }
        }
    }

    public void addPlayer(String name, Color color) {
        this.players.put(this.controller.addPlayer(name), color);
    }

    public Color getPlayerColor(final Player player) {
        return this.players.get(player);
    }
}
