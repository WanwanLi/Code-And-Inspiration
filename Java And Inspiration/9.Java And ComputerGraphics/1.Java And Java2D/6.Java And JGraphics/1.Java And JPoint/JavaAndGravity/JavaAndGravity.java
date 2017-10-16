import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
public class JavaAndGravity extends JFrame implements KeyListener,MouseListener,MouseMotionListener,ActionListener
{
	private int V,X,Y,G,R,X0,Y0,dX,dY,v;
	private Color color;
	private int[] coordinates;
	private int screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int screenHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private Ball ball;
	private Image screenImage;
	private Timer timer;
	public static void main(String[] args)
	{
		Color color=Color.green;
		int V=30,R=50;
		new JavaAndGravity(V,R,color);
	}
	public JavaAndGravity(int V,int R,Color color)
	{
		this.v=0;
		this.V=V;
		this.G=5;
		this.X=R;
		this.Y=2*R;
		this.R=R;
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
		this.timer=new Timer(10,this);
		this.timer.start();
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.ball=new Ball(X,Y,R);
		this.setBackground(Color.black);
		this.screenImage=new BufferedImage(screenWidth,screenHeight,1);
		this.ball.fill(screenImage,color);
		this.setVisible(true);
	}
	public void paint(Graphics g)
	{
		g.drawImage(screenImage,0,0,null);
	}
	public void actionPerformed(ActionEvent e)
	{
		this.getPosition();
		this.ball=new Ball(X,Y,R);
		this.screenImage=new BufferedImage(screenWidth,screenHeight,1);
		this.ball.fill(screenImage,color);
		this.repaint();
	}
	private void getPosition()
	{

		this.X+=V;
		this.Y+=v;
		if(Y>screenHeight-R)
		{
			this.Y=screenHeight-R;
			v-=2*v-v/20;
		}
		v+=G;
		if(X<R||X>screenWidth-R)V*=-1;
		if(X<R)this.X=R;
		if(X>screenWidth-R)this.X=screenWidth-R;
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


class Ball
{
	private int X,Y,R;
	public Ball(int X,int Y,int R)
	{
		this.X=X;
		this.Y=Y;
		this.R=R;
	}
	public void fill(Image image,Color color)
	{
		Graphics g=image.getGraphics();
		int Red=color.getRed();
		int Green=color.getGreen();
		int Blue=color.getBlue();
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
			for(int j=0;j<=x;j++)
			{
				int red=Red*(R*R-(j*j+y*y))/(R*R);
				int green=Green*(R*R-(j*j+y*y))/(R*R);
				int blue=Blue*(R*R-(j*j+y*y))/(R*R);
				if(red>255)red=255;
				if(green>255)green=255;
				if(blue>255)blue=255;
				if(red<0)red=0;
				if(green<0)green=0;
				if(blue<0)blue=0;
				g.setColor(new Color(red,green,blue));
				g.drawLine(j+X,y+Y,j+X,y+Y);
				g.drawLine(-j+X,y+Y,-j+X,y+Y);
				g.drawLine(j+X,-y+Y,j+X,-y+Y);
				g.drawLine(-j+X,-y+Y,-j+X,-y+Y);
			}
		}
	}
}
