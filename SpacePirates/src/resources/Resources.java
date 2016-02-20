package resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Resources {
	public Skin skin;
	public TextureAtlas gameArt;
	public Texture box;
	
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
		loadBox();
		
		skin = assets.get(root+"gui.json", Skin.class);
		gameArt = assets.get(root+ "sprites.atlas", TextureAtlas.class);
	}
	
	private void loadBox(){
		Pixmap pbox = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
		pbox.setColor(Color.WHITE);
		pbox.fill();
		box = new Texture(pbox);
		pbox.dispose();
	}
	
	public void dispose(){
		assets.dispose();
		box.dispose();
	}
}