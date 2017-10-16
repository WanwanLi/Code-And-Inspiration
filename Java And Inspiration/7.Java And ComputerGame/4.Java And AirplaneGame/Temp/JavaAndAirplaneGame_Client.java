import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class JavaAndAirplaneGame_Client
{
	public static void main(String[] args)
	{
		new JFrame_AirplaneGame_Client();
	}
}
class JFrame_AirplaneGame_Client extends JFrame implements KeyListener,MouseListener,MouseMotionListener
{
	private final int X=0,Y=1,EMPTY=0,HIT=1,RUIN=2;
	private int myState=0,opponentState=0;
	private boolean canAttack=true;
	private int row=11,column=15,number=6;
	private int x0=50,y0=50,left=x0+50,top=y0+40;
	private int width=600,height=450,frameWidth=800,frameHeight=600;
	private Image backgroundImage;
	private Color color_light=Color.white;
	private Color color_dark=new Color(33,49,62);
	private Color color_white=new Color(1f,1f,1f,0.4f);
	private int[][] myGrids=new int[row][column];
	private int[][] opponentGrids=new int[row][column];
	private int[][] airplanePositions=new int[number][2];
	private Image[] airplaneImages=new Image[number];
	private Image[] airplaneSketchImages=new Image[number];
	private int currentI=-1,currentJ=-1;
	private boolean gameOver=false;
	private int score=0;
	private String IP="127.0.0.1";
	private int port=8080;
	private String[] stateStrings=new String[]{"EMPTY","HIT","RUIN"};
	private int[][] airplane0=new int[][]
	{
		{number,number,number,0},
		{number,number,-number,0},
		{0,0,0,number}
	};
	private int[][] airplane1=new int[][]
	{
		{0,0,1,1},
		{-1,1,1,1},
		{0,0,1,1}
	};
	private int[][] airplane2=new int[][]
	{
		{2,2,2,0},
		{2,2,2,-2},
		{2,2,2,0}
	};
	private int[][] airplane3=new int[][]
	{
		{0,3,0},
		{3,3,3},
		{0,3,0},
		{0,-3,0},
		{0,3,0}
	};
	private int[][] airplane4=new int[][]
	{
		{0,0,-4,0,0},
		{4,4,4,4,4},
		{0,0,4,0,0},
		{0,4,4,4,0}
	};
	private int[][] airplane5=new int[][]
	{
		{0,0,5,0,0},
		{5,0,5,5,0},
		{5,5,5,5,-5},
		{5,0,5,5,0},
		{0,0,5,0,0}
	};
	private int[][][] airplanes=new int[][][]
	{
		airplane0,
		airplane1,
		airplane2,
		airplane3,
		airplane4,
		airplane5
	};
	private Color[] airplaneColors=new Color[]
	{
		new Color(30,31,32),
		new Color(165,165,165),
		new Color(63,68,74),
		new Color(216,136,56),
		new Color(110,106,82),
		new Color(221,225,217)
	};
	public JFrame_AirplaneGame_Client()
	{
		this.backgroundImage=Toolkit.getDefaultToolkit().getImage("background.gif");
		for(int i=0;i<number;i++)
		{
			this.airplaneImages[i]=Toolkit.getDefaultToolkit().getImage("airplane\\"+i+".gif");
			this.airplaneSketchImages[i]=Toolkit.getDefaultToolkit().getImage("airplane\\"+(100+i)+".gif");
		}
		this.initAirplaneGame();
		this.setBounds(x0,y0,frameWidth,frameHeight);
		this.setResizable(false);
		this.setTitle("JavaAndAirplaneGame_Client");
		this.setVisible(true);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.sendMyAirplanePositions();
		this.receiveOpponentAirplanePositions();
	}
	public void paint(Graphics g)
	{
		g.drawImage(backgroundImage,0,0,this);
		this.drawAirplanes(g);
		this.drawGrids(g,1);
		this.drawString(g,"Your score is:"+score,x0-20,y0+20,20);
		if(gameOver)this.drawString(g,"Game Over!",x0-30,top+250,100);
		if(canAttack)this.drawString(g,"Your could Attack now!",x0+400,y0+20,20);
		else this.drawString(g,"You should wait now!",x0+400,y0+20,20);
		this.drawString(g,"Your attack is: "+stateStrings[myState],x0+20,y0+520,20);
		this.drawString(g,"Opponent attack is: "+stateStrings[opponentState],x0+450,y0+520,20);
	}
	private void initAirplaneGame()
	{
		this.score=0;
		this.gameOver=false;
		int maxLoop=50;
		for(int i=0;i<row;i++)for(int j=0;j<column;j++)this.myGrids[i][j]=0;
		for(int i=0;i<number;i++)
		{
			boolean conflict=false;
			int rangeX=column-airplanes[i][0].length,x=0;
			int rangeY=row-airplanes[i].length,y=0,loop=0;
			do
			{
				x=(int)(rangeX*Math.random());
				y=(int)(rangeY*Math.random());
				conflict=this.thereIsConflict(i,x,y);
				loop++;
			}
			while(conflict&&loop<maxLoop);
			if(!conflict)this.placeAirplane(i,x,y);
			else
			{
				this.airplanePositions[i][X]=column;
				this.airplanePositions[i][Y]=row;
			}
		}
		this.repaint();
	}
	private boolean thereIsConflict(int a,int x0,int y0)
	{
		int c=airplanes[a][0].length;
		int r=airplanes[a].length;
		for(int i=0;i<r;i++)
		{
			for(int j=0;j<c;j++)
			{
				if(airplanes[a][i][j]*myGrids[y0+i][x0+j]!=0)return true;
			}
		}
		return false;
	}
	private void placeAirplane(int a,int x0,int y0)
	{
		this.airplanePositions[a][X]=x0;
		this.airplanePositions[a][Y]=y0;
		int c=airplanes[a][0].length;
		int r=airplanes[a].length;
		for(int i=0;i<r;i++)
		{
			for(int j=0;j<c;j++)
			{
				this.myGrids[y0+i][x0+j]=airplanes[a][i][j];
			}
		}
	}
	private void drawString(Graphics g,String string,int x,int y,int size)
	{
		g.setColor(color_dark);
		g.setFont(new Font(null,Font.PLAIN,size));
		g.drawString(string,x+3,y+2);
		g.setColor(color_light);
		g.setFont(new Font(null,Font.PLAIN,size));
		g.drawString(string,x,y);
	}
	private void drawGrids(Graphics g,int size)
	{
		int h=height/row,w=width/column;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				int oppoGrid=opponentGrids[i][j];
				oppoGrid=oppoGrid<0?-oppoGrid:oppoGrid;
				if(oppoGrid!=0)
				{
					if(oppoGrid==number)oppoGrid=0;
					g.setColor(airplaneColors[oppoGrid]);
					g.fillRect(left+j*w,top+i*h,w,h);
				}
				if(currentI==i&&currentJ==j)
				{
					g.setColor(color_white);
					g.fillRect(left+j*w,top+i*h,w,h);
				}
				this.drawRect(g,left+j*w,top+i*h,w,h,size);
/*
				int grid=myGrids[i][j];
				if(gameOver)grid=grid<0?-grid:grid;
				if(grid>0||gameOver)g.drawImage(luckImages[grid],left+j*w,top+i*h,this);
*/
			}
		}
	}
	private void drawAirplanes(Graphics g)
	{
		int h=height/row,w=width/column;
		for(int a=0;a<number;a++)
		{
			int j=airplanePositions[a][X];
			int i=airplanePositions[a][Y];
			if(j<column&&i<row)g.drawImage(airplaneImages[a],left+j*w,top+i*h,this);
		}
	}
	private void drawRect(Graphics g,int x,int y,int w,int h,int size)
	{
		g.setColor(color_dark);
		for(int i=0;i<size;i++)g.drawRect(x-i,y-i,w+2*i,h+2*i);
		g.setColor(color_light);
		for(int i=size;i<2*size;i++)g.drawRect(x-i,y-i,w+2*i,h+2*i);
	}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e)
	{
		String t=KeyEvent.getKeyText(e.getKeyChar());
		if(t.equals("Enter"))
		{
			this.initAirplaneGame();
			this.repaint();
		}
	}
	public void mouseMoved(MouseEvent e)
	{
		int i0=currentI,j0=currentJ;
		int x=e.getX(),y=e.getY();
		int h=height/row,w=width/column;
		this.currentJ=(x-left)/w;
		this.currentI=(y-top)/h;
		if(currentI!=i0||currentJ!=j0)this.repaint();
	}
	public void mouseClicked(MouseEvent e)
	{
		if(gameOver)return;
		if(canAttack&&isValidPosition(currentI,currentJ))
		{
			this.attack(currentI,currentJ);
			this.canAttack=false;
			this.repaint();
			this.waitForAttack();
		}
	}
	private void attack(int i,int j)
	{
		int state0=myState;
		if(opponentGrids[i][j]==0)
		{
			this.myState=EMPTY;
			this.sendInt(EMPTY);
		}
	}
	private void waitForAttack()
	{
		while(true)
		{
			int signal=this.receiveInt();
			if(signal==EMPTY)
			{
				this.opponentState=EMPTY;
				this.canAttack=true;
System.out.println("receive : "+signal);
				this.repaint();
				break;
			}
		}
	}
	private boolean isValidPosition(int i,int j)
	{
		if(i<0)return false;
		if(j<0)return false;
		if(i>=row)return false;
		if(j>=column)return false;
		return true;
	}
	private void receiveOpponentAirplanePositions()
	{
		for(int a=0;a<number;a++)
		{
			int x0=this.receiveInt();
			int y0=this.receiveInt();
			if(x0>=column&&y0>=row)continue;
			int c=airplanes[a][0].length;
			int r=airplanes[a].length;
			for(int i=0;i<r;i++)
			{
				for(int j=0;j<c;j++)
				{
					this.opponentGrids[y0+i][x0+j]=airplanes[a][i][j];
				}
			}
		}
		this.repaint();
	}
	private void sendMyAirplanePositions()
	{
		for(int a=0;a<number;a++)
		{
			this.sendInt(airplanePositions[a][X]);
			this.sendInt(airplanePositions[a][Y]);
		}
	}
	private void sendInt(int integer)
	{
		try
		{
			Socket socket=new Socket(IP,port);
			DataOutputStream DataOutputStream1=new DataOutputStream(socket.getOutputStream());
			DataOutputStream1.write(integer);
			socket.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	private int receiveInt()
	{
		int integer=0;
		try
		{
			Socket socket=new Socket(IP,port);
			BufferedReader BufferedReader1=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			integer=BufferedReader1.read();
			socket.close();
		}
		catch(Exception e){e.printStackTrace();}
		return integer;
	}
	private void sendString(String string)
	{
		try
		{
			Socket socket=new Socket(IP,port);
			DataOutputStream DataOutputStream1=new DataOutputStream(socket.getOutputStream());
			DataOutputStream1.writeBytes(string);
			socket.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	private String receiveString()
	{
		String newString="";
		try
		{
			Socket socket=new Socket(IP,port);
			BufferedReader BufferedReader1=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			newString=BufferedReader1.readLine();
			BufferedReader1.close();
			socket.close();
		}
		catch(Exception e){e.printStackTrace();}
		return newString;
	}
	private void runClient()
	{
		while(true)
		{
			try
			{
				Socket socket=new Socket(IP,port);
				DataOutputStream DataOutputStream1=new DataOutputStream(socket.getOutputStream());
				BufferedReader BufferedReader1=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedReader BufferedReader2=new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Client.Input>");
				String input=BufferedReader2.readLine()+"\n";
				DataOutputStream1.writeBytes(input);
				String reply=BufferedReader1.readLine();
				System.out.println("Server.Reply>"+reply);
				BufferedReader1.close();
				socket.close();
				if(input.equals("exit\n"))break;
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseDown(MouseEvent e){}
	public void mouseUp(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseDragged(MouseEvent e){}
}
