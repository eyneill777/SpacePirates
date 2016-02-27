package spacepirates.game.level;

import java.awt.Point;
import java.util.ArrayList;

import spacepirates.game.Actor;
import spacepirates.game.Game;
import spacepirates.game.TestActor;
import spacepirates.game.tiles.FloorTile;
import spacepirates.game.tiles.TileMap;
import spacepirates.game.tiles.WallTile;

public class Room
{
	private Game game;
	private ArrayList<Actor> actors;
	private TileMap tiles;
	private Point location;
	
	private float width, height;
	//RoomBoundary boundary;
	Room[] connectedRooms;
	
	public Room(int x, int y)
	{
		width = 15;
		height = 15;
		location = new Point(x,y);
		
		connectedRooms = new Room[4];
		
		//boundary = new RoomBoundary(-width/2, -height/2, width, height);
		actors = new ArrayList<>();
		actors.add(new TestActor());
		tiles = new TileMap(15, 15);
		tiles.setTile(new WallTile(), 2, 2);
		tiles.setTile(new WallTile(), 1, 2);
		
		if(getAdjacentRoom('N') != null){
			tiles.setTile(new FloorTile(), tiles.getWidth()/2, tiles.getHeight()-1);
		}
		if(getAdjacentRoom('S') != null){
			tiles.setTile(new FloorTile(), tiles.getWidth()/2, 0);
		}
		if(getAdjacentRoom('E') != null){
			tiles.setTile(new FloorTile(), tiles.getHeight()-1, tiles.getHeight()/2);
		}
		if(getAdjacentRoom('W') != null){
			tiles.setTile(new FloorTile(), 0, tiles.getHeight()/2);
		}
		
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
