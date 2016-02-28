package spacepirates.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Resources {
	public Skin skin;
	public TextureAtlas gameArt;
	public TextureRegion box, circle;
	
	private Texture shapes;
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
		Pixmap pbox = new Pixmap(256, 128, Pixmap.Format.RGBA8888);
		pbox.setColor(Color.WHITE);
		pbox.fillCircle(192, 64, 64);
		
		pbox.fillRectangle(0, 0, 128, 128);
		shapes = new Texture(pbox);
		pbox.dispose();
		
		box = new TextureRegion(shapes, 0, 0, 128, 128);
		circle = new TextureRegion(shapes, 128, 0, 128, 128);
	}
	
	public void dispose(){
		assets.dispose();
		shapes.dispose();
	}
}