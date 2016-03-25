package gken.rustyice.game.level;

import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import gken.rustyice.game.Actor;
import gken.rustyice.game.Game;
import gken.rustyice.game.SimpleLight;
import gken.rustyice.game.tiles.DoorTile;
import gken.rustyice.game.tiles.TileMap;
import gken.rustyice.game.tiles.WallTile;
import gken.rustyice.game.TestActor;

public class Section
{
	private Game game;
	private ArrayList<Actor> actors;
	private TileMap tiles;
	private Point location;
	
	private float width, height;
	//RoomBoundary boundary;
	Section[] connectedSections;
	
	public Section(int x, int y)
	{
		width = 30;
		height = 30;
		location = new Point(x,y);

		connectedSections = new Section[4];
		
		//boundary = new RoomBoundary(-width/2, -height/2, width, height);
		actors = new ArrayList<>();
		TestActor testActor = new TestActor();
		actors.add(testActor);
		
		Color[] colors = {Color.RED, Color.CYAN, Color.GOLD, Color.CORAL};
		for(int i = 0; i < 4;i++){
			SimpleLight light = new SimpleLight();
			light.setPosition(4 + (i/2)*15, 4 + (i%2)*15);
			light.setColor(colors[i]);
			actors.add(light);
		}
		
		tiles = new TileMap(30, 30);

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
	
	public Section getAdjacentRoom(int direction)
	{
		if(direction == 'N' || direction == 'n')
		{
			return connectedSections[0];
		}
		else if(direction == 'E' || direction == 'e')
		{
			return connectedSections[1];
		}
		else if(direction == 'S' || direction == 's')
		{
			return connectedSections[2];
		}
		else if(direction == 'W' || direction == 'w')
		{
			return connectedSections[3];
		}
		return null;
	}
}
