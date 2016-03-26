package rustyice.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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
		root = "";
		assets = new AssetManager();
	}
	
	public void loadAll(){
		assets.load("gui.json", Skin.class);
		assets.load("sprites.atlas", TextureAtlas.class);

		assets.finishLoading();
		loadShapes();
		
		skin = assets.get("gui.json", Skin.class);
		gameArt = assets.get("sprites.atlas", TextureAtlas.class);
	}
	
	private void loadShapes(){
		Pixmap pbox = new Pixmap(136, 128, Pixmap.Format.RGBA8888);
		pbox.setColor(Color.WHITE);
		pbox.fillCircle(64, 64, 64);
		
		pbox.fillRectangle(129, 0, 4, 4);
		shapes = new Texture(pbox);
		pbox.dispose();
		
		shapes.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		circle = new TextureRegion(shapes, 0, 0, 128, 128);
		box = new TextureRegion(shapes, 129, 0, 4, 4);
	}
	
	public void dispose(){
		assets.dispose();
		shapes.dispose();
	}
}