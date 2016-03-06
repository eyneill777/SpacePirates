package spacepirates.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import spacepirates.game.level.Level;
import spacepirates.game.level.Room;
import spacepirates.game.physics.Collidable;
import spacepirates.game.tiles.TileMap;
import spacepirates.graphics.Camera;
import spacepirates.input.PlayerInput;
import spacepirates.resources.Resources;

public class Game implements ContactFilter {
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
	private ArrayList<Actor> actors;
	private ArrayList<Actor> removeList;
	private ArrayList<Actor> addList;
	private TileMap tiles;

	public Game() {
		world = new World(Vector2.Zero, true);
		world.setContactFilter(this);

		camera = new Camera();
		camera.setWidth(10);
		camera.setHeight(10);

		Level level = new Level(5);

		roomToLoad = level.getStartingRoom();

		// actors = new ArrayList<>();
		addList = new ArrayList<>();
		removeList = new ArrayList<>();

		Player player = new Player();
		camera.setTarget(player);
		camera.setTracking(true);

		addActor(player);
	}

	private void loadRoom(Room room) {
		if (actors != null) {
			for (Actor actor : actors) {
				actor.store();
			}
		}

		room.setGame(this);
		currentRoom = room;
		actors = room.getActors();
		for (Actor actor : actors) {
			actor.setGame(this);
			actor.init();
		}

		if (tiles != null) {
			tiles.store();
		}
		tiles = room.getTiles();
		tiles.setGame(this);
		tiles.init();
	}

    public void setRoomToLoad(Room roomToLoad){
        this.roomToLoad = roomToLoad;
    }

	public TileMap getTiles(){
		return tiles;
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
		return actors.get(i);
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
				actors.add(actor);
				actor.init();
			}
			addList.clear();
		}

		if (roomToLoad != null) {
			loadRoom(roomToLoad);
			roomToLoad = null;
		}

		for (Actor actor : actors) {
			actor.update(delta);
		}

		tiles.update(delta);

		world.step(delta, 6, 2);

        for(Contact contact: world.getContactList()){
            if(contact.isTouching()){
                resolveCollidable(contact.getFixtureA()).collision(contact.getFixtureA(), contact.getFixtureB(), contact);
                resolveCollidable(contact.getFixtureB()).collision(contact.getFixtureB(), contact.getFixtureA(), contact);
            }
        }

		for (Actor actor : removeList) {
			actors.remove(actor);
			actor.store();
		}
		removeList.clear();

		camera.update(delta);
	}

	public void render(SpriteBatch batch) {
		tiles.render(batch);

		for (Actor actor : actors) {
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
}