package no.hiof.gruppeoblig.engine;

import no.hiof.gruppeoblig.engine.gfx.Sprite;

import java.awt.image.BufferedImage;

public class Player {
    private BufferedImage[] walkingLeft = {Sprite.getSprite(0, 1), Sprite.getSprite(2, 1)};
    private BufferedImage[] walkingRight = {Sprite.getSprite(0, 2), Sprite.getSprite(2, 2)};
    private BufferedImage[] standing = {Sprite.getSprite(1, 0)};

    private Animation walkLeft = new Animation(walkingLeft, 10);
    private Animation walkRight = new Animation(walkingRight, 10);
    private Animation stand = new Animation(standing, 10);

    private Animation animation = standing;
}
