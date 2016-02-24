package spacepirates.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import spacepirates.game.level.Level;
import spacepirates.game.level.Room;
import spacepirates.game.tiles.TileMap;
import spacepirates.graphics.Camera;
import spacepirates.input.PlayerInput;
import spacepirates.resources.Resources;

public class Game implements ContactListener{
	public static final short CAT_BOUNDARY    = 0x0001;
	public static final short CAT_AGENT       = 0x0002;
	public static final short CAT_TILE        = 0x0004;
	public static final short CAT_BULLET      = 0x0008;
	public static final short CAT_ITEM        = 0x0010;
	public static final short CAT_OPEN_PORTEL = 0x0020;
	public static final short CAT_ALL         = -1;
	
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
	
	public Game(){
		world = new World(Vector2.Zero, true);
		world.setContactListener(this);
		
		camera = new Camera();
		camera.setWidth(10);
		camera.setHeight(10);
		
		Level level = new Level(5);
		
		roomToLoad = level.getStartingRoom();
		
		
		//actors = new ArrayList<>();
		addList = new ArrayList<>();
		removeList = new ArrayList<>();
		
		Player player = new Player();
		camera.setTarget(player);
		camera.setTracking(true);
		
		addActor(player);
		addActor(new TestActor());
	}
	
	private void loadRoom(Room room){
		if(actors != null){
			for(Actor actor: actors){
				actor.store();
			}
		}
		
		room.setGame(this);
		currentRoom = room;
		actors = room.getActors();
		for(Actor actor: actors){
			actor.setGame(this);
			actor.init();
		}
		
		if(tiles != null){
			tiles.store();
		}
		tiles = room.getTiles();
		tiles.setGame(this);
		tiles.init();
		
		room.loadRoom();
	}
	
	public void setPlayerInput(PlayerInput playerInput){
		this.playerInput = playerInput;
	}
	
	public PlayerInput getPlayerInput(){
		return playerInput;
	}
	
	public void addActor(Actor actor){
		addList.add(actor);
		actor.setGame(this);
	}
	
	public void removeActor(Actor actor){
		removeList.add(actor);
	}
	
	public Actor getActor(int i){
		return actors.get(i);
	}
	
	public void setResources(Resources resources){
		this.resources = resources;
	}
	
	public Resources getResources(){
		return resources;
	}
	
	public void update(float delta){
		if(actors != null){
			for(Actor actor: addList){
				actors.add(actor);
				actor.init();
			}
			addList.clear();
		}
		
		if(roomToLoad != null){
			loadRoom(roomToLoad);
			roomToLoad = null;
		}
		
		world.step(delta, 6, 2);
		
		for(Actor actor: actors){
			actor.update(delta);
		}
		
		tiles.update(delta);
		
		for(Actor actor: removeList){
			actors.remove(actor);
			actor.store();
		}
		removeList.clear();
		
		camera.update(delta);
	}
	
	public void render(SpriteBatch batch){
		tiles.render(batch);
		
		for(Actor actor: actors){
			actor.render(batch);
		}
	}
	
	
	public Camera getCamrea(){
		return camera;
	}
	
	public World getWorld(){
		return world;
	}
	
	public void dispose(){
		world.dispose();
	}

	@Override
	public void beginContact(Contact contact) {
		Collidable actorA;
		Collidable actorB;
		
		if(contact.getFixtureA().getUserData() == null){
			actorA = (Collidable)contact.getFixtureA().getBody().getUserData();
		} else {
			actorA = (Collidable)contact.getFixtureA().getUserData();
		}
		
		if(contact.getFixtureB().getUserData() == null){
			actorB = (Collidable)contact.getFixtureB().getBody().getUserData();
		} else {
			actorB = (Collidable)contact.getFixtureB().getUserData();
		}
		
		actorA.beginCollision(contact.getFixtureA(), contact.getFixtureB(), contact);
		actorB.beginCollision(contact.getFixtureB(), contact.getFixtureA(), contact);
	}

	@Override
	public void endContact(Contact contact) {
		Collidable actorA;
		Collidable actorB;
		
		if(contact.getFixtureA().getUserData() == null){
			actorA = (Collidable)contact.getFixtureA().getBody().getUserData();
		} else {
			actorA = (Collidable)contact.getFixtureA().getUserData();
		}
		
		if(contact.getFixtureB().getUserData() == null){
			actorB = (Collidable)contact.getFixtureB().getBody().getUserData();
		} else {
			actorB = (Collidable)contact.getFixtureB().getUserData();
		}
		
		actorA.endCollision(contact.getFixtureA(), contact.getFixtureB(), contact);
		actorB.endCollision(contact.getFixtureB(), contact.getFixtureA(), contact);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}
	
}