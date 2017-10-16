 import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
public class JavaAndCurve
{
	public static void main(String[] args)
	{
		Frame_Curve Frame_Curve1=new Frame_Curve();
		Frame_Curve1.setVisible(true);
	}
}
class Frame_Curve extends Frame
{
	Area p1,p2,p3,p4,p5,p6,p7;
	QuadCurve2D quadCurve2D;
	public Frame_Curve()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		double x1=100.0,y1=400.0,x2=200.0,y2=100.0,x3=300.0,y3=350.0;
		quadCurve2D=new QuadCurve2D.Double(x1,y1,x2,y2,x3,y3);
		p1=new Area(new Ellipse2D.Double(x1-5.0,y1-5.0,10.0,10.0));
		p2=new Area(new Ellipse2D.Double(x2-5.0,y2-5.0,10.0,10.0));
		p3=new Area(new Ellipse2D.Double(x3-5.0,y3-5.0,10.0,10.0));
		
	}
	public void paint(Graphics g)
	{
		Graphics2D g2D=(Graphics2D)g;
		g2D.draw(quadCurve2D);
		g2D.fill(p1);
		g2D.fill(p2);
		g2D.fill(p3);
	}
}