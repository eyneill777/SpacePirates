package Main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Main extends JFrame implements Runnable
{
	static Dimension windowDim;
	BufferedImage buffer;
	long lastUpdate = 0;
	
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
	}
	
	public void run()
	{
		for(;;)
		{
			
		}
	}
	
	public void update()
	{
		
	}
	
	public void paint(Graphics gfx)
	{
		Graphics2D g = (Graphics2D) buffer.getGraphics();
		
		
		
		gfx.drawImage(buffer, 0, 0, null);
	}
}
