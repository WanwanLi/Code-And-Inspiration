public class JavaAndMaze
{
	public static void main(String[] args)
	{
		Maze Maze0=new Maze();
		Maze0.showResult();
	}
}
class Maze
{
	class Clue
	{
		public int i;
		public int j;
		public int d;
		public Clue Next;
		public Clue(int i,int j,int d)
		{
			this.i=i;
			this.j=j;
			this.d=d;
		}
	}
	class Path
	{
		public Clue Clue0;
		public void addClue(int i,int j,int d)
		{
			Clue clue=new Clue(i,j,d);
			clue.Next=Clue0;
			Clue0=clue;
		}
		public void deleteClue()
		{
			Clue0=Clue0.Next;
		}
		public boolean hasClue()
		{
			return (Clue0!=null);
		}
	}
	char[][] Map;
	Path Path0;
	boolean findNextClue;
	public Maze()
	{
		Map=new char[][]
		{
			{'*','*','*','*','*','*','*','*','*','*'},
			{'*','_','_','*','_','_','_','*','_','*'},
			{'*','_','_','*','_','_','_','*','_','*'},
			{'*','_','_','_','_','*','*','_','_','*'},
			{'*','_','*','*','*','_','_','_','_','*'},
			{'*','_','_','_','*','_','_','_','_','*'},
			{'*','_','*','_','_','_','*','_','_','*'},
			{'*','_','*','*','*','_','*','*','_','*'},
			{'*','*','_','_','_','_','_','_','_','*'},
			{'*','*','*','*','*','*','*','*','*','*'}
		};
		Path0=new Path();
	}	
	private Clue getNextClue(int d)
	{
		Clue nextClue=Path0.Clue0;
		switch(d)
		{
			case 0:nextClue=new Clue(Path0.Clue0.i,Path0.Clue0.j+1,0);break;
			case 1:nextClue=new Clue(Path0.Clue0.i+1,Path0.Clue0.j,0);break;
			case 2:nextClue=new Clue(Path0.Clue0.i,Path0.Clue0.j-1,0);break;
			case 3:nextClue=new Clue(Path0.Clue0.i-1,Path0.Clue0.j,0);break;
		}
		return nextClue;
	}
	public void showPath()
	{
		String s="Solve Maze Problem:\n\n";
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				if(Map[i][j]=='x')Map[i][j]='_';
				s+=" "+Map[i][j];
			}
			s+="\n";
		}
		System.out.println(s);
		System.exit(0);		
	}
	public void showResult()
	{
		int i,j,d;
		Clue clue;
		Map[1][1]='z';
		Path0.addClue(1,1,0);
		while(Path0.hasClue())
		{
			clue=Path0.Clue0;
			i=clue.i;
			j=clue.j;
			d=clue.d;
			if(i==8&&j==8)this.showPath();
			findNextClue=false;
			for(;d<4;d++)
			{
				clue=this.getNextClue(d);
				if(Map[clue.i][clue.j]=='_'){findNextClue=true;break;}
			}
			if(findNextClue)
			{
				Path0.Clue0.d=d;
				Path0.addClue(clue.i,clue.j,0);
				Map[clue.i][clue.j]='z';
			}
			else
			{
				Map[i][j]='x';
				Path0.deleteClue();
			}
		}
		System.out.println("Find No Exit!");
	}
}






