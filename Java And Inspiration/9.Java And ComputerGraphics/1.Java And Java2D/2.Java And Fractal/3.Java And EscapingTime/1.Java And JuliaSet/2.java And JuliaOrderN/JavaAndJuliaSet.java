import java.awt.*;
import java.awt.event.*;
public class JavaAndJuliaSet 
{
	public static void main(String[] args)
	{
		double x0=-2.0,y0=-1.2,x1=2.0,y1=1.2;
		int w=1380,h=780,n=Integer.parseInt(args[0]);
		double[]  p={-0.46,  0.5363637, 0.5363637, 0.5363637,-0.2727273};
		double[]  q={ 0.57,-0.1545455,-0.6818182,-0.6272727,-0.8181819};
		Frame_JuliaSet Frame_JuliaSet1=new Frame_JuliaSet(x0,y0,x1,y1,p,q,n,w,h);
		Frame_JuliaSet1.setVisible(true);
	}
}
class Frame_JuliaSet extends Frame
{
	private int order,width,height;
	private double p=-0.65175,q=0.41850;
	private double minX,minY,maxX,maxY;
	public Frame_JuliaSet(double x0,double y0,double x1,double y1,double[] p,double[] q,int order,int width,int height)
	{
		this.minX=x0;
		this.minY=y0;
		this.maxX=x1;
		this.maxY=y1;
		this.p=p[order-2];
		this.q=q[order-2];
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
		drawJulia(g);
	}
	
	/*    
	 	z=x+yi
	      	zn+1=zn*zn+C
		C=p+qi
	*/

	public void drawJulia(Graphics g)
	{				
		double x0,dx=(maxX-minX)/(width-1);
		double y0,dy=(maxY-minY)/(height-1);		
		y0=minY;
		for(int y=0;y<height;y++)
		{
			x0=minX;
			for(int x=0;x<width;x++)
			{
				int color=(int)(2.5*Julia(x0,y0));
				g.setColor(new Color(0,color,0));
				g.drawLine(x,y,x,y);
				x0+=dx;							
			}
			y0+=dy;			
		}
	}
	public int Julia(double x0,double y0)
	{
		switch(order)
		{
			case 2:return Julia2(x0,y0);
			case 3:return Julia3(x0,y0);
			case 4:return Julia4(x0,y0);
			case 5:return Julia5(x0,y0);
			case 6:return Julia6(x0,y0);
			default:return Julia2(x0,y0);
		}
	}
	public int Julia2(double x0,double y0)
	{
		double x;
		double y;
		int i;
		for(i=0;i<100;i++)
		{
			x=x0*x0-y0*y0+p;
			y=2*x0*y0+q;
			if (x*x+y*y>4) return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	public int Julia3(double x0,double y0)
	{
		double x;
		double y;
		int i;
		for(i=0;i<100;i++)
		{
			x=x0*x0*x0-3*x0*y0*y0+p;
			y=3*x0*x0*y0-y0*y0*y0+q;
			if (x*x+y*y>4) return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	public int Julia4(double x0,double y0)
	{
		double x;
		double y;
		int i;
		for(i=0;i<100;i++)
		{
			x=x0*x0*x0*x0-6*x0*x0*y0*y0+y0*y0*y0*y0+p;
			y=4*x0*x0*x0*y0-4*x0*y0*y0*y0+q;
			if (x*x+y*y>4) return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	public int Julia5(double x0,double y0)
	{
		double x;
		double y;
		int i;
		for(i=0;i<100;i++)
		{
			x=x0*x0*x0*x0*x0-10*x0*x0*x0*y0*y0+5*x0*y0*y0*y0*y0+p;
			y=y0*y0*y0*y0*y0-10*x0*x0*y0*y0*y0+5*x0*x0*x0*x0*y0+q;
			if (x*x+y*y>4) return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
	public int Julia6(double x0,double y0)
	{
		double x;
		double y;
		int i;
		for(i=0;i<100;i++)
		{
			x=6*x0*x0*x0*x0*x0*y0-20*x0*x0*x0*y0*y0*y0+6*x0*y0*y0*y0*y0*y0+p;
			y=x0*x0*x0*x0*x0*x0-y0*y0*y0*y0*y0*y0-15*x0*x0*x0*x0*y0*y0+15*x0*x0*y0*y0*y0*y0+q;
			if (x*x+y*y>4) return i;			         
			x0=x;
			y0=y;
		}
		return i;		
	}
}
