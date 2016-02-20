package Game.Level;

import java.awt.Point;
import java.util.ArrayList;

public class Level 
{
	private boolean isFirstPoint = true;
	int difficulty;
	int startX, startY;
	
	public Level(int difficulty)
	{
		this.difficulty = difficulty;
		generateLevel();
	}
	
	private void generateLevel()
	{
		int width = (int) (Math.random()*difficulty-difficulty/2.0)+difficulty;
		int height = (int) (Math.random()*difficulty-difficulty/2.0)+difficulty*2;
		boolean[][] map = new boolean[width][height];
		for(int x = 0;x<width;x++)//initialize map to false
		{
			for(int y = 0;y<height;y++)
			{
				map[x][y]=false;
			}
		}
		int startPathLength = difficulty;
		createPath(map, startPathLength, width, height);
		createPath(map, startPathLength, width, height);
		createPath(map, startPathLength, width, height);
		map=removeDisconnectedRooms(map, width, height);//todo
		printMap(map);
	}
	
	private boolean[][] removeDisconnectedRooms(boolean[][] originalMap, int width, int height)
	{
		ArrayList<Point> pointsToCheck = new ArrayList<Point>();
		pointsToCheck.add(new Point(startX, startY));
		boolean[][] map = new boolean[width][height];
		for(int x = 0;x<width;x++)//initialize map to false
		{
			for(int y = 0;y<height;y++)
			{
				map[x][y]=false;
			}
		}
		
	}
	
	private void createPath(boolean[][] map, int numPoints, int width, int height)
	{
		int x = (int) (Math.random()*width);
		int y = (int) (Math.random()*height);
		if(isFirstPoint)
		{
			startX = x;
			startY = y;
			isFirstPoint = false;
		}
		for(int i = 0;i<numPoints;i++)
		{
			int dx=0, dy=0;
			int breakCount = 0;
			while((dx==0 && dy==0) || x+dx<0 || y+dy < 0 || x+dx>=width || y+dy>=height || map[x+dx][y+dy]==true)
			{
				breakCount++;
				double seed = Math.random();
				if(seed<=.25)
				{
					dx = -1;dy = 0;
				}
				else if(seed<=.5)
				{
					dy = -1;dx = 0;
				}
				else if(seed<=.75)
				{
					dx=1;dy=0;
				}
				else
				{
					dx=0;dy=1;
				}
				if(breakCount>50)
					break;
			}
			try{
			map[x+dx][y+dy] = true;}
			catch(Exception e){};
			x=x+dx;
			y=y+dy;
		}
	}
	
	private void printMap(boolean[][] map)
	{
		for(int x = 0;x<map.length;x++)
		{
			for(int y = 0;y<map[0].length;y++)
			{
				if(map[x][y])
					System.out.print("0");
				else
					System.out.print(".");
			}
			System.out.println();
		}
	}
}
