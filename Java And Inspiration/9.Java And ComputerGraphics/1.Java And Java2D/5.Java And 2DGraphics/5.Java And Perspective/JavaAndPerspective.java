import java.awt.*;
import java.awt.event.*;
public class JavaAndPerspective
{
	public static void main(String[] args)
	{
		Prism Prism1=new Prism(4,300,200);
		Prism1.rotate(0,Math.PI/6,0);//(0,Math.PI/6,Math.PI/3);
		Prism1.perspective(0,800);
		Prism1.setVisible(true);
		Prism1.setMode(Prism.HIDE_LINES);
	}
}
class Prism extends Frame
{
	double[] aX,aY,aZ,bX,bY,bZ;
	int m;
	double h,r;
	int dx=800,dy=400,dz=0;
	public static final int HIDE_LINES=1;
	int mode=0;
	public Prism(int m,double h,double r)
	{
		this.m=m;
		this.h=h;
		this.r=r;
		aX=new double[m];
		aY=new double[m];
		aZ=new double[m];
		bX=new double[m];
		bY=new double[m];
		bZ=new double[m];
		this.getCoordinates();
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	private void getCoordinates()
	{
		double w=2*Math.PI/m;
		for(int i=0;i<m;i++)
		{
			aX[i]=bX[i]=r*Math.cos(i*w);
			aY[i]=bY[i]=r*Math.sin(i*w);
			aZ[i]=h/2+dz;
			bZ[i]=-h/2+dz;
		}
	}
	public void rotate(double rotX,double rotY,double rotZ)
	{
		for(int i=0;i<m;i++)
		{
			double y=aY[i];
			double z=aZ[i];
			aY[i]=y*Math.cos(rotX)-z*Math.sin(rotX);
			aZ[i]=y*Math.sin(rotX)+z*Math.cos(rotX);
			y=bY[i];
			z=bZ[i];
			bY[i]=y*Math.cos(rotX)-z*Math.sin(rotX);
			bZ[i]=y*Math.sin(rotX)+z*Math.cos(rotX);
		}
		for(int i=0;i<m;i++)
		{
			double z=aZ[i];
			double x=aX[i];
			aZ[i]=z*Math.cos(rotY)-x*Math.sin(rotY);
			aX[i]=z*Math.sin(rotY)+x*Math.cos(rotY);
			z=bZ[i];
			x=bX[i];
			bZ[i]=z*Math.cos(rotY)-x*Math.sin(rotY);
			bX[i]=z*Math.sin(rotY)+x*Math.cos(rotY);
		}
		for(int i=0;i<m;i++)
		{
			double x=aX[i];
			double y=aY[i];
			aX[i]=x*Math.cos(rotZ)-y*Math.sin(rotZ);
			aY[i]=x*Math.sin(rotZ)+y*Math.cos(rotZ);
			x=bX[i];
			y=bY[i];
			bX[i]=x*Math.cos(rotZ)-y*Math.sin(rotZ);
			bY[i]=x*Math.sin(rotZ)+y*Math.cos(rotZ);
		}		
	}
	public void perspective(double center,double distance)
	{
		for(int i=0;i<m;i++)
		{
			aX[i]*=(distance-aZ[i])/(distance-center);
			bX[i]*=(distance-bZ[i])/(distance-center);
			aY[i]*=(distance-aZ[i])/(distance-center);
			bY[i]*=(distance-bZ[i])/(distance-center);
		}
	}
	private boolean isVisible(double x0,double y0,double x1,double y1,double x2,double y2)
	{
		double dx1=x1-x0;
		double dy1=y1-y0;
		double dx2=x2-x0;
		double dy2=y2-y0;
		return ((dx1*dy2-dx2*dy1)<0?true:false);
	}
	public void setMode(int mode)
	{
		this.mode=mode;
	}
	private void paintPrism(Graphics g)
	{
		if(isVisible(aX[0],aY[0],aX[1],aY[1],aX[2],aY[2]))for(int i=0;i<m;i++)g.drawLine((int)aX[i]+dx,-(int)aY[i]+dy,(int)aX[(i+1)%m]+dx,-(int)aY[(i+1)%m]+dy);
		else for(int i=0;i<m;i++)g.drawLine((int)bX[i]+dx,-(int)bY[i]+dy,(int)bX[(i+1)%m]+dx,-(int)bY[(i+1)%m]+dy);
		for(int i=0;i<m;i++)
		{
			if(isVisible(aX[i],aY[i],bX[i],bY[i],aX[(i+1)%m],aY[(i+1)%m]))
			{
				g.drawLine((int)aX[i]+dx,-(int)aY[i]+dy,(int)aX[(i+1)%m]+dx,-(int)aY[(i+1)%m]+dy);
				g.drawLine((int)aX[i]+dx,-(int)aY[i]+dy,(int)bX[i]+dx,-(int)bY[i]+dy);
				g.drawLine((int)aX[(i+1)%m]+dx,-(int)aY[(i+1)%m]+dy,(int)bX[(i+1)%m]+dx,-(int)bY[(i+1)%m]+dy);
				g.drawLine((int)bX[i]+dx,-(int)bY[i]+dy,(int)bX[(i+1)%m]+dx,-(int)bY[(i+1)%m]+dy);
			}
		}
	}
	public void paint(Graphics g)
	{
		if(mode==1){paintPrism(g);return;}
		for(int i=0;i<m;i++)
		{
			g.drawLine((int)aX[i]+dx,-(int)aY[i]+dy,(int)aX[(i+1)%m]+dx,-(int)aY[(i+1)%m]+dy);
			g.drawLine((int)aX[i]+dx,-(int)aY[i]+dy,(int)bX[i]+dx,-(int)bY[i]+dy);
			g.drawLine((int)bX[i]+dx,-(int)bY[i]+dy,(int)bX[(i+1)%m]+dx,-(int)bY[(i+1)%m]+dy);
		}
	}
}