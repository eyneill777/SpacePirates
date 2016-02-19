package Main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import Game.Game;

public class Main extends JFrame implements Runnable
{
	static Dimension windowDim;
	BufferedImage buffer;
	long lastUpdate = 0;
	ApplicationState currentState;
	
	public static void main(String args[])
	{
		windowDim = Toolkit.getDefaultToolkit().getScreenSize();
		EventQueue.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				Main f = new Main();
				f.setSize(windowDim);
				f.setUndecorated(true);
				f.setDefaultCloseOperation(EXIT_ON_CLOSE);
				f.setVisible(true);
			}
		});
	}
	
	public Main()
	{
		buffer = new BufferedImage(windowDim.width, windowDim.height, BufferedImage.TYPE_INT_ARGB);
		currentState = new Game();
	}
	
	public void run()
	{
		for(;;)
		{
			update();
			repaint();
		}
	}
	
	public void update()
	{
		currentState.update();
	}
	
	public void paint(Graphics gfx)
	{
		Graphics2D g = (Graphics2D) buffer.getGraphics();
		
		currentState.draw(g);
		
		gfx.drawImage(buffer, 0, 0, null);
	}
}
