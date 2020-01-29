package no.hiof.gruppeoblig.engine;

public abstract class AbstractGame {
    public abstract void update(GameContainer gameContainer, float dt);
    public abstract void render(GameContainer gameContainer, Renderer renderer);
}
