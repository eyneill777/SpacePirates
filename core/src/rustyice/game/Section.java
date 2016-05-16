package rustyice.game;

import java.util.ArrayList;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import rustyice.game.actors.Actor;
import rustyice.game.actors.TestActor;
import rustyice.game.lights.SimpleLight;
import rustyice.game.tiles.TileMap;
import rustyice.game.tiles.WallTile;
import rustyice.graphics.Camera;
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
        TestActor testActor = new TestActor();
        addActor(testActor);

        Color[] colors = { Color.RED, Color.CYAN, Color.GOLD, Color.CORAL };
        for (int i = 0; i < 4; i++) {
            SimpleLight light = new SimpleLight();
            light.setPosition(4 + (i / 2) * 15, 4 + (i % 2) * 15);
            light.setColor(colors[i]);
            addActor(light);
        }

        this.tiles = new TileMap();

        this.tiles.setTile(new WallTile(), 5, 5);
        this.tiles.setTile(new WallTile(), 2, 2);
        this.tiles.setTile(new WallTile(), 5, 9);
        this.tiles.setTile(new WallTile(), 4, 9);
        this.tiles.setTile(new WallTile(), 3, 9);
        this.tiles.setTile(new WallTile(), 2, 9);
    }

    protected void createContents() {
        /*
         * if(getAdjacentRoom('S') != null){ tiles.setTile(new DoorTile(getAdjacentRoom('S')),
         * tiles.getWidth()/2, tiles.getHeight()-1); } if(getAdjacentRoom('N') != null){
         * tiles.setTile(new DoorTile(getAdjacentRoom('N')), tiles.getWidth()/2, 0);
         * //tiles.setTile(new FloorTile(), tiles.getWidth()/2, 0); } if(getAdjacentRoom('E') !=
         * null){ tiles.setTile(new DoorTile(getAdjacentRoom('E')), tiles.getHeight()-1,
         * tiles.getHeight()/2); } if(getAdjacentRoom('W') != null){ tiles.setTile(new
         * DoorTile(getAdjacentRoom('W')), 0, tiles.getHeight()/2); }
         */
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

        for (Actor actor : this.actors) {
            actor.store();
        }

        this.initialized = false;
    }

    public void finishAdding() {
        for (Actor actor : this.addList) {
            this.actors.add(actor);
            actor.init();
        }
        this.addList.clear();
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

    public void render(SpriteBatch batch, Camera camera) {
        this.tiles.render(batch, camera);

        for (Actor actor : this.actors) {
            if (actor.getX() + actor.getWidth() / 2 > camera.getX() - camera.getHalfRenderSize() && actor.getX() - actor.getWidth() / 2 < camera.getX() + camera.getHalfRenderSize()
                    && actor.getY() + actor.getHeight() / 2 > camera.getY() - camera.getHalfRenderSize()
                    && actor.getY() - actor.getHeight() / 2 < camera.getY() + camera.getHalfRenderSize()) {
                actor.render(batch);
            }
        }
    }

    public Resources getResources() {
        return this.game.getResources();
    }

    public RayHandler getRayHandler() {
        return this.game.getRayHandler();
    }

    public World getWorld() {
        return this.game.getWorld();
    }

    public void addActor(Actor actor) {
        if (this.initialized) {
            this.addList.add(actor);
        } else {
            this.actors.add(actor);
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
