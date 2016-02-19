package Game;

import java.awt.Graphics2D;

import Game.Level.Level;
import Main.ApplicationState;

public class Game extends ApplicationState
{
	Level currentLevel;
	
	public Game()
	{
		currentLevel = new Level(5);
	}

	@Override
	public void update() 
	{
		
	}

	@Override
	public void draw(Graphics2D g) 
	{
		
	}

}