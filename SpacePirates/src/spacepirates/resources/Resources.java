package spacepirates.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Resources {
	public Skin skin;
	public TextureAtlas gameArt;
	public Texture box, circle;
	
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
		loadShapes();
		
		skin = assets.get(root+"gui.json", Skin.class);
		gameArt = assets.get(root+ "sprites.atlas", TextureAtlas.class);
	}
	
	private void loadShapes(){
		Pixmap pbox = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
		pbox.setColor(Color.WHITE);
		pbox.fillCircle(64, 64, 64 );
		circle = new Texture(pbox);
		
		pbox.fill();
		box = new Texture(pbox);
		pbox.dispose();
	}
	
	public void dispose(){
		assets.dispose();
		box.dispose();
		circle.dispose();
	}
}