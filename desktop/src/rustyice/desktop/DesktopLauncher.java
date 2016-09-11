package rustyice.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import rustyice.core.Core;
import rustyice.core.GeneralSettings;

public class DesktopLauncher {
	public static void main(String[] arg) {

	    //Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
	    //config.useOpenGL3(true, 3, 2);
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		GeneralSettings settings = new GeneralSettings(new Lwjgl3Preferences("setting.perf", ".rustyice"));
		
		if(settings.isFullscreen()){
			//Graphics.DisplayMode mode = LwjglApplicationConfiguration.getDesktopDisplayMode();
			config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
			//config.fullscreen = true;
			//config.width = mode.width;
			//config.height = mode.height;
		} else {
		    config.setWindowedMode(settings.getWidth(), settings.getHeight());
			//config.width = settings.getWidth();
			//config.height = settings.getHeight();
		}



		config.useVsync(settings.isVSync());
		//config.vSyncEnabled = settings.isVSync();

		new Lwjgl3Application(new Core(settings), config);
	}
}
