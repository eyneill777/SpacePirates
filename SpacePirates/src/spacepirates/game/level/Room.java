package spacepirates.game.level;

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
}
