import java.awt.*;
import java.awt.event.*;
public class JavaAndJuliaSet 
{
	public static void main(String[] args)
	{
		double p,q;
		p=0.235;
		q=-0.515;
		
		p=-0.77;
		q=0.08;
		
		p=-0.41;
		q=-0.635;
		
		p=0.21;
		q=-0.555;
		
		p=0.4399;
		q=0.175;
		
		p=-0.925;
		q=0.255;
		
		p=0.41;
		q=-0.19;
		
		p=-0.135;
		q=-0.65;
		
		p=-0.595;
		q=-0.435;
		
		p=-0.11;
		q=0.6557;
		
		p=-0.615;
		q=-0.43;
		
		p=-0.199;
		q=-0.66;
		
		p=-0.74543;
		q=0.11301;
		
		p=-0.65175;
		q=0.41850;
		
		p=-0.46;
		q=0.57;	

		int w=1380,h=780;
		double x0=-2.0,y0=-1.2,x1=2.0,y1=1.2;
		Frame_JuliaSet Frame_JuliaSet1=new Frame_JuliaSet(x0,y0,x1,y1,p,q,w,h);
		Frame_JuliaSet1.setVisible(true);
	}
}
class Frame_JuliaSet extends Frame
{
	private int width,height;
	private double p=-0.65175,q=0.41850;
	private double minX,minY,maxX,maxY;
	public Frame_JuliaSet(double x0,double y0,double x1,double y1,double p,double q,int width,int height)
	{
		this.p=p;
		this.q=q;
		this.minX=x0;
		this.minY=y0;
		this.maxX=x1;
		this.maxY=y1;
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
				int color=Julia(x0,y0)*2;							
				if(color>255)g.setColor(Color.WHITE);
				else g.setColor(new Color(0,color,0));
				g.drawLine(x,y,x,y);
				x0+=dx;							
			}
			y0+=dy;			
		}
	}
	public int Julia(double x0,double y0)
	{
		double x;
		double y;
		int i;
		for(i=0;i<125;i++)
		{
		    	x=x0*x0-y0*y0+p;
			y=2*x0*y0+q;
			if (x*x+y*y>4)return i;
			x0=x;
			y0=y;
		}
		return i;		
	}
}
