import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
public class JavaAndGravity extends JFrame implements KeyListener,MouseListener,MouseMotionListener,ActionListener
{
	private int V,X,Y,G,R,X0,Y0,dX,dY,v;
	private Color color;
	private int screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int screenHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private BallList ballList;
	private Image screenImage;
	private Timer timer;
	public static void main(String[] args)
	{
		Color color=Color.green;
		int Vx=30,R=50;
		new JavaAndGravity(Vx,R,color);
	}
	public JavaAndGravity(int Vx,int R,Color color)
	{
		int X=R;
		int Y=2*R;
		int Vy=0;
		this.color=color;
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
		this.ballList=new BallList();
		Ball ball=new Ball(Vx,Vy,X,Y,color,R,screenWidth,screenHeight);
		this.ballList.add(ball);
		Vx=-10;X=screenWidth-R;color=Color.yellow;
		ball=new Ball(Vx,Vy,X,Y,color,R,screenWidth,screenHeight);
		this.ballList.add(ball);
		Vx=-40;X=screenWidth-R;Y=screenHeight/3;color=Color.cyan;
		ball=new Ball(Vx,Vy,X,Y,color,R,screenWidth,screenHeight);
		this.ballList.add(ball);
		Vx=+30;X=screenWidth-R;Y=screenHeight/3;color=Color.pink;
		ball=new Ball(Vx,Vy,X,Y,color,R,screenWidth,screenHeight);
		this.ballList.add(ball);
		this.setBackground(Color.black);
		this.setVisible(true);
	}
	public void paint(Graphics g)
	{
		g.drawImage(screenImage,0,0,null);
	}
	public void actionPerformed(ActionEvent e)
	{
		this.screenImage=new BufferedImage(screenWidth,screenHeight,1);
		Ball ball=this.ballList.first();
		while(ball!=null)
		{
			ball.getPosition(ballList);
			ball.fill(screenImage);
			ball=ball.next;
		}
		this.repaint();
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
	public int Vx,Vy,X,Y;
	private int R,screenWidth,screenHeight,G=5;
	private Color color;
	public Ball next;
	public Ball(int Vx,int Vy,int X,int Y,Color color,int R,int screenWidth,int screenHeight)
	{
		this.Vx=Vx;
		this.Vy=Vy;
		this.X=X;
		this.Y=Y;
		this.R=R;
		this.color=color;
		this.screenWidth=screenWidth;
		this.screenHeight=screenHeight;
	}
	public void getPosition(BallList ballList)
	{
		this.getCollision(ballList);
		this.X+=Vx;
		this.Y+=Vy;
		if(Y>screenHeight-R)
		{
			this.Y=screenHeight-R;
			Vy-=2*Vy-Vy/20;
		}
		Vy+=G;
		if(X<R||X>screenWidth-R)Vx*=-1;
		if(X<R)this.X=R;
		if(X>screenWidth-R)this.X=screenWidth-R;
	}
	private void getCollision(BallList ballList)
	{
		Ball ball=ballList.first();
		while(ball!=null)
		{
			if(ball!=this)
			{
				int x=ball.X,y=ball.Y,r=ball.R;
				int dx=X-x,dy=Y-y,dr=R+r;
				if(dx*dx+dy*dy<dr*dr)
				{
					int ball_Vx=ball.Vx;
					ball.Vx=this.Vx;
					this.Vx=ball_Vx;
					int ball_Vy=ball.Vy;
					ball.Vy=this.Vy;
					this.Vy=ball_Vy;
				}
			}
			ball=ball.next;
		}
	}
	public void fill(Image image)
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



class BallList
{
	Ball first,last,current;
	int length=0;
	public BallList()
	{
		this.first=this.last=null;
		this.current=this.first;
	}
	public void add(Ball ball)
	{
		this.length++;
		if(first==null)
		{
			this.first=this.last=ball;
			this.current=this.first;
		}
		else 
		{
			this.last.next=ball;
			this.last=ball;
		}
	}
	public int length()
	{
		return length;
	}
	public Ball first()
	{
		this.current=first;
		return this.next();
	}
	public Ball next()
	{
		if(current==null)return null;
		Ball b=current;
		this.current=current.next;
		return b;
	}
}