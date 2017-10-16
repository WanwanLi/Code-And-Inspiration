import java.util.*;

public class JavaAndDungeon
{
	public static void main(String[] args)
	{
		int mapLength=Integer.parseInt(args[0]);
		int roomsLength=Integer.parseInt(args[1]);
		Dungeon dungeon=new Dungeon(mapLength, roomsLength);
		dungeon.printMap();
	}
}
class Monster
{
	public char name; 
	public int row, col;
	public Monster(char name, int row, int col)
	{
		this.name=name;
		this.row=row;
		this.col=col;
	}
}
class Dungeon
{
	boolean[][] hasCorridor;
	boolean[][] isRoom, isCorridor;
	LinkedList<Monster> monstersList;
	int[] rogue; char[][] map; int[][] rooms;
	public char Rogue='@', Room='.', Corridor='+';
	public Dungeon(int mapLength, int roomsLength)
	{
		this.rogue=new int[2];
		this.rooms=new int[roomsLength][4];
		this.map=new char[mapLength][mapLength];
		this.isRoom=new boolean[mapLength][mapLength];
		this.isCorridor=new boolean[mapLength][mapLength];
		this.hasCorridor=new boolean[roomsLength][roomsLength];
		this.createMap(); this.createRooms(); 
		this.monstersList=new LinkedList<Monster>();
		this.addCorridors(); this.addRogue(); this.addMonster('M');
	}
	void createRooms()
	{
		for(int i=0; i<rooms.length; i++)
		{
			for(int j=0; j<rooms.length; hasCorridor[i][j++]=false);
			this.rooms[i]=randomRectangle();
			for(int t=0; t<100&&intersectRoom(i); t++)
			{
				this.rooms[i]=randomRectangle();
			}
			this.createRoom(rooms[i]);
		}
	}
	void addCorridors()
	{
		for(int i=0; i<rooms.length-1; i++)
		{
			Integer[] corridors=getCorridors(getCorridors(i, i+1));
			for(int j=0; j<corridors.length; j+=2)
			{
				this.addCorridor(center(corridors[j]), center(corridors[j+1]));
			}
		}
	}
	int[] center(int index)
	{
		int x=this.rooms[index][0]+this.rooms[index][2]/2;
		int y=this.rooms[index][1]+this.rooms[index][3]/2;
		return new int[]{x, y};
	}
	boolean intersectRoom(int index)
	{
		for(int i=0; i<index; i++)
		{
			if(intersectRoom(rooms[i], rooms[index]))return true;
		}
		return false;
	}
	boolean intersectRoom(int[] room1, int[] room2)
	{
		int x10=room1[0], y10=room1[1];
		int x20=room2[0], y20=room2[1];
		int x11=x10+room1[2], y11=y10+room1[3];
		int x21=x20+room2[2], y21=y20+room2[3];
		int[] startX=new int[]{x10, x20}, endX=new int[]{x11, x21};
		int[] startY=new int[]{y10, y20}, endY=new int[]{y11, y21};
		return intersect(startX, endX)&&intersect(startY, endY);
	}
	boolean intersect(int[] start, int[] end)
	{
		if(start[0]<=start[1]&&start[1]<=end[0])return true;
		if(start[1]<=start[0]&&start[0]<=end[1])return true;
		return false;
	}
	int[] randomRectangle()
	{
		int x=random(), y=random();
		int z=map.length/rooms.length;
		int w=random()/rooms.length;
		int h=random()/rooms.length;
		if(x+z+w>=map.length)w=map.length-1-z-x;
		if(y+z+h>=map.length)h=map.length-1-z-y;
		return new int[]{x, y, z+w, z+h};
	}
	int random()
	{
		return (int)((map.length-1.0)*Math.random());
	}
	void createMap()
	{
		for(int i=0; i<map.length; i++)
		{
			for(int j=0; j<map[0].length; j++)
			{
				this.map[i][j]=' ';
				this.isRoom[i][j]=false;
				this.isCorridor[i][j]=false;
			}
		}
	}
	void createRoom(int[] room)
	{
		int x=room[0], y=room[1];
		int w=room[2], h=room[3];
		for(int i=0; i<h; i++)
		{
			for(int j=0; j<w; j++)
			{
				this.map[i+y][j+x]=Room;
				this.isRoom[i+y][j+x]=true;
			}
		}
	}
	public void printMap()
	{
		for(int i=0; i<map.length; i++)
		{
			for(int j=0; j<map[0].length; j++)
			{
				char map=this.map[i][j];
				char monster=getMonsterName(i, j);
				if(monster!=0)map=monster;
				else if(isRogue(i, j))map=Rogue;
				System.out.print(map+" ");
			}
			System.out.println();
		}
	}
	char getMonsterName(int row, int col)
	{
		for(Monster m : monstersList)
		{
			if(m.row==row&&m.col==col)return m.name;
		}
		return '\0';
	}
	boolean isRogue(int row, int col)
	{
		return row==rogue[0]&&col==rogue[1];
	}
	void addRogue()
	{
		int row=random();
		int col=random();
		if(isRoom[row][col])
		{
			this.rogue[0]=row;
			this.rogue[1]=col;
		}
		else addRogue();
	}
	void addMonster(char name)
	{
		int row=random();
		int col=random();
		if(isRoom[row][col])
		{
			Monster monster=new Monster(name, row, col);
			this.monstersList.add(monster);
		}
		else addMonster(name);
	}
	Integer[] getCorridors(Integer[] corridors)
	{
		LinkedList<Integer> corridorsList=new LinkedList<Integer>();
		for(int i=0; i<corridors.length-1; i++)
		{
			int from=corridors[i];
			int to=corridors[i+1];
			if(!hasCorridor[from][to])
			{
				corridorsList.add(from);
				corridorsList.add(to);
				this.hasCorridor[from][to]=true;
				this.hasCorridor[to][from]=true;
			}
		}
		return (Integer[])corridorsList.toArray(new Integer[corridorsList.size()]);
	}
	Integer[] getCorridors(int from, int to)
	{
		LinkedList<Integer> corridors=new LinkedList<Integer>();
		int room=from, current=room;
		corridors.add(room);
		int[] start=center(from);
		int[] end=center(to);
		int x0=start[0], y0=start[1];
		int x1=end[0], y1=end[1];
		for(int j=x0; isIn(j, x0, x1)||isIn(j, x1, x0); j+=x0<=x1?1:-1)
		{
			current=getRoom(j, y0);
			if(current!=-1&&current!=room)
			{
				room=current;
				corridors.add(room);
			}
		}
		for(int i=y0; isIn(i, y0, y1)||isIn(i, y1, y0); i+=y0<=y1?1:-1)
		{
			current=getRoom(x1, i);
			if(current!=-1&&current!=room)
			{
				room=current;
				corridors.add(room);
			}
		}
		return (Integer[])corridors.toArray(new Integer[corridors.size()]);
	}
	int getRoom(int x, int y)
	{
		if(!isRoom[y][x])return -1;
		for(int i=0; i<rooms.length; i++)
		{
			if(isInRoom(i, x, y))return i;
		}
		return -1;
	}
	boolean isInRoom(int i, int x, int y)
	{
		int x0=this.rooms[i][0], y0=this.rooms[i][1];
		int x1=x0+rooms[i][2], y1=y0+rooms[i][3];
		return isIn(x, x0, x1)&&isIn(y, y0, y1);
	}
	boolean isIn(int x, int min, int max)
	{
		return min<=x&&x<=max;
	}
	void addCorridor(int[] start, int[] end)
	{
		int x0=start[0], y0=start[1];
		int x1=end[0], y1=end[1];
		if(x0>x1)x0=swap(x1, x1=x0);
		for(int j=x0; j<=x1; j++)
		{
			this.addCorridor(j, y0);
		}
		x1=end[0];
		if(y0>y1)y0=swap(y1, y1=y0);
		for(int i=y0; i<=y1; i++)
		{
			this.addCorridor(x1, i);
		}
	}
	void addCorridor(int x, int y)
	{
		if(map[y][x]==' ')
		{
			this.map[y][x]=Corridor;
			this.isCorridor[y][x]=true;
		}
	}
	int swap(int a, int b)
	{
		return a;
	}
}
