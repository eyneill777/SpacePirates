package gken.rustyice.game;

import java.util.ArrayList;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import gken.rustyice.game.physics.Collidable;
import gken.rustyice.game.physics.Collision;
import gken.rustyice.graphics.Camera;
import gken.rustyice.resources.Resources;
import gken.rustyice.game.level.Level;
import gken.rustyice.game.level.Section;
import gken.rustyice.game.tiles.TileMap;
import gken.rustyice.input.PlayerInput;

public class Game implements ContactListener {
	private ArrayList<Camera> cameras;
    private RayHandler rayHandler;
	private Resources resources;
	private PlayerInput playerInput;
	private World world;
	private Section currentSection;
	private Section sectionToLoad;
	private ArrayList<Actor> removeList;
	private ArrayList<Actor> addList;
    private ArrayList<Collision> collisions;

    private PointLight testLight;

	public Game() {
		world = new World(Vector2.Zero, true);
        world.setContactListener(this);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0, 0, 0, .025f);

		Camera camera = new Camera();
		camera.setWidth(10);
		camera.setHeight(10);

		Level level = new Level(5);

		sectionToLoad = level.getStartingRoom();

		// actors = new ArrayList<>();
		addList = new ArrayList<>();
		removeList = new ArrayList<>();
		collisions = new ArrayList<>();
        cameras = new ArrayList<>();

        cameras.add(camera);

		Player player = new Player();
        player.setPosition(7, 7);
		camera.setTarget(player);
		camera.setTracking(true);

		addActor(player);
	}

    public Section getCurrentSection(){
        return currentSection;
    }

    public RayHandler getRayHandler(){
        return rayHandler;
    }

	private void loadRoom(Section section) {
		if (currentSection != null) {
			for(Actor actor: getActors()){
                actor.store();
            }
            getTiles().store();
		}
        for(Camera camera: cameras){
            camera.setX(camera.getTarget().getX());
            camera.setY(camera.getTarget().getY());
        }

        if(testLight != null){
            testLight.remove(true);
        }
        testLight = new PointLight(rayHandler, 500, new Color(0, 1, 1, .8f), 10,
                7, 7);
        //testLight.setStaticLight(true);

		section.setGame(this);
		currentSection = section;
		for (Actor actor : currentSection.getActors()) {
			actor.setGame(this);
			actor.init();
		}

		getTiles().setGame(this);
		getTiles().init();
	}

    public void setSectionToLoad(Section sectionToLoad){
        this.sectionToLoad = sectionToLoad;
    }

	public TileMap getTiles(){
		return currentSection.getTiles();
	}

    public ArrayList<Actor> getActors(){
        return currentSection.getActors();
    }

	public void setPlayerInput(PlayerInput playerInput) {
		this.playerInput = playerInput;
	}

	public PlayerInput getPlayerInput() {
		return playerInput;
	}

	public void addActor(Actor actor) {
		addList.add(actor);
		actor.setGame(this);
	}

	public void removeActor(Actor actor) {
		removeList.add(actor);
	}

	public Actor getActor(int i) {
		return currentSection.getActors().get(i);
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}

	public Resources getResources() {
		return resources;
	}

	public void update(float delta) {
		if (currentSection != null) {
			for (Actor actor : addList) {
				getActors().add(actor);
				actor.init();
			}
			addList.clear();
		}

		if (sectionToLoad != null) {
			loadRoom(sectionToLoad);
			sectionToLoad = null;
		}

        world.step(delta, 6, 2);

        for(Collision col: collisions){
            if(col.isBegin()){
                Collidable collidable = resolveCollidable(col.getThisFixture());
                if(collidable != null) {
                    collidable.beginCollision(col);
                }
            } else {
                Collidable collidable = resolveCollidable(col.getThisFixture());
                if(collidable != null){
                    collidable.endCollision(col);
                }
            }
        }
        collisions.clear();

        for (Actor actor : getActors()) {
			actor.update(delta);
		}

		getTiles().update(delta);

        for (Actor actor : removeList) {
            getActors().remove(actor);
            actor.store();
        }
        removeList.clear();

		for(Camera camera: cameras){
            camera.update(delta);
        }
	}

	public void render(SpriteBatch batch) {
		getTiles().render(batch);

		for (Actor actor : getActors()) {
			actor.render(batch);
		}
	}

	public Camera getCamera() {
		return cameras.get(0);
	}

	public World getWorld() {
		return world;
	}

	public void dispose() {
		world.dispose();
	}

	public Collidable resolveCollidable(Fixture fixture) {
        return (Collidable) fixture.getUserData();
	}

    @Override
    public void beginContact(Contact contact) {
        collisions.add(new Collision(contact.getFixtureA(), contact.getFixtureB(), contact, true));
        collisions.add(new Collision(contact.getFixtureB(), contact.getFixtureA(), contact, true));
    }

    @Override
    public void endContact(Contact contact) {
        collisions.add(new Collision(contact.getFixtureA(), contact.getFixtureB(), contact, false));
        collisions.add(new Collision(contact.getFixtureB(), contact.getFixtureA(), contact, false));
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}