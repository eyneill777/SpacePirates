package spacepirates.screens;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import spacepirates.main.GeneralSettings;

public class SettingsScreen extends Screen {
	private static final float BUTT_WIDTH = 150;
	private static final float PAD = 5;
	private GeneralSettings settings;
	private FileHandle settingFile;
	
	private Table root;
	private ButtonGroup<TextButton> catButtions;
	private Table categoryPane;
	private Table settingPane;
	
	public SettingsScreen(GeneralSettings settings, FileHandle settingFile) {
		this.settings = settings;
		this.settingFile = settingFile;
	}
	
	@Override
	public void load(Skin skin) {	
		root = new Table(skin);
		root.setFillParent(true);
		
		categoryPane = new Table(skin);
		settingPane = new Table(skin);
		
		root.add(categoryPane).left().expand();
		root.add(settingPane);
		
		TextButton videoButt = new TextButton("Video", skin, "toggle");
		categoryPane.add(videoButt).prefWidth(BUTT_WIDTH).pad(PAD).row();;
		
		TextButton audioButt = new TextButton("Audio", skin, "toggle");
		categoryPane.add(audioButt).prefWidth(BUTT_WIDTH).pad(PAD).row();
		
		TextButton inputButt = new TextButton("Input", skin, "toggle");
		categoryPane.add(inputButt).prefWidth(BUTT_WIDTH).pad(PAD).row();;
		
		TextButton backButt = new TextButton("Back", skin, "toggle");
		backButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
		        getManager().popScreen();
		    }
		});
		categoryPane.add(backButt).prefWidth(BUTT_WIDTH).pad(PAD);
		
		catButtions = new ButtonGroup<>(videoButt, audioButt, inputButt, backButt);
		catButtions.setMinCheckCount(1);
		catButtions.setMaxCheckCount(1);
	}

	@Override
	public void show(Stage stage) {
		stage.addActor(root);
	}

	@Override
	public void hide(Stage stage) {
		stage.clear();
		catButtions.uncheckAll();
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
