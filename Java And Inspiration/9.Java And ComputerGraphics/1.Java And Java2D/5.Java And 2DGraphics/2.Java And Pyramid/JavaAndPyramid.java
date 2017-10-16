import java.awt.*;
import java.awt.event.*;
public class JavaAndPyramid
{
	public static void main(String[] args)
	{
		Pyramid Pyramid1=new Pyramid(300,200,Math.PI/6,Math.PI/3);
		Pyramid1.setVisible(true);
	}
}
class Pyramid extends Frame implements MouseListener,MouseMotionListener
{
	double[] aX,aY,aZ,bX,bY,bZ;
	int m=4;
	double h,r;
	int dx=800,dy=400,dz=0;
	double rotY,rotX;
	public Pyramid(double h,double r,double rotY,double rotX)
	{
		this.m=m;
		this.h=h;
		this.r=r;
		this.rotY=rotY;
		this.rotX=rotX;
		aX=new double[m];
		aY=new double[m];
		aZ=new double[m];
		bX=new double[m];
		bY=new double[m];
		bZ=new double[m];
		this.getCoordinates();
		this.getTransform3D();
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	private void getCoordinates()
	{
		double w=2*Math.PI/m;
		for(int i=0;i<m;i++)
		{
			aX[i]=0;
			bX[i]=r*Math.cos(i*w);
			aY[i]=0;
			bY[i]=r*Math.sin(i*w);
			aZ[i]=h/2+dz;
			bZ[i]=-h/2+dz;
		}
	}
	private void getTransform3D()
	{
		for(int i=0;i<m;i++)
		{
			double x=aX[i];
			double z=aZ[i];
			aX[i]=x*Math.cos(rotY)-z*Math.sin(rotY);
			aZ[i]=x*Math.sin(rotY)+z*Math.cos(rotY);
			x=bX[i];
			z=bZ[i];
			bX[i]=x*Math.cos(rotY)-z*Math.sin(rotY);
			bZ[i]=x*Math.sin(rotY)+z*Math.cos(rotY);
		}
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
	}
	public void paint(Graphics g)
	{
		for(int i=0;i<m;i++)
		{
			g.drawLine((int)aX[i]+dx,(int)aY[i]+dy,(int)aX[(i+1)%m]+dx,(int)aY[(i+1)%m]+dy);
			g.drawLine((int)aX[i]+dx,(int)aY[i]+dy,(int)bX[i]+dx,(int)bY[i]+dy);
			g.drawLine((int)bX[i]+dx,(int)bY[i]+dy,(int)bX[(i+1)%m]+dx,(int)bY[(i+1)%m]+dy);
		}
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	int X=0,Y=0;
	double K=2*Math.PI/200;
	public void mousePressed(MouseEvent e)
	{
		X=e.getX();
		Y=e.getY();
	}
	public void mouseDragged(MouseEvent e)
	{
		double dy=e.getY()-Y;
		double dx=e.getX()-X;
		rotY+=dy*K;
		rotX+=dx*K;
		System.out.println("rotY="+rotY/Math.PI*180+"Degree,rotX="+rotX/Math.PI*180+"Degree");
		this.getTransform3D();
		this.repaint();
		rotY-=dy*K;
		rotX-=dx*K;
	}
	
}