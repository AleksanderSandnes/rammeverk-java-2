package no.hiof.gruppeoblig.engine.gfx;

public class LightRequest {
    public Light light;
    public int locationX, locationY;

    public LightRequest(Light light, int locationX, int locationY) {
        this.light = light;
        this.locationX = locationX;
        this.locationY = locationY;
    }
}
