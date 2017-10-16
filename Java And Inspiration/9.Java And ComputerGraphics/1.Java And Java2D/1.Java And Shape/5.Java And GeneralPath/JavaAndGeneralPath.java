import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
public class JavaAndGeneralPath
{
	public static void main(String[] args)
	{
		Frame_GeneralPath Frame_GeneralPath1=new Frame_GeneralPath();
		Frame_GeneralPath1.setVisible(true);
	}
}
class Frame_GeneralPath extends Frame
{
	GeneralPath GeneralPath1,GeneralPath2;
	public Frame_GeneralPath()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		double dX=500;
		double[] coordinatesX=new double[]{200,400,600,550,250};
		double[] coordinatesY=new double[]{300,100,300,550,550};
		GeneralPath1=new GeneralPath();
		GeneralPath1.moveTo(coordinatesX[0],coordinatesY[0]);
		GeneralPath1.lineTo(coordinatesX[2],coordinatesY[2]);
		GeneralPath1.lineTo(coordinatesX[4],coordinatesY[4]);
		GeneralPath1.lineTo(coordinatesX[1],coordinatesY[1]);
		GeneralPath1.lineTo(coordinatesX[3],coordinatesY[3]);
		GeneralPath1.lineTo(coordinatesX[0],coordinatesY[0]);
		GeneralPath1.setWindingRule(GeneralPath.WIND_NON_ZERO);
		GeneralPath1.closePath();
		GeneralPath2=new GeneralPath();
		GeneralPath2.moveTo(coordinatesX[0]+dX,coordinatesY[0]);
		GeneralPath2.lineTo(coordinatesX[2]+dX,coordinatesY[2]);
		GeneralPath2.lineTo(coordinatesX[4]+dX,coordinatesY[4]);
		GeneralPath2.lineTo(coordinatesX[1]+dX,coordinatesY[1]);
		GeneralPath2.lineTo(coordinatesX[3]+dX,coordinatesY[3]);
		GeneralPath2.lineTo(coordinatesX[0]+dX,coordinatesY[0]);
		GeneralPath2.setWindingRule(GeneralPath.WIND_EVEN_ODD);
		GeneralPath2.closePath();
	}
	public void paint(Graphics g)
	{
		Graphics2D g2D=(Graphics2D)g;
		g2D.draw(GeneralPath1);
		g2D.fill(GeneralPath1);
		g2D.fill(GeneralPath2);
	}
}