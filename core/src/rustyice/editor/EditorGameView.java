package rustyice.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import rustyice.game.Game;
import rustyice.game.actors.Actor;
import rustyice.game.tiles.TileMap;
import rustyice.graphics.Camera;
import rustyice.graphics.GameDisplay;
import rustyice.graphics.GraphicsUtils;
import rustyice.graphics.RenderFlags;

public class EditorGameView {
    private EditorSelectionPane selectionPane;
    private EditorPropertyPane propertyPane;
    private GameDisplay display;

    private EditorInput editorInput;
    private Camera camera;
    private Game game;

    private boolean mouseDraging = false;
    private Vector2 mouseDownPos;
    private Vector2 mouseDraggedPos;
    private Actor selectionActor;
    private Vector2 selectionPos;

    private Vector2 tileSelectStart;
    private Vector2 tileSelectEnd;

    public EditorGameView(Game game){
        this.game = game;

        camera = new Camera();
        camera.setWidth(20);
        camera.setHeight(20);
        camera.enableFlag(RenderFlags.EDITOR);

        mouseDownPos = new Vector2();
        mouseDraggedPos = new Vector2();
        selectionActor = null;
        selectionPos = new Vector2();

        tileSelectStart = new Vector2();
        tileSelectEnd = new Vector2();

        display = new GameDisplay();
        display.setTarget(game, camera);

        editorInput = new EditorInput();
        display.addListener(editorInput);
    }

    public void render(SpriteBatch batch, float delta) {
        game.getCurrentSection().finishAdding();

        if (editorInput.moveUp) {
            camera.setY(camera.getY() + camera.getHeight() * delta);
        }
        if (editorInput.moveDown) {
            camera.setY(camera.getY() - camera.getHeight() * delta);
        }
        if (editorInput.moveLeft) {
            camera.setX(camera.getX() - camera.getWidth() * delta);
        }
        if (editorInput.moveRight) {
            camera.setX(camera.getX() + camera.getWidth() * delta);
        }

        if (editorInput.zoomIn) {
            camera.setWidth(camera.getWidth() * (1 - 0.75f * delta));
            camera.setHeight(camera.getHeight() * (1 - 0.75f * delta));
        }
        if (editorInput.zoomOut) {
            camera.setWidth(camera.getWidth() * (1 + 0.75f * delta));
            camera.setHeight(camera.getHeight() * (1 + 0.75f * delta));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            camera.toggleFlag(RenderFlags.LIGHTING);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            camera.toggleFlag(RenderFlags.POV);
        }

        display.render(batch, delta);

        if (selectionPane.isTileMode() && mouseDraging) {
            display.getFBO().begin();
            batch.begin();

            batch.setColor(Color.LIGHT_GRAY);
            GraphicsUtils.drawRect(
                    tileSelectStart.x * TileMap.TILE_SIZE,
                    tileSelectStart.y  * TileMap.TILE_SIZE,
                    (tileSelectEnd.x - tileSelectStart.x)  * TileMap.TILE_SIZE,
                    (tileSelectEnd.y - tileSelectStart.y)  * TileMap.TILE_SIZE, 0.1f
            );

            batch.end();
            display.getFBO().end();
        }
    }

    public GameDisplay getDisplay(){
        return display;
    }

    public void dispose(){
        display.dispose();
    }

    public void setSelectionPane(EditorSelectionPane selectionPane){
        this.selectionPane = selectionPane;
    }

    public void setPropertyPane(EditorPropertyPane propertyPane){
        this.propertyPane = propertyPane;
    }

    private class EditorInput extends ClickListener {
        boolean moveUp, moveDown, moveLeft, moveRight, zoomIn, zoomOut;


        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch (keycode){
                case Input.Keys.UP:
                    moveUp = true;
                    return true;
                case Input.Keys.DOWN:
                    moveDown = true;
                    return true;
                case Input.Keys.LEFT:
                    moveLeft = true;
                    return true;
                case Input.Keys.RIGHT:
                    moveRight = true;
                    return true;
                case Input.Keys.Z:
                    zoomIn = true;
                    return true;
                case Input.Keys.X:
                    zoomOut = true;
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public boolean keyUp(InputEvent event, int keycode) {
            switch (keycode) {
                case Input.Keys.UP:
                    moveUp = false;
                    return true;
                case Input.Keys.DOWN:
                    moveDown = false;
                    return true;
                case Input.Keys.LEFT:
                    moveLeft = false;
                    return true;
                case Input.Keys.RIGHT:
                    moveRight = false;
                    return true;
                case Input.Keys.Z:
                    zoomIn = false;
                    return true;
                case Input.Keys.X:
                    zoomOut = false;
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            event.getStage().setKeyboardFocus(event.getListenerActor());

            if (selectionPane.isActorMode() && selectionPane.hasSelectedActor()) {
                Actor actor = selectionPane.buildSelectedActor();
                actor.setPosition(mouseDownPos.x, mouseDownPos.y);
                game.getCurrentSection().addActor(actor);
            }
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            super.touchDown(event, x, y, pointer, button);

            mouseDownPos.set(x, y);
            display.stageToSection(mouseDownPos);

            if (selectionPane.isActorMode()) {
                for (Actor actor : game.getCurrentSection().getActors()) {
                    if (mouseDownPos.x >= actor.getX() - actor.getWidth() / 2 && mouseDownPos.y >= actor.getY() - actor.getHeight() / 2
                            && mouseDownPos.x < actor.getX() + actor.getWidth() / 2 && mouseDownPos.y < actor.getY() + actor.getHeight() / 2) {
                        selectionActor = actor;
                        selectionPos.set(actor.getX(), actor.getY());
                        propertyPane.setSelected(selectionActor);
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

            if (selectionPane.hasSelectedTile() && mouseDraging) {
                for (int i = (int) tileSelectStart.y; i < tileSelectEnd.y; i++) {
                    for (int j = (int) tileSelectStart.x; j < tileSelectEnd.x; j++) {
                        game.getTiles().setTile(selectionPane.buildSelectedTile(), j, i);
                    }
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

            if (selectionPane.isTileMode()) {
                if (mouseDownPos.x < mouseDraggedPos.x) {
                    tileSelectStart.x = tileFloor(mouseDownPos.x);
                    tileSelectEnd.x = tileCeil(mouseDraggedPos.x);
                } else {
                    tileSelectStart.x = tileFloor(mouseDraggedPos.x);
                    tileSelectEnd.x = tileCeil(mouseDownPos.x);
                }

                if (mouseDownPos.y < mouseDraggedPos.y) {
                    tileSelectStart.y = tileFloor(mouseDownPos.y);
                    tileSelectEnd.y = tileCeil(mouseDraggedPos.y);
                } else {
                    tileSelectStart.y = tileFloor(mouseDraggedPos.y);
                    tileSelectEnd.y = tileCeil(mouseDownPos.y);
                }
            }

            if (selectionActor != null) {
                selectionActor.setX(selectionPos.x + (mouseDraggedPos.x - mouseDownPos.x));
                selectionActor.setY(selectionPos.y + (mouseDraggedPos.y - mouseDownPos.y));
            }
        }

        private float tileFloor(float value){
            return MathUtils.floor(value / TileMap.TILE_SIZE);
        }

        private float tileCeil(float value){
            return MathUtils.ceil(value / TileMap.TILE_SIZE);
        }
    }
}
