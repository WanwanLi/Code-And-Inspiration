import java.awt.*;
import java.awt.event.*;
public class JavaAndTree 
{
	public static void main(String[] args)
	{
		double angle=Math.PI/6;
		int x0=200,y0=500,x1=200,y1=300;
		Frame_Tree Frame_Tree1=new Frame_Tree(angle,x0,y0,x1,y1);
		Frame_Tree1.setVisible(true);
	}
}
class Frame_Tree extends Frame
{
	private double angle;
	private int x0,y0,x1,y1;
	public Frame_Tree(double angle,int x0,int y0,int x1,int y1)
	{
		this.x0=x0;
		this.y0=y0;
		this.x1=x1;
		this.y1=y1;
		this.angle=angle;
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
		this.drawTree(g,angle,x0,y0,x1,y1);
	}
	private final double PI=3.1415926;	
	private void drawTree(Graphics g,double a,double xA,double yA,double xB,double yB)
	{
		double L=Math.sqrt((xB-xA)*(xB-xA)+(yB-yA)*(yB-yA));
		double Q=Math.atan((yB-yA)/(xB-xA));
		if(xB-xA<0)Q+=PI;
		if(L>1)
		{
			double xC=xB+0.5*L*Math.cos(Q+a);				
			double yC=yB+0.5*L*Math.sin(Q+a);		
			double xD=xB+0.5*L*Math.cos(Q-a);							
			double yD=yB+0.5*L*Math.sin(Q-a);
			double xE=xB+0.5*L*Math.cos(Q);				
			double yE=yB+0.5*L*Math.sin(Q);
			g.drawLine((int)xA,(int)yA,(int)xB,(int)yB);
			g.drawLine((int)xB,(int)yB,(int)xC,(int)yC);		
			g.drawLine((int)xB,(int)yB,(int)xD,(int)yD);
			g.drawLine((int)xB,(int)yB,(int)xE,(int)yE);
			drawTree(g,a,xB,yB,xC,yC);								
			drawTree(g,a,xB,yB,xD,yD);	
			drawTree(g,a,xB,yB,xE,yE);								
		}						
	}
}
