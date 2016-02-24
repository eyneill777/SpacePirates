package spacepirates.game.level;

import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import spacepirates.game.Actor;
import spacepirates.game.Game;

public class Room
{
	private Game game;
	private Sprite background;
	private ArrayList<Actor> actors;
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
	}
	
	public ArrayList<Actor> getActors(){
		return actors;
	}
	
	public void setGame(Game game){
		this.game = game;
	}
	
	public void loadRoom(){
		background = new Sprite(game.getResources().box);
		background.setBounds(-width/2, -height/2, width, height);;
		background.setColor(Color.GRAY);
		
		boundary.init(game.getWorld());
	}
	
	public void renderBackground(SpriteBatch batch){
		background.draw(batch);
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
