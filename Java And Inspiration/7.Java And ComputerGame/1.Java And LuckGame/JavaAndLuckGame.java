import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

public class JavaAndLuckGame
{
	public static void main(String[] args)
	{
		new LuckGame();
	}
}
class LuckGame extends Frame implements KeyListener, MouseListener, MouseMotionListener, ActionListener
{
	private int x0=50,y0=50,left=x0+50,top=y0+40,width=400,height=450;
	private Image backgroundImage;
	private Color color_light=Color.white;
	private Color color_dark=new Color(255,189,4);
	private Color color_white=new Color(1f,1f,1f,0.4f);
	private int row=11,column=10,luck=8;
	private int[][] grids=new int[row][column];
	private Image[] luckImages=new Image[luck];
	private int currentI=-1,currentJ=-1;
	private boolean gameOver=false;
	private int score=0;
	public LuckGame()
	{
		this.backgroundImage=Toolkit.getDefaultToolkit().getImage("background.jpg");
		for(int i=0;i<luck;i++)this.luckImages[i]=Toolkit.getDefaultToolkit().getImage("luck\\"+i+".gif");
		this.initLuckGame();
		this.setBounds(x0,y0,600,600);
		this.setResizable(false);
		this.setVisible(true);
		this.addTimer(this);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
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
		this.drawGrids(g,2);
		this.drawString(g,"Your score is:"+score,x0+20,y0+20,30);
		if(gameOver)this.drawString(g,"Game Over!",x0-30,top+250,100);
	}
	private void addTimer(ActionListener actionListener)
	{
		new Timer(100, actionListener).start();
	}
	private void initLuckGame()
	{
		this.score=0;
		this.gameOver=false;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				this.grids[i][j]=-(int)(luck*Math.random());
			}
		}
		System.out.println("Press enter key to restart...");
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
				if(currentI==i&&currentJ==j)
				{
					g.setColor(color_white);
					g.fillRect(left+j*w,top+i*h,w,h);
				}
				this.drawRect(g,left+j*w,top+i*h,w,h,size);
				int grid=grids[i][j];
				if(gameOver)grid=grid<0?-grid:grid;
				if(grid>0||gameOver)g.drawImage(luckImages[grid],left+j*w,top+i*h,this);
			}
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
		if(t.equals("Enter"))this.initLuckGame();
	}
	public void mouseMoved(MouseEvent e)
	{
		int i0=currentI,j0=currentJ;
		int x=e.getX(),y=e.getY();
		int h=height/row,w=width/column;
		this.currentJ=(x-left)/w;
		this.currentI=(y-top)/h;
	}
	public void mouseClicked(MouseEvent e)
	{
		if(gameOver)return;
		if(isValidPosition(currentI,currentJ))
		{
			this.addScore(currentI,currentJ);
		}
	}
	private void addScore(int i,int j)
	{
		int grid=grids[i][j];
		if(grid==0){gameOver=true;return;}
		else if(grid>0)return;
		this.grids[i][j]=-grid;
		this.score+=grids[i][j];
		if(isValidPosition(i,j+1)&&grid==grids[i][j+1])addScore(i,j+1);
		if(isValidPosition(i,j-1)&&grid==grids[i][j-1])addScore(i,j-1);
		if(isValidPosition(i+1,j)&&grid==grids[i+1][j])addScore(i+1,j);
		if(isValidPosition(i-1,j)&&grid==grids[i-1][j])addScore(i-1,j);
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
