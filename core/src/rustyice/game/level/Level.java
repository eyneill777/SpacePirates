package rustyice.game.level;

import java.awt.Dimension;
import java.util.ArrayList;

public class Level 
{
	int difficulty;
	int startX, startY, width, height;
	private Section[][] sectionMap;
	
	public Level(int difficulty)
	{
		this.difficulty = difficulty;
		generateLevel();
	}
	
	public Section getStartingRoom()
	{
		return sectionMap[startX][startY];
	}
	
	public Section[][] getMap()
	{
		return sectionMap;
	}
	
	public Dimension getSize()
	{
		return new Dimension(width, height);
	}
	
	private void generateLevel()
	{		
		width = difficulty*5;//These can be changed later to reflect differences in ship design between races
		height = difficulty*5;
		boolean[][] map = new boolean[width][height];
		
		createPath(map, difficulty*10, width, height);
		map = trimMap(map);
		initializeRooms(map, width, height);
		printMap(map);
	}
	
	private boolean[][] trimMap(boolean[][] map)
	{
		int topLine, bottomLine, leftLine, rightLine;
		
		boolean foundPoint = false;
		int x = 0;
		while(!foundPoint)
		{
			for(int y = 0;y<height;y++)
			{
				if(map[x][y])
					foundPoint = true;
			}
			if(!foundPoint)
				x++;
		}
		leftLine = x;
		
		foundPoint = false;
		x = width-1;
		while(!foundPoint)
		{
			for(int y = 0;y<height;y++)
			{
				if(map[x][y])
					foundPoint = true;
			}
			if(!foundPoint)
				x--;
		}
		rightLine = x;
		
		foundPoint = false;
		int y = 0;
		while(!foundPoint)
		{
			for(x = 0;x<width;x++)
			{
				if(map[x][y])
					foundPoint = true;
			}
			if(!foundPoint)
				y++;
		}
		topLine = y;
		
		foundPoint = false;
		y = height-1;
		while(!foundPoint)
		{
			for(x = 0;x<width;x++)
			{
				if(map[x][y])
					foundPoint = true;
			}
			if(!foundPoint)
				y--;
		}
		bottomLine = y;
		
		boolean[][] newMap = new boolean[rightLine-leftLine+1][bottomLine-topLine+1];
		for(int dx = 0;dx<newMap.length;dx++)
		{
			for(int dy=0;dy<newMap[0].length;dy++)
			{
				newMap[dx][dy]=map[dx+leftLine][dy+topLine];
			}
		}
		startX-=leftLine;
		startY-=topLine;
		width = newMap.length;
		height = newMap[0].length;
		return newMap;
	}
	
	private void initializeRooms(boolean[][] map,int width, int height)
	{
		ArrayList<Section> roomsToCheck = new ArrayList<Section>();
		sectionMap = new Section[width][height];
		for(int x = 0;x<width;x++)
		{
			for(int y = 0;y<height;y++)
			{
				if(map[x][y])
				{
					sectionMap[x][y] = new Section(x,y);
					roomsToCheck.add(sectionMap[x][y]);
				}
			}
		}
		for(Section r:roomsToCheck)
		{
			int x = r.getLocation().x;
			int y = r.getLocation().y;
			if(x-1 >= 0 && map[x-1][y])
			{
				r.connectedSections[3] = sectionMap[x-1][y];
			}
			if(x+1 < width && map[x+1][y])
			{
				r.connectedSections[1] = sectionMap[x+1][y];
			}
			if(y-1 >= 0 && map[x][y-1])
			{
				r.connectedSections[0] = sectionMap[x][y-1];
			}
			if(y+1 < height && map[x][y+1])
			{
				r.connectedSections[2] = sectionMap[x][y+1];
			}
		}
		
		for(Section r :roomsToCheck){
			r.createContents();
		}
	}
	
	private void createPath(boolean[][] map, int numPoints, int width, int height)
	{
		int x = (int) (Math.random()*width);
		int y = (int) (Math.random()*height);
		startX = x;
		startY = y;
		map[startX][startY] = true;
		
		for(int i = 0;i<numPoints;i++)
		{
			boolean isValidPoint = false;
			while(!isValidPoint)
			{
				int dx=0,dy=0;
				double seed = Math.random();
				if(seed<=.25)
				{
					dx=0;
					dy=-1;
				}
				else if(seed <=.5)
				{
					dx=1;
					dy=0;
				}
				else if(seed <=.75)
				{
					dx=0;
					dy=1;
				}
				else
				{
					dx=-1;
					dy=0;
				}
				
				if(x+dx >= 0 && x+dx < width && y+dy >=0 && y+dy < height)
				{
					isValidPoint = true;
					x+=dx;
					y+=dy;
					map[x][y]=true;
				}
			}
		}
	}
	
	private void printMap(boolean[][] map)
	{
		for(int y = 0;y<map[0].length;y++)
		{
			for(int x = 0;x<map.length;x++)
			{
				if(map[x][y] && !(x == startX && y == startY))
					System.out.print("0");
				else if( x == startX && y == startY)
					System.out.print("S");
				else
					System.out.print(".");
			}
			System.out.println();
		}
	}
}