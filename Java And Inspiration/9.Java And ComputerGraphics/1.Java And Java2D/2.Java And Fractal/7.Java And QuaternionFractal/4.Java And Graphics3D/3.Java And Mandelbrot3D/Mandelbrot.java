import java.awt.*;
import java.applet.*;
public class Mandelbrot extends Applet
{
	public void paint(Graphics g)
	{
		drawMandelbrot(g);
	//	drawMandelbrotWithColor(g);
	}
	
	/*    
	 	z=x+yi
	      	zn+1=zn*zn+C
		C=p+qi
	*/
	
	public double pMin,pMax;
	public double qMin,qMax;
	public int color;
	public void drawMandelbrot(Graphics g)
	{				
		double p0;
		double q0;
		pMin=-2.2;
		pMax=0.7;
		qMin=-1.2;
		qMax=1.2;
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
		double p0;
		double q0;
		pMin=-2.2;
		pMax=0.7;
		qMin=-1.2;
		qMax=1.2;
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
}
