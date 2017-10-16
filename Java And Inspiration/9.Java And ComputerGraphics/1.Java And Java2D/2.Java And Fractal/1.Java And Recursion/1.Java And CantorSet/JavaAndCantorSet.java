import java.awt.*;
import java.awt.event.*;
public class JavaAndCantorSet 
{
	public static void main(String[] args)
	{
		int x0=50,y0=100,x1=1200,y1=100,w=10,h=6;
		Frame_CantorSet Frame_CantorSet1=new Frame_CantorSet(x0,y0,x1,y1,w,h);
		Frame_CantorSet1.setVisible(true);
	}
}
class Frame_CantorSet extends Frame
{
	private int x0,y0,x1,y1,width,height;
	public Frame_CantorSet(int x0,int y0,int x1,int y1,int width,int height)
	{
		this.x0=x0;
		this.y0=y0;
		this.x1=x1;
		this.y1=y1;
		this.width=width;
		this.height=height;
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
		drawCantorTernarySet(g,x0,y0,x1,y1,0);
	}
	private int Distance=50;
	private void drawLine(Graphics g,int x1,int y1,int x2,int y2)
	{
		for(int i=0;i<width;i++)
		{
			g.drawLine(x1,y1+i,x2,y2+i);
		}
	}
	private void drawCantorTernarySet(Graphics g,int x1,int y1,int x2,int y2,int h)
	{
		this.drawLine(g,x1,y1,x2,y2);
		if(h<height)
		{							
			int x3=x1+(x2-x1)/3;
			int x4=x2-(x2-x1)/3;
			int y3=y1+Distance;
			int y4=y1+Distance;
			y1+=Distance;y2+=Distance;
			drawCantorTernarySet(g,x1,y1,x3,y3,h+1);
			drawCantorTernarySet(g,x4,y4,x2,y2,h+1);
		}
	}
}
