package rustyice.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import rustyice.core.Core;
import rustyice.core.GeneralSettings;

public class DesktopLauncher {
	public static void main(String[] arg) {

	    //Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
	    //config.useOpenGL3(true, 3, 2);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		GeneralSettings settings = new GeneralSettings(new LwjglPreferences("setting.perf", ".rustyice"));
		
		if(settings.isFullscreen()){
			Graphics.DisplayMode mode = LwjglApplicationConfiguration.getDesktopDisplayMode();
			//config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
			config.fullscreen = true;
			config.width = mode.width;
			config.height = mode.height;
		} else {
		    //config.setWindowedMode(settings.getWidth(), settings.getHeight());
			config.width = settings.getWidth();
			config.height = settings.getHeight();
		}



		//config.useVsync(settings.isVSync());
		config.vSyncEnabled = settings.isVSync();

		new LwjglApplication(new Core(settings), config);
	}
}
