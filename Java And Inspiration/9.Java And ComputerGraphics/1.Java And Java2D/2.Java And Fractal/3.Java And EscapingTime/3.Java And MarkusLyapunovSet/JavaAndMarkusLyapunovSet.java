import java.awt.*;
import java.awt.event.*;
public class JavaAndMarkusLyapunovSet
{
	public static void main(String[] args)
	{
		String str="AB";
		int w=480,h=380,t=40;
		double p0=0,q0=0,p1=Math.PI,q1=Math.PI;
		Frame_MarkusLyapunovSet Frame_MarkusLyapunovSet1=new Frame_MarkusLyapunovSet(str,p0,q0,p1,q1,t,w,h);
		Frame_MarkusLyapunovSet1.setVisible(true);
	}
}
class Frame_MarkusLyapunovSet extends Frame
{
	private String string;
	private double[][] pixels;
	private int times,width,height;
	private double minP,minQ,maxP,maxQ;
	private double minZ=Double.MAX_VALUE;
	private double maxZ=Double.MIN_VALUE;
	public Frame_MarkusLyapunovSet(String s,double p0,double q0,double p1,double q1,int times,int width,int height)
	{
		this.string=s;
		this.minP=p0;
		this.minQ=q0;
		this.maxP=p1;
		this.maxQ=q1;
		this.times=times;
		this.width=width;
		this.height=height;
		this.initMarkusLyapunov();
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	public void initMarkusLyapunov()
	{
		this.pixels=new double[height][width];
		double dp=(maxP-minP)/(width-1);
		double dq=(maxQ-minQ)/(height-1);
		for(int y=0;y<height;y++)
		{
			double q=minQ+y*dq;
			for(int x=0;x<width;x++)
			{	
				double p=minP+x*dp;
				double z=MarkusLyapunov(p,q);
				minZ=Math.min(z,minZ);
				maxZ=Math.max(z,maxZ);
				pixels[y][x]=z;
			}		
		}
	}
	public void paint(Graphics g)
	{
		drawMarkusLyapunov(g);
	}
	
	/*    
	 	z=x+yi
	      	zn+1=zn*zn+C
		C=p+qi
	*/
	
	public void drawMarkusLyapunov(Graphics g)
	{
		double dz=maxZ-minZ;
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				double z=pixels[y][x];
				int color=(int)(255*(z-minZ)/dz);
				g.setColor(new Color(color,0,0));
				g.drawLine(x,y,x,y);
			}		
		}
	}
	public double MarkusLyapunov(double x,double y)
	{
		double r,z=0;
		double b=2.5,q,p=0;
		int length=string.length();
		double z1=(Math.sqrt(5.0)-1.0)/2.0;
		double z0=-(Math.sqrt(5.0)+1.0)/2.0;
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<length;j++)
			{
				r=(string.charAt(j)=='A')?x:y;
				q=2*b*Math.sin(p+r)*Math.cos(p+r);
			}
		}
		for(int i=0;i<times;i++)
		{
			for(int j=0;j<length;j++)
			{
				r=(string.charAt(j)=='A')?x:y;
				q=2*b*Math.sin(p+r)*Math.cos(p+r);
				p=b*Math.sin(p+r)*Math.sin(p+r);
				z+=Math.log(Math.abs(q));
			}
		}
		return z/(z>0?z1:z0*times*length);
	}
}
