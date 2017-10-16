import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
public class JavaAndPolygon
{
	public static void main(String[] args)
	{
		Frame_Polygon Frame_Polygon1=new Frame_Polygon();
		Frame_Polygon1.setVisible(true);
	}
}
class Frame_Polygon extends Frame
{
	Polygon Polygon1;
	public Frame_Polygon()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		int[] coordinatesX=new int[]{200,400,600,500,300};
		int[] coordinatesY=new int[]{300,100,300,500,500};
		int length=5;
		Polygon1=new Polygon(coordinatesX,coordinatesY,length);
	}
	public void paint(Graphics g)
	{
		Graphics2D g2D=(Graphics2D)g;
		g2D.draw(Polygon1);
	}
}