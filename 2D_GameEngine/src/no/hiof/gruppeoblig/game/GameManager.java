package no.hiof.gruppeoblig.game;

import no.hiof.gruppeoblig.engine.AbstractGame;
import no.hiof.gruppeoblig.engine.GameContainer;
import no.hiof.gruppeoblig.engine.Renderer;
import no.hiof.gruppeoblig.engine.audio.SoundClip;
import no.hiof.gruppeoblig.engine.gfx.Image;
import no.hiof.gruppeoblig.engine.gfx.ImageTile;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class GameManager extends AbstractGame {
    private ImageTile image1;
    private Image image2;
    private ImageTile image3;
    private SoundClip clip;

    public GameManager() {
        //image1 = new ImageTile("/wood.png", 16, 16);
        image2 = new Image("/light.png");
        image2.setAlpha(true);
        image3 = new ImageTile("/white.png", 16, 16);
        image3.setAlpha(true);
        clip = new SoundClip("/audio/minecraft.wav");

    }

    @Override
    public void update(GameContainer gameContainer, float dt) {
        if(gameContainer.getInput().isKeyDown(KeyEvent.VK_A)) {
            clip.play();
        }

        temp += dt * 20;

        if(temp > 3) {
            temp = 0;
        }
    }

    float temp = 0;

    @Override
    public void render(GameContainer gameContainer, Renderer renderer) {
        //renderer.drawImageTile(image1, gameContainer.getInput().getMouseX() -8, gameContainer.getInput().getMouseY() -16, (int)temp, 0);
        //renderer.drawFillRect(gameContainer.getInput().getMouseX() - 16, gameContainer.getInput().getMouseY() - 16, 32, 32, 0xffffccff);

        for(int x = 0; x < image2.getWidth(); x++) {
            for(int y = 0; y < image2.getHeight(); y++) {
                renderer.setLightMap(x, y, image2.getPixels()[x + y * image2.getWidth()]);
            }
        }

        renderer.setzDepth(0);
        //renderer.drawImageTile(image3, gameContainer.getInput().getMouseX(), gameContainer.getInput().getMouseY(), 1, 1);
        renderer.drawImage(image3, gameContainer.getInput().getMouseX(), gameContainer.getInput().getMouseY());
        //renderer.setzDepth(0);
        //renderer.drawImage(image2, 10, 10);
    }

    public static void main(String args[]) {
        GameContainer gameContainer = new GameContainer(new GameManager());
        gameContainer.start();
    }
}
