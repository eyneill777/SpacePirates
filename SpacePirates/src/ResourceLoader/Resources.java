package ResourceLoader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Resources {
	public Skin skin;
	
	private AssetManager assets;
	private String root;
	
	public Resources(){
		root = "res/";
		assets = new AssetManager();
	}
	
	public void loadAll(){
		assets.load(root+"gui.json", Skin.class);
		
		assets.finishLoading();
		
		skin = assets.get(root+"gui.json", Skin.class);
	}
	
	public void dispose(){
		assets.dispose();
	}
}
