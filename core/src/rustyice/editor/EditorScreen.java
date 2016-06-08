package rustyice.editor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import rustyice.game.Game;
import rustyice.game.Section;
import rustyice.screens.Screen;

import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * @author Gabriel Keith
 */
public class EditorScreen extends Screen {

    private Game game;

    private Kryo kryo;

    private VisTable root;
    private MenuBar menuBar;

    private FileHandle loadFile;

    private EditorPropertyPane propertyPane;
    private EditorSelectionPane selectionPane;
    private EditorGameView gameView;

    public EditorScreen(Game game, Kryo kryo) {
        this.game = game;
        this.kryo = kryo;
    }

    @Override
    public void load() {
        root = new VisTable();
        root.setFillParent(true);

        buildMenuBar();

        selectionPane = new EditorSelectionPane();
        propertyPane = new EditorPropertyPane();

        gameView = new EditorGameView(game);
        gameView.setSelectionPane(selectionPane);
        gameView.setPropertyPane(propertyPane);

        root.add(menuBar.getTable()).growX().row();


        VisSplitPane centerSplit = new VisSplitPane(gameView.getDisplay(), propertyPane.getTable(), true);
        centerSplit.setSplitAmount(0.85f);

        VisSplitPane centerSelectionSplit = new VisSplitPane(centerSplit, selectionPane.getTable(), false);
        centerSelectionSplit.setSplitAmount(0.8f);

        root.add(centerSelectionSplit).grow();
    }

    private void buildMenuBar() {
        menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");

        MenuItem newSection = new MenuItem("new");
        MenuItem saveSection = new MenuItem("save");
        saveSection.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFileChooser(Mode.OPEN);
            }
        });

        MenuItem loadSection = new MenuItem("load");
        loadSection.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFileChooser(Mode.SAVE);
            }
        });

        MenuItem quit = new MenuItem("quit");

        quit.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                getManager().popScreen();
            }
        });

        fileMenu.addItem(newSection);
        fileMenu.addItem(saveSection);
        fileMenu.addItem(loadSection);
        fileMenu.addItem(quit);

        menuBar.addMenu(fileMenu);
    }

    private void showFileChooser(Mode mode) {
        if(mode == Mode.OPEN){
            FileChooser saveChooser = new FileChooser(Mode.SAVE);
            saveChooser.setSelectionMode(SelectionMode.FILES);
            saveChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(Array<FileHandle> files) {
                    FileHandle saveFile = files.first();
                    saveSection(saveFile);
                }
            });
            getStage().addActor(saveChooser.fadeIn());
        } else{
            FileChooser loadChooser = new FileChooser(Mode.OPEN);
            loadChooser.setSelectionMode(SelectionMode.FILES);
            loadChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(Array<FileHandle> files) {
                    loadFile = files.first();
                    loadSection(loadFile);
                }
            });
            getStage().addActor(loadChooser.fadeIn());
        }
    }

    private void saveSection(FileHandle file) {
        Output output = new Output(new DeflaterOutputStream(file.write(false)));

        kryo.writeObject(output, game.getCurrentSection());

        output.close();
    }

    private void loadSection(FileHandle file) {
        Input input = new Input(new InflaterInputStream(file.read()));

        Section section = kryo.readObject(input, Section.class);

        game.setSectionToLoad(section);
        game.finishLoadingSection();

        input.close();
    }

    @Override
    public void show(Stage stage) {
        // stage.addActor(root);
        stage.addActor(root);
        gameView.getDisplay().setSize(stage.getWidth(), stage.getHeight());


        game.finishLoadingSection();
    }

    @Override
    public void hide(Stage stage) {
        stage.clear();
        gameView.dispose();
    }

    @Override
    public void dispose() {
        if(gameView != null){
            gameView.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        gameView.render(batch, delta);
    }
}
