import java.awt.*;
import java.applet.*;
public class Mandelbrot extends Applet
{
	public void paint(Graphics g)
	{
		int i=Integer.parseInt(getParameter("Parameter"));
		
		switch(i)
		{
			case 3:drawMandelbrot3(g);break;
			case 4:drawMandelbrot4(g);break;
			case 5:drawMandelbrot5(g);break;
			case 6:drawMandelbrot6(g);break;
			default:drawMandelbrot2(g);break;
		}
		
	}
	
	/*    
	 	  z=x+yi
	      zn+1=zn*zn+C
		  C=p0+qi
	*/
	public double pMin,pMax;
	public double qMin,qMax;	
	public void drawMandelbrot2(Graphics g)
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
				if(Mandelbrot2(p0,q0)==100)g.drawLine(x,y,x,y);			
			}		
		}
	}
	public int Mandelbrot2(double p0,double q0)
	{
		double x,x0,y,y0;
		x0=0;
		y0=0;		
		int i;
		for(i=1;i<100;i++)
		{
		    x=x0*x0-y0*y0+p0;
			y=2*x0*y0+q0;
			if (x*x+y*y>100)return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	
	
	public void drawMandelbrot3(Graphics g)
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
				if(Mandelbrot3(p0,q0)==100)g.drawLine(x,y,x,y);			
			}		
		}
	}
	public int Mandelbrot3(double p0,double q0)
	{
		double x,x0,y,y0;
		x0=0;
		y0=0;		
		int i;
		for(i=1;i<100;i++)
		{
		    x=x0*x0*x0-3*x0*y0*y0+p0;
			y=3*x0*x0*y0-y0*y0*y0+q0;
			if (x*x+y*y>100)return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	

	
	public void drawMandelbrot4(Graphics g)
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
				if(Mandelbrot4(p0,q0)==100)g.drawLine(x,y,x,y);			
			}		
		}
	}
	public int Mandelbrot4(double p0,double q0)
	{
		double x,x0,y,y0;
		x0=0;
		y0=0;		
		int i;
		for(i=1;i<100;i++)
		{
		    x=x0*x0*x0*x0-6*x0*x0*y0*y0+y0*y0*y0*y0+p0;
			y=4*x0*x0*x0*y0-4*x0*y0*y0*y0+q0;
			if (x*x+y*y>100)return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	


	public void drawMandelbrot5(Graphics g)
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
				if(Mandelbrot5(p0,q0)==100)g.drawLine(x,y,x,y);			
			}		
		}
	}
	public int Mandelbrot5(double p0,double q0)
	{
		double x,x0,y,y0;
		x0=0;
		y0=0;		
		int i;
		for(i=1;i<100;i++)
		{
		    x=x0*x0*x0*x0*x0-10*x0*x0*x0*y0*y0+5*x0*y0*y0*y0*y0+p0;
			y=y0*y0*y0*y0*y0-10*x0*x0*y0*y0*y0+5*x0*x0*x0*x0*y0+q0;
			if (x*x+y*y>100)return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	


	public void drawMandelbrot6(Graphics g)
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
				if(Mandelbrot6(p0,q0)==100)g.drawLine(x,y,x,y);			
			}		
		}
	}
	public int Mandelbrot6(double p0,double q0)
	{
		double x,x0,y,y0;
		x0=0;
		y0=0;		
		int i;
		for(i=1;i<100;i++)
		{
		    x=6*x0*x0*x0*x0*x0*y0-20*x0*x0*x0*y0*y0*y0+6*x0*y0*y0*y0*y0*y0+p0;
			y=x0*x0*x0*x0*x0*x0-y0*y0*y0*y0*y0*y0-15*x0*x0*x0*x0*y0*y0+15*x0*x0*y0*y0*y0*y0+q0;
			if (x*x+y*y>100)return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
}
