package Game.Level;

public class Level 
{
	int difficulty;
	
	public Level(int difficulty)
	{
		this.difficulty = difficulty;
		generateLevel();
	}
	
	private void generateLevel()
	{
		int width = (int) (Math.random()*20-10)+difficulty*3;
		int height = (int) (Math.random()*20-10)+difficulty*5;
		boolean[][] map = new boolean[width][height];
		for(int x = 0;x<width;x++)//initialize map to false
		{
			for(int y = 0;y<height;y++)
			{
				map[x][y]=false;
			}
		}
		int startPathLength = difficulty*10;
		createPath(map, startPathLength, width, height);
		createPath(map, startPathLength, width, height);
		createPath(map, startPathLength, width, height);
		removeDisconnectedRooms();//todo
		printMap(map);
	}
	
	private void removeDisconnectedRooms()
	{
		
	}
	
	private void createPath(boolean[][] map, int numPoints, int width, int height)
	{
		int x = (int) (Math.random()*width);
		int y = (int) (Math.random()*height);
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
