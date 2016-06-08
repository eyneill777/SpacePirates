package rustyice.screens.effects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import rustyice.core.Core;

public class ClickSound extends ClickListener{
    private Sound effect;
    private static ClickSound defaultSound;
    
    public static ClickSound getDefaultSound(){
        if(defaultSound == null){
            defaultSound = new ClickSound(Core.resources.get("gui/sfx/click3.wav"));
        }
        return defaultSound;
    }
    
    public static void addDefault(Actor target){
        target.addListener(getDefaultSound());
    }
    
    public ClickSound(Sound effect){
        this.effect = effect;
    }
    
    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        effect.play(Core.settings.getSoundVolume());
    }
}
