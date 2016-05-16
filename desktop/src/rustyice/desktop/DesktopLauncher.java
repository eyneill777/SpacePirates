package rustyice.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import rustyice.main.GdxMain;
import rustyice.main.GeneralSettings;

public class DesktopLauncher {
	public static void main(String[] arg) {

	    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
	    //config.useOpenGL3(true, 3, 2);
	    
		FileHandle settingsFile = new FileHandle("settings.json");
		GeneralSettings settings;
		if (settingsFile.exists()) {
			settings = new Json().fromJson(GeneralSettings.class, settingsFile);
		} else {
			settings = new GeneralSettings();
		}
		
		if(settings.fullscreen){
		    config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		} else {
		    config.setWindowedMode(settings.width, settings.height);
		}

		config.useVsync(false);
		
		new Lwjgl3Application(new GdxMain(settings, settingsFile), config);
	}
}
