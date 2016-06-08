package rustyice.screens.effects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Static constructors for tween effects.
 * @author gabek
 */
public class GuiEffects {
    
    
    
    /**
     * First sets alpha to 0 then transitions to 1, much call start(tweenmaneger) to start the effect.
     * @param target the widget to fading in.
     * @param duration in seconds.
     * @param callback called after the effect if finished.
     * @return the tween which can be sequenced or started.
     */
    public static Timeline fadeIn(Actor target, float duration, Runnable callback){
        return fadeIn(target, duration).setCallback(
                (int flag, BaseTween<?> tween) ->{callback.run();});
    }
    
    /**
     * Transitions alpha to 0, much call start(tweenmaneger) to start the effect.
     * @param target the widget to fadeing out.
     * @param duration in seconds.
     * @param callback called after the effect if finished.
     * @return the tween which can be sequenced or started.
     */
    public static Tween fadeOut(Actor target, float duration, Runnable callback){
        return fadeOut(target, duration).setCallback(
                (int flag, BaseTween<?> tween) ->{callback.run();});
    }
    
    /**
     * First sets alpha to 0 then transitions to 1, much call start(tweenmaneger) to start the effect.
     * @param target the widget to fading in.
     * @param duration in seconds.
     * @param callback called after the effect if finished.
     * @return the tween which can be sequenced or started.
     */
    public static Timeline fadeIn(Actor target, float duration){
        return Timeline.createSequence().push(Tween.set(target, GuiAccessor.ALPHA).target(0)).
                push(Tween.to(target, GuiAccessor.ALPHA, duration).target(1));
    }
    
    /**
     * Transitions alpha to 0, much call start(tweenmaneger) to start the effect.
     * @param target the widget to fadeing out.
     * @param duration in seconds.
     * @param callback called after the effect if finished.
     * @return the tween which can be sequenced or started.
     */
    public static Tween fadeOut(Actor target, float duration){
        return Tween.to(target, GuiAccessor.ALPHA, duration).target(0);
    }
}
