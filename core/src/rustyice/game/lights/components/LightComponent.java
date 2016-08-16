package rustyice.game.lights.components;

import box2dLight.Light;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import rustyice.editor.annotations.ComponentProperty;
import rustyice.game.GameObject;
import rustyice.game.physics.FillterFlags;

public abstract class LightComponent {
    static final float LIGHT_RES = 1f;
    private static final Color DEFAULT_COLOR = new Color(.75f, .75f, .75f, .75f);

    private transient boolean initialized = false;
    private transient Light light;
    private transient GameObject parent;

    private Color color;
    private boolean isStatic;
    private boolean isXRay;
    private float distance;
    private float direction;
    private float x, y;

    public LightComponent(){
        color = DEFAULT_COLOR;
        isStatic = false;
        isXRay = false;
        distance = 10;
    }

    @ComponentProperty(title = "Color")
    public Color getColor() {
        return color;
    }

    @ComponentProperty(title = "Color")
    public void setColor(Color color) {
        this.color = color;
        if(initialized){
            light.setColor(color);
        }
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        if(initialized){
            light.setPosition(x, y);
        }
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void setDirection(float direction){
        this.direction = direction;
        if(initialized){
            light.setDirection(direction);
        }
    }


    public float getDirection(){
        return direction;
    }

    @ComponentProperty(title = "Static")
    public void setStaticLight(boolean isStatic) {
        this.isStatic = isStatic;
        if(initialized){
            light.setStaticLight(isStatic);
        }
    }

    @ComponentProperty(title = "Static")
    public boolean isStaticLight() {
        return isStatic;
    }

    @ComponentProperty(title = "XRay")
    public void setXRay(boolean xRay){
        isXRay = xRay;
        if(initialized){
            light.setXray(isXRay);
        }
    }

    @ComponentProperty(title = "XRay")
    public boolean isXRay(){
        return isXRay;
    }

    @ComponentProperty(title = "Distance")
    public float getDistance() {
        return distance;
    }

    @ComponentProperty(title = "Distance")
    public void setDistance(float distance) {
        this.distance = distance;
        rebuild();
    }

    protected abstract Light buildLight(RayHandler rayHandler);

    public boolean isInitialized(){
        return initialized;
    }

    public void init(GameObject parent){
        if(!initialized){
            initialized = true;
            this.parent = parent;

            light = buildLight(parent.getSection().getRayHandler());

            light.setContactFilter(FillterFlags.LIGHT, (short)0, FillterFlags.OPAQUE);
            light.setColor(color);
            light.setDistance(distance);
            light.setPosition(x, y);
            light.setStaticLight(isStatic);
            light.setXray(isXRay);
            light.setDirection(direction);

            //light.setSoft(false);
        }
    }

    public void rebuild(){
        if(isInitialized()){
            store();
            init(parent);
        }
    }

    public void store(){
        if(initialized){
            initialized = false;

            light.remove();
            light = null;
        }
    }
}
