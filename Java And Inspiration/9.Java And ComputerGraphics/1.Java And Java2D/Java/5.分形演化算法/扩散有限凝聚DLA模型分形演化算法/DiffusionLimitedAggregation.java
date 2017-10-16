import java.awt.*;
import java.applet.*;
public class DiffusionLimitedAggregation extends Applet
{
	public void paint(Graphics g)
	{
		drawDiffusionLimitedAggregation(g,240);
	}
	public void drawDiffusionLimitedAggregation(Graphics g,int center)
	{
		
		int[][] k=new int[481][481];
		k[center][center]=1;
		g.drawLine(center,center,center,center);
		int i,j;
		double d;
		int c=0;
		while(c<50000)
		{
			i=(int)(Math.random()*480);
			j=(int)(Math.random()*480);
			while(true)
			{
				while(i>=480||i<=1)i=(int)(Math.random()*480);
				while(j>=480||j<=1)j=(int)(Math.random()*480);
				if((k[i][j-1]==1||k[i][j+1]==1||k[i+1][j+1]==1||k[i+1][j-1]==1||k[i+1][j]==1||k[i-1][j+1]==1||k[i-1][j-1]==1||k[i-1][j]==1)&&k[i][j]==0)
				{
					k[i][j]=1;
					g.drawLine(j,i,j,i);
					c++;
					break;
				}
				d=Math.random();
				if(d<0.125)j--;
				else if(d<0.25){j--;i++;}
				else if(d<0.375)i++;
				else if(d<0.5){j++;i++;}
				else if(d<0.625)j++;
				else if(d<0.75){j++;i--;}
				else if(d<0.875)i--;
				else {j--;i--;}						
			}														
		}								
	}	
}