import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Hashtable;
import javax.swing.Timer;

public class JavaAndRogueGame
{
	public static void main(String[] args)
	{
		int interval=Integer.parseInt(args[0]);
		int maxTime=Integer.parseInt(args[1]);
		new RogueGame(interval, maxTime);
	}
}
class RogueGame extends Frame implements ActionListener
{
	int x0=50, y0=50;
	int time=0, maxTime;
	int[] rogue; char[][] map;
	int left=x0+50, top=y0+40;
	int width=400, height=450;
	boolean[][] isRoom, isCorridor;
	public boolean isNotGameOver;
	LinkedList<Monster> monstersList;
	Hashtable<String, Image> imageTable;
	LinkedList<int[]> corridorEntrancesList;
	Image backgroundImage, rogueImage;
	Color darkColor=new Color(20, 70, 90);
	Color lightColor=new Color(1f,1f,1f,0.4f);
	public char Rogue='@', Room='.', Corridor='+', Wall=' ';
	int[][] directionTable4D=
	{
		             {0, -1}, 
		{-1,  0}, {0,  0}, {1,  0},
		             {0,  1} 
	};
	int[][] directionTable8D=
	{
		{-1, -1}, {0, -1}, {1, -1},
		{-1,  0}, {0,  0}, {1,  0},
		{-1,  1}, {0,  1}, {1,  1}
	};
	int[][][] directionTable5D=
	{
		{
			{-1,  0}, {0,  0}, {1,  0},
			{-1,  1}, {0,  1}, {1,  1}
		},
		{
			{0, -1}, {1, -1},
			{0,  0}, {1,  0},
			{0,  1}, {1,  1}
		},
		{
			{0,  0}
		},
		{
			{-1, -1}, {0, -1}, 
			{-1,  0}, {0,  0}, 
			{-1,  1}, {0,  1}
		},
		{
			{-1, -1}, {0, -1}, {1, -1},
			{-1,  0}, {0,  0}, {1,  0}
		}
	};
	public RogueGame(int interval, int maxTime)
	{
		this.initGame();
		this.initImageTable();
		this.maxTime=maxTime;
		this.addTimer(this, interval);
		this.setBounds(x0, y0, 600, 600);
		this.setResizable(false);
		this.setVisible(true);
	}
	private void addTimer(ActionListener actionListener, int interval)
	{
		new Timer(interval*100, actionListener).start();
	}
	public void actionPerformed(ActionEvent e)
	{
		if(!isNotGameOver)return;
		if(time++>maxTime){isNotGameOver=false;return;}
		if(time%2==1)this.moveRogue();
		else this.moveMonsters();
		this.repaint();
	}
	public void paint(Graphics g)
	{
		BufferedImage bufferedImage=new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		this.drawImage(bufferedImage.getGraphics()); g.drawImage(bufferedImage, 0, 0, null);
	}
	void drawImage(Graphics g)
	{
		g.drawImage(backgroundImage, 0, 0, this);
		this.drawRect(g, left-2, top-2, width+4, height+4);
		this.drawMap(g);
		this.drawGrids(g);
		g.setColor(Color.white);
		int t0=(time+1)/2+1, t1=time/2+1;
		g.drawString("Rogue.Step#:"+t0, left, top-10);
		g.drawString("Monsters.Step#:"+t1, left+300, top-10);
		if(!isNotGameOver)
		{
			Image image=(Image)imageTable.get("GameOver.gif");
			g.drawImage(image,left-25,top+100,this);
		}
	}
	void drawMap(Graphics g)
	{
		int n=map.length;
		int h=height/n,w=width/n;
		for(int i=0; i<n; i++)
		{
			for(int j=0; j<n; j++)
			{
				char map=this.map[i][j];
				if(map==Corridor||map==Wall)
				{
					String name=map==Wall?"Wall":Corridor+"";
					Image image=(Image)imageTable.get(name+".gif");
					g.drawImage(image, left+j*w, top+i*h, this);
				}
				char monster=getMonsterName(i, j);
				if(monster!=0)map=monster;
				else if(isRogue(i, j))map=Rogue;
				if(map!=this.map[i][j])
				{
					Image image=(Image)imageTable.get(map+".gif");
					g.drawImage(image, left+j*w, top+i*h, this);
				}
			}
		}
	}
	void drawGrids(Graphics g)
	{
		int n=map.length;
		int h=height/n,w=width/n;
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				this.drawRect(g, left+j*w, top+i*h, w, h);
			}
		}
	}
	void drawRect(Graphics g, int x, int y, int w, int h)
	{
		g.setColor(darkColor);
		g.drawRect(x, y, w, h);
		g.setColor(lightColor);
		g.drawRect(x-1, y-1, w+2, h+2);
	}
	void initImageTable()
	{
		this.imageTable=new Hashtable<String, Image>();
		String dir="images", name; Image image;
		File[] files=new File(dir).listFiles(); 
		for(File file : files) 
		{
			name=file.getName();
			image=Toolkit.getDefaultToolkit().getImage(dir+"\\"+name);
			this.imageTable.put(name, image);
		}
		this.backgroundImage=(Image)imageTable.get("background.gif");
	}
	void initGame()
	{
		this.corridorEntrancesList=new LinkedList<int[]>();
		this.monstersList=new LinkedList<Monster>();
		Scanner scanner=new Scanner(System.in);
		String string=scanner.nextLine(); 
		int n=Integer.parseInt(string);
		this.isNotGameOver=true;
		this.rogue=new int[2];
		this.map=new char[n][n];
		this.isRoom=new boolean[n][n];
		this.isCorridor=new boolean[n][n];
		for(int i=0; i<n; i++)
		{
			string=scanner.nextLine();
			for(int j=0; j<n; j++)
			{
				this.map[i][j]=string.charAt(j*2);
				this.isRoom[i][j]=map[i][j]==Room;
				this.isCorridor[i][j]=map[i][j]==Corridor;
				if(map[i][j]==Rogue)this.addRogue(i, j);
				else if(isMonster(i, j))this.addMonster(i, j);
			}
		}
		for(int i=0; i<n; i++)
		{
			for(int j=0; j<n; j++)
			{
				if(isCorridorEntrance(i, j))
				{
					this.corridorEntrancesList.add(new int[]{i, j});
				}
			}
		}
	}
	void addRogue(int row, int col)
	{
		this.rogue[0]=row;
		this.rogue[1]=col;
		this.map[row][col]=Room;
		this.isRoom[row][col]=true;
	}
	void addMonster(int row, int col)
	{
		Monster monster=new Monster(this, map[row][col], row, col);
		this.monstersList.add(monster);
		this.isRoom[row][col]=true;
		this.map[row][col]=Room;
	}
	boolean isCorridorEntrance(int row, int col)
	{
		int[][] dir=directionTable4D;
		if(!isRoom[row][col])return false;
		for(int i=0; i<dir.length; i++)
		{
			int x=row+dir[i][0];
			int y=col+dir[i][1];
			if(!isValidIndex(x))continue;
			if(!isValidIndex(y))continue;
			if(isCorridor[x][y])return true;
		}
		return false;
	}
	boolean isRogue(int row, int col)
	{
		return row==rogue[0]&&col==rogue[1];
	}
	boolean isMonster(int row, int col)
	{
		return 'A'<=map[row][col]&&map[row][col]<='Z';
	}
	char getMonsterName(int row, int col)
	{
		for(Monster m : monstersList)
		{
			if(m.row==row&&m.col==col)return m.name;
		}
		return '\0';
	}
	boolean isValidIndex(int index)
	{
		return 0<=index&&index<map.length;
	}
	public boolean isValidMove(int row, int col)
	{
		boolean isValidPosition=isValidIndex(row)&&isValidIndex(col);
		return isValidPosition&&(isRoom[row][col]||isCorridor[row][col]);
	}
	public void moveRogue()
	{
		Monster m=getClosestMonster();
		int[][] dir=getDirectionTable(rogue);
		int d=getBestMoveDirection(dir, m);
		if(isRoom[rogue[0]][rogue[1]])
		d=getBetterMoveDirection(dir, m, d);
		this.rogue[0]+=dir[d][0];
		this.rogue[1]+=dir[d][1];
	}
	public void moveMonsters()
	{
		for(Monster monster : monstersList)
		{
			monster.moveTowards(rogue);
			if(monster.capturedRogue)
			{
				this.isNotGameOver=false;
			}
		}
	}
	Monster getClosestMonster()
	{
		int x0=rogue[0], y0=rogue[1];
		Monster closestMonster=null;
		double minDistance=maxDistance(), d;
		for(Monster m : monstersList)
		{
			int x1=m.row, y1=m.col;
			d=distance(x0, y0, x1, y1);
			if(d<minDistance)
			{
				minDistance=d;
				closestMonster=m;
			}
		}
		return closestMonster;
	}
	int getBestMoveDirection(int[][] dir, Monster monster)
	{
		int bestMoveDir=4;
		int x0=monster.row;
		int y0=monster.col;
		double maxDistance=0, d;
		for(int i=0; i<dir.length; i++)
		{
			int x1=rogue[0]+dir[i][0];
			int y1=rogue[1]+dir[i][1];
			if(!isValidMove(x1, y1))continue;
			d=distance(x0, y0, x1, y1);
			if(d>maxDistance)
			{
				maxDistance=d;
				bestMoveDir=i;
			}
		}
		return bestMoveDir;
	}
	int getBetterMoveDirection(int[][] dir, Monster monster, int bestMoveDir)
	{
		int x0=rogue[0], y0=rogue[1];
		int betterMoveDir=bestMoveDir;
		double minDistance=maxDistance(), d;
		for(int[] corridor : corridorEntrancesList)
		{
			int x1=corridor[0], y1=corridor[1], i=getCorridorDirection(dir, corridor);
			if((d=distance(x0, y0, x1, y1))<minDistance&&isGettingAwayFrom(monster, dir, i))
			{
				minDistance=d; betterMoveDir=i;
			}
		}
		return betterMoveDir;
	}
	boolean isGettingAwayFrom(Monster monster, int[][] dir, int direction)
	{
		int x=monster.row, y=monster.col;
		int x0=rogue[0], y0=rogue[1];
		int x1=x0+dir[direction][0];
		int y1=y0+dir[direction][1];
		int d0=distance(x, y, x0, y0);
		int d1=distance(x, y, x1, y1);
		return d1>=d0;
	}
	int getCorridorDirection(int[][] dir, int[] corridor)
	{
		int corridorDirection=0;
		int[] d=sub(corridor, rogue);
		double l=length(d), a;
		double minAngle=Math.PI;
		for(int i=0; i<dir.length; i++)
		{
			int x1=rogue[0]+dir[i][0];
			int y1=rogue[1]+dir[i][1];
			if(!isValidMove(x1, y1))continue;
			if(l==0&&isCorridor[x1][y1])return i;
			if((a=angle(d, dir[i]))<=minAngle)
			{
				minAngle=a;
				corridorDirection=i;
			}
		}
		return corridorDirection;
	}
	int[] sub(int[] x, int[] y)
	{
		return new int[]{x[0]-y[0], x[1]-y[1]};
	}
	double angle(int[] x, int[] y)
	{
		double[] a=normalized(x);
		double[] b=normalized(y);
		return Math.acos(dot(a, b));
	}
	double[] normalized(int[] x)
	{
		double l=length(x); if(l==0)l=1;
		return new double[]{x[0]/l, x[1]/l};
	}
	double length(int[] x)
	{
		return Math.sqrt(x[0]*x[0]+x[1]*x[1]);
	}
	double dot(double[] x, double[] y)
	{
		return x[0]*y[0]+x[1]*y[1];
	}
	int[][] getDirectionTable(int[] position)
	{
		return this.getDirectionTable(position[0], position[1]);
	}
	String toString(int[] x)
	{
		return "("+x[0]+", "+x[1]+")";
	}
	public int[][] getDirectionTable(int row, int col)
	{
		if(isCorridor[row][col])return directionTable4D;
		if(isRoom[row][col])
		{
			int[][] dir=directionTable4D;
			for(int i=0; i<dir.length; i++)
			{
				int x1=row+dir[i][0];
				int y1=col+dir[i][1];
				if(!isValidMove(x1, y1))
				{
					return directionTable5D[i];
				}
			}
			return directionTable8D;
		}
		return directionTable8D;
	}
	public int distance(int x0, int y0, int x1, int y1)
	{
		int dx=x1-x0, dy=y1-y0;
		return dx*dx+dy*dy;
	}
	public int maxDistance()
	{
		int n=map.length;
		return n*n+n*n;
	}
	public void printMap()
	{
		int n=map.length;
		for(int i=0; i<n; i++)
		{
			for(int j=0; j<n; j++)
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
}
class Monster
{
	public char name; 
	public int row, col;
	public RogueGame game; 
	public boolean capturedRogue;
	public Monster(RogueGame game, char name, int row, int col)
	{
		this.capturedRogue=false;
		this.game=game;
		this.name=name;
		this.row=row;
		this.col=col;
	}
	public void moveTowards(int[] rogue)
	{
		int[][] dir=game.getDirectionTable(row, col);
		int d=getBestMoveDirection(dir, rogue);
		this.row+=dir[d][0]; this.col+=dir[d][1];
		if(capturedRogue)
		{
			dir=game.getDirectionTable(row, col);
			this.captureRogue(dir, rogue);
		}
	}
	int getBestMoveDirection(int[][] dir, int[] rogue)
	{
		int bestMoveDir=4;
		int x0=rogue[0], y0=rogue[1];
		double minDistance=game.maxDistance(), d;
		for(int i=0; i<dir.length; i++)
		{
			int x1=row+dir[i][0];
			int y1=col+dir[i][1];
			if(!game.isValidMove(x1, y1))continue;
			d=game.distance(x0, y0, x1, y1);
			if(d<minDistance)
			{
				minDistance=d;
				bestMoveDir=i;
			}
			if(minDistance<=2)
			{
				this.capturedRogue=true;
				return bestMoveDir;
			}
		}
		return bestMoveDir;
	}
	void captureRogue(int[][] dir, int[] rogue)
	{
		for(int i=0; i<dir.length; i++)
		{
			int x=row+dir[i][0], y=col+dir[i][1];
			if(rogue[0]==x&&rogue[1]==y)
			{
				this.capturedRogue=true; return;
			}
		}
		this.capturedRogue=false;
	}
}