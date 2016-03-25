package gfen.rustyice.desktop;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import gken.rustyice.main.GdxMain;
import gken.rustyice.main.GeneralSettings;

import java.awt.*;


public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        //config.useGL30 = true;

		Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();

		FileHandle settingsFile = new FileHandle("settings.json");
		GeneralSettings settings;
		if(settingsFile.exists()){
			settings = new Json().fromJson(GeneralSettings.class, settingsFile);
		} else {
			settings = new GeneralSettings();
		}

		if(settings.fullscreen){
            config.width = windowSize.width;
            config.height = windowSize.height;
            config.fullscreen = true;
        } else {
            config.width = settings.width;
            config.height = settings.height;
        }

        config.addIcon("icon.png", Files.FileType.Internal);
        
        //config.vSyncEnabled = false;
        //config.foregroundFPS = 0;
        
		new LwjglApplication(new GdxMain(settings, settingsFile), config);
	}
}
