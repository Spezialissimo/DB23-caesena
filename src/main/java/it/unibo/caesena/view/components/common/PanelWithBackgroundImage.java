package it.unibo.caesena.view.components.common;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * A class defining a JPanel with an Image as background.
 */
public class PanelWithBackgroundImage extends JPanel {

    private final BufferedImage image;

    /**
     * Public constructor that accepts an image to be placed as background.
     *
     * @param image to set as background
     */
    public PanelWithBackgroundImage(final BufferedImage image) {
        super();
        this.image = image;
    }

    /**
     * {@inheritDoc}
     *
     * Places the image in the background without stretching it.
     */
    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        double ratioWidht = (double)this.getWidth() / (double)image.getWidth();
        double ratioHeight = (double)this.getHeight() / (double)image.getHeight();
        double width = image.getWidth() * (ratioHeight > ratioWidht ? ratioHeight : ratioWidht);
        double height = image.getHeight() * (ratioHeight > ratioWidht ? ratioHeight : ratioWidht);
        graphics.drawImage(image, 0, 0,
                (int) Math.round(width),
                (int) Math.round(height), null);
    }
}
