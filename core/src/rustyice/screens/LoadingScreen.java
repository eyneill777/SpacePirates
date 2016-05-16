package rustyice.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import rustyice.screens.menus.effects.GuiEffects;

public class LoadingScreen extends Screen{
    private Container<VisProgressBar> root;
    private VisProgressBar progressBar;
    
    @Override
    public void load() {
        super.load();
        
        progressBar = new VisProgressBar(0, 1, 0.01f, false);
        root = new Container<>(progressBar);
        root.setFillParent(true);
    }

    @Override
    public void show(Stage stage) {
        getStage().addActor(root);
    }

    @Override
    public void hide(Stage stage) {
        GuiEffects.fadeOut(root, 0.5f, () -> {
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
        if(getResources().update()){
            getResources().loadAll();
            getManager().showScreen("main_menu");
        }
        
        progressBar.setValue(getResources().getProgress());
    }

}
