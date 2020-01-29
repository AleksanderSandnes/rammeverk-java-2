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
    private ImageTile image;
    private SoundClip clip;

    public GameManager() {
        image = new ImageTile("/test.png", 16, 16);
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
        renderer.drawImageTile(image, gameContainer.getInput().getMouseX() -8, gameContainer.getInput().getMouseY() -16, (int)temp, 0);
    }

    public static void main(String args[]) {
        GameContainer gameContainer = new GameContainer(new GameManager());
        gameContainer.start();
    }
}
