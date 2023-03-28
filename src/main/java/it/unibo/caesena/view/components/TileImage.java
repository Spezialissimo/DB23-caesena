package it.unibo.caesena.view.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import it.unibo.caesena.model.meeple.Meeple;
import it.unibo.caesena.model.tile.Tile;
import it.unibo.caesena.model.tile.TileSection;
import it.unibo.caesena.utils.Pair;
import it.unibo.caesena.utils.ResourceUtil;
import net.coobird.thumbnailator.Thumbnails;

public class TileImage {

    private Tile tile;
    private Color color;
    public Optional<Pair<Meeple, TileSection>> meeple;

    TileImage(Tile tile, Color color) {
        this.tile = tile;
        this.color = color;
        this.meeple = Optional.empty();
    }

    TileImage(Tile tile) {
        this.tile = tile;
        this.meeple = Optional.empty();
        this.color = Color.BLACK;
    }

    public BufferedImage getAsBufferedImage() {
        BufferedImage image = null;
        try {
            image = ResourceUtil.getBufferedImage(tile.getTileType().name() + ".png", List.of("tiles"));
            image = Thumbnails.of(image).size(image.getWidth(), image.getHeight()).rotate(90*tile.getRotationCount()).asBufferedImage();
            if (meeple.isPresent()) {
                image = getTileImageWithMeeple(image);               
            }

        } catch (IOException e) {
            throw new IllegalStateException("Image path not valid");
        }

        return image;
    }

    private BufferedImage setColorForAllPixels(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x,y);
                if(pixel != 0) {
                    image.setRGB(x, y, color.getRGB());
                }
            }
        }
        return image;
    }

    public void addMeeple(Meeple meeple, TileSection section) {
        this.meeple = Optional.of(new Pair<>(meeple, section));
    }

    public BufferedImage getTileImageWithMeeple(BufferedImage image) {
        Graphics2D finalGraphics = image.createGraphics();
        finalGraphics.drawImage(image, 0, 0, null);
        if (this.meeple.isPresent()) {
            int meepleSize = (int)((double)image.getHeight(null)/5);
            int scaling = Image.SCALE_SMOOTH;
            BufferedImage meepleImage = setColorForAllPixels(ResourceUtil.getBufferedImage( "meepleBlank.png", List.of("meeple")));
            Image scaledMeepleImage = meepleImage.getScaledInstance(meepleSize, meepleSize, scaling);
            Pair<Integer, Integer> meeplePosition = getMeeplePosition(image.getHeight(null)-scaledMeepleImage.getHeight(null));
            finalGraphics.drawImage(scaledMeepleImage, meeplePosition.getX(), meeplePosition.getY(), null);
        }
        finalGraphics.dispose();
        return image;
    }

    private Pair<Integer, Integer> getMeeplePosition(int max) {
        final int closePadding = max/10;
        final int farPadding = max/5;
        final int centralPadding = max/2;
        return switch (meeple.get().getY()) {
            case CENTER -> new Pair<Integer,Integer>(centralPadding, centralPadding);
            case DOWN_CENTER -> new Pair<Integer,Integer>(centralPadding, max - closePadding);
            case DOWN_LEFT -> new Pair<Integer,Integer>(farPadding, max - closePadding);
            case DOWN_RIGHT -> new Pair<Integer,Integer>(max - farPadding, max - closePadding);
            case LEFT_CENTER -> new Pair<Integer,Integer>(closePadding, centralPadding);
            case LEFT_DOWN -> new Pair<Integer,Integer>(closePadding, max - farPadding);
            case LEFT_UP -> new Pair<Integer,Integer>(closePadding, farPadding);
            case RIGHT_CENTER -> new Pair<Integer,Integer>(max - closePadding, centralPadding);
            case RIGHT_DOWN -> new Pair<Integer,Integer>(max - closePadding, max - farPadding);
            case RIGHT_UP -> new Pair<Integer,Integer>(max - closePadding, farPadding);
            case UP_CENTER -> new Pair<Integer,Integer>(centralPadding, closePadding);
            case UP_LEFT -> new Pair<Integer,Integer>(farPadding, closePadding);
            case UP_RIGHT -> new Pair<Integer,Integer>(max - farPadding, closePadding);
        };
    }
}