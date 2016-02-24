package spacepirates.game.level;

import java.awt.Point;
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
	private Point location;
	
	private float width, height;
	RoomBoundary boundary;
	Room[] connectedRooms;
	
	public Room(int x, int y)
	{
		width = 10;
		height = 10;
		location = new Point(x,y);
		
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
	
	public Point getLocation()
	{
		return location;
	}
	
	public Room getAdjacentRoom(char direction)
	{
		if(direction == 'N')
		{
			return connectedRooms[0];
		}
		else if(direction == 'E')
		{
			return connectedRooms[1];
		}
		else if(direction == 'S')
		{
			return connectedRooms[2];
		}
		else if(direction == 'W')
		{
			return connectedRooms[3];
		}
		return null;
	}
}
