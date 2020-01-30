package no.hiof.gruppeoblig.engine.gfx;

import java.awt.*;

public class ImageTile extends Image {
    private int tileW, tileH;

    public ImageTile(String path, int tileW, int tileH) {
        super(path);
        this.tileW = tileW;
        this.tileH = tileH;
    }

    public Image getTileImage(int tileX, int tileY) {
        int[] pixels = new int[tileW * tileH];

        for(int y = 0; y < tileW; y++) {
            for(int x = 0; x < tileH; x++) {
                pixels[x + y * tileW] = this.getPixels()[(x + tileX * tileW) + (y + tileY + tileW) * this.getWidth()];
            }
        }

        return new Image(pixels, tileW, tileH);
    }

    public int getTileW() {
        return tileW;
    }

    public void setTileW(int tileW) {
        this.tileW = tileW;
    }

    public int getTileH() {
        return tileH;
    }

    public void setTileH(int tileH) {
        this.tileH = tileH;
    }
}
