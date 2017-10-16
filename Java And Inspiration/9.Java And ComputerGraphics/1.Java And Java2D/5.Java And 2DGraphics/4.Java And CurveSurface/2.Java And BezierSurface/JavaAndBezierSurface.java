import java.awt.*;
import java.awt.event.*;
public class JavaAndBezierSurface
{
	public static void main(String[] args)
	{
		double[][][] p=
		{
			{{-200,0,200},{-100,300,200},{100,300,200},{200,0,200}},
			{{-200,100,100},{-100,500,100},{100,500,100},{200,100,100}},
			{{-200,100,-100},{-100,500,-100},{100,500,-100},{200,0,-100}},
			{{-200,0,-200},{-100,300,-200},{100,300,-200},{200,0,-200}}
		};
		BezierSurface BezierSurface1=new BezierSurface(p,15,18,Math.PI/6,Math.PI/3);
		BezierSurface1.setVisible(true);
	}
}
class BezierSurface extends Frame implements MouseListener,MouseMotionListener
{
	double[] coordinate_X,coordinate_Y,coordinate_Z;
	int m;
	int n;
	int dx=600,dy=400,dz=0;
	double r,rotY,rotX;
	final int X=0,Y=1,Z=2;
	double[][] pX,pY,pZ;
	public BezierSurface(double[][][] p,int m,int n,double rotY,double rotX)
	{
		this.m=m;
		this.n=n;
		this.r=r;
		this.rotY=rotY;
		this.rotX=rotX;
		this.pX=new double[4][4];
		this.pY=new double[4][4];
		this.pZ=new double[4][4];
		for(int i=0;i<4;i++)for(int j=0;j<4;j++){pX[i][j]=p[i][j][X];pY[i][j]=p[i][j][Y];pZ[i][j]=p[i][j][Z];}
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
	private double U(int j,double u)
	{
		switch(j)
		{
			case 0:return 1*(1-u)*(1-u)*(1-u);
			case 1:return 3*u*(1-u)*(1-u);
			case 2:return 3*u*u*(1-u);
			case 3:return 1*u*u*u;
			default:return 0;
		}
	}
	private double getProductOfMatrix(double u,double[][] p,double v)
	{
		double[] t=new double[4];
		for(int j=0;j<4;j++)for(int i=0;i<4;i++)t[j]+=U(i,u)*p[i][j];
		double product=0;
		for(int j=0;j<4;j++)product+=t[j]*U(j,v);
		return product;
	}	
	private double[] getBezierSurfaceCoordinates(double u,double v)
	{
		double[] BezierSurfaceCoordinates=new double[3];
		BezierSurfaceCoordinates[X]=getProductOfMatrix(u,pX,v);
		BezierSurfaceCoordinates[Y]=getProductOfMatrix(u,pY,v);
		BezierSurfaceCoordinates[Z]=getProductOfMatrix(u,pZ,v);
		return BezierSurfaceCoordinates;
	}
	private void getCoordinates()
	{

		for(int i=0;i<m;i++)
		{
			double u=i*(1.0/m);
			for(int j=0;j<n;j++)
			{
				double v=j*(1.0/n);
				double[] BezierSurfaceCoordinates=getBezierSurfaceCoordinates(u,v);
				coordinate_X[i*n+j]=BezierSurfaceCoordinates[X];
				coordinate_Y[i*n+j]=BezierSurfaceCoordinates[Y];
				coordinate_Z[i*n+j]=BezierSurfaceCoordinates[Z];
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
		for(int i=0;i<m-1;i++)for(int j=0;j<n-1;j++)g.drawLine((int)coordinate_X[i*n+j]+dx,(int)coordinate_Y[i*n+j]+dy,(int)coordinate_X[i*n+(j+1)]+dx,(int)coordinate_Y[i*n+(j+1)]+dy);
		for(int i=0;i<m-1;i++)for(int j=0;j<n-1;j++)g.drawLine((int)coordinate_X[i*n+j]+dx,(int)coordinate_Y[i*n+j]+dy,(int)coordinate_X[(i+1)*n+j]+dx,(int)coordinate_Y[(i+1)*n+j]+dy);
		for(int i=0;i<m-1;i++)g.drawLine((int)coordinate_X[i*n+(n-1)]+dx,(int)coordinate_Y[i*n+(n-1)]+dy,(int)coordinate_X[(i+1)*n+(n-1)]+dx,(int)coordinate_Y[(i+1)*n+(n-1)]+dy);
		for(int j=0;j<n-1;j++)g.drawLine((int)coordinate_X[(m-1)*n+j]+dx,(int)coordinate_Y[(m-1)*n+j]+dy,(int)coordinate_X[(m-1)*n+(j+1)]+dx,(int)coordinate_Y[(m-1)*n+(j+1)]+dy);
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