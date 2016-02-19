package Main;

import java.awt.Graphics2D;

public abstract class ApplicationState 
{
	public ApplicationState()
	{
		
	}
	
	public abstract void update();
	
	public abstract void draw(Graphics2D g);
}
