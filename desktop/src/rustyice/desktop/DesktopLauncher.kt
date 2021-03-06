package rustyice.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences
import rustyice.Core
import rustyengine.GeneralSettings

fun main(args: Array<String>) {
    //Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

    val config = LwjglApplicationConfiguration()
    config.useGL30 = true


    GeneralSettings.preferences =  LwjglPreferences("setting.perf", ".rustyice")

    if(GeneralSettings.fullscreen){
        val mode = LwjglApplicationConfiguration.getDesktopDisplayMode()
        //config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        config.fullscreen = true
        config.width = mode.width
        config.height = mode.height
    } else {
        //config.setWindowedMode(settings.getWidth(), settings.getHeight());
        config.width = GeneralSettings.width
        config.height = GeneralSettings.height
    }



    //config.useVsync(settings.isVSync());
    config.vSyncEnabled = GeneralSettings.vsync

    LwjglApplication(Core, config)
}