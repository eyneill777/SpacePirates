package spacepirates.game.level;

import java.awt.Point;
import java.util.ArrayList;

public class Level 
{
	private boolean isFirstPoint = true;
	int difficulty;
	int startX, startY;
	Room[][] roomMap;
	
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
		int startPathLength = difficulty*2;
		createPath(map, startPathLength, width, height);
		createPath(map, startPathLength, width, height);
		createPath(map, startPathLength, width, height);
		printMap(map);
		System.out.println();
		map=removeDisconnectedRooms(map, width, height);
		initializeRooms(map, width, height);
		printMap(map);
	}
	
	private void initializeRooms(boolean[][] map,int width, int height)
	{
		ArrayList<Room> roomsToCheck = new ArrayList<Room>();
		roomMap = new Room[width][height];
		for(int x = 0;x<width;x++)
		{
			for(int y = 0;y<height;y++)
			{
				if(map[x][y])
				{
					roomMap[x][y] = new Room(x,y);
					roomsToCheck.add(roomMap[x][y]);
				}
			}
		}
		for(Room r:roomsToCheck)
		{
			int x = r.getLocation().x;
			int y = r.getLocation().y;
			if(x-1 >= 0 && map[x-1][y])
			{
				r.connectedRooms[3] = roomMap[x-1][y];
			}
			if(x+1 < width && map[x+1][y])
			{
				r.connectedRooms[1] = roomMap[x+1][y];
			}
			if(y-1 >= 0 && map[x][y-1])
			{
				r.connectedRooms[0] = roomMap[x][y-1];
			}
			if(y+1 < height && map[x][y+1])
			{
				r.connectedRooms[2] = roomMap[x][y+1];
			}
		}
	}
	
	private boolean[][] removeDisconnectedRooms(boolean[][] originalMap, int width, int height)
	{
		boolean[][] map = new boolean[width][height];
		ArrayList<Point> pointsToCheck = new ArrayList<Point>();
		pointsToCheck.add(new Point(startX, startY));
		
		while(!pointsToCheck.isEmpty())
		{
			System.out.println(pointsToCheck.get(0).x+" "+pointsToCheck.get(0).y);
			Point p = pointsToCheck.get(0);
			if(originalMap[p.x][p.y])
			{
				map[p.x][p.y] = true;
			}
			pointsToCheck.remove(p);
		}
		
		return map;
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
		for(int y = 0;y<map[0].length;y++)
		{
			for(int x = 0;x<map.length;x++)
			{
				if(map[x][y] && x != startX && y != startY)
					System.out.print("0");
				else if( x == startX && y == startY)
					System.out.print("S");
				else
					System.out.print(".");
			}
			System.out.println();
		}
	}
	
	public Room getStartingRoom()
	{
		return roomMap[startX][startY];
	}
}