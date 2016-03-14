package spacepirates.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import spacepirates.game.level.Level;
import spacepirates.game.level.Room;
import spacepirates.game.physics.Collidable;
import spacepirates.game.physics.Collision;
import spacepirates.game.tiles.TileMap;
import spacepirates.graphics.Camera;
import spacepirates.input.PlayerInput;
import spacepirates.resources.Resources;

public class Game implements ContactFilter, ContactListener {
	public static final short CAT_BOUNDARY = 0x0001;
	public static final short CAT_AGENT = 0x0002;
	public static final short CAT_TILE = 0x0004;
	public static final short CAT_BULLET = 0x0008;
	public static final short CAT_ITEM = 0x0010;
	public static final short CAT_OPEN_PORTEL = 0x0020;
	public static final short CAT_ALL = -1;

	private Camera camera;
	private Resources resources;
	private PlayerInput playerInput;
	private World world;
	private Room currentRoom;
	private Room roomToLoad;
	private ArrayList<Actor> removeList;
	private ArrayList<Actor> addList;
    private ArrayList<Collision> collisions;

	public Game() {
		world = new World(Vector2.Zero, true);
        world.setContactListener(this);
		world.setContactFilter(this);

		camera = new Camera();
		camera.setWidth(10);
		camera.setHeight(10);

		Level level = new Level(5);

		roomToLoad = level.getStartingRoom();

		// actors = new ArrayList<>();
		addList = new ArrayList<>();
		removeList = new ArrayList<>();
		collisions = new ArrayList<>();

		Player player = new Player();
		camera.setTarget(player);
		camera.setTracking(true);

		addActor(player);
	}

	private void loadRoom(Room room) {
		if (currentRoom != null) {
			for(Actor actor: getActors()){
                actor.store();
            }
            getTiles().store();
		}

		room.setGame(this);
		currentRoom = room;
		for (Actor actor : currentRoom.getActors()) {
			actor.setGame(this);
			actor.init();
		}

		getTiles().setGame(this);
		getTiles().init();
	}

    public void setRoomToLoad(Room roomToLoad){
        this.roomToLoad = roomToLoad;
    }

	public TileMap getTiles(){
		return currentRoom.getTiles();
	}

    public ArrayList<Actor> getActors(){
        return currentRoom.getActors();
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
		return currentRoom.getActors().get(i);
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}

	public Resources getResources() {
		return resources;
	}

	public void update(float delta) {
		if (currentRoom != null) {
			for (Actor actor : addList) {
				getActors().add(actor);
				actor.init();
			}
			addList.clear();
		}

		if (roomToLoad != null) {
			loadRoom(roomToLoad);
			roomToLoad = null;
		}

		for (Actor actor : getActors()) {
			actor.update(delta);
		}

		getTiles().update(delta);

		world.step(delta, 6, 2);

        for(Collision col: collisions){
            if(col.isBegin()){
                resolveCollidable(col.getThisFixture()).beginCollision(col);
            } else {
                resolveCollidable(col.getThisFixture()).endCollision(col);
            }
        }
        collisions.clear();

		for (Actor actor : removeList) {
			getActors().remove(actor);
			actor.store();
		}
		removeList.clear();

		camera.update(delta);
	}

	public void render(SpriteBatch batch) {
		getTiles().render(batch);

		for (Actor actor : getActors()) {
			actor.render(batch);
		}
	}

	public Camera getCamera() {
		return camera;
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
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return resolveCollidable(fixtureA).shouldCollide(fixtureA, fixtureB) &&
                resolveCollidable(fixtureB).shouldCollide(fixtureB, fixtureA);
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