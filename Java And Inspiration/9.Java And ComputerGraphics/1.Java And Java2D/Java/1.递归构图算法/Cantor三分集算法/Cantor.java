import java.awt.*;
import java.applet.*;
public class Cantor extends Applet
{
	public void paint(Graphics g)
	{
		try
		{
			g.setColor(Color.black);
			drawCantorShape(g,50,50,1000,50);
		}
		catch(Exception e)	{javax.swing.JOptionPane.showMessageDialog(null,e.getMessage());}
	}
	private final double minLen=1;
	private final double Distance=50;
	private void drawCantorShape(Graphics g,double x1,double y1,double x2,double y2)
	{
		if(x2-x1>minLen)
		{							
			g.drawLine((int)x1,(int)y1,(int)(x2),(int)(y2));
			double x3=x1+(x2-x1)/3;
			double x4=x2-(x2-x1)/3;
			double y3=y1+Distance,y4=y1+Distance;
			y1+=Distance;y2+=Distance;			
			drawCantorShape(g,x1,y1,x3,y3);
			drawCantorShape(g,x4,y4,x2,y2);
		}
		else g.drawLine((int)x1,(int)y1,(int)(x2),(int)(y2));				
	}
}
