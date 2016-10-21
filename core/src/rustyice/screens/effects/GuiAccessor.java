package rustyice.screens.effects;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class GuiAccessor implements TweenAccessor<Actor> {
    public static final int ALPHA = 0;
    
    
    @Override
    public int getValues(Actor target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;
        }
        assert false; return -1;
    }

    @Override
    public void setValues(Actor target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ALPHA:
                target.getColor().a = newValues[0];
                break;
            default:
                break;
        }
        assert false;
    }

}
