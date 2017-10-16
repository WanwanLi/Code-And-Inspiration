import java.awt.*;
import java.applet.*;
public class Tree1 extends Applet
{
	public void paint(Graphics g)
	{
		try
		{
			g.setColor(Color.black);			
			drawTree1(g,PI/6,200,500,200,300);
		}
		catch(Exception e){javax.swing.JOptionPane.showMessageDialog(null,e.getMessage());}
	}
	private final double minLen=1;
	private final double Distance=50;
	private final double PI=3.1415926;	
	private void drawTree1(Graphics g,double a,double xA,double yA,double xB,double yB)
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
			g.drawLine((int)xA,(int)yA,(int)xB,(int)yB);
			g.drawLine((int)xB,(int)yB,(int)xC,(int)yC);		
			g.drawLine((int)xB,(int)yB,(int)xD,(int)yD);
			drawTree1(g,a,xB,yB,xC,yC);									
			drawTree1(g,a,xB,yB,xD,yD);					
		}						
	}
}
