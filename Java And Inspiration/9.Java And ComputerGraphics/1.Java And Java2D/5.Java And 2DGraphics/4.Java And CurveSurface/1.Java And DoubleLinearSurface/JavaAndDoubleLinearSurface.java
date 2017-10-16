import java.awt.*;
import java.awt.event.*;
public class JavaAndDoubleLinearSurface
{
	public static void main(String[] args)
	{
		double[] p00={1000,0,500},p01={500,500,-500},p10={-500,500,-500},p11={-500,0,500};
		DoubleLinearSurface DoubleLinearSurface1=new DoubleLinearSurface(p00,p01,p10,p11,15,18,Math.PI/6,Math.PI/3);
		DoubleLinearSurface1.setVisible(true);
	}
}
class DoubleLinearSurface extends Frame implements MouseListener,MouseMotionListener
{
	double[] coordinate_X,coordinate_Y,coordinate_Z;
	int m;
	int n;
	int dx=600,dy=400,dz=0;
	double r,rotY,rotX;
	final int X=0,Y=1,Z=2;
	double[] p00,p01,p11,p10;
	public DoubleLinearSurface(double[] p00,double[] p01,double[] p10,double[] p11,int m,int n,double rotY,double rotX)
	{
		this.m=m;
		this.n=n;
		this.r=r;
		this.rotY=rotY;
		this.rotX=rotX;
		this.p00=p00;
		this.p01=p01;
		this.p10=p10;
		this.p11=p11;
		coordinate_X=new double[m*n];
		coordinate_Y=new double[m*n];
		coordinate_Z=new double[m*n];
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
	private double[] getDoubleLinearSurfaceCoordinates(double u,double v)
	{
		double[] doubleLinearSurfaceCoordinates=new double[3];
		doubleLinearSurfaceCoordinates[X]=p00[X]*(1-u)*(1-v)+p01[X]*(1-u)*v+p10[X]*u*(1-v)+p11[X]*u*v;
		doubleLinearSurfaceCoordinates[Y]=p00[Y]*(1-u)*(1-v)+p01[Y]*(1-u)*v+p10[Y]*u*(1-v)+p11[Y]*u*v;
		doubleLinearSurfaceCoordinates[Z]=p00[Z]*(1-u)*(1-v)+p01[Z]*(1-u)*v+p10[Z]*u*(1-v)+p11[Z]*u*v;
		return doubleLinearSurfaceCoordinates;
	}
	private void getCoordinates()
	{

		for(int i=0;i<m;i++)
		{
			double u=i*(1.0/m);
			for(int j=0;j<n;j++)
			{
				double v=j*(1.0/n);
				double[] doubleLinearSurfaceCoordinates=getDoubleLinearSurfaceCoordinates(u,v);
				coordinate_X[i*n+j]=doubleLinearSurfaceCoordinates[X];
				coordinate_Y[i*n+j]=doubleLinearSurfaceCoordinates[Y];
				coordinate_Z[i*n+j]=doubleLinearSurfaceCoordinates[Z];
			}
		}
	}
	private void getTransform3D()
	{
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				double x=coordinate_X[i*n+j];
				double z=coordinate_Z[i*n+j];
				coordinate_X[i*n+j]=z*Math.cos(rotY)-x*Math.sin(rotY);
				coordinate_Z[i*n+j]=z*Math.sin(rotY)+x*Math.cos(rotY);
			}
		}
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				double y=coordinate_Y[i*n+j];
				double z=coordinate_Z[i*n+j];
				coordinate_Y[i*n+j]=y*Math.cos(rotX)-z*Math.sin(rotX);
				coordinate_Z[i*n+j]=y*Math.sin(rotX)+z*Math.cos(rotX);
			}
		}
	}
	public void paint(Graphics g)
	{
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)g.drawLine((int)coordinate_X[i*n+j]+dx,(int)coordinate_Y[i*n+j]+dy,(int)coordinate_X[i*n+((j+1)%n)]+dx,(int)coordinate_Y[i*n+((j+1)%n)]+dy);
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)g.drawLine((int)coordinate_X[i*n+j]+dx,(int)coordinate_Y[i*n+j]+dy,(int)coordinate_X[((i+1)%m)*n+j]+dx,(int)coordinate_Y[((i+1)%m)*n+j]+dy);
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	int ecoordinate_X=0,ecoordinate_Y=0;
	double K=2*Math.PI/200;
	public void mousePressed(MouseEvent e)
	{
		ecoordinate_X=e.getX();
		ecoordinate_Y=e.getY();
	}
	public void mouseDragged(MouseEvent e)
	{
		double dy=e.getY()-ecoordinate_Y;
		double dx=e.getX()-ecoordinate_X;
		rotY+=dy*K;
		rotX+=dx*K;
		this.getTransform3D();
		this.repaint();
		rotY-=dy*K;
		rotX-=dx*K;
	}
	
}