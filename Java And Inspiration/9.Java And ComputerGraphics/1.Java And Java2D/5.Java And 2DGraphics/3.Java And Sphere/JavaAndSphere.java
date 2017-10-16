import java.awt.*;
import java.awt.event.*;
public class JavaAndSphere
{
	public static void main(String[] args)
	{
		Sphere Sphere1=new Sphere(300,15,18,Math.PI/6,Math.PI/3);
		Sphere1.setVisible(true);
		Sphere1.setMode(Sphere.HIDE_LINES);
	}
}
class Sphere extends Frame implements MouseListener,MouseMotionListener
{
	double[] X,Y,Z;
	int m;
	int n;
	int dx=600,dy=400,dz=0;
	double r,rotY,rotX;
	public static final int HIDE_LINES=1;
	int mode=0;
	public Sphere(double r,int m,int n,double rotY,double rotX)
	{
		this.m=m;
		this.n=n;
		this.r=r;
		this.rotY=rotY;
		this.rotX=rotX;
		X=new double[m*n];
		Y=new double[m*n];
		Z=new double[m*n];
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
		double u=Math.PI/(m-1);
		double v=2*Math.PI/n;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				X[i*n+j]=r*Math.sin(i*u)*Math.cos(j*v);
				Y[i*n+j]=r*Math.cos(i*u);
				Z[i*n+j]=r*Math.sin(i*u)*Math.sin(j*v);
			}
		}
	}
	private void getTransform3D()
	{
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				double x=X[i*n+j];
				double z=Z[i*n+j];
				X[i*n+j]=z*Math.cos(rotY)-x*Math.sin(rotY);
				Z[i*n+j]=z*Math.sin(rotY)+x*Math.cos(rotY);
			}
		}
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				double y=Y[i*n+j];
				double z=Z[i*n+j];
				Y[i*n+j]=y*Math.cos(rotX)-z*Math.sin(rotX);
				Z[i*n+j]=y*Math.sin(rotX)+z*Math.cos(rotX);
			}
		}
	}
	public void setMode(int mode)
	{
		this.mode=mode;
	}
	private void paintSphere(Graphics g)
	{
		double u=Math.PI/(m-1);
		double v=2*Math.PI/n;
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)if(Z[i*n+j]>=r*Math.sin(i*u)*Math.sin(-v)&&Z[i*n+((j+1)%n)]>=r*Math.sin(i*u)*Math.sin(-v))g.drawLine((int)X[i*n+j]+dx,(int)Y[i*n+j]+dy,(int)X[i*n+((j+1)%n)]+dx,(int)Y[i*n+((j+1)%n)]+dy);
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)if(Z[i*n+j]>=r*Math.sin(i*u)*Math.sin(-v)&&Z[((i+1)%m)*n+j]>=r*Math.sin(i*u)*Math.sin(-v))g.drawLine((int)X[i*n+j]+dx,(int)Y[i*n+j]+dy,(int)X[((i+1)%m)*n+j]+dx,(int)Y[((i+1)%m)*n+j]+dy);
	}
	public void paint(Graphics g)
	{
		if(mode==1){paintSphere(g);return;}
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)g.drawLine((int)X[i*n+j]+dx,(int)Y[i*n+j]+dy,(int)X[i*n+((j+1)%n)]+dx,(int)Y[i*n+((j+1)%n)]+dy);
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)g.drawLine((int)X[i*n+j]+dx,(int)Y[i*n+j]+dy,(int)X[((i+1)%m)*n+j]+dx,(int)Y[((i+1)%m)*n+j]+dy);
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	int eX=0,eY=0;
	double K=2*Math.PI/200;
	public void mousePressed(MouseEvent e)
	{
		eX=e.getX();
		eY=e.getY();
	}
	public void mouseDragged(MouseEvent e)
	{
		double dy=e.getY()-eY;
		double dx=e.getX()-eX;
		rotY+=dy*K;
		rotX+=dx*K;
		this.getTransform3D();
		this.repaint();
		rotY-=dy*K;
		rotX-=dx*K;
	}
	
}