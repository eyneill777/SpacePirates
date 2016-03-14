package spacepirates.game.level;

import java.awt.Point;
import java.util.ArrayList;

import spacepirates.game.Actor;
import spacepirates.game.Game;
import spacepirates.game.TestActor;
import spacepirates.game.tiles.DoorTile;
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
		TestActor testActor = new TestActor();
		testActor.setPosition(7 + getXOffset(), 4 + getYOffset());
		actors.add(testActor);

		tiles = new TileMap(15, 15);
        tiles.setOffset(getXOffset(), getYOffset());

        tiles.setTile(new WallTile(), 5, 5);
		tiles.setTile(new WallTile(), 2, 2);
        tiles.setTile(new WallTile(), 5, 9);
        tiles.setTile(new WallTile(), 4, 9);
        tiles.setTile(new WallTile(), 3, 9);
        tiles.setTile(new WallTile(), 2, 9);
	}

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public float getXOffset(){
        return location.x * width;
    }

    public float getYOffset(){
        return location.y * height;
    }

	protected void createContents(){
		if(getAdjacentRoom('S') != null){
			tiles.setTile(new DoorTile(getAdjacentRoom('S')), tiles.getWidth()/2, tiles.getHeight()-1);
		}
		if(getAdjacentRoom('N') != null){
			tiles.setTile(new DoorTile(getAdjacentRoom('N')), tiles.getWidth()/2, 0);
			//tiles.setTile(new FloorTile(), tiles.getWidth()/2, 0);
		}
		if(getAdjacentRoom('E') != null){
			tiles.setTile(new DoorTile(getAdjacentRoom('E')), tiles.getHeight()-1, tiles.getHeight()/2);
		}
		if(getAdjacentRoom('W') != null){
			tiles.setTile(new DoorTile(getAdjacentRoom('W')), 0, tiles.getHeight()/2);
		}
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
	
	public Room getAdjacentRoom(int direction)
	{
		if(direction == 'N' || direction == 'n')
		{
			return connectedRooms[0];
		}
		else if(direction == 'E' || direction == 'e')
		{
			return connectedRooms[1];
		}
		else if(direction == 'S' || direction == 's')
		{
			return connectedRooms[2];
		}
		else if(direction == 'W' || direction == 'w')
		{
			return connectedRooms[3];
		}
		return null;
	}
}
