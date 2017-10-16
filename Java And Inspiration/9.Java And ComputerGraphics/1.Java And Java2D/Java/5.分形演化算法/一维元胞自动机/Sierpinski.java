import java.awt.*;
import java.applet.*;
public class Sierpinski extends Applet
{
	public void paint(Graphics g)
	{
		drawSierpinski(g,340);
	}
	public void drawSierpinski(Graphics g,int center)
	{
		
		int[] k0=new int[681];
		for(int i=1;i<680;i++)k0[i]=0;
		int[] k1=new int[681];
		for(int i=1;i<680;i++)k1[i]=0;
		k1[center]=1;
		boolean canDraw=true;
		int c=0;
		for(int j=1;j<460;j++)
		{	
			c=1-c;	
			for(int i=1;i<680;i++)
			{				
				if(c==1)
				{
					if((k1[i-1]==0&&k1[i]==0&&k1[i+1]==1)||(k1[i-1]==1&&k1[i]==0&&k1[i+1]==0))
					{
						k0[i]=1;
						g.drawRect(i,j,1,1);
					}
					else k0[i]=0;	
				}
				else
				{
					if((k0[i-1]==0&&k0[i]==0&&k0[i+1]==1)||(k0[i-1]==1&&k0[i]==0&&k0[i+1]==0))
					{
						k1[i]=1;
						g.drawRect(i,j,1,1);
					}
					else k1[i]=0;
				}
								
			}
		}
	}
}