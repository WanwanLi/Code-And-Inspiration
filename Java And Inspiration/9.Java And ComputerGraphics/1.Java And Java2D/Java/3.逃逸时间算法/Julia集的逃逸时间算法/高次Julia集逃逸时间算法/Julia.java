import java.awt.*;
import java.applet.*;
public class Julia extends Applet
{
	public void paint(Graphics g)
	{
		int i=Integer.parseInt(getParameter("Parameter"));
		
		switch(i)
		{
			case 3:drawJulia3(g);break;
			case 4:drawJulia4(g);break;
			case 5:drawJulia5(g);break;
			case 6:drawJulia6(g);break;
			default:drawJulia2(g);break;
		}
		
	}
	
	/*    
	 	  z=x+yi
	      zn+1=zn*zn+C
		  C=p+qi
	*/
	
	public double p=-0.65175;
	public double q=0.41850;
	public void drawJulia2(Graphics g)
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
				if(Julia2(x0,y0)==100)g.drawLine(x,y,x,y);
				x0+=d;			
			}
			y0+=d;
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
	
	
	
	public void drawJulia3(Graphics g)
	{				
		double x0;
		double y0;
		double d;			
		p=0.5363637;
		q=-0.1545455;				
		d=0.0055;		
		y0=-1.5;
		for(int y=0;y<480;y++)
		{
			x0=-1.5;
			for(int x=0;x<640;x++)
			{								
				if(Julia3(x0,y0)==100)g.drawLine(x,y,x,y);
				x0+=d;			
			}
			y0+=d;
		}
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
	
	public void drawJulia4(Graphics g)
	{				
		double x0;
		double y0;
		double d;			
		p=0.5363637;
		q=-0.6818182;
		d=0.0055;		
		y0=-1.5;
		for(int y=0;y<480;y++)
		{
			x0=-1.5;
			for(int x=0;x<640;x++)
			{								
				if(Julia4(x0,y0)==100)g.drawLine(x,y,x,y);
				x0+=d;			
			}
			y0+=d;
		}
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
	
	public void drawJulia5(Graphics g)
	{				
		double x0;
		double y0;
		double d;			
		p=0.5363637;
		q=-0.6272727;
		d=0.0055;		
		y0=-1.5;
		for(int y=0;y<480;y++)
		{
			x0=-1.5;
			for(int x=0;x<640;x++)
			{								
				if(Julia5(x0,y0)==100)g.drawLine(x,y,x,y);
				x0+=d;			
			}
			y0+=d;
		}
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
	
	public void drawJulia6(Graphics g)
	{				
		double x0;
		double y0;
		double d;			
		p=-0.2727273;
		q=-0.8181819;
		d=0.0055;		
		y0=-1.5;
		for(int y=0;y<480;y++)
		{
			x0=-1.5;
			for(int x=0;x<640;x++)
			{								
				if(Julia6(x0,y0)==100)g.drawLine(x,y,x,y);
				x0+=d;			
			}
			y0+=d;
		}
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


