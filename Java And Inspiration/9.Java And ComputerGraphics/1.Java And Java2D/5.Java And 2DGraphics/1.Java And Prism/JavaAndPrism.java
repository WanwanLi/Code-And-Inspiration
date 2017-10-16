import java.awt.*;
import java.awt.event.*;
public class JavaAndPrism 
{
	public static void main(String[] args)
	{
		Prism Prism1=new Prism(6,300,200,Math.PI/6,Math.PI/3);
		Prism1.setVisible(true);
		Prism1.setMode(Prism.HIDE_LINES);
	}
}
class Prism extends Frame implements KeyListener,MouseListener,MouseMotionListener
{
	double[] aX,aY,aZ,bX,bY,bZ,AX,AY,AZ,BX,BY,BZ;
	int m;
	double h,r;
	int dx=800,dy=400,dz=0;
	double Y0,X0,dY,dX,rotY,rotX;
	public static final int HIDE_LINES=1;
	int mode=0;
	public Prism(int m,double h,double r,double rotY,double rotX)
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
		AX=new double[m];
		AY=new double[m];
		AZ=new double[m];
		BX=new double[m];
		BY=new double[m];
		BZ=new double[m];
		this.getCoordinates();
		this.getTransform3D();
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	private void getCoordinates()
	{
		double w=2*Math.PI/m;
		for(int i=0;i<m;i++)
		{
			AX[i]=BX[i]=aX[i]=bX[i]=r*Math.cos(i*w);
			AY[i]=BY[i]=aY[i]=bY[i]=r*Math.sin(i*w);
			AZ[i]=aZ[i]=h/2+dz;
			BZ[i]=bZ[i]=-h/2+dz;
		}
	}
	private void getTransform3D()
	{
		double x,y,z;
		for(int i=0;i<m;i++)
		{
			x=AX[i];
			z=AZ[i];
			aX[i]=x*Math.cos(rotY)-z*Math.sin(rotY);
			aZ[i]=x*Math.sin(rotY)+z*Math.cos(rotY);
			x=BX[i];
			z=BZ[i];
			bX[i]=x*Math.cos(rotY)-z*Math.sin(rotY);
			bZ[i]=x*Math.sin(rotY)+z*Math.cos(rotY);
		}
		for(int i=0;i<m;i++)
		{
			y=AY[i];
			z=aZ[i];
			aY[i]=y*Math.cos(rotX)-z*Math.sin(rotX);
			aZ[i]=y*Math.sin(rotX)+z*Math.cos(rotX);
			y=BY[i];
			z=bZ[i];
			bY[i]=y*Math.cos(rotX)-z*Math.sin(rotX);
			bZ[i]=y*Math.sin(rotX)+z*Math.cos(rotX);
		}
		for(int i=0;i<m;i++)
		{
			aX[i]+=X0+dX;
			bX[i]+=X0+dX;
			aY[i]+=Y0+dY;
			bY[i]+=Y0+dY;
		}
	}
	private boolean isVisible(double x0,double y0,double x1,double y1,double x2,double y2)
	{
		double dx1=x1-x0;
		double dy1=y1-y0;
		double dx2=x2-x0;
		double dy2=y2-y0;
		return ((dx1*dy2-dx2*dy1)>0?true:false);
	}
	public void setMode(int mode)
	{
		this.mode=mode;
	}
	private void paintPrism(Graphics g)
	{
		if(isVisible(aX[0],aY[0],aX[1],aY[1],aX[2],aY[2]))for(int i=0;i<m;i++)g.drawLine((int)aX[i]+dx,(int)aY[i]+dy,(int)aX[(i+1)%m]+dx,(int)aY[(i+1)%m]+dy);
		else for(int i=0;i<m;i++)g.drawLine((int)bX[i]+dx,(int)bY[i]+dy,(int)bX[(i+1)%m]+dx,(int)bY[(i+1)%m]+dy);
		for(int i=0;i<m;i++)
		{
			if(isVisible(aX[i],aY[i],bX[i],bY[i],aX[(i+1)%m],aY[(i+1)%m]))
			{
				g.drawLine((int)aX[i]+dx,(int)aY[i]+dy,(int)aX[(i+1)%m]+dx,(int)aY[(i+1)%m]+dy);
				g.drawLine((int)aX[i]+dx,(int)aY[i]+dy,(int)bX[i]+dx,(int)bY[i]+dy);
				g.drawLine((int)aX[(i+1)%m]+dx,(int)aY[(i+1)%m]+dy,(int)bX[(i+1)%m]+dx,(int)bY[(i+1)%m]+dy);
				g.drawLine((int)bX[i]+dx,(int)bY[i]+dy,(int)bX[(i+1)%m]+dx,(int)bY[(i+1)%m]+dy);
			}
		}
	}
	public void paint(Graphics g)
	{
		if(mode==1){paintPrism(g);return;}
		for(int i=0;i<m;i++)
		{
			g.drawLine((int)aX[i]+dx,(int)aY[i]+dy,(int)aX[(i+1)%m]+dx,(int)aY[(i+1)%m]+dy);
			g.drawLine((int)aX[i]+dx,(int)aY[i]+dy,(int)bX[i]+dx,(int)bY[i]+dy);
			g.drawLine((int)bX[i]+dx,(int)bY[i]+dy,(int)bX[(i+1)%m]+dx,(int)bY[(i+1)%m]+dy);
		}
	}
	public void mouseClicked(MouseEvent e){}
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
		this.dY=e.getY()-Y;
		this.dX=e.getX()-X;
		this.getTransform3D();
		this.repaint();
	}
	public void mouseReleased(MouseEvent e)
	{
		this.X0+=dX;
		this.Y0+=dY;
		this.dY=0;
		this.dX=0;
	}
	public void keyReleased(KeyEvent e){}
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyChar()=='y')
		{
			this.rotY+=0.005*Math.PI;
			this.getTransform3D();
			this.repaint();
		}
		if(e.getKeyChar()=='x')
		{
			this.rotX+=0.005*Math.PI;
			this.getTransform3D();
			this.repaint();
		}
	}
	public void  keyTyped(KeyEvent e){}
}