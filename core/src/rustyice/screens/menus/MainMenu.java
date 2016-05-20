package rustyice.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import rustyice.screens.Screen;
import rustyice.screens.menus.effects.ClickSound;
import rustyice.screens.menus.effects.GuiEffects;

/**
 * The menu that greets the player when they start the program.
 *  should contain a splash screen as well as buttons to navigate, maybe an background as well intresting.
 * @author gabek
 * 
 */
public class MainMenu extends Screen {

    /**
     * width of the buttons in the central table.
     */
    private static final float BUTT_WIDTH = 220;
    private static final float PAD = 5;
    private static final float FADE_DURATION = 0.5f;
    
    private VisTable root;

    @Override
    public void load() {
        super.load();
        
        root = new VisTable();

        VisTextButton newButt = new VisTextButton("New Game");
        newButt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getManager().showScreen("playing");
            }
        });
        ClickSound.addDefault(newButt);

        VisTextButton loadButt = new VisTextButton("Load Game");
        ClickSound.addDefault(loadButt);
        
        VisTextButton settingsButt = new VisTextButton("Settings");
        settingsButt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getManager().showScreen("settings");
            }
        });
        ClickSound.addDefault(settingsButt);

        VisTextButton editorButt = new VisTextButton("Level Edit");
        editorButt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getManager().showScreen("editor");
            }
        });
        ClickSound.addDefault(editorButt);

        VisTextButton quitButt = new VisTextButton("Quit");
        quitButt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        ClickSound.addDefault(quitButt);
        
        //root.add("Rusty Ice").pad(20).row();
        root.add(newButt).pad(PAD).prefWidth(BUTT_WIDTH).row();
        root.add(loadButt).pad(PAD).prefWidth(BUTT_WIDTH).row();
        root.add(settingsButt).pad(PAD).prefWidth(BUTT_WIDTH).row();
        root.add(editorButt).pad(PAD).prefWidth(BUTT_WIDTH).row();
        root.add(quitButt).pad(PAD).prefWidth(BUTT_WIDTH);

        root.setFillParent(true);
        root.setTransform(false);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        //batch.setShader(badBackground);
        //batch.begin();

        //batch.draw(getResources().box, 0, 0, 400, 400);
        
        //batch.end();
        //batch.setShader(null);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show(Stage stage) {
        stage.addActor(root);
        GuiEffects.fadeIn(root, FADE_DURATION).start(getTweenManager());
    }

    @Override
    public void hide(Stage stage) {
        //stage.getActors().removeValue(root, true);
        
        GuiEffects.fadeOut(root, FADE_DURATION, () -> {
            stage.getActors().removeValue(root, true);
        }).start(getTweenManager());
    }

    @Override
    public void dispose() {
    }
}
