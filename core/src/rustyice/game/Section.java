package rustyice.game;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import rustyice.game.actors.Actor;
import rustyice.game.tiles.TileMap;
import rustyice.graphics.Camera;
import rustyice.graphics.RenderFlags;
import rustyice.resources.Resources;

public class Section {

    private ArrayList<Actor> actors;
    private TileMap tiles;

    private transient Game game;
    private transient ArrayList<Actor> addList;
    private transient ArrayList<Actor> removeList;
    private transient boolean initialized = false;

    public Section() {
        this.addList = new ArrayList<>();
        this.removeList = new ArrayList<>();

        // boundary = new RoomBoundary(-width/2, -height/2, width, height);
        this.actors = new ArrayList<>();

        tiles = new TileMap();
    }

    public void init() {
        for (Actor actor : this.actors) {
            actor.setSection(this);
            actor.init();
        }

        this.tiles.setSection(this);
        this.tiles.init();

        this.initialized = true;
    }

    public void store() {
        finishAdding();

        actors.forEach(Actor::store);

        this.initialized = false;
    }

    public void finishAdding() {
        for (Actor actor : addList) {
            actors.add(actor);
            actor.init();
        }
        addList.clear();
    }

    public void update(float delta) {
        finishAdding();

        for (Actor actor : getActors()) {
            actor.update(delta);
        }

        this.tiles.update(delta);

        for (Actor actor : this.removeList) {
            getActors().remove(actor);
            actor.store();
        }
        this.removeList.clear();
    }

    public void render(SpriteBatch batch, Camera camera, int flags) {
        tiles.render(batch, camera, flags);

        actors.stream().filter(
                actor -> actor.getX() + actor.getWidth() / 2 > camera.getX() - camera.getHalfRenderSize()
                && actor.getX() - actor.getWidth() / 2 < camera.getX() + camera.getHalfRenderSize()
                && actor.getY() + actor.getHeight() / 2 > camera.getY() - camera.getHalfRenderSize()
                && actor.getY() - actor.getHeight() / 2 < camera.getY() + camera.getHalfRenderSize())
                .forEach(actor -> actor.render(batch, camera, flags));
    }

    public Resources getResources() {
        return game.getResources();
    }

    public RayHandler getRayHandler() {
        return game.getRayHandler();
    }

    public World getWorld() {
        return game.getWorld();
    }

    public void addActor(Actor actor) {
        if (initialized) {
            addList.add(actor);
        } else {
            actors.add(actor);
        }
        actor.setSection(this);
    }

    public void removeActor(Actor actor) {
        this.removeList.add(actor);
    }

    public Actor getActor(int i) {
        return this.actors.get(i);
    }

    public ArrayList<Actor> getActors() {
        return this.actors;
    }

    public TileMap getTiles() {
        return this.tiles;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
