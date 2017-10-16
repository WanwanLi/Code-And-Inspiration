import java.awt.*;
import java.applet.*;
import java.awt.event.*;
public class Julia extends Applet implements MouseListener,MouseMotionListener
{
	public Cursor Cursor1;
	public Cursor Cursor2;
	public Graphics G;
	public Graphics2D G2D;
	public int x1,y1,x2,y2;
	public double xMin,xMax;
	public double yMin,yMax;
	public double Minx,Maxx;
	public double Miny,Maxy;
	public int color;
	public double p;
	public double q;
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
		p=-0.46;
		q=0.57;
		xMin=-1.5;
		yMin=-1.5;
		xMax=xMin+0.0055*640;
		yMax=yMin+0.0055*480;			
	}
	public void paint(Graphics g)
	{		
		drawJulia(G);
		this.setCursor(Cursor2);	
	}
	public void drawJulia(Graphics g)
	{				
		double x0;
		double y0;	
		y0=yMin;					
		for(int y=0;y<480;y++)
		{	
			x0=xMin;		
			for(int x=0;x<640;x++)
			{	
				if(Julia(x0,y0)==100)g.drawLine(x,y,x,y);
				x0+=(xMax-xMin)/(640-0);
			}
			y0+=(yMax-yMin)/(480-0);		
		}
	}

	public int Julia(double x0,double y0)
	{
		double x,y;
		int i;
		for(i=1;i<100;i++)
		{
		    	x=x0*x0-y0*y0+p;
			y=2*x0*y0+q;
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
		Minx=xMin+x1*(xMax-xMin)/(640-0);			
		Maxx=xMin+x2*(xMax-xMin)/(640-0);
		Miny=yMin+y1*(yMax-yMin)/(480-0);  
		Maxy=yMin+y2*(yMax-yMin)/(480-0);
		xMin=Minx;
		xMax=Maxx;
		yMin=Miny;
		yMax=Maxy;
		repaint();		
	}

	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}	
	public void mouseMoved(MouseEvent e){}
}

