package rustyice.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class MainMenu extends Screen{
	private static final float BUTT_WIDTH = 220;
	private static final float PAD = 5;
	
	private VisTable root;
	
	public MainMenu(){
		
	}

	@Override
	public void load() {
		root = new VisTable();
		
		VisTextButton newButt = new VisTextButton("New Game");
		newButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				getManager().showScreen("playing");
			}
		});
		
		VisTextButton loadButt = new VisTextButton("Load Game");
		
		VisTextButton settingsButt = new VisTextButton("Settings");
		settingsButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				getManager().showScreen("settings");
			}
		});
		
		VisTextButton editorButt = new VisTextButton("Level Edit");
		editorButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				getManager().showScreen("editor");
			}
		});
		
		VisTextButton quitButt = new VisTextButton("Quit");
		quitButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
		}});
		
		root.add("Rusty Ice").pad(20).row();
		root.add(newButt).pad(PAD).prefWidth(BUTT_WIDTH).row();
		root.add(loadButt).pad(PAD).prefWidth(BUTT_WIDTH).row();
		root.add(settingsButt).pad(PAD).prefWidth(BUTT_WIDTH).row();
		root.add(editorButt).pad(PAD).prefWidth(BUTT_WIDTH).row();
		root.add(quitButt).pad(PAD).prefWidth(BUTT_WIDTH);
		
		root.setFillParent(true);
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show(Stage stage) {
		stage.addActor(root);
	}
	
	@Override
	public void hide(Stage stage) {
		stage.clear();
	}
	
	@Override
	public void dispose() {
	}
}
