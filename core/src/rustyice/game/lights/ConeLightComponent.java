package rustyice.game.lights;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.RayHandler;
import com.badlogic.gdx.math.MathUtils;
import rustyice.editor.annotations.ComponentProperty;

/**
 * @author gabek
 */
public class ConeLightComponent extends LightComponent{
    private transient ConeLight coneLight;
    private float coneDegree = 25;

    @ComponentProperty(title = "Cone Degree")
    public void setConeDegree(float degree){
        this.coneDegree = degree;
        rebuild();
    }

    @ComponentProperty(title = "Cone Degree")
    public float getConeDegree(){
        return coneDegree;
    }

    @Override
    protected Light buildLight(RayHandler rayHandler) {
        return coneLight = new ConeLight(rayHandler, getRayNum(getDistance(), LIGHT_RES, coneDegree), getColor(),
                getDistance(), getX(), getY(), getDirection(), coneDegree);
    }

    private int getRayNum(float r, float space, float coneDegree) {
        return (int) ((coneDegree * MathUtils.PI2 * r) / (space * 180));
    }
}
