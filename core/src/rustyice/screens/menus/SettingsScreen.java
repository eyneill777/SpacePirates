package rustyice.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.*;
import rustyice.core.Core;
import rustyice.screens.Screen;
import rustyice.screens.menus.effects.ClickSound;
import rustyice.screens.menus.effects.GuiEffects;

public class SettingsScreen extends Screen {

    private static final float BUTT_WIDTH = 150;
    private static final float PAD = 5;
    private static final float FADE_DURATION = 0.5f;

    private ButtonGroup<TextButton> catButtions;
    private VisTable root;
    private VisTable categoryPane;
    private VisTable settingPane;
    private VisTable bottemPane;

    private VisCheckBox fullscreenCheck;
    private VisCheckBox vsyncCheck;

    private VisSlider soundSlider;
    private VisLabel soundLabel;
    private VisLabel soundLevelLabel;
    private VisSlider musicSlider;
    private VisLabel musicLabel;
    private VisLabel musicLevelLabel;

    private TextButton videoButt, audioButt, inputButt;

    @Override
    public void load() {
        super.load();
        
        root = new VisTable();
        root.setFillParent(true);

        categoryPane = new VisTable();
        settingPane = new VisTable();
        bottemPane = new VisTable();

        videoButt = new VisTextButton("Video", "toggle");
        videoButt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                switchToVideo();
            }
        });
        ClickSound.addDefault(videoButt);

        audioButt = new VisTextButton("Audio", "toggle");
        audioButt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchAudio();
            }
        });
        ClickSound.addDefault(audioButt);
        
        inputButt = new VisTextButton("Input", "toggle");
        inputButt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchInput();
            }
        });
        ClickSound.addDefault(inputButt);

        VisTextButton backButt = new VisTextButton("Back");
        backButt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getManager().popScreen();
            }
        });
        ClickSound.addDefault(backButt);

        VisTextButton applyButt = new VisTextButton("Apply");
        applyButt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                apply();
            }
        });
        ClickSound.addDefault(applyButt);

        catButtions = new ButtonGroup<>(videoButt, audioButt, inputButt);
        catButtions.setMinCheckCount(1);
        catButtions.setMaxCheckCount(1);

        loadVideo();
        loadAudio();

        categoryPane.add(videoButt).prefWidth(BUTT_WIDTH).pad(PAD).row();
        categoryPane.add(audioButt).prefWidth(BUTT_WIDTH).pad(PAD).row();
        categoryPane.add(inputButt).prefWidth(BUTT_WIDTH).pad(PAD);

        bottemPane.add(backButt).expandX();
        bottemPane.add(applyButt).expandX();

        root.add(categoryPane).fillY().right();
        root.add(settingPane).fill().prefSize(800, 640).row();
        root.add(bottemPane).colspan(2).fillX();
        // root.setDebug(true, true);
    }

    private void loadVideo() {
        fullscreenCheck = new VisCheckBox("Fullscreen");
        fullscreenCheck.setChecked(Core.settings.isFullscreen());
        ClickSound.addDefault(fullscreenCheck);

        vsyncCheck = new VisCheckBox("VSync");
        vsyncCheck.setChecked(Core.settings.isVsync());
        ClickSound.addDefault(vsyncCheck);
    }

    private void loadAudio(){
        soundSlider = new VisSlider(0, 1, 0.1f, false);
        soundLabel = new VisLabel("Sound: ");
        soundLevelLabel = new VisLabel("");

        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundLevelLabel.setText(String.valueOf((int)(soundSlider.getValue() * 100)) + "%");
                Core.settings.setSoundVolume(soundSlider.getValue());
            }
        });

        musicSlider = new VisSlider(0, 1, 0.1f, false);
        musicLabel = new VisLabel("Music: ");
        musicLevelLabel = new VisLabel("");

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicLevelLabel.setText(String.valueOf((int)(musicSlider.getValue() * 100)) + "%");
                Core.settings.setMusicVolume(musicSlider.getValue());
            }
        });
    }

    private void switchToVideo() {
        settingPane.clear();
        settingPane.add(fullscreenCheck);
        settingPane.add(vsyncCheck);
    }

    private void switchAudio() {
        settingPane.clear();
        settingPane.add(soundLabel).pad(20);
        settingPane.add(soundSlider).pad(20).prefWidth(100);
        settingPane.add(soundLevelLabel).pad(20).row();

        settingPane.add(musicLabel).pad(20);
        settingPane.add(musicSlider).pad(20).prefWidth(100);
        settingPane.add(musicLevelLabel).pad(20).row();

        soundSlider.setValue(Core.settings.getSoundVolume());
        soundSlider.fire(new ChangeListener.ChangeEvent());
        musicSlider.setValue(Core.settings.getMusicVolume());
        musicSlider.fire(new ChangeListener.ChangeEvent());
    }

    private void switchInput() {
        settingPane.clear();
    }

    private void apply() {
        TextButton cat = catButtions.getChecked();
        if (cat == videoButt) {
            if (fullscreenCheck.isChecked() != Core.settings.isFullscreen()) {
                Core.settings.setFullscreen(fullscreenCheck.isChecked());
                Core.settings.setVSync(vsyncCheck.isChecked());
                Gdx.graphics.setVSync(vsyncCheck.isChecked());
                if (Core.settings.isFullscreen()) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                } else {
                    Gdx.graphics.setWindowedMode(Core.settings.getWidth(), Core.settings.getHeight());
                }
            }
        } else if (cat == audioButt) {

        } else if (cat == inputButt) {

        }
    }

    @Override
    public void show(Stage stage) {
        stage.addActor(root);

        GuiEffects.fadeIn(root, FADE_DURATION).start(getTweenManager());
    }

    @Override
    public void hide(Stage stage) {
        GuiEffects.fadeOut(root, FADE_DURATION,
                () -> stage.getActors().removeValue(root, true)
        ).start(getTweenManager());
    }

    @Override
    public void dispose() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void render(SpriteBatch batch, float delta) {}
}
