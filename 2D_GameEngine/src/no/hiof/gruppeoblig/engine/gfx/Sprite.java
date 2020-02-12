package no.hiof.gruppeoblig.engine.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite {
    private static BufferedImage spriteSheet;
    private static final int TILE_SIZE = 32;

    public static BufferedImage loadSprite(String file) {
        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(new File("res/" + file + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sprite;
    }

    public static BufferedImage getSprite(int xGrid, int yGrid) {
        if(spriteSheet == null) {
            spriteSheet = loadSprite("AnimationSpriteSheet");
        }
        return spriteSheet.getSubimage(xGrid * TILE_SIZE, yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
