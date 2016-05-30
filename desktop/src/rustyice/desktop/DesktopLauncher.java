package rustyice.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import rustyice.core.Core;
import rustyice.core.GeneralSettings;

public class DesktopLauncher {
	public static void main(String[] arg) {

	    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
	    //config.useOpenGL3(true, 3, 2);

		GeneralSettings settings = new GeneralSettings(new Lwjgl3Preferences("setting.perf", ".rustyice"));
		
		if(settings.isFullscreen()){
			config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		} else {
		    config.setWindowedMode(settings.getWidth(), settings.getHeight());
		}

		config.useVsync(settings.isVSync());
		
		new Lwjgl3Application(new Core(settings), config);
	}
}
