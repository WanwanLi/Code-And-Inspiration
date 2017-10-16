import java.awt.*;
import java.awt.event.*;
public class JavaAndDLAGrass
{
	public static void main(String[] args)
	{
		Frame_DLAGrass Frame_DLAGrass1=new Frame_DLAGrass();
		Frame_DLAGrass1.setVisible(true);
	}
}
class Frame_DLAGrass extends Frame
{
	public Frame_DLAGrass()
	{
		this.setBackground(Color.BLACK);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

	}
	public void paint(Graphics g)
	{
		drawDLAGrass(g);
	}
	public void drawDLAGrass(Graphics g)
	{
		g.setColor(Color.GREEN);
		int[][] k=new int[481][641];
		for(int j=0;j<640;j++)
		{
			k[480][j]=1;
			g.drawLine(j,480,j,480);
		}
		int i,j,l;
		double d;
		int c=0;
		i=1;
		while(c<50000)
		{
			i=1;
			j=(int)(Math.random()*640);
			while(true)
			{
				if(i>=480||i<=0)i=1;
				while(j>=640||j<=1)j=(int)((Math.random()*640)%640);
				if((k[i+1][j]==1||k[i][j-1]==1||k[i][j+1]==1||k[i+1][j+1]==1||k[i+1][j-1]==1)&&k[i][j]==0)
				{
					k[i][j]=1;
					g.drawLine(j,i,j,i);
					c++;
					break;
				}
				d=Math.random();
				if(d<0.2)j--;
				else if(d<0.4){j--;i++;}
				else if(d<0.6)i++;
				else if(d<0.8){j++;i++;}
				else j++;							
			}														
		}								
	}	
}