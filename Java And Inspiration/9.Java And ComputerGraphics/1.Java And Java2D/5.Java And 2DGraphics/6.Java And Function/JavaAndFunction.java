import java.awt.*;
import java.awt.event.*;
public class JavaAndFunction
{
	public static void main(String[] args)
	{
		Function Function1=new Function(Math.PI/6,Math.PI/3);
		Function1.setVisible(true);
		Function1.setMode(Function1.HIDE_LINES);
	}
}
class Function extends Frame implements MouseListener,MouseMotionListener
{
	double[] X,Y,Z;
	int m=640;
	int n=480;
	int dx=600,dy=800,dz=0;
	double rotY,rotX;
	int INF=1600;
	int Max=2000;
	int[] yMin=new int[Max];
	int[] yMax=new int[Max];
	int[][] f=new int[m][Max];
	int mode =0;
	public static final int HIDE_LINES=1;
	public Function(double rotY,double rotX)
	{
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
	private double function(double x,double z)
	{
		double H=800;
		double A=100;
		double B=100;
		return -H*Math.exp(-x*x/(A*A)-z*z/(B*B));
	}
	private double function1(double x,double z)
	{
		double Q=1000;
		double D=100;
		return Q/Math.sqrt((x-D)*(x-D)+z*z)-Q/Math.sqrt((x+D)*(x+D)+z*z);
	}
	private void getCoordinates()
	{
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				X[i*n+j]=i;
				Y[i*n+j]=function1(i-0.5*m,j-0.5*n);
				Z[i*n+j]=j;
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
				X[i*n+j]=x*Math.cos(rotY)-z*Math.sin(rotY);
				Z[i*n+j]=x*Math.sin(rotY)+z*Math.cos(rotY);
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
	public void paintFunction(Graphics g)
	{
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)f[i][(int)X[i*n+j]+dx]=(int)Y[i*n+j]+dy;
		for(int x=0;x<Max;x++){yMin[x]=INF;yMax[x]=0;}
		for(int i=0;i<m;i++)
		{
			for(int x=0;x<Max;x++)if(yMin[x]==0)yMin[x]=INF;
			for(int x=0;x<Max;x++)
			{
				if(f[i][x]<yMin[x]){yMin[x]=f[i][x];g.drawLine(x,f[i][x],x,f[i][x]);}
				if(f[i][x]>yMax[x]){yMax[x]=f[i][x];g.drawLine(x,f[i][x],x,f[i][x]);}
			}
		}
	}
	public void setMode(int mode)
	{
		this.mode=mode;
	}
	public void paint(Graphics g)
	{
		if(mode==HIDE_LINES){paintFunction(g);return;}
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)g.drawLine((int)X[i*n+j]+dx,(int)Y[i*n+j]+dy,(int)X[i*n+j]+dx,(int)Y[i*n+j]+dy);
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