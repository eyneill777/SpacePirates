package rustyice.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTable;

import rustyice.screens.Screen;

/**
 * @author Gabriel Keith
 */
public class EditorScreen extends Screen{
	private VisTable root;
	private MenuBar menuBar;
	
	public EditorScreen() {
		
	}
	
    @Override
    public void load() {
    	root = new VisTable();
    	root.setFillParent(true);
    	
    	buildMenuBar();
    	
    	root.add(menuBar.getTable()).top();
    	
    	root.add("test").grow().row();
    }
    
    private void buildMenuBar(){
    	menuBar = new MenuBar();
    	
    	Menu fileMenu = new Menu("File");
    	
    	MenuItem newSection = new MenuItem("new");
    	MenuItem saveSection = new MenuItem("save");
    	MenuItem quit = new MenuItem("quit");
    	
    	quit.addListener(new ChangeListener(){
			public void changed(ChangeEvent event, Actor actor) {
				getManager().popScreen();
			}
    	});
    	
    	fileMenu.addItem(newSection);
    	fileMenu.addItem(saveSection);
    	fileMenu.addItem(quit);
    	
    	menuBar.addMenu(fileMenu);
    }

    @Override
    public void show(Stage stage) {
    	//stage.addActor(root);
    	stage.addActor(root);
    }

    @Override
    public void hide(Stage stage) {
    	stage.clear();
    }

    @Override
    public void dispose() {
    	VisUI.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(SpriteBatch batch, float delta) {
    	
    }
}
