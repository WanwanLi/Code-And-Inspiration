import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
public class JavaAndArea
{
	public static void main(String[] args)
	{
		Frame_Area Frame_Area1=new Frame_Area();
		Frame_Area1.setVisible(true);
	}
}
class Frame_Area extends Frame
{
	Shape Shape1,Shape2,Shape3,Shape4;
	Area Area1,Area2,Area3,Area4,Area5,Area6,Area7;
	public Frame_Area()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		double dx=30.0,x0=50.0,y0=300.0,length=200.0,width=150.0;
		Shape1=new Ellipse2D.Double(x0,y0,length,width);
		Area1=new Area(Shape1);
		x0+=dx+length;
		Shape1=new Ellipse2D.Double(x0,y0,length,width);
		Shape2=new Ellipse2D.Double(x0+length/2,y0,length,width);
		Area3=new Area(Shape1);
		Area2=new Area(Shape2);
		Area3.add(Area2);
		x0+=5*dx+length;
		Shape1=new Ellipse2D.Double(x0,y0,length,width);
		Shape2=new Ellipse2D.Double(x0+length/2,y0,length,width);
		Area4=new Area(Shape1);
		Area2=new Area(Shape2);
		Area4.subtract(Area2);
		x0+=4*dx;
		Shape1=new Ellipse2D.Double(x0,y0,length,width);
		Shape2=new Ellipse2D.Double(x0+length/2,y0,length,width);
		Area5=new Area(Shape1);
		Area2=new Area(Shape2);
		Area5.intersect(Area2);
		x0+=2*dx+length;
		Shape1=new Ellipse2D.Double(x0,y0,length,width);
		Shape2=new Ellipse2D.Double(x0+length/2,y0,length,width);
		Area6=new Area(Shape1);
		Area2=new Area(Shape2);
		Area6.exclusiveOr(Area2);
	}
	public void paint(Graphics g)
	{
		Graphics2D g2D=(Graphics2D)g;
		g2D.fill(Area1);
		g2D.fill(Area3);
		g2D.fill(Area4);
		g2D.fill(Area5);
		g2D.fill(Area6);
	}
}