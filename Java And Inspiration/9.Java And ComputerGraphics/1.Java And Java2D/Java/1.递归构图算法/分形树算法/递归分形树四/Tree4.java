import java.awt.*;
import java.applet.*;
public class Tree4 extends Applet
{
	public void paint(Graphics g)
	{
		try
		{
			g.setColor(Color.black);			
			drawTree4(g,PI/6,200,500,200,400);
		}
		catch(Exception e){javax.swing.JOptionPane.showMessageDialog(null,e.getMessage());}
	}
	private final double minLen=1;
	private final double Distance=50;
	private final double PI=3.1415926;	
	private void drawTree4(Graphics g,double a,double xA,double yA,double xB,double yB)
	{
		double L=Math.sqrt((xB-xA)*(xB-xA)+(yB-yA)*(yB-yA));
		double Q=Math.atan((yB-yA)/(xB-xA));
		if(xB-xA<0)Q+=PI;
		if(L>5)
		{
			double xC=xB+0.5*L*Math.cos(Q+a);				
			double yC=yB+0.5*L*Math.sin(Q+a);		
			double xD=xB+0.5*L*Math.cos(Q-a);							
			double yD=yB+0.5*L*Math.sin(Q-a);
			double xE=xB+0.5*L*Math.cos(Q);				
			double yE=yB+0.5*L*Math.sin(Q);
			double xF=xE+0.5*L*Math.cos(Q+a);
			double yF=yE+0.5*L*Math.sin(Q+a);
			double xG=xE+0.5*L*Math.cos(Q-a);
			double yG=yE+0.5*L*Math.sin(Q-a);
			double xH=xE+0.5*L*Math.cos(Q);		
			double yH=yE+0.5*L*Math.sin(Q);
			g.drawLine((int)xA,(int)yA,(int)xB,(int)yB);
			g.drawLine((int)xB,(int)yB,(int)xC,(int)yC);		
			g.drawLine((int)xB,(int)yB,(int)xD,(int)yD);
			g.drawLine((int)xB,(int)yB,(int)xE,(int)yE);
			g.drawLine((int)xE,(int)yE,(int)xF,(int)yF);
			g.drawLine((int)xE,(int)yE,(int)xG,(int)yG);
			g.drawLine((int)xE,(int)yE,(int)xH,(int)yH);
			drawTree4(g,a,xB,yB,xC,yC);								
			drawTree4(g,a,xB,yB,xD,yD);	
			drawTree4(g,a,xE,yE,xF,yF);								
			drawTree4(g,a,xE,yE,xG,yG);	
			drawTree4(g,a,xE,yE,xH,yH);	
		}						
	}
}
