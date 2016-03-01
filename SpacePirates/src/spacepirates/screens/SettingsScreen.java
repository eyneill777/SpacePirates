package spacepirates.screens;

import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import javafx.scene.control.ToggleButton;
import spacepirates.main.GeneralSettings;

public class SettingsScreen extends Screen {
	private static final float BUTT_WIDTH = 150;
	private static final float PAD = 5;
	private GeneralSettings settings;
	private FileHandle settingFile;
	private Json json;
	
	private ButtonGroup<TextButton> catButtions;
	private Table root;
	private Table categoryPane;
	private Table settingPane;
	private Table bottemPane;
	
	private CheckBox fullscreenCheck;
	private TextButton videoButt, audioButt, inputButt;
	
	public SettingsScreen(GeneralSettings settings, FileHandle settingFile) {
		this.settings = settings;
		this.settingFile = settingFile;
		json = new Json();
	}
	
	@Override
	public void load(Skin skin) {	
		root = new Table(skin);
		root.setFillParent(true);
		
		categoryPane = new Table(skin);
		settingPane = new Table(skin);
		bottemPane = new Table(skin);
		
		videoButt = new TextButton("Video", skin, "toggle");
		videoButt.addListener(new ClickListener(){
			public void clicked(InputEvent e, float x, float y){
				switchToVideo();
			}
		});
		
		audioButt = new TextButton("Audio", skin, "toggle");
		audioButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
		        switchAudio();
		    }
		});
		
		inputButt = new TextButton("Input", skin, "toggle");
		inputButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
		        switchInput();
		    }
		});
		
		TextButton backButt = new TextButton("Back", skin);
		backButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
		        getManager().popScreen();
		    }
		});
		
		TextButton applyButt = new TextButton("Apply", skin);
		applyButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
		        apply();
		    }
		});
		
		catButtions = new ButtonGroup<>(videoButt, audioButt, inputButt);
		catButtions.setMinCheckCount(1);
		catButtions.setMaxCheckCount(1);
		
		loadVideo(skin);
		
		categoryPane.add(videoButt).prefWidth(BUTT_WIDTH).pad(PAD).row();
		categoryPane.add(audioButt).prefWidth(BUTT_WIDTH).pad(PAD).row();
		categoryPane.add(inputButt).prefWidth(BUTT_WIDTH).pad(PAD);
		
		bottemPane.add(backButt).expandX();
		bottemPane.add(applyButt).expandX();
		
		root.add(categoryPane).fillY().right();
		root.add(settingPane).fill().prefSize(800, 800).row();
		root.add(bottemPane).colspan(2).fillX();
		//root.setDebug(true, true);
	}

	public void loadVideo(Skin skin){
		fullscreenCheck = new CheckBox("Fullscreen", skin);
		fullscreenCheck.setChecked(settings.fullscreen);
	}
	
	private void switchToVideo(){
		settingPane.clear();
		settingPane.add(fullscreenCheck);
	}
	
	private void switchAudio(){
		settingPane.clear();
	}
	
	private void switchInput(){
		settingPane.clear();
	}
	
	private void apply(){
		TextButton cat = catButtions.getChecked();
		if(cat == videoButt){
			if(fullscreenCheck.isChecked() != settings.fullscreen){
				settings.fullscreen = fullscreenCheck.isChecked();
				if(settings.fullscreen){
					DisplayMode desktop = Gdx.graphics.getDesktopDisplayMode();
					Gdx.graphics.setDisplayMode(desktop.width, desktop.height, true);
				} else {
					Gdx.graphics.setDisplayMode(settings.width, settings.height, false);
				}
			}
		} else if(cat == audioButt){
			
		} else if(cat == inputButt){
			
		}
		json.toJson(settings, settingFile);
	}
	
	@Override
	public void show(Stage stage) {
		stage.addActor(root);
	}

	@Override
	public void hide(Stage stage) {
		stage.clear();
		//catButtions.uncheckAll();
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
