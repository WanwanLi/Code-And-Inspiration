import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
public class JavaAndPoint extends Frame implements KeyListener,MouseListener,MouseMotionListener
{
	private int X,Y,X0,Y0,dX,dY;
	private Color color;
	private int[] coordinates;
	private int screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int screenHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private Point point;
	private Image screenImage;
	public static void main(String[] args)
	{
		Color color=Color.green;
		int X=400,Y=400,R=300;
		new JavaAndPoint(color,X,Y,R);
	}
	public JavaAndPoint(Color color,int X,int Y,int R)
	{
		this.color=color;
		this.coordinates=coordinates;
		this.setBounds(0,0,screenWidth,screenHeight);
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
		this.point=new Point(X,Y,R);
		this.setBackground(Color.black);
		this.screenImage=new BufferedImage(screenWidth,screenHeight,2);
		this.point.fill(screenImage,color);
		this.setVisible(true);
	}
	public void paint(Graphics g)
	{
		g.drawImage(screenImage,0,0,null);
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mousePressed(MouseEvent e)
	{
		X=e.getX();
		Y=e.getY();
	}
	public void mouseDragged(MouseEvent e)
	{
		this.dY=e.getY()-Y;
		this.dX=e.getX()-X;
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
	public void keyPressed(KeyEvent e){}
	public void  keyTyped(KeyEvent e){}
}


class Point
{
	private int X,Y,R;
	public Point(int X,int Y,int R)
	{
		this.X=X;
		this.Y=Y;
		this.R=R;
	}
	public void fill(Image image,Color color)
	{
		Graphics g=image.getGraphics();
		g.setColor(color);
		int x=0,y=R,x1=x+1,y0=y-1;
		g.drawLine(x+X,y+Y,x+X,y+Y);
		int D=x1*x1+y0*y0-R*R;
		while(x<=R&&y>=0)
		{
			if(D<0)
			{
				int d=2*(D+y)-1;
				if(d<=0){x++;D+=2*x+1;}
				else{x++;y--;D+=2*x-2*y+2;}			
			}
			else if(D>=0)
			{
				int d=2*(D-x)-1;
				if(d<=0){x++;y--;D+=2*x-2*y+2;}
				else {y--;D+=-2*y+1;}
			}
			else {x++;y--;D+=2*x-2*y+2;}
			g.drawLine(-x+X,y+Y,x+X,y+Y);
			g.drawLine(-x+X,-y+Y,x+X,-y+Y);
		}
	}
}
