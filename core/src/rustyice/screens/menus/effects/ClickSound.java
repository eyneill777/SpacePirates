package rustyice.screens.menus.effects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ClickSound extends ClickListener{
    private Sound effect;
    
    public ClickSound(Sound effect){
        this.effect = effect;
    }
    
    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        effect.play();
    }
}
