import java.awt.*;
import java.awt.event.*;
public class JavaAndMandelbrotSet 
{
	public static void main(String[] args)
	{
		double p0=-3.2,q0=-2.0,p1=1.6,q1=1.6;
		int n=Integer.parseInt(args[0]),w=1380,h=780;
		Frame_MandelbrotSet Frame_MandelbrotSet1=new Frame_MandelbrotSet(p0,q0,p1,q1,n,w,h);
		Frame_MandelbrotSet1.setVisible(true);
	}
}
class Frame_MandelbrotSet extends Frame
{
	private int order,width,height;
	private double minP,minQ,maxP,maxQ;
	public Frame_MandelbrotSet(double p0,double q0,double p1,double q1,int order,int width,int height)
	{
		this.minP=p0;
		this.minQ=q0;
		this.maxP=p1;
		this.maxQ=q1;
		this.order=order;
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
		drawMandelbrot(g);
	}
	
	/*    
	 	z=x+yi
	      	zn+1=zn*zn+C
		C=p+qi
	*/
	
	public void drawMandelbrot(Graphics g)
	{
		double p0,dp=(maxP-minP)/(width-1);
		double q0,dq=(maxQ-minQ)/(height-1);
		for(int y=0;y<height;y++)
		{
			q0=minQ+y*dq;
			for(int x=0;x<width;x++)
			{	
				p0=minP+x*dp;
				int c=min(Mandelbrot(p0,q0)*2,255);
				g.setColor(new Color(c,c,0));
				g.drawLine(x,y,x,y);
			}		
		}
	}
	public int Mandelbrot(double p0,double q0)
	{
		switch(order)
		{
			case 2:return Mandelbrot2(p0,q0);
			case 3:return Mandelbrot3(p0,q0);
			case 4:return Mandelbrot4(p0,q0);
			case 5:return Mandelbrot5(p0,q0);
			case 6:return Mandelbrot6(p0,q0);
			default:return Mandelbrot2(p0,q0);
		}
	}
	public int Mandelbrot2(double p0,double q0)
	{
		double x,x0,y,y0;
		x0=0;
		y0=0;		
		int i;
		for(i=1;i<125;i++)
		{
			x=x0*x0-y0*y0+p0;
			y=2*x0*y0+q0;
			if (x*x+y*y>100)return i;
			x0=x;
			y0=y;
		}
		return i;		
	}
	public int Mandelbrot3(double p0,double q0)
	{
		double x,x0,y,y0;
		x0=0;
		y0=0;		
		int i;
		for(i=1;i<125;i++)
		{
			x=x0*x0*x0-3*x0*y0*y0+p0;
			y=3*x0*x0*y0-y0*y0*y0+q0;
			if (x*x+y*y>100)return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	public int Mandelbrot4(double p0,double q0)
	{
		double x,x0,y,y0;
		x0=0;
		y0=0;		
		int i;
		for(i=1;i<125;i++)
		{
			x=x0*x0*x0*x0-6*x0*x0*y0*y0+y0*y0*y0*y0+p0;
			y=4*x0*x0*x0*y0-4*x0*y0*y0*y0+q0;
			if (x*x+y*y>100)return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	public int Mandelbrot5(double p0,double q0)
	{
		double x,x0,y,y0;
		x0=0;
		y0=0;		
		int i;
		for(i=1;i<125;i++)
		{
			x=x0*x0*x0*x0*x0-10*x0*x0*x0*y0*y0+5*x0*y0*y0*y0*y0+p0;
			y=y0*y0*y0*y0*y0-10*x0*x0*y0*y0*y0+5*x0*x0*x0*x0*y0+q0;
			if (x*x+y*y>100)return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	public int Mandelbrot6(double p0,double q0)
	{
		double x,x0,y,y0;
		x0=0;
		y0=0;		
		int i;
		for(i=1;i<125;i++)
		{
			x=6*x0*x0*x0*x0*x0*y0-20*x0*x0*x0*y0*y0*y0+6*x0*y0*y0*y0*y0*y0+p0;
			y=x0*x0*x0*x0*x0*x0-y0*y0*y0*y0*y0*y0-15*x0*x0*x0*x0*y0*y0+15*x0*x0*y0*y0*y0*y0+q0;
			if (x*x+y*y>100)return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	private int min(int a,int b){return a<b?a:b;}
}
