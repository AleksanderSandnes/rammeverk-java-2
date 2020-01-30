package no.hiof.gruppeoblig.engine;

import no.hiof.gruppeoblig.engine.gfx.Font;
import no.hiof.gruppeoblig.engine.gfx.Image;
import no.hiof.gruppeoblig.engine.gfx.ImageRequest;
import no.hiof.gruppeoblig.engine.gfx.ImageTile;

import java.awt.*;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Renderer {
    private Font font = Font.STANDARD;
    private ArrayList<ImageRequest> imageRequests = new ArrayList<ImageRequest>();

    private int pixelWidth, pixelHeight;
    private int[] pixels;
    private int[] zBuffer;
    private int[] lightMap;
    private int[] lightBlock;

    private int ambientColor = 0xff6b6b6b;
    private int zDepth = 0;
    private boolean processing = false;

    public Renderer(GameContainer gameContainer) {
        pixelWidth = gameContainer.getWidth();
        pixelHeight = gameContainer.getHeight();
        pixels = ((DataBufferInt)gameContainer.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[pixels.length];
        lightMap = new int[pixels.length];
        lightBlock = new int[pixels.length];
    }

    public void clear() {
        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
            zBuffer[i] = 0;
            lightMap[i] = ambientColor;
            lightBlock[i] = 0;
        }
    }

    public void process() {
        processing = true;

        Collections.sort(imageRequests, new Comparator<ImageRequest>() {
            @Override
            public int compare(ImageRequest o1, ImageRequest o2) {
                if(o1.zDepth < o2.zDepth) {
                    return -1;
                }

                if(o1.zDepth > o2.zDepth) {
                    return 1;
                }
                return 0;
            }
        });

        for(int i = 0; i < imageRequests.size(); i++) {
            ImageRequest ir = imageRequests.get(i);
            setzDepth(ir.zDepth);
            drawImage(ir.image, ir.offX, ir.offY);
        }

        for(int i = 0; i < pixels.length; i++) {
            float r = ((lightMap[i] >> 16) & 0xff) / 255f;
            float g = ((lightMap[i] >> 8) & 0xff) / 255f;
            float b = (lightMap[i] & 0xff) / 255f;

            pixels[i] = (int)(((pixels[i] >> 16) & 0xff) * r) << 16 | (int)(((pixels[i] >> 8) & 0xff) * g) << 8 | (int)((pixels[i] & 0xff) * b);
        }

        imageRequests.clear();
        processing = false;
    }

    public void setPixel(int x, int y, int value) {
        int alpha = ((value >> 24) & 0xff);

        if((x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) || alpha == 0) {
            return;
        }

        int index = x + y * pixelWidth;

        if(zBuffer[index] > zDepth) {
            return;
        }

        zBuffer[index] = zDepth;

        if(alpha == 255) {
            pixels[index] = value;
        }
        else {
            int pixelColor = pixels[index];

            int newRed = ((pixelColor >> 16) & 0xff) + (int)((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int newGreen = ((pixelColor >> 8) & 0xff) + (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int newBlue = (pixelColor & 0xff) - (int)(((pixelColor & 0xff) - (value & 0xff)) * (alpha / 255f));

            pixels[index] = (newRed << 16 | newGreen << 8 | newBlue);
        }
    }

    public void setLightMap(int x, int y, int value) {
        if(x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) {
            return;
        }

        int baseColor = lightMap[x + y * pixelWidth];

        int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff);
        int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff);
        int maxBlue = Math.max(baseColor & 0xff, value & 0xff);

        lightMap[x + y * pixelWidth] = (maxRed << 16 | maxGreen << 8 | maxBlue);;
    }

    public void drawText(String text, int offX, int offY, int color) {
        //text = text.toUpperCase();
        int offset = 0;

        for(int i = 0; i < text.length(); i++) {
            //int unicode = text.codePointAt(i) - 32;
            int unicode = text.codePointAt(i);

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
        if(image.isAlpha() && !processing) {
            imageRequests.add(new ImageRequest(image, zDepth, offX, offY));
            return;
        }

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
        if(image.isAlpha() && !processing) {
            imageRequests.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offX, offY));
            return;
        }

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
        if (offX < 0) {newX -= offX;}
        if (offY < 0) {newY -= offY;}
        if (newWidth + offX >= pixelWidth) {newWidth -= newWidth + offX - pixelWidth;}
        if (newHeight + offY >= pixelHeight) {newHeight -= newHeight + offY - pixelHeight;}

        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWidth; x++) {
                setPixel(x + offX, y + offY, image.getPixels()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getWidth()]);
            }
        }
    }

    public void drawRect(int offX, int offY, int width, int height, int color) {
        for(int y = 0; y <= height; y++) {
            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);
        }

        for(int x = 0; x <= width; x++) {
            setPixel(x + offX, offY, color);
            setPixel(x + offX, offY + height, color);
        }
    }

    public void drawFillRect(int offX, int offY, int width, int height, int color) {
        //Don't render code
        if (offX < -width) return;
        if (offY < -height) return;
        if (offX >= pixelWidth) return;
        if (offY >= pixelHeight) return;

        int newX = 0;
        int newY = 0;
        int newWidth = width;
        int newHeight = height;

        //Clipping code
        if (offX < 0) {newX -= offX;}
        if (offY < 0) {newY -= offY;}
        if (newWidth + offX >= pixelWidth) {newWidth -= newWidth + offX - pixelWidth;}
        if (newHeight + offY >= pixelHeight) {newHeight -= newHeight + offY - pixelHeight;}

        for(int y = newY; y < newHeight; y++) {
            for(int x = newX; x < newWidth; x++) {
                setPixel(x + offX, y + offY, color);
            }
        }
    }

    public int getzDepth() {
        return zDepth;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }
}
