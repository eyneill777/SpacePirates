package spacepirates.game.level;

import java.util.ArrayList;

import spacepirates.game.Actor;
import spacepirates.game.Game;
import spacepirates.game.tiles.TileMap;
import spacepirates.game.tiles.WallTile;

public class Room
{
	private Game game;
	private ArrayList<Actor> actors;
	private TileMap tiles;
	
	private float width, height;
	RoomBoundary boundary;
	
	Room[] connectedRooms;
	
	public Room()
	{
		width = 10;
		height = 10;
		
		connectedRooms = new Room[4];
		boundary = new RoomBoundary(-width/2, -height/2, width, height);
		actors = new ArrayList<>();
		tiles = new TileMap(10, 10);
		tiles.setTile(new WallTile(), 2, 2);
		tiles.setOffset(-width/2, -height/2);
	}
	
	public ArrayList<Actor> getActors(){
		return actors;
	}
	
	public TileMap getTiles(){
		return tiles;
	}
	
	public void setGame(Game game){
		this.game = game;
	}
	
	public void loadRoom(){
		boundary.init(game.getWorld());
	}
	
	public void unloadRoom(){
		boundary.store(game.getWorld());
	}
}
