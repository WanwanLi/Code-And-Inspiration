import java.awt.*;
import java.awt.event.*;
public class JavaAndKochSnow 
{
	public static void main(String[] args)
	{
		int x=100,y=100,r=200;
		Frame_KochSnow Frame_KochSnow1=new Frame_KochSnow(x,y,r);
		Frame_KochSnow1.setVisible(true);
	}
}
class Frame_KochSnow extends Frame
{
	private int x,y,r;
	public Frame_KochSnow(int x,int y,int r)
	{
		this.x=x; this.y=y; this.r=r;
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
		g.setColor(Color.black);			
		drawKochSnow(g,x,y,r);
	}
	private final double minLen=1;
	private final double Distance=50;
	private final double PI=3.1415926;
	private void drawKochSnow(Graphics g,double x,double y,double radius)
	{
		drawKochCurve(g,x+2*radius,y+2*radius,x+2*radius*Math.cos(PI/3),y);
		drawKochCurve(g,x+2*radius*Math.cos(PI/3),y,x,y+2*radius);
		drawKochCurve(g,x,y+2*radius,x+2*radius,y+2*radius);
	}
	private void drawKochCurve(Graphics g,double x1,double y1,double x2,double y2)
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
			double a=Math.atan((y4-y3)/(x4-x3))+PI/3;
			if(x4-x3<0)a+=PI;
			double x5=x3+L*Math.cos(a) ;     
			double y5=y3+L*Math.sin(a);						
			drawKochCurve( g, x1, y1, x3, y3);
			drawKochCurve( g, x4, y4, x2, y2);
			drawKochCurve( g, x3, y3, x5, y5);
			drawKochCurve( g, x5, y5, x4, y4);
		}		
	}
}
