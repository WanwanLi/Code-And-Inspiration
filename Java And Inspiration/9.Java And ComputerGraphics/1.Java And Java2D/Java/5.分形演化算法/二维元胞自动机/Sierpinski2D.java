import java.awt.*;
import java.applet.*;
public class Sierpinski2D extends Applet
{
	public void paint(Graphics g)
	{
		drawSierpinski2D(g,230);
	}
	public void drawSierpinski2D(Graphics g,int center)
	{
		
		int[][] k0=new int[461][461];
		int[][] k1=new int[461][461];
		k1[center][center]=1;
		int c=0;		
		for(int j=center;j<460;j++)
		{	
			c=1-c;	
			for(int i=1;i<460;i++)
			{				
				if(c==1)
				{
					if((k1[i-1][j-1]+k1[i][j-1]+k1[i+1][j-1]+k1[i+1][j]+k1[i+1][j+1]+k1[i][j+1]+k1[i-1][j+1]+k1[i-1][j])%2==1)
					{
						k0[i][j]=1;
						g.drawLine(i,j,i,j);
					}
					else k0[i][j]=0;	
				}
				else
				{
					if((k0[i-1][j-1]+k0[i][j-1]+k0[i+1][j-1]+k0[i+1][j]+k0[i+1][j+1]+k0[i][j+1]+k0[i-1][j+1]+k0[i-1][j])%2==1)
					{
						k1[i][j]=1;
						g.drawLine(i,j,i,j);
					}
					else k1[i][j]=0;
				}
								
			}
		}
		c=0;
		for(int j=center;j>0;j--)
		{	
			c=1-c;	
			for(int i=1;i<460;i++)
			{				
				if(c==1)
				{
					if((k1[i-1][j-1]+k1[i][j-1]+k1[i+1][j-1]+k1[i+1][j]+k1[i+1][j+1]+k1[i][j+1]+k1[i-1][j+1]+k1[i-1][j])%2==1)
					{
						k0[i][j]=1;
						g.drawLine(i,j,i,j);
					}
					else k0[i][j]=0;	
				}
				else
				{
					if((k0[i-1][j-1]+k0[i][j-1]+k0[i+1][j-1]+k0[i+1][j]+k0[i+1][j+1]+k0[i][j+1]+k0[i-1][j+1]+k0[i-1][j])%2==1)
					{
						k1[i][j]=1;
						g.drawLine(i,j,i,j);
					}
					else k1[i][j]=0;
				}
								
			}
		}
		c=0;
		for(int j=center;j<460;j++)
		{	
			c=1-c;	
			for(int i=1;i<460;i++)
			{				
				if(c==1)
				{
					if((k1[i-1][j-1]+k1[i][j-1]+k1[i+1][j-1]+k1[i+1][j]+k1[i+1][j+1]+k1[i][j+1]+k1[i-1][j+1]+k1[i-1][j])%2==1)
					{
						k0[i][j]=1;
						g.drawLine(j,i,j,i);
					}
					else k0[i][j]=0;	
				}
				else
				{
					if((k0[i-1][j-1]+k0[i][j-1]+k0[i+1][j-1]+k0[i+1][j]+k0[i+1][j+1]+k0[i][j+1]+k0[i-1][j+1]+k0[i-1][j])%2==1)
					{
						k1[i][j]=1;
						g.drawLine(j,i,j,i);
					}
					else k1[i][j]=0;
				}
								
			}
		}
		c=0;
		
		for(int j=center;j>0;j--)
		{	
			c=1-c;	
			for(int i=1;i<460;i++)
			{				
				if(c==1)
				{
					if((k1[i-1][j-1]+k1[i][j-1]+k1[i+1][j-1]+k1[i+1][j]+k1[i+1][j+1]+k1[i][j+1]+k1[i-1][j+1]+k1[i-1][j])%2==1)
					{
						k0[i][j]=1;
						g.drawLine(j,i,j,i);
					}
					else k0[i][j]=0;	
				}
				else
				{
					if((k0[i-1][j-1]+k0[i][j-1]+k0[i+1][j-1]+k0[i+1][j]+k0[i+1][j+1]+k0[i][j+1]+k0[i-1][j+1]+k0[i-1][j])%2==1)
					{
						k1[i][j]=1;
						g.drawLine(j,i,j,i);
					}
					else k1[i][j]=0;
				}
								
			}
		}
	}
}