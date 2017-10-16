import java.awt.*;
import java.applet.*;
import java.awt.event.*;
public class Mandelbrot extends Applet implements MouseListener,MouseMotionListener
{
	public Cursor Cursor1;
	public Cursor Cursor2;
	public Graphics G;
	public Graphics2D G2D;
	public int x1,y1,x2,y2;
	public double pMin,pMax;
	public double qMin,qMax;
	public int color;
	public double p0;
	public double q0;
	double Minp;
	double Maxp;
	double Minq;
	double Maxq;
		
	public void init()
	{
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		Cursor1=new Cursor(Cursor.WAIT_CURSOR);
		Cursor2=new Cursor(Cursor.CROSSHAIR_CURSOR);
		this.setCursor(Cursor2);
		G=this.getGraphics();
		G2D=(Graphics2D)G;	
		x1=0;
		x2=640;
		y1=0;
		y2=480;	
		pMin=-2.2;
		pMax=0.7;
		qMin=-1.2;
		qMax=1.2;
			
	}
	public void start()
	{
		
	}
	public void paint(Graphics g)
	{
		
		drawMandelbrot(G);
		this.setCursor(Cursor2);
	//	drawMandelbrotWithColor(g);
	}
	
	/*    
	 	  z=x+yi
	      zn+1=zn*zn+C
		  C=p+qi
	*/
	public void drawMandelbrot(Graphics g)
	{				
		double p0;
		double q0;						
		for(int y=0;y<480;y++)
		{			
			for(int x=0;x<640;x++)
			{	
				p0=pMin+x*(pMax-pMin)/(640-1);							
				q0=qMin+y*(qMax-qMin)/(480-1);
				if(Mandelbrot(p0,q0)==100)g.drawLine(x,y,x,y);			
			}		
		}
	}
	public void drawMandelbrotWithColor(Graphics g)
	{				
		for(int y=0;y<480;y++)
		{			
			for(int x=0;x<640;x++)
			{	
				p0=pMin+x*(pMax-pMin)/(640-1);							
				q0=qMin+y*(qMax-qMin)/(480-1);
				if(Mandelbrot(p0,q0)==100)g.setColor(Color.WHITE);
				else g.setColor(new Color(color));
				g.drawLine(x,y,x,y);							
			}		
		}						
	}
	public int Mandelbrot(double p0,double q0)
	{
		double x,x0,y,y0;
		x0=0;
		y0=0;		
		int i;
		for(i=1;i<100;i++)
		{
		    x=x0*x0-y0*y0+p0;
			y=2*x0*y0+q0;
			if (x*x+y*y>100){color=200*i;return i;}			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	
	public void mousePressed(MouseEvent e)
	{
		x1=e.getX();
		y1=e.getY();
	}
	public void mouseDragged(MouseEvent e)
	{
		x2=e.getX();
		y2=e.getY();				
		G2D.setColor(Color.BLUE);
		G2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.05f));		
		G2D.fillRect(x1,y1,x2-x1,y2-y1);
		G2D.setColor(Color.BLACK);
		G2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));		
	}
	public void mouseReleased(MouseEvent e)
	{		
		G.drawRect(x1,y1,x2-x1,y2-y1);
		this.setCursor(Cursor1);		
		Minp=pMin+x1*(pMax-pMin)/(640-0);			
		Maxp=pMin+x2*(pMax-pMin)/(640-0);
		Minq=qMin+y1*(qMax-qMin)/(480-0);  
		Maxq=qMin+y2*(qMax-qMin)/(480-0);
		pMin=Minp;
		pMax=Maxp;
		qMin=Minq;
		qMax=Maxq;
		repaint();		
	}

	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}	
	public void mouseMoved(MouseEvent e){}
}
