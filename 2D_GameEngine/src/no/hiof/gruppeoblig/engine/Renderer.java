package no.hiof.gruppeoblig.engine;

import no.hiof.gruppeoblig.engine.gfx.Font;
import no.hiof.gruppeoblig.engine.gfx.Image;
import no.hiof.gruppeoblig.engine.gfx.ImageTile;

import java.awt.*;
import java.awt.image.DataBufferInt;

public class Renderer {
    private int pixelWidth, pixelHeight;
    private int[] pixels;

    private Font font = Font.STANDARD;

    public Renderer(GameContainer gameContainer) {
        pixelWidth = gameContainer.getWidth();
        pixelHeight = gameContainer.getHeight();
        pixels = ((DataBufferInt)gameContainer.getWindow().getImage().getRaster().getDataBuffer()).getData();
    }

    public void clear() {
        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }

    public void setPixel(int x, int y, int value) {
        if((x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) || value == 0xffff00ff) {
            return;
        }

        pixels[x + y * pixelWidth] = value;
    }

    public void drawText(String text, int offX, int offY, int color) {
        text = text.toUpperCase();
        int offset = 0;

        for(int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i) - 32;

            for(int y = 0; y < font.getFontImage().getHeight(); y++) {
                for(int x = 0; x < font.getWidths()[unicode]; x++) {
                    if(font.getFontImage().getPixels()[x + font.getOffsets()[unicode] + y * font.getFontImage().getWidth()] == 0xffffffff) {
                        setPixel(x + offX + offset, y + offY, color);
                    }
                }
            }
            offset += font.getWidths()[unicode];
        }
    }

    public void drawImage(Image image, int offX, int offY) {
        //Don't Render Code
        if(offX < -image.getWidth()) return;
        if(offY < -image.getWidth()) return;
        if(offX >= pixelWidth - 48) return;
        if(offY >= pixelHeight - 48) return;

        int newX = 0;
        int newY = 0;
        int newWidth = image.getWidth();
        int newHeight = image.getHeight();

        //Clipping code
        if(offX < 0) {newX -= offX;}
        if(offY < 0) {newY -= offY;}

        if(newWidth + offX > pixelWidth) {
            newWidth -= newWidth + offX - pixelWidth;
        }

        if(newHeight + offY > pixelHeight) {
            newHeight -= newHeight + offY - pixelHeight;
        }

        for(int y = newY; y < newHeight; y++) {
            for(int x = newX; x < newWidth; x++) {
                setPixel(x + offX, y + offY, image.getPixels()[x + y * image.getWidth()]);
            }
        }
    }

    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY) {
        //Don't render code
        if (offX < -image.getTileW()) return;
        if (offY < -image.getTileH()) return;
        if (offX >= pixelWidth) return;
        if (offY >= pixelHeight) return;

        int newX = 0;
        int newY = 0;
        int newWidth = image.getTileW();
        int newHeight = image.getTileH();

        //Clipping code
        if (offX < 0) {
            newX -= offX;
        }
        if (offY < 0) {
            newY -= offY;
        }
        if (newWidth + offX >= pixelWidth) {
            newWidth -= newWidth + offX - pixelWidth;
        }
        if (newHeight + offY >= pixelHeight) {
            newHeight -= newHeight + offY - pixelHeight;
        }

        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWidth; x++) {
                setPixel(x + offX, y + offY, image.getPixels()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getWidth()]);
            }
        }
    }
}
