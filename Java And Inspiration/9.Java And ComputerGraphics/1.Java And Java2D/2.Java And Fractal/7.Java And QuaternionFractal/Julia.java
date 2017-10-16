


import java.awt.*;
import java.applet.*;
public class Julia extends Applet
{
	public void paint(Graphics g)
	{
		drawJulia(g);
	//	drawJuliaWithColor(g);
	}
	
	/*    
	 	z=x+yi
	      	zn+1=zn*zn+C
		C=p+qi
	*/
	
	public double p=-0.65175;
	public double q=0.41850;
	public int color;
	public void drawJulia(Graphics g)
	{				
		double x0;
		double y0;
		double d;
		
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
		d=0.0055;		
		y0=-1.5;
		for(int y=0;y<480;y++)
		{
			x0=-1.5;
			for(int x=0;x<640;x++)
			{								
				if(Julia(x0,y0)==100)g.drawLine(x,y,x,y);
				x0+=d;			
			}
			y0+=d;
		}
	}
	
	public void drawJuliaWithColor(Graphics g)
	{				
		double x0;
		double y0;
		double d;
		p=-0.46;
		q=0.57;				
		d=0.0055;		
		y0=-1.5;
		for(int y=0;y<480;y++)
		{
			x0=-1.5;
			for(int x=0;x<640;x++)
			{								
				if(Julia(x0,y0)==100)g.setColor(Color.WHITE);
				else g.setColor(new Color(color));
				g.drawLine(x,y,x,y);
				x0+=d;							
			}
			y0+=d;			
		}
	}
	
	public int Julia(double x0,double y0)
	{
		double x;
		double y;
		int i;
		for(i=0;i<100;i++)
		{
		    x=x0*x0-y0*y0+p;
			y=2*x0*y0+q;
			if (x*x+y*y>4){color=i*200; return i;}			         
			x0=x;
			y0=y;
		}
		return i;		
	}
}
