package rustyice.game.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameHudObject 
{
	public GameHudObject()
	{
		
	}
	
	public abstract void render(SpriteBatch batch);
	
	public abstract void update(float delta);
}
