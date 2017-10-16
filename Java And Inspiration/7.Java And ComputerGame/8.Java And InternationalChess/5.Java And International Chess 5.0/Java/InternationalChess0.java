import java.applet.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.util.*;
public class InternationalChess0 extends Applet implements ActionListener,MouseListener
{
	char[][] chess;
	Scanner Scanner1;
	Frame Frame1;
	String command;
	boolean canMoveYellow;
	int recursionCounter=0;
	public void init()
	{
		chess=new char[][]
		{
			{'R','N','B','K','Q','B','N','R'},
			{'P','P','P','P','P','P','P','P'},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{'p','p','p','p','p','p','p','p'},
			{'r','n','b','k','q','b','n','r'}
		};
		this.addMouseListener(this);
		canMoveYellow=false;
	}	
	int J=690;
	int I=55;
	public void paint(Graphics g)
	{
		getAndSetPositionForBlack();
		g.drawImage(this.getImage(this.getCodeBase(),"chess//InternationalChess5.0.jpg"),0,0,this);
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				if(mouseIsClicked&&canMoveFrom(Y0,X0,i,j))g.drawImage(getImage(getCodeBase(),"chess//win.gif"),80*j+J,80*i+I,this);
				if(chess[i][j]>'Z'&&chess[i][j]<'z')g.drawImage(getImage(getCodeBase(),"chess//chessman//Black//"+chess[i][j]+".gif"),80*j+J,80*i+I,this);
				else if(chess[i][j]>'A'&&chess[i][j]<'Z')g.drawImage(getImage(getCodeBase(),"chess//chessman//Yellow//"+chess[i][j]+".gif"),80*j+J,80*i+I,this);
			}
		}
		getAndSetPositionForYellow();
	}
	private int Abs(int n)
	{
		return (n>0?n:-n);
	}
	private boolean isSameColor(int i0,int j0,int i1,int j1)
	{
		if(chess[i0][j0]==' '||chess[i1][j1]==' ')return false;
		else return ((chess[i0][j0]<'Z')==(chess[i1][j1]<'Z'));
	}
	private boolean canMoveFrom(int i0,int j0,int i1,int j1)
	{
		if(i0<0||j0<0||i1<0||j1<0||i0>7||j0>7||i1>7||j1>7||chess[i0][j0]==' '||isSameColor(i0,j0,i1,j1))return false;
		int di=Abs(i1-i0);
		int dj=Abs(j1-j0);
		int i,j;
		if(di==0&&dj==0)return false;
		boolean isVHLine=(di==0||dj==0);
		boolean isSkewLine=(di==dj);
		switch(chess[i0][j0])
		{
			case 'K':
				if(di>1)return false;
				else if(i0==0&&j0==3)
				{
					if(i1==0&&j1==1&&chess[0][0]=='R'&&chess[0][1]==' '&&chess[0][2]==' ')return true;
					else if(i1==0&&j1==6&&chess[0][7]=='R'&&chess[0][6]==' '&&chess[0][5]==' '&&chess[0][4]==' ')return true;
				}
				if(dj>1)return false;
				else return true;
			case 'k':
				if(di>1)return false;
				else if(i0==7&&j0==3)
				{
					if(i1==7&&j1==1&&chess[7][0]=='r'&&chess[7][1]==' '&&chess[7][2]==' ')return true;
					else if(i1==7&&j1==6&&chess[7][7]=='r'&&chess[7][6]==' '&&chess[7][5]==' '&&chess[7][4]==' ')return true;
				}
				if(dj>1)return false;
				else return true;
			case 'Q':
			case 'q':
				if(isSkewLine)
				{
					if(i0<i1)
					{
						if(j0<j1)
						{
							for(i=i0+1,j=j0+1;i<i1;i++,j++)if(chess[i][j]!=' ')return false;
						}
						else for(i=i0+1,j=j0-1;i<i1;i++,j--)if(chess[i][j]!=' ')return false;
					}
					else 
					{
						if(j0<j1)
						{
							for(i=i0-1,j=j0+1;i>i1;i--,j++)if(chess[i][j]!=' ')return false;
						}
						else for(i=i0-1,j=j0-1;i>i1;i--,j--)if(chess[i][j]!=' ')return false;
					}
					return true;
				}
				else if(isVHLine)
				{
					if(dj==0)
					{
						if(i0<i1)
						{
							for(i=i0+1;i<i1;i++)if(chess[i][j0]!=' ')return false;
						}
						else for(i=i0-1;i>i1;i--)if(chess[i][j0]!=' ')return false;
					}
					else 
					{
						if(j0<j1)
						{
							for(j=j0+1;j<j1;j++)if(chess[i0][j]!=' ')return false;
						}
						else for(j=j0-1;j>j1;j--)if(chess[i0][j]!=' ')return false;
					}
					return true;
				}
				else return false;
			case 'B':
			case 'b':
				if(isSkewLine)
				{
					if(i0<i1)
					{
						if(j0<j1)
						{
							for(i=i0+1,j=j0+1;i<i1;i++,j++)if(chess[i][j]!=' ')return false;
						}
						else for(i=i0+1,j=j0-1;i<i1;i++,j--)if(chess[i][j]!=' ')return false;
					}
					else 
					{
						if(j0<j1)
						{
							for(i=i0-1,j=j0+1;i>i1;i--,j++)if(chess[i][j]!=' ')return false;
						}
						else for(i=i0-1,j=j0-1;i>i1;i--,j--)if(chess[i][j]!=' ')return false;
					}
					return true;
				}
				else return false;
			case 'N':
			case 'n':
				if((di==2&&dj==1)||(di==1&&dj==2))return true;
				else return false;
			case 'R':
			case 'r':
				if(isVHLine)
				{
					if(dj==0)
					{
						if(i0<i1)
						{
							for(i=i0+1;i<i1;i++)if(chess[i][j0]!=' ')return false;
						}
						else for(i=i0-1;i>i1;i--)if(chess[i][j0]!=' ')return false;
					}
					else 
					{
						if(j0<j1)
						{
							for(j=j0+1;j<j1;j++)if(chess[i0][j]!=' ')return false;
						}
						else for(j=j0-1;j>j1;j--)if(chess[i0][j]!=' ')return false;
					}
					return true;
				}
				else return false;
			case 'P':
				if(di==0)return false;
				if(i1<i0||dj>1||di>2)return false;
				if(di==2&&(i0!=1||dj!=0||chess[i0+1][j0]!=' '))return false;
				if(dj==1&&chess[i1][j1]==' ')return false;
				if(dj==0&&chess[i1][j1]!=' ')return false;
				return true;
			case 'p':
				if(di==0)return false;
				if(i1>i0||dj>1||di>2)return false;
				if(di==2&&(i0!=6||dj!=0||chess[i0-1][j0]!=' '))return false;
				if(dj==1&&chess[i1][j1]==' ')return false;
				if(dj==0&&chess[i1][j1]!=' ')return false;
				return true;
			default:
				return false;
		}
	}
	private void moveFrom(int i0,int j0,int i1,int j1)
	{
		if(chess[i1][j1]=='K')
		{
			JOptionPane.showMessageDialog(null,"YELLOW KING LOSE!");
		}
		if(chess[i1][j1]=='k')
		{
			JOptionPane.showMessageDialog(null,"black king lose!");
		}
		if(chess[i0][j0]=='K')
		{
			if(Abs(j1-j0)>1)
			{
				if(j1==1)
				{
					chess[0][1]='K';
					chess[0][3]=' ';
					chess[0][2]='R';
					chess[0][0]=' ';
				}
				else if(j1==6)
				{
					chess[0][6]='K';
					chess[0][3]=' ';
					chess[0][5]='R';
					chess[0][7]=' ';
				}
				return;
			}
		}
		if(chess[i0][j0]=='k')
		{
			if(Abs(j1-j0)>1)
			{
				if(j1==1)
				{
					chess[7][1]='k';
					chess[7][3]=' ';
					chess[7][2]='r';
					chess[7][0]=' ';
				}
				else if(j1==6)
				{
					chess[7][6]='k';
					chess[7][3]=' ';
					chess[7][5]='r';
					chess[7][7]=' ';
				}
				return;
			}
		}		
		if(chess[i0][j0]=='P')
		{
			if(i1==7)
			{
				chess[i1][j1]='Q';	
				chess[i0][j0]=' ';
				return ;
			}
		}
		if(chess[i0][j0]=='p')
		{
			if(i1==0)
			{
				chess[i1][j1]='q';	
				chess[i0][j0]=' ';
				return ;
			}
		}
		chess[i1][j1]=chess[i0][j0];
		chess[i0][j0]=' ';
	}
	private int x0,y0,x1,y1;
	public void actionPerformed(ActionEvent e){}
	public void mousePressed(MouseEvent e){}
	int X0;
	int Y0;
	int X1;
	int Y1;
	boolean mouseIsClicked=false;
	boolean hasFinishedInput=false;
	public void mouseClicked(MouseEvent e)
	{
		if(!mouseIsClicked)
		{
			X0=(e.getX()-J)/80;
			Y0=(e.getY()-I)/80;
			if(chess[Y0][X0]<'a')return;
			mouseIsClicked=true;
			this.repaint();
		}
		else 
		{
			X1=(e.getX()-J)/80;
			Y1=(e.getY()-I)/80;
			hasFinishedInput=true;
			mouseIsClicked=false;
			this.repaint();
		}
	}
	private void getAndSetPositionForYellow()
	{
		if(canMoveYellow)
		{
			String ReceiveString=this.receive();
			while(ReceiveString==null)ReceiveString=this.receive();
			int y0=Integer.parseInt(ReceiveString.charAt(0)+"");
			int x0=Integer.parseInt(ReceiveString.charAt(1)+"");
			int y1=Integer.parseInt(ReceiveString.charAt(2)+"");
			int x1=Integer.parseInt(ReceiveString.charAt(3)+"");
			this.moveFrom(y0,x0,y1,x1);
			this.repaint();
			canMoveYellow=false;
		}
	}			
	private void getAndSetPositionForBlack()
	{
		if(hasFinishedInput&&canMoveFrom(Y0,X0,Y1,X1))
		{
			this.moveFrom(Y0,X0,Y1,X1);
			this.repaint();
			String SendString=Y0+""+X0+""+Y1+""+X1;
			this.send(SendString);
			canMoveYellow=true;
			hasFinishedInput=false;
			X0=Y0=X1=Y1=0;
			return;
		}
	}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	private void send(String s)
	{
		try
		{
			DatagramPacket DatagramPacket1=new DatagramPacket(s.getBytes(),s.length(),InetAddress.getByName("localhost"),8080);
			DatagramSocket DatagramSocket1=new DatagramSocket();
			DatagramSocket1.send(DatagramPacket1);
			DatagramSocket1.close();
		}
		catch(Exception e){}
	}
	private String receive()
	{
		String s=null;
		try
		{
			byte[] bytes=new byte[1024];
			DatagramPacket DatagramPacket1=new DatagramPacket(bytes,1024);
			DatagramSocket DatagramSocket1=new DatagramSocket(8080);
			DatagramSocket1.receive(DatagramPacket1);
			s=new String(bytes,0,DatagramPacket1.getLength());
			DatagramSocket1.close();
		}
		catch(Exception e){}
		return s;
	}
}