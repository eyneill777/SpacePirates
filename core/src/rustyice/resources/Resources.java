package rustyice.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Resources extends AssetManager{

    public TextureAtlas gameArt;
    public TextureRegion box, circle;
    public Resources() {
        setLoader(ShaderProgram.class, new ShaderLoader(getFileHandleResolver()));
    }
    
    public void startLoading(){
        load("art.atlas", TextureAtlas.class);
        //load("shaders/simple", ShaderProgram.class);
        load("gui/sfx/click3.wav", Sound.class);
    }
    
    public void loadAll() {
        finishLoading();
        // loadShapes();

        gameArt = get("art.atlas", TextureAtlas.class);
        box = gameArt.findRegion("rect");
        circle = gameArt.findRegion("circle");
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}