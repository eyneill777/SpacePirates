package rustyice.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import rustyice.game.Game;
import rustyice.game.actors.Actor;
import rustyice.graphics.Camera;
import rustyice.graphics.GameDisplay;
import rustyice.graphics.GraphicsUtils;

public class EditorGameView {
    private EditorSelectionPane selectionPane;
    private EditorPropertyPane propertyPane;
    private GameDisplay display;

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

        mouseDownPos = new Vector2();
        mouseDraggedPos = new Vector2();
        selectionActor = null;
        selectionPos = new Vector2();

        tileSelectStart = new Vector2();
        tileSelectEnd = new Vector2();

        display = new GameDisplay();
        display.addListener(new EditorInput());
        display.setTarget(game, camera);
    }

    public void render(SpriteBatch batch, float delta) {
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

        display.render(batch, delta);

        if (selectionPane.isTileMode() && mouseDraging) {
            display.getFBO().begin();
            batch.begin();

            batch.setColor(Color.LIGHT_GRAY);
            GraphicsUtils.drawRect(tileSelectStart.x, tileSelectStart.y, tileSelectEnd.x - tileSelectStart.x, tileSelectEnd.y - tileSelectStart.y, 0.1f);

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

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
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

            if (selectionPane.isTileMode() && mouseDraging) {
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
