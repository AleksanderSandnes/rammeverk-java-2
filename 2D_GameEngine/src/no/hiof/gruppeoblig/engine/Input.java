package no.hiof.gruppeoblig.engine;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private GameContainer gameContainer;
    private Animation animation;

    private final int NUM_KEYS = 256;
    private boolean[] keys = new boolean[NUM_KEYS];
    private boolean[] keysLast = new boolean[NUM_KEYS];

    private final int NUM_BUTTONS = 5;
    private boolean[] buttons = new boolean[NUM_BUTTONS];
    private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    private int mouseX, mouseY;
    private int scroll;

    public Input(GameContainer gameContainer, Animation animation) {
        this.gameContainer = gameContainer;
        this.animation = animation;
        mouseX = 0;
        mouseY = 0;
        scroll = 0;

        gameContainer.getWindow().getCanvas().addKeyListener(this);
        gameContainer.getWindow().getCanvas().addMouseListener(this);
        gameContainer.getWindow().getCanvas().addMouseMotionListener(this);
        gameContainer.getWindow().getCanvas().addMouseWheelListener(this);
    }

    public void update() {
        scroll = 0;

        for(int i = 0; i < NUM_KEYS; i++) {
            keysLast[i] = keys[i];
        }

        for(int i = 0; i < NUM_BUTTONS; i++) {
            buttonsLast[i] = buttons[i];
        }
    }

    public boolean isKey(int keyCode) {
        return keys[keyCode];
    }

    public boolean isKeyUp(int keyCode) {
        return !keys[keyCode] && keysLast[keyCode];
    }

    public boolean isKeyDown(int keyCode) {
        return keys[keyCode] && !keysLast[keyCode];
    }

    public boolean isButton(int button) {
        return buttons[button];
    }

    public boolean isButtonUp(int button) {
        return !buttons[button] && buttonsLast[button];
    }

    public boolean isButtonDown(int button) {
        return buttons[button] && !buttonsLast[button];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
        animation = walkLeft;
        animation.start();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
        animation.stop();
        animation.reset();
        animation = standing;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = (int)(e.getX() / gameContainer.getScale());
        mouseY = (int)(e.getY() / gameContainer.getScale());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int)(e.getX() / gameContainer.getScale());
        mouseY = (int)(e.getY() / gameContainer.getScale());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = e.getWheelRotation();
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getScroll() {
        return scroll;
    }


}
