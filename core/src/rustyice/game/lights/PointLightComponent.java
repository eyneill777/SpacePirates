package rustyice.game.lights;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author gabek
 */
public class PointLightComponent extends LightComponent{

    @Override
    protected Light buildLight(RayHandler rayHandler) {
        return new PointLight(rayHandler, getRayNum(getDistance(), LIGHT_RES),
                getColor(), getDistance(), getX(), getY());
    }

    private int getRayNum(float r, float space) {
        return (int) ((MathUtils.PI2 * r) / space);
    }
}
