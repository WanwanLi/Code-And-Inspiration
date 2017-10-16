import java.awt.*;
import java.applet.*;
public class SierpinskiCarpet extends Applet
{
	public void paint(Graphics g)
	{
		try
		{
			g.setColor(Color.black);
			drawSierpinskiCarpetShape(g,300,300,500);
		}
		catch(Exception e)	{javax.swing.JOptionPane.showMessageDialog(null,e.getMessage());}
	}
	private final double minLen=1;
	private final double Distance=50;
	private final double PI=3.1415926;
	private void drawSierpinskiCarpetShape(Graphics g,double x,double y,double L)
	{	
		double xA=x-L/2;
		double yA=y+(L/2)*Math.tan(PI/6);					   
		double xB=x+L/2;
		double yB=y+(L/2)*Math.tan(PI/6);
		double xC=x;					 
		double yC=y-L*Math.tan(PI/6);						
		g.drawLine((int)xA,(int)yA,(int)xB,(int)yB);
		g.drawLine((int)xA,(int)yA,(int)xC,(int)yC);
		g.drawLine((int)xB,(int)yB,(int)xC,(int)yC);					
		if(L>10)
		{
			double x1=x-(Math.sqrt(3)/6)*L*Math.sin(PI/3);
			double y1=y+(Math.sqrt(3)/6)*L*Math.cos(PI/3);
			double x2=x+(Math.sqrt(3)/6)*L*Math.sin(PI/3);
			double y2=y+(Math.sqrt(3)/6)*L*Math.cos(PI/3);
			double x3=x;          
			double y3=y-(Math.sqrt(3)/6)*L;
			drawSierpinskiCarpetShape(g,x1,y1,L/2);
			drawSierpinskiCarpetShape(g,x2,y2,L/2);
			drawSierpinskiCarpetShape(g,x3,y3,L/2);	
		} 
	}
}

