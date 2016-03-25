package gken.rustyice.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu extends Screen{
	private static final float BUTT_WIDTH = 220;
	private static final float PAD = 5;
	
	private Table root;
	
	public MainMenu(){
		
	}

	@Override
	public void load(Skin skin) {
		root = new Table(skin);
		
		TextButton newButt = new TextButton("New Game", skin);
		newButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				getManager().showScreen("playing");
			}
		});
		
		TextButton loadButt = new TextButton("Load Game", skin);
		
		TextButton settingsButt = new TextButton("Settings", skin);
		settingsButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				getManager().showScreen("settings");
			}
		});
		
		TextButton editorButt = new TextButton("Level Edit", skin);
		editorButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				getManager().showScreen("editor");
			}
		});
		
		TextButton quitButt = new TextButton("Quit", skin);
		quitButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
		}});
		
		root.add("Rusty Ice", "title").pad(20).row();
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
