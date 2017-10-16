import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class JavaAndAirplaneGame
{
	public static void main(String[] args)
	{
		new AirplaneGame(args[0]);
	}
}
class AirplaneGame extends Frame implements KeyListener, MouseListener, MouseMotionListener, ActionListener
{
	private final int X=0,Y=1,LOSS=0,HIT=1,RUIN=2,MSG=3,END=4;
	private int server=0;
	private int myState=0,opponentState=0;
	private boolean canAttack=false;
	private int row=11,column=15,number=6;
	private int x0=50,y0=50,left=x0+50,top=y0+40;
	private int width=600,height=450,frameWidth=800,frameHeight=600;
	private Image backgroundImage;
	private Color color_light=Color.white;
	private Color color_dark=new Color(33,49,62);
	private Color color_white=new Color(1f,1f,1f,0.4f);
	private int[][] myGrids=new int[row][column];
	private int[][] opponentGrids=new int[row][column];
	private int[][] myAirplanePositions=new int[number][2];
	private int[][] opponentAirplanePositions=new int[number][2];
	private Image[] airplaneImages=new Image[number];
	private Image[] airplaneSketchImages=new Image[number];
	private Image redSpotImage;
	private int currentI=-1,currentJ=-1;
	private boolean gameOver=false;
	private int score=0;
	private String IP="127.0.0.1";
	private int port=8080;
	private ServerSocket ServerSocket1;
	private String[] stateStrings=new String[]{"LOSS","HIT","RUIN"};
	private boolean isServer=false;
	private int currentNumber=0;
	private String message="";
	private int[][] airplane0=new int[][]
	{
		{number,number,number,0},
		{number,number,number,0},
		{0,0,0,-number}
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
		{0,0,4,0,0},
		{4,4,-4,4,4},
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
		new Color(30f/255,31f/255,32f/255,0.8f),
		new Color(165f/255,165f/255,165f/255,0.8f),
		new Color(63f/255,68f/255,74f/255,0.8f),
		new Color(216f/255,136f/255,56f/255,0.8f),
		new Color(110f/255,106f/255,82f/255,0.8f),
		new Color(221f/255,225f/255,217f/255,0.8f)
	};
	public AirplaneGame(String type)
	{
		this.backgroundImage=Toolkit.getDefaultToolkit().getImage("background.gif");
		this.redSpotImage=Toolkit.getDefaultToolkit().getImage("redspot.gif");
		for(int i=0;i<number;i++)
		{
			this.airplaneImages[i]=Toolkit.getDefaultToolkit().getImage("airplane\\"+i+".gif");
			this.airplaneSketchImages[i]=Toolkit.getDefaultToolkit().getImage("airplane\\"+(100+i)+".gif");
		}
		this.initAirplaneGame();
		this.setBounds(x0,y0,frameWidth,frameHeight);
		this.setResizable(false);
		this.setVisible(true);
		this.addTimer(this);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		if(type.equals("server"))this.runServer();
		else if(type.equals("client"))this.runClient();
		else {System.out.println("Imput Error!");System.exit(0);}
	}
	private void runServer()
	{
		this.setTitle("JavaAndAirplaneGame_Server");
		this.isServer=true;
		try{this.ServerSocket1=new ServerSocket(port);}catch(Exception e){e.printStackTrace();}
		this.receiveOpponentAirplanePositions();
		this.sendMyAirplanePositions();
		this.canAttack=false;
		this.waitForAttack();
	}
	private void runClient()
	{
		this.setTitle("JavaAndAirplaneGame_Client");
		this.isServer=false;
		this.sendMyAirplanePositions();
		this.receiveOpponentAirplanePositions();
		this.canAttack=true;
	}
	public void mouseClicked(MouseEvent e)
	{
		if(gameOver)return;
		if(canAttack&&isValidPosition(currentI,currentJ))
		{
			this.attack(currentI,currentJ);
			this.waitForAttack();
		}
	}
	private void sendMessage()
	{
		if(!message.equals(""))
		{
			int l=message.length();
			if(l>=4)
			{
				String s=message.substring(0,4);
				if(s.equals("Msg:"))return;
			}
			this.sendInt(MSG);
			this.sendString(message);
		}
	}
	private void attack(int i,int j)
	{
		this.sendMessage();
		if(gameOver)this.sendInt(END);
		else if(opponentGrids[i][j]==0)
		{
			this.myState=LOSS;
			this.sendInt(LOSS);
		}
		else if(opponentGrids[i][j]>0)
		{
			this.myState=HIT;
			this.sendInt(HIT);
			this.sendInt(i);
			this.sendInt(j);
			this.score+=opponentGrids[i][j]%100;
			this.opponentGrids[i][j]+=100;
		}
		else
		{
			this.myState=RUIN;
			this.sendInt(RUIN);
			this.sendInt(i);
			this.sendInt(j);
			this.score+=opponentGrids[i][j]*-10;
			this.removeOpponentAirplane(-opponentGrids[i][j]);
		}
		this.canAttack=false;
	}
	private void waitForAttack()
	{
		int signal=this.receiveInt();
		if(signal==MSG)
		{
			this.message="Msg:"+this.receiveString();
			signal=this.receiveInt();
		}
		if(signal==END)this.gameOver=true;
		else if(signal==LOSS)this.opponentState=LOSS;
		else if(signal==HIT)
		{
			this.opponentState=HIT;
			int i=this.receiveInt();
			int j=this.receiveInt();
			this.score-=myGrids[i][j]%100;
			this.myGrids[i][j]+=100;
		}
		else
		{
			this.opponentState=RUIN;
			int i=this.receiveInt();
			int j=this.receiveInt();
			this.score-=myGrids[i][j]*-10;
			this.removeMyAirplane(-myGrids[i][j]);
			if(currentNumber==0)
			{
				this.gameOver=true;
				this.sendInt(END);
			}
		}
		this.canAttack=true;
	}
	public void actionPerformed(ActionEvent e)
	{
		this.repaint();
	}
	public void paint(Graphics g)
	{
		BufferedImage bufferedImage=new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		this.drawImage(bufferedImage.getGraphics()); g.drawImage(bufferedImage, 0, 0, null);
	}
	private void drawImage(Graphics g)
	{
		g.drawImage(backgroundImage,0,0,this);
		this.drawAirplanes(g);
		this.drawGrids(g,1);
		this.drawString(g,"Your score is:"+score,x0-20,y0+20,20);
		if(gameOver)this.drawString(g,"Game Over!",x0+70,top+250,100);
		if(canAttack)this.drawString(g,"Your could Attack now!",x0+400,y0+20,20);
		else this.drawString(g,"You should wait now!",x0+400,y0+20,20);
		this.drawString(g,"Your attack is: "+stateStrings[myState],x0+20,y0+520,20);
		this.drawString(g,"Opponent attack is: "+stateStrings[opponentState],x0+450,y0+520,20);
		this.drawString(g,message,x0+250,y0+520,20);
	}
	private void addTimer(ActionListener actionListener)
	{
		new Timer(100, actionListener).start();
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
			if(!conflict)this.placeMyAirplane(i,x,y);
			else
			{
				this.myAirplanePositions[i][X]=column;
				this.myAirplanePositions[i][Y]=row;
			}
		}
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
				if(oppoGrid>100)
				{
					oppoGrid%=100;
					if(oppoGrid==number)oppoGrid=0;
					g.setColor(airplaneColors[oppoGrid]);
					g.fillRect(left+j*w,top+i*h,w,h);
				}
				int myGrid=myGrids[i][j];
				if(myGrid>100)g.drawImage(redSpotImage,left+j*w,top+i*h,this);
				if(currentI==i&&currentJ==j)
				{
					g.setColor(color_white);
					g.fillRect(left+j*w,top+i*h,w,h);
				}
				this.drawRect(g,left+j*w,top+i*h,w,h,size);
			}
		}
	}
	private void drawAirplanes(Graphics g)
	{
		int h=height/row,w=width/column;
		for(int a=0;a<number;a++)
		{
			int j=myAirplanePositions[a][X];
			int i=myAirplanePositions[a][Y];
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
		char c=e.getKeyChar();
		String t=KeyEvent.getKeyText(c);
		if(t.equals("Delete"))this.message="";
		else if(t.equals("Backspace"))
		{
			int l=message.length();
			if(l==0)return;
			this.message=message.substring(0,l-1);
		}
		else this.message+=c;
	}
	public void mouseMoved(MouseEvent e)
	{
		int i0=currentI,j0=currentJ;
		int x=e.getX(),y=e.getY();
		int h=height/row,w=width/column;
		this.currentJ=(x-left)/w;
		this.currentI=(y-top)/h;
	}
	private void sendMyAirplanePositions()
	{
		for(int a=0;a<number;a++)
		{
			this.sendInt(myAirplanePositions[a][X]);
			this.sendInt(myAirplanePositions[a][Y]);
		}
	}
	private void receiveOpponentAirplanePositions()
	{
		for(int a=0;a<number;a++)
		{
			int x0=this.receiveInt();
			int y0=this.receiveInt();
			this.placeOpponentAirplane(a,x0,y0);
		}
	}
	private void placeOpponentAirplane(int a,int x0,int y0)
	{
		this.opponentAirplanePositions[a][X]=x0;
		this.opponentAirplanePositions[a][Y]=y0;
		if(x0>=column||y0>=row)return;
		int c=airplanes[a][0].length;
		int r=airplanes[a].length;
		for(int i=0;i<r;i++)
		{
			for(int j=0;j<c;j++)
			{
				if(airplanes[a][i][j]!=0)this.opponentGrids[y0+i][x0+j]=airplanes[a][i][j];
			}
		}
	}
	private void placeMyAirplane(int a,int x0,int y0)
	{
		this.currentNumber++;
		this.myAirplanePositions[a][X]=x0;
		this.myAirplanePositions[a][Y]=y0;
		int c=airplanes[a][0].length;
		int r=airplanes[a].length;
		for(int i=0;i<r;i++)
		{
			for(int j=0;j<c;j++)
			{
				if(airplanes[a][i][j]!=0)this.myGrids[y0+i][x0+j]=airplanes[a][i][j];
			}
		}
	}
	private void removeMyAirplane(int a)
	{
		this.currentNumber--;
		if(a==number)a=0;
		int x0=myAirplanePositions[a][X];
		int y0=myAirplanePositions[a][Y];
		this.myAirplanePositions[a][X]=column;
		this.myAirplanePositions[a][Y]=row;
		int c=airplanes[a][0].length;
		int r=airplanes[a].length;
		for(int i=0;i<r;i++)
		{
			for(int j=0;j<c;j++)
			{
				if(airplanes[a][i][j]!=0)this.myGrids[y0+i][x0+j]=0;
			}
		}
	}
	private void removeOpponentAirplane(int a)
	{
		if(a==number)a=0;
		int x0=opponentAirplanePositions[a][X];
		int y0=opponentAirplanePositions[a][Y];
		this.opponentAirplanePositions[a][X]=column;
		this.opponentAirplanePositions[a][Y]=row;
		int c=airplanes[a][0].length;
		int r=airplanes[a].length;
		for(int i=0;i<r;i++)
		{
			for(int j=0;j<c;j++)
			{
				if(airplanes[a][i][j]!=0)this.opponentGrids[y0+i][x0+j]=0;
			}
		}
	}
	private void sendInt(int integer)
	{
		try
		{
			Socket socket;
			if(isServer)socket=ServerSocket1.accept();
			else socket=new Socket(IP,port);
			DataOutputStream DataOutputStream1=new DataOutputStream(socket.getOutputStream());
			DataOutputStream1.write(integer);
			if(!isServer)socket.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	private int receiveInt()
	{
		int integer=0;
		try
		{
			Socket socket;
			if(isServer)socket=ServerSocket1.accept();
			else socket=new Socket(IP,port);
			BufferedReader BufferedReader1=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			integer=BufferedReader1.read();
			if(!isServer)socket.close();
		}
		catch(Exception e){e.printStackTrace();}
		return integer;
	}
	private void sendString(String string)
	{
		try
		{
			Socket socket;
			if(isServer)socket=ServerSocket1.accept();
			else socket=new Socket(IP,port);
			DataOutputStream DataOutputStream1=new DataOutputStream(socket.getOutputStream());
			DataOutputStream1.writeBytes(string+"\n");
			if(!isServer)socket.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	private String receiveString()
	{
		String string="";
		try
		{
			Socket socket;
			if(isServer)socket=ServerSocket1.accept();
			else socket=new Socket(IP,port);
			BufferedReader BufferedReader1=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			string=BufferedReader1.readLine();
			if(!isServer)socket.close();
		}
		catch(Exception e){e.printStackTrace();}
		return string;
	}
	private boolean isValidPosition(int i,int j)
	{
		if(i<0)return false;
		if(j<0)return false;
		if(i>=row)return false;
		if(j>=column)return false;
		return true;
	}
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseDown(MouseEvent e){}
	public void mouseUp(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseDragged(MouseEvent e){}
}