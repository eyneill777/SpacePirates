package spacepirates.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;

public class GeneralSettings {
	public int width = 800, height = 800;
	public boolean fullscreen = false;
	
	public void updateDisplayMode(){
		if(fullscreen){
			DisplayMode desktop = Gdx.graphics.getDesktopDisplayMode();
			Gdx.graphics.setDisplayMode(desktop.width, desktop.height, true);
		} else {
			Gdx.graphics.setDisplayMode(width, height, false);
		}
	}
}
