import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
public class JavaAndRectangularShape
{
	public static void main(String[] args)
	{
		Frame_RectangularShape Frame_RectangularShape1=new Frame_RectangularShape();
		Frame_RectangularShape1.setVisible(true);
	}
}
class Frame_RectangularShape extends Frame
{
	Rectangle2D rectangle2D;
	RoundRectangle2D roundRectangle2D;
	Ellipse2D ellipse2D;
	Arc2D arc2D;
	public Frame_RectangularShape()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		double dx=30.0,x0=50.0,y0=300.0,length=300.0,width=150.0,arcX=100,arcY=50,angle0=30.0,dAngle=180.0+30.0;
		rectangle2D=new Rectangle2D.Double(x0,y0,length,width);
		x0+=dx+length;
		roundRectangle2D=new RoundRectangle2D.Double(x0,y0,length,width,arcX,arcY);
		x0+=dx+length;
		ellipse2D=new Ellipse2D.Double(x0,y0,length,width);
		x0+=dx+length;
		int closeMode=Arc2D.OPEN;
		closeMode=Arc2D.CHORD;
		closeMode=Arc2D.PIE;
		arc2D=new Arc2D.Double(x0,y0,length,width,angle0,dAngle,closeMode);
	}
	public void paint(Graphics g)
	{
		Graphics2D g2D=(Graphics2D)g;
		g2D.draw(rectangle2D);
		g2D.draw(roundRectangle2D);
		g2D.draw(ellipse2D);
		g2D.draw(arc2D);
	}
}