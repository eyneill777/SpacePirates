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

import javafx.scene.control.ToggleButton;
import spacepirates.main.GeneralSettings;

public class SettingsScreen extends Screen {
	private static final float BUTT_WIDTH = 150;
	private static final float PAD = 5;
	private GeneralSettings settings;
	private FileHandle settingFile;
	
	private ButtonGroup<TextButton> catButtions;
	private Table root;
	private Table categoryPane;
	private Table settingPane;
	private Table bottemPane;
	
	private CheckBox fullscreenCheck;
	
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
		bottemPane = new Table(skin);
		
		TextButton videoButt = new TextButton("Video", skin, "toggle");
		videoButt.addListener(new ClickListener(){
			public void clicked(InputEvent e, float x, float y){
				switchToVideo();
			}
		});
		
		TextButton audioButt = new TextButton("Audio", skin, "toggle");
		audioButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
		        switchAudio();
		    }
		});
		
		TextButton inputButt = new TextButton("Input", skin, "toggle");
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
		
		catButtions = new ButtonGroup<>(videoButt, audioButt, inputButt);
		catButtions.setMinCheckCount(1);
		catButtions.setMaxCheckCount(1);
		
		loadVideo(skin);
		
		categoryPane.add(videoButt).prefWidth(BUTT_WIDTH).pad(PAD).row();
		categoryPane.add(audioButt).prefWidth(BUTT_WIDTH).pad(PAD).row();
		categoryPane.add(inputButt).prefWidth(BUTT_WIDTH).pad(PAD);
		
		bottemPane.add(backButt).padRight(100);
		bottemPane.add(applyButt);
		
		root.add(categoryPane);
		root.setDebug(true, true);
		root.add(settingPane).prefSize(500).row();
		root.add(bottemPane).colspan(2);
	}

	public void loadVideo(Skin skin){
		fullscreenCheck = new CheckBox("Fullscreen", skin);
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
