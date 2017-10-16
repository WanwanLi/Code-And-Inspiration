import java.awt.*;
import java.awt.event.*;
public class JavaAndSierpinski 
{
	public static void main(String[] args)
	{
		int x0=400,y0=200,x1=100,y1=700,x2=700,y2=700,h=7;
		Frame_Sierpinski Frame_Sierpinski1=new Frame_Sierpinski(x0,y0,x1,y1,x2,y2,h);
		Frame_Sierpinski1.setVisible(true);
	}
}
class Frame_Sierpinski extends Frame
{
	private int x0,y0,x1,y1,x2,y2,height;
	public Frame_Sierpinski(int x0,int y0,int x1,int y1,int x2,int y2,int height)
	{
		this.x0=x0;
		this.y0=y0;
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
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
		this.drawSierpinskiShape(g);
	}
	private void drawSierpinskiShape(Graphics g)
	{
		g.drawLine(x0,y0,x1,y1);
		g.drawLine(x1,y1,x2,y2);
		g.drawLine(x2,y2,x0,y0);
		drawTriangle(g,x0,y0,x1,y1,x2,y2,0);
	}
	private void drawTriangle(Graphics g,int x0,int y0,int x1,int y1,int x2,int y2,int h)
	{
		if(h>height)return;
		int x01=(x0+x1)/2;
		int y01=(y0+y1)/2;
		int x12=(x1+x2)/2;
		int y12=(y1+y2)/2;
		int x20=(x2+x0)/2;
		int y20=(y2+y0)/2;
		g.drawLine(x01,y01,x12,y12);
		g.drawLine(x12,y12,x20,y20);
		g.drawLine(x20,y20,x01,y01);
		drawTriangle(g,x0,y0,x01,y01,x20,y20,h+1);
		drawTriangle(g,x1,y1,x12,y12,x01,y01,h+1);
		drawTriangle(g,x2,y2,x20,y20,x12,y12,h+1);
	}
}