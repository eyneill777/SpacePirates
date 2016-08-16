package rustyice.game.lights.components;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.math.MathUtils;
import rustyice.game.GameObject;
import rustyice.game.Actor;

/**
 * @author gabek
 */
public class PointLightComponent extends LightComponent{
    private GameObject parent;


    public PointLightComponent(){
    }

    public PointLightComponent(GameObject parent){
        super();
        this.parent = parent;
    }

    @Override
    protected Light buildLight(RayHandler rayHandler) {
        return new PointLight(parent.getSection().getRayHandler(), getRayNum(getDistance(), LIGHT_RES),
                getColor(), getDistance(), getX(), getY());
    }

    private int getRayNum(float r, float space) {
        return (int) ((MathUtils.PI2 * r) / space);
    }
}
