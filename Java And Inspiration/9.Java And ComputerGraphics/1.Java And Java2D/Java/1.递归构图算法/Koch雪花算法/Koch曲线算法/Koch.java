import java.awt.*;
import java.applet.*;
public class Koch extends Applet
{
	public void paint(Graphics g)
	{
		try
		{
			g.setColor(Color.black);
			drawKochShape(g,50,50,1000,50);
		}
		catch(Exception e)	{javax.swing.JOptionPane.showMessageDialog(null,e.getMessage());}
	}
	private final double minLen=1;
	private final double Distance=50;
	private final double PI=3.1415926;
	private void drawKochShape(Graphics g,double x1,double y1,double x2,double y2)
	{
		double L=(x2-x1)*(x2-x1)+(y2-y1)*(y2-y1);
		if (L<minLen)g.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
		else
		{			
			double x3=x1+(x2-x1)/3;   
			double y3=y1+(y2-y1)/3;			 
			double x4=x2-(x2-x1)/3;
			double y4=y2-(y2-y1)/3;
			L=Math.sqrt((x4-x3)*(x4-x3)+(y4-y3)*(y4-y3));
			double a=Math.atan((y4-y3)/(x4-x3))+PI/3;if(x4-x3<0)a+=PI;			
			double x5=x3+L*Math.cos(a) ;     
			double y5=y3+L*Math.sin(a);						
			drawKochShape( g, x1, y1, x3, y3);
			drawKochShape( g, x4, y4, x2, y2);
			drawKochShape( g, x3, y3, x5, y5);
			drawKochShape( g, x5, y5, x4, y4);
		}		
	}
}
