package rustyice.game.lights.components;

import box2dLight.ConeLight;
import box2dLight.Light;
import com.badlogic.gdx.math.MathUtils;
import rustyice.editor.annotations.ComponentProperty;
import rustyice.game.GameObject;
import rustyice.game.Actor;

/**
 * @author gabek
 */
public class ConeLightComponent extends LightComponent{
    private GameObject parent;

    private transient ConeLight coneLight;
    private float coneDegree = 25;

    public ConeLightComponent(){
    }

    public ConeLightComponent(Actor parent){
        super();
        this.parent = parent;
    }

    @ComponentProperty(title = "Cone Degree")
    public void setConeDegree(float degree){
        this.coneDegree = degree;
        if(isInitialized()){
            store();
            init();
        }
    }

    @ComponentProperty(title = "Cone Degree")
    public float getConeDegree(){
        return coneDegree;
    }

    @Override
    protected Light buildLight() {
        return coneLight = new ConeLight(parent.getSection().getRayHandler(), getRayNum(getDistance(), LIGHT_RES, coneDegree), getColor(),
                getDistance(), 0, 0, 0, coneDegree);
    }

    private int getRayNum(float r, float space, float coneDegree) {
        return (int) ((coneDegree * MathUtils.PI2 * r) / (space * 180));
    }
}
