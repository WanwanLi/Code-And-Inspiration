import java.awt.*;
import java.applet.*;
public class Sierpinski extends Applet
{
	public void paint(Graphics g)
	{
		drawSierpinski(g,0,0,1,1,12,400,300);
	}
	public void drawSierpinski(Graphics g,double a,double b,double c,double d,double t,int m,int R)
	{
		double x,y;
		boolean canDraw=true;
		for(int i=1;i<m;i++)
		{
			for(int j=1;j<m;j++)
			{
				x=a+(c-a)*i/m;
				y=b+(d-b)*j/m;
				for(int k=1;k<t;k++)
				{
					if(y>0.5){x=x*2;y=y*2-1;}
					else if(x>=0.5){x=x*2-1;y=y*2;}
					else {x=x*2;y=y*2;}
					if((x*x+y*y)>R){canDraw=false;break;}
				}
				if(canDraw)g.drawRect(i,j,1,1);	
				canDraw=true;			
			}
		}
	}
}