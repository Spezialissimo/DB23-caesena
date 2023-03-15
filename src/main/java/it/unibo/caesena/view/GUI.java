package it.unibo.caesena.view;

import java.awt.Toolkit;
import java.awt.Dimension;

import javax.swing.JFrame;

import it.unibo.caesena.controller.Controller;
import it.unibo.caesena.utils.Color;

public class GUI extends JFrame implements UserInterface {
    // TODO rimuovere
    // RAGA È SOLO PER DEBUG, SE ATTIVO UNO DISATTIVATE GLI ALTRI!!
    private static boolean DEBUG_GAME_VIEW = true;
    private static boolean DEBUG_GAME_OVER_VIEW = false;
    private Controller controller;
    private View startView;
    private View gameView;
    private View pauseView;
    private View gameOverView;

    public GUI(final Controller controller) {
        super();
        this.controller = controller;

        // TODO background and size options
        // TODO default options
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int sw = (int) screen.getWidth();
        final int sh = (int) screen.getHeight();
        this.setSize(sw / 3, sh / 3);
        this.setLocationByPlatform(true);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //TODO rimuovere
        if (DEBUG_GAME_VIEW || DEBUG_GAME_OVER_VIEW) {
            Color color1 = Color.createColor("FF0000", "Red");
            Color color2 = Color.createColor("00FF00", "Green");
            this.controller.addPlayer("Giocatore1", color1);
            this.controller.addPlayer("Giocatore2", color2);
            this.startGame();
            if (DEBUG_GAME_OVER_VIEW) {
                this.showGameOverView();
            }
        } else {
            this.showStartView();
        }
    }

    public void showStartView() {
        this.startView = new StartView(this);
        this.pauseView = null;
        this.gameView = null;
        this.gameOverView = null;

        this.startView.setVisible(true);
        this.setContentPane(startView);
        this.validate();
        this.repaint();
    }

    public void startGame() {
        //this.startView.setVisible(false);

        this.startView = null;
        this.gameView = new GameView(this);
        this.pauseView = new PauseView(this);

        // TODO cambia sta roba
        ((GameView)gameView).start();
        this.gameView.setVisible(true);
        this.setContentPane(gameView);
        this.validate();
        this.repaint();
    }

    public void showPauseView() {
        this.gameView.setVisible(false);
        this.pauseView.setVisible(true);

        this.setContentPane(pauseView);
        this.validate();
        this.repaint();
    }

    public void hidePauseView() {
        this.pauseView.setVisible(false);
        this.gameView.setVisible(true);

        this.setContentPane(gameView);
        this.validate();
        this.repaint();
    }

    public void showGameOverView() {
        this.gameOverView = new GameOverView(this);

        this.gameView.setVisible(false);
        this.pauseView.setVisible(false);
        this.gameOverView.setVisible(true);

        this.setContentPane(gameOverView);
        this.validate();
        this.repaint();
    }

    public void exit() {
        this.controller.exitGame();
        // chiudi app
    }

    public Controller getController() {
        return this.controller;
    }
}
