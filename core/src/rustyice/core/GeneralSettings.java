package rustyice.core;

import com.badlogic.gdx.Preferences;

public class GeneralSettings {
    public static final String APPID = "rustyice.";

    private Preferences preferences;
    private int width;
    private int height;
    private boolean fullscreen;
    private float soundVolume;
    private float musicVolume;
    private boolean vsync;

    public GeneralSettings(Preferences preferences){
        this.preferences = preferences;

        width = preferences.getInteger(APPID + "width", 800);
        height = preferences.getInteger(APPID + "height", 640);
        fullscreen = preferences.getBoolean(APPID + "fullscreen", false);
        soundVolume = preferences.getFloat(APPID + "soundVolume", 1);
        musicVolume = preferences.getFloat(APPID + "musicVolume", 1);
        vsync = preferences.getBoolean(APPID + "vsync", true);
    }

    public void save(){
        preferences.flush();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        preferences.putInteger(APPID + "width", width);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        preferences.putInteger(APPID + "height", height);
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        preferences.putBoolean(APPID + "fullscreen", fullscreen);
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
        preferences.putFloat(APPID + "sound", soundVolume);
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
        preferences.putFloat(APPID + "music", musicVolume);
    }

    public boolean isVsync() {
        return vsync;
    }

    public void setVSync(boolean vsync) {
        this.vsync = vsync;
        preferences.putBoolean(APPID + "vsync", vsync);
    }
}
