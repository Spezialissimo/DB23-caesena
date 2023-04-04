package it.unibo.caesena.view.scene;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.caesena.utils.ResourceUtil;
import it.unibo.caesena.view.GUI;
import it.unibo.caesena.view.LocaleHelper;
import it.unibo.caesena.view.components.common.ModalPanel;

/**
 * A class defining the pause menu for the game.
 */
public class PauseScene implements Scene<JPanel> {
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255, 80);
    private final GUI userInterface;
    private final JPanel mainPanel;

    /**
     * Public constructor that sets up the components and places them.
     *
     * @param userInterface the interface in which this scene is displayed
     */
    public PauseScene(final GUI userInterface) {
        this.userInterface = userInterface;

        this.mainPanel = new JPanel();
        this.mainPanel.setBackground(BACKGROUND_COLOR);

        final JPanel modal = new ModalPanel(ResourceUtil.getBufferedImage("background_Modal.png", List.of()), true);
        modal.setOpaque(false);
        modal.setLayout(new BoxLayout(modal, BoxLayout.Y_AXIS));

        final JLabel titleLbl = new JLabel(LocaleHelper.getSceneTitle("PauseScene", false));
        titleLbl.setFont(GUI.BIG_BOLD_FONT);
        titleLbl.setBorder(BorderFactory.createEmptyBorder(GUI.DEFAULT_PADDING * 10, 0, GUI.DEFAULT_PADDING, 0));
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new GridBagLayout());
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(GUI.DEFAULT_PADDING, 0, GUI.DEFAULT_PADDING, 0);

        final JButton resumeGameButton = new JButton(LocaleHelper.getResumeGameText());
        resumeGameButton.setFont(GUI.MEDIUM_BOLD_FONT);
        resumeGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resumeGameButton.addActionListener((e) -> {
            userInterface.togglePauseScene();
        });
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        buttonsPanel.add(resumeGameButton, gridBagConstraints);

        final JButton backToStartMenuButton = new JButton(LocaleHelper.getBackToStartMenuText());
        backToStartMenuButton.setFont(GUI.MEDIUM_BOLD_FONT);
        backToStartMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToStartMenuButton.addActionListener((e) -> {
            userInterface.showBackTostartSceneDialog();
        });
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        buttonsPanel.add(backToStartMenuButton, gridBagConstraints);

        final JButton exitButton = new JButton(LocaleHelper.getExitApplicationText());
        exitButton.setFont(GUI.MEDIUM_BOLD_FONT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener((e) -> {
            userInterface.showExitDialog();
        });
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        buttonsPanel.add(exitButton, gridBagConstraints);

        modal.add(titleLbl);
        modal.add(buttonsPanel);
        this.mainPanel.setLayout(new GridBagLayout());
        this.mainPanel.add(modal);
        this.setVisible(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JPanel getComponent() {
        return this.mainPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final GUI getUserInterface() {
        return this.userInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
    }

    @Override
    public boolean isVisible() {
        return this.mainPanel.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        this.mainPanel.setVisible(visible);
    }
}
