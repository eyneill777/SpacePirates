package rustyice.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import rustyice.main.GeneralSettings;
import rustyice.screens.Screen;
import rustyice.screens.menus.effects.GuiEffects;

public class SettingsScreen extends Screen {

    private static final float BUTT_WIDTH = 150;
    private static final float PAD = 5;
    private static final float FADE_DURATION = 0.5f;
    private GeneralSettings settings;
    private FileHandle settingFile;
    private Json json;

    private ButtonGroup<TextButton> catButtions;
    private VisTable root;
    private VisTable categoryPane;
    private VisTable settingPane;
    private VisTable bottemPane;

    private VisCheckBox fullscreenCheck;
    private TextButton videoButt, audioButt, inputButt;

    public SettingsScreen(GeneralSettings settings, FileHandle settingFile) {
        this.settings = settings;
        this.settingFile = settingFile;
        this.json = new Json();
    }

    @Override
    public void load() {
        super.load();
        
        this.root = new VisTable();
        this.root.setFillParent(true);

        this.categoryPane = new VisTable();
        this.settingPane = new VisTable();
        this.bottemPane = new VisTable();

        this.videoButt = new VisTextButton("Video", "toggle");
        this.videoButt.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent e, float x, float y) {
                switchToVideo();
            }
        });

        this.audioButt = new VisTextButton("Audio", "toggle");
        this.audioButt.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchAudio();
            }
        });

        this.inputButt = new VisTextButton("Input", "toggle");
        this.inputButt.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchInput();
            }
        });

        VisTextButton backButt = new VisTextButton("Back");
        backButt.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                getManager().popScreen();
            }
        });

        VisTextButton applyButt = new VisTextButton("Apply");
        applyButt.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                apply();
            }
        });

        this.catButtions = new ButtonGroup<>(this.videoButt, this.audioButt, this.inputButt);
        this.catButtions.setMinCheckCount(1);
        this.catButtions.setMaxCheckCount(1);

        loadVideo();

        this.categoryPane.add(this.videoButt).prefWidth(BUTT_WIDTH).pad(PAD).row();
        this.categoryPane.add(this.audioButt).prefWidth(BUTT_WIDTH).pad(PAD).row();
        this.categoryPane.add(this.inputButt).prefWidth(BUTT_WIDTH).pad(PAD);

        this.bottemPane.add(backButt).expandX();
        this.bottemPane.add(applyButt).expandX();

        this.root.add(this.categoryPane).fillY().right();
        this.root.add(this.settingPane).fill().prefSize(800, 800).row();
        this.root.add(this.bottemPane).colspan(2).fillX();
        // root.setDebug(true, true);
    }

    public void loadVideo() {
        this.fullscreenCheck = new VisCheckBox("Fullscreen");
        this.fullscreenCheck.setChecked(this.settings.fullscreen);
    }

    private void switchToVideo() {
        this.settingPane.clear();
        this.settingPane.add(this.fullscreenCheck);
    }

    private void switchAudio() {
        this.settingPane.clear();
    }

    private void switchInput() {
        this.settingPane.clear();
    }

    private void apply() {
        TextButton cat = this.catButtions.getChecked();
        if (cat == this.videoButt) {
            if (this.fullscreenCheck.isChecked() != this.settings.fullscreen) {
                this.settings.fullscreen = this.fullscreenCheck.isChecked();
                if (this.settings.fullscreen) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    System.out.println(Gdx.graphics.getDisplayMode().refreshRate);
                } else {
                    Gdx.graphics.setWindowedMode(this.settings.width, this.settings.height);
                }
            }
        } else if (cat == this.audioButt) {

        } else if (cat == this.inputButt) {

        }
        this.json.toJson(this.settings, this.settingFile);
    }

    @Override
    public void show(Stage stage) {
        stage.addActor(root);

        GuiEffects.fadeIn(root, FADE_DURATION).start(getTweenManager());
    }

    @Override
    public void hide(Stage stage) {
        GuiEffects.fadeOut(root, FADE_DURATION, () ->{
            stage.getActors().removeValue(root, true);
        }).start(getTweenManager());
    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(SpriteBatch batch, float delta) {

    }
}
