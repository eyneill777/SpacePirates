package rustyice.game.lights.components;

import box2dLight.Light;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import rustyice.editor.annotations.ComponentProperty;
import rustyice.game.GameObject;
import rustyice.game.actors.Actor;

/**
 * @author gabek
 */
public class PointLightComponent extends LightComponent{
    private GameObject parent;


    public PointLightComponent(){
    }

    public PointLightComponent(Actor parent){
        super();
        this.parent = parent;
    }

    @Override
    protected Light buildLight() {
        return new PointLight(parent.getSection().getRayHandler(), getRayNum(getDistance(), LIGHT_RES));
    }

    private int getRayNum(float r, float space) {
        return (int) ((MathUtils.PI2 * r) / space);
    }
}
