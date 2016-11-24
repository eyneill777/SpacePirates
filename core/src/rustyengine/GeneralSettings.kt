package rustyengine;

import com.badlogic.gdx.Preferences;

object GeneralSettings {
    private const val APPID = "rustyice"

    var preferences: Preferences? = null
        set(value) {
            field = value

            if(value != null) {
                width = value.getInteger("${APPID}.width", width)
                height = value.getInteger("${APPID}.height", height)
                fullscreen = value.getBoolean("${APPID}.fullscreen", fullscreen)
                soundVolume = value.getFloat("${APPID}.soundVolume", soundVolume)
                musicVolume = value.getFloat("${APPID}.musicVolume", musicVolume)
                vsync = value.getBoolean("${APPID}.vsync", vsync)
            }
        }

    var width: Int = 800
        set(value) {
            field = value
            preferences?.putInteger("${APPID}.width", value)
        }

    var height: Int = 640
        set(value) {
            field = value
            preferences?.putInteger("${APPID}.width", value)
        }

    var soundVolume: Float = 1f
        set(value) {
            field = value
            preferences?.putFloat("${APPID}.soundVolume", value)
        }

    var musicVolume: Float = 1f
        set(value) {
            field = value
            preferences?.putFloat("${APPID}.musicVolume", value)
        }

    var vsync: Boolean = true
        set(value) {
            field = value
            preferences?.putBoolean("${APPID}.vsync", value)
        }

    var fullscreen: Boolean = false
        set(value) {
            field = value
            preferences?.putBoolean("${APPID}.fullscreen", value)
        }

    fun save(){
        preferences?.flush()
    }
}
