package rustyice.editor;

import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisSplitPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTree;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

import rustyice.game.Game;
import rustyice.game.Section;
import rustyice.game.actors.Actor;
import rustyice.game.actors.Player;
import rustyice.game.actors.TestActor;
import rustyice.game.tiles.FloorTile;
import rustyice.game.tiles.Tile;
import rustyice.game.tiles.WallTile;
import rustyice.graphics.Camera;
import rustyice.graphics.GameDisplay;
import rustyice.graphics.GraphicsUtils;
import rustyice.screens.Screen;

/**
 * @author Gabriel Keith
 */
public class EditorScreen extends Screen {

    private Kryo kryo;

    private VisTable root;
    private MenuBar menuBar;
    private GameDisplay display;

    private VisTree actorSelectionTree;
    private VisTree tileSelectionTree;
    private VisScrollPane selectionScroll;

    private FileChooser saveChooser;
    private FileChooser loadChooser;

    private FileHandle loadFile;

    private ButtonGroup<TextButton> modeGroup;
    private boolean tileEditMode = false;
    private Camera camera;
    private Game game;

    private boolean mouseDraging = false;
    private Vector2 mouseDownPos;
    private Vector2 mouseDraggedPos;
    private Actor selectionActor;
    private Vector2 selectionPos;

    private Vector2 tileSelectStart;
    private Vector2 tileSelectEnd;

    public EditorScreen(Game game, Kryo kryo) {
        this.game = game;
        this.kryo = kryo;

        camera = new Camera();
        camera.setWidth(20);
        camera.setHeight(20);

        mouseDownPos = new Vector2();
        mouseDraggedPos = new Vector2();
        selectionActor = null;
        selectionPos = new Vector2();

        tileSelectStart = new Vector2();
        tileSelectEnd = new Vector2();
    }

    @Override
    public void load() {
        root = new VisTable();
        root.setFillParent(true);

        buildMenuBar();
        buildSelectionPane();
        buildFileChooser();

        root.add(menuBar.getTable()).growX().row();

        display = new GameDisplay();
        display.addListener(new EditorInput());
        display.setTarget(game, camera);

        selectionScroll = new VisScrollPane(actorSelectionTree);
        selectionScroll.setScrollingDisabled(true, false);
        selectionScroll.setFadeScrollBars(false);

        VisTable sidePane = new VisTable();

        VisTextButton actorModeButt = new VisTextButton("Actor", "toggle");
        actorModeButt.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                selectionScroll.setWidget(actorSelectionTree);
                tileEditMode = false;
            }
        });
        sidePane.add(actorModeButt).fillX();

        VisTextButton tileModeButt = new VisTextButton("Tile", "toggle");
        tileModeButt.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                selectionScroll.setWidget(tileSelectionTree);
                tileEditMode = true;
            }
        });
        sidePane.add(tileModeButt).fillX().row();

        modeGroup = new ButtonGroup<>(actorModeButt, tileModeButt);
        modeGroup.setMinCheckCount(1);
        modeGroup.setMaxCheckCount(1);
        modeGroup.setChecked("Actor");

        sidePane.add(selectionScroll).colspan(2).grow();

        VisSplitPane selectionPane = new VisSplitPane(display, sidePane, false);
        selectionPane.setSplitAmount(0.8f);

        root.add(selectionPane).grow();
    }

    private void buildSelectionPane() {
        actorSelectionTree = new VisTree();

        Node nodeToAdd = new Node(new VisLabel("Player"));
        nodeToAdd.setObject(Player.class);
        actorSelectionTree.add(nodeToAdd);

        nodeToAdd = new Node(new VisLabel("A Gabe"));
        nodeToAdd.setObject(TestActor.class);
        actorSelectionTree.add(nodeToAdd);

        tileSelectionTree = new VisTree();

        nodeToAdd = new Node(new VisLabel("Floor"));
        nodeToAdd.setObject(FloorTile.class);
        tileSelectionTree.add(nodeToAdd);

        nodeToAdd = new Node(new VisLabel("Wall"));
        nodeToAdd.setObject(WallTile.class);
        tileSelectionTree.add(nodeToAdd);
    }

    private void buildMenuBar() {
        menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");

        MenuItem newSection = new MenuItem("new");
        MenuItem saveSection = new MenuItem("save");
        saveSection.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                getStage().addActor(saveChooser);
            }
        });

        MenuItem loadSection = new MenuItem("load");
        loadSection.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                getStage().addActor(loadChooser);
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

    private void setEditMode(boolean editTiles) {
        if (tileEditMode) {
            modeGroup.setChecked("Actor");
            // selectionScroll.setWidget(actorSelectionTree);
        } else {
            modeGroup.setChecked("Tile");
            // selectionScroll.setWidget(tileSelectionTree);
        }
    }

    private void buildFileChooser() {
        saveChooser = new FileChooser(Mode.SAVE);
        saveChooser.setSelectionMode(SelectionMode.FILES);
        saveChooser.setListener(new FileChooserAdapter() {

            @Override
            public void selected(Array<FileHandle> files) {
                FileHandle saveFile = files.first();
                saveSection(saveFile);
            }
        });

        loadChooser = new FileChooser(Mode.OPEN);
        loadChooser.setSelectionMode(SelectionMode.FILES);
        loadChooser.setListener(new FileChooserAdapter() {

            @Override
            public void selected(Array<FileHandle> files) {
                loadFile = files.first();
                loadSection(loadFile);
            }
        });
    }

    private void saveSection(FileHandle file) {
        Output output = new Output(new DeflaterOutputStream(file.write(false)));

        kryo.writeObject(output, game.getCurrentSection());

        output.close();
    }

    private void loadSection(FileHandle file) {
        com.esotericsoftware.kryo.io.Input input = new com.esotericsoftware.kryo.io.Input(new InflaterInputStream(file.read()));

        Section section = kryo.readObject(input, Section.class);

        game.setSectionToLoad(section);
        game.finishLoadingSection();

        input.close();
    }

    @Override
    public void show(Stage stage) {
        // stage.addActor(root);
        stage.addActor(root);
        display.setSize(stage.getWidth(), stage.getHeight());

        game.finishLoadingSection();
    }

    @Override
    public void hide(Stage stage) {
        stage.clear();
        display.dispose();
    }

    @Override
    public void dispose() {
    	if(display != null){
            display.dispose();
    	}
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        // this.game.update(delta);
        game.getCurrentSection().finishAdding();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.setY(camera.getY() + camera.getHeight() * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.setY(camera.getY() - camera.getHeight() * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.setX(camera.getX() - camera.getWidth() * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.setX(camera.getX() + camera.getWidth() * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            camera.setWidth(camera.getWidth() * (1 - 0.75f * delta));
            camera.setHeight(camera.getHeight() * (1 - 0.75f * delta));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            camera.setWidth(camera.getWidth() * (1 + 0.75f * delta));
            camera.setHeight(camera.getHeight() * (1 + 0.75f * delta));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            display.setLightsActive(!display.isLightsActive());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            if (tileEditMode) {
                setEditMode(false);
            } else {
                setEditMode(true);
            }
        }

        display.render(batch, delta);

        if (tileEditMode && mouseDraging) {
            display.getFBO().begin();
            batch.begin();

            batch.setColor(Color.LIGHT_GRAY);
            GraphicsUtils.drawRect(batch, getResources(), tileSelectStart.x, tileSelectStart.y, tileSelectEnd.x - tileSelectStart.x, tileSelectEnd.y - tileSelectStart.y, 0.1f);

            batch.end();
            display.getFBO().end();
        }
    }

    private class EditorInput extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            if (!tileEditMode && actorSelectionTree.getSelection().hasItems()) {
                try {

                    Actor actor = (Actor) ((Class<?>) actorSelectionTree.getSelection().getLastSelected().getObject()).newInstance();
                    actor.setPosition(mouseDownPos.x, mouseDownPos.y);
                    // EditorScreen.this.display.getMouseControl().y);
                    game.getCurrentSection().addActor(actor);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            super.touchDown(event, x, y, pointer, button);

            mouseDownPos.set(x, y);
            display.stageToSection(mouseDownPos);

            if (!tileEditMode) {
                for (Actor actor : game.getCurrentSection().getActors()) {
                    if (mouseDownPos.x >= actor.getX() - actor.getWidth() / 2 && mouseDownPos.y >= actor.getY() - actor.getHeight() / 2
                            && mouseDownPos.x < actor.getX() + actor.getWidth() / 2 && mouseDownPos.y < actor.getY() + actor.getHeight() / 2) {
                        selectionActor = actor;
                        selectionPos.set(actor.getX(), actor.getY());
                        System.out.println(actor.getWidth());
                        break;
                    }
                }
            }

            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
            selectionActor = null;

            if (tileEditMode && mouseDraging) {
                Class<?> target = (Class<?>) tileSelectionTree.getSelection().first().getObject();

                try {
                    for (int i = (int) tileSelectStart.y; i < tileSelectEnd.y; i++) {
                        for (int j = (int) tileSelectStart.x; j < tileSelectEnd.x; j++) {
                            game.getTiles().setTile((Tile) target.newInstance(), j, i);
                        }
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            mouseDraging = false;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            super.touchDragged(event, x, y, pointer);

            mouseDraging = true;
            mouseDraggedPos.set(x, y);
            display.stageToSection(mouseDraggedPos);

            if (tileEditMode) {
                if (mouseDownPos.x < mouseDraggedPos.x) {
                    tileSelectStart.x = MathUtils.floor(mouseDownPos.x);
                    tileSelectEnd.x = MathUtils.ceil(mouseDraggedPos.x);
                } else {
                    tileSelectStart.x = MathUtils.floor(mouseDraggedPos.x);
                    tileSelectEnd.x = MathUtils.ceil(mouseDownPos.x);
                }

                if (mouseDownPos.y < mouseDraggedPos.y) {
                    tileSelectStart.y = MathUtils.floor(mouseDownPos.y);
                    tileSelectEnd.y = MathUtils.ceil(mouseDraggedPos.y);
                } else {
                    tileSelectStart.y = MathUtils.floor(mouseDraggedPos.y);
                    tileSelectEnd.y = MathUtils.ceil(mouseDownPos.y);
                }
            }

            if (selectionActor != null) {
                selectionActor.setX(selectionPos.x + (mouseDraggedPos.x - mouseDownPos.x));
                selectionActor.setY(selectionPos.y + (mouseDraggedPos.y - mouseDownPos.y));
            }
        }
    }
}
