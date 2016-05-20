package rustyice.game.lights.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import rustyice.game.actors.Actor;

/**
 * @author gabek
 */
public class PointLightComponent implements LightComponent{
    private transient box2dLight.PointLight pointLight;

    private boolean initialized = false;
    private Actor parent;
    private float lightRes = 0.25f;
    private float distance = 5;
    private Color color = Color.WHITE;
    private boolean isStatic = false;
    private boolean isXRay = false;


    public PointLightComponent(){
    }

    public PointLightComponent(Actor parent){
        this.parent = parent;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        if (initialized) {
            pointLight.setColor(color);
        }
    }

    @Override
    public void setStaticLight(boolean isStatic) {
        this.isStatic = isStatic;
        if(initialized){
            pointLight.setStaticLight(isStatic);
        }
    }

    @Override
    public boolean isStaticLight() {
        return isStatic;
    }

    @Override
    public void init() {
        if(!initialized){
            initialized = true;
            pointLight = new box2dLight.PointLight(parent.getSection().getRayHandler(), getRayNum(distance, lightRes));
            pointLight.setColor(color);
            pointLight.setDistance(distance);
            pointLight.setPosition(parent.getX(), parent.getY());
            pointLight.setStaticLight(isStatic);
            pointLight.setXray(isXRay);
        }
    }

    @Override
    public void store(){
        if(initialized){
            initialized = false;
            pointLight.remove(true);
            pointLight = null;
        }
    }

    private int getRayNum(float r, float space) {
        return (int) ((MathUtils.PI2 * r) / space);
    }

    @Override
    public void setPosition(float x, float y){
        if(initialized){
            pointLight.setPosition(x, y);
        }
    }
}
