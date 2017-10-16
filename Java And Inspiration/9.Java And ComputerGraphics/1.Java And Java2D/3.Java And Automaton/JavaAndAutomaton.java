import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
public class JavaAndAutomaton
{
	public static void main(String[] args)
	{
		Frame_Automaton Frame_Automaton1=new Frame_Automaton();
		Frame_Automaton1.setVisible(true);
	}
}
class Frame_Automaton extends Frame
{
	Automaton cellularAutomaton0,cellularAutomaton1,cellularAutomaton2,cellularAutomaton_Picture;
	public Frame_Automaton()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.getCellularAutomaton();
		this.getCellularAutomaton_Picture();
	}
	private void getCellularAutomaton()
	{
		char[] states=new char[]{'D','A'};
		Color[] colors=new Color[]{Color.black,Color.red};
		int[][] initState=new int[6][8];
		initState[2][2]=1;
		initState[3][3]=1;
		initState[3][4]=1;
		String[] transformRule=new String[]
		{
			"AAAAAD",
			"AAAADD",
			"AAADDD",
			"DADDDA",
			"ADDDDD"
		};
		int x0=50,y0=60;
		this.cellularAutomaton0=new Automaton(states,colors,initState,transformRule,x0,y0);
		this.cellularAutomaton0.iterate(0);
		this.cellularAutomaton1=new Automaton(states,colors,initState,transformRule,x0,y0+150);
		this.cellularAutomaton1.iterate(1);
		this.cellularAutomaton2=new Automaton(states,colors,initState,transformRule,x0,y0+300);
		this.cellularAutomaton2.iterate(2);
	}
	private void getCellularAutomaton_Picture()
	{
		int row=240,column=300;
		char[] states=new char[]{'D','A'};
		Color[] colors=new Color[]{Color.black,Color.red};
		int[][] initState=new int[row][column];
		initState[row/2][column/2]=1;
		String[] transformRule=new String[]
		{
			"DDDDDA",
			"DDDAAA",
			"DDAAAA",
			"DAAAAA",
			"ADDDDD",
			"ADDAAD",
			"AAAAAD"

		};
		int x0=50,y0=60;
		this.cellularAutomaton_Picture=new Automaton(states,colors,initState,transformRule,x0+300,y0);
		this.cellularAutomaton_Picture.iterate(50);
	}
	public void paint(Graphics g)
	{
		if(this.cellularAutomaton0!=null)this.cellularAutomaton0.paint(g,20);
		if(this.cellularAutomaton1!=null)this.cellularAutomaton1.paint(g,20);
		if(this.cellularAutomaton2!=null)this.cellularAutomaton2.paint(g,20);
		if(this.cellularAutomaton_Picture!=null)this.cellularAutomaton_Picture.paint(g);
	}
}
class Automaton
{
	private char[] states;
	private Color[] colors;
	private int row,column;
	private int[][] stateArray;
	private String[] transformRule;
	private int x0,y0;
	public Automaton(char[] states,Color[] colors,int[][] initState,String[] transformRule,int x0,int y0)
	{
		this.x0=x0;
		this.y0=y0;
		this.states=states;
		this.colors=colors;
		this.row=initState.length;
		this.column=initState[0].length;
		this.stateArray=new int[row][column];
		for(int i=0;i<row;i++)for(int j=0;j<column;j++)this.stateArray[i][j]=initState[i][j];
		this.transformRule=transformRule;
	}
	public void iterate(int times)
	{
		int[][] newStateArray=new int[row][column];
		for(int t=0;t<times;t++)
		{
			for(int i=1;i<row-1;i++)
			{
				for(int j=1;j<column-1;j++)
				{
					char currentState=states[stateArray[i][j]];
					String adjacentState=""+states[stateArray[i-1][j]]+states[stateArray[i+1][j]]+states[stateArray[i][j-1]]+states[stateArray[i][j+1]];
					char nextState=this.getNextState(currentState,adjacentState);
					if(nextState==currentState)newStateArray[i][j]=stateArray[i][j];
					else newStateArray[i][j]=this.getStateIndex(nextState);
				}
			}
			for(int i=0;i<row;i++)for(int j=0;j<column;j++)this.stateArray[i][j]=newStateArray[i][j];
		}
	}
	private static boolean isSimilar(String s1,String s2)
	{
		int l1=s1.length();
		int l2=s2.length();
		if(l1!=l2)return false;
		char[] c2=s2.toCharArray();
		for(int i=0;i<l1;i++)
		{
			char c1=s1.charAt(i);
			boolean hasC1=false;
			for(int j=0;j<l2;j++)
			{
				if(c2[j]==c1)
				{
					hasC1=true;
					c2[j]='.';
					break;
				}
			}
			if(!hasC1)return false;
		}
		return true;
	}
	private char getNextState(char currentState,String adjacentState)
	{
		String s=currentState+adjacentState;
		int l=transformRule.length;
		for(int i=0;i<l;i++)
		{
			if(currentState==transformRule[i].charAt(0))
			{
				String transformState=transformRule[i].substring(1,5);
				if(isSimilar(adjacentState,transformState))return transformRule[i].charAt(5);
			}
		}
		return currentState;
	}
	private int getStateIndex(char state)
	{
		int l=states.length;
		for(int i=0;i<l;i++)if(state==states[i])return i;
		return l;
	}
	public void paint(Graphics g)
	{
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				g.setColor(colors[stateArray[i][j]]);
				g.drawLine(j+x0,i+y0,j+x0,i+y0);
			}	
		}
	}
	public void paint(Graphics g,int r)
	{
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				g.setColor(colors[stateArray[i][j]]);
				((Graphics2D)g).fill(new Area(new Ellipse2D.Double(j*r+x0,i*r+y0,r,r)));
			}	
		}
	}
}
