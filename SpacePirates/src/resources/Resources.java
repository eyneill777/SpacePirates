package resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Resources {
	public Skin skin;
	public TextureAtlas gameArt;
	
	private AssetManager assets;
	private String root;
	
	
	public Resources(){
		root = "res/";
		assets = new AssetManager();
	}
	
	public void loadAll(){
		assets.load(root+"gui.json", Skin.class);
		assets.load(root+"sprites.atlas", TextureAtlas.class);
		
		assets.finishLoading();
		
		skin = assets.get(root+"gui.json", Skin.class);
		gameArt = assets.get(root+ "sprites.atlas", TextureAtlas.class);
	}
	
	public void dispose(){
		assets.dispose();
	}
}
