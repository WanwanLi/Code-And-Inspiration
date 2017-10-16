import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndJHandWritingReader
{
	public static void main(String[] args)
	{
		JHandWritingReader JHandWritingReader1=new JHandWritingReader();
		JHandWritingReader1.setVisible(true);
	}
}
class JHandWritingReader extends JFrame implements MouseListener,MouseMotionListener,ActionListener
{
	private Timer timer;
	private Image image,image1;
	private int imageWidth,imageHeight,imageX,imageY;
	private Graphics g;
	private int counter;
	private boolean[][] characterTemplateSet;
	private int characterWidth=10,characterHeight=20,minASCII=33,maxASCII=127;
	private boolean isTiming;
	private TextArea textArea;
	public JHandWritingReader()
	{
		this.timer=new Timer(1000,this);
		this.timer.start();
		this.counter=0;
		this.imageWidth=500;
		this.imageHeight=500;
		this.imageX=200;
		this.imageY=100;
		this.textArea=new TextArea();
		this.textArea.setBounds(imageX+imageWidth+30,imageY,imageWidth,imageHeight);
		this.setLayout(null);
		this.add(textArea);
		this.textArea.setFont(new Font(null,Font.BOLD,100));
		this.image=new BufferedImage(imageWidth,imageHeight,2);
		this.g=image.getGraphics();
		this.g.setColor(Color.white);
		this.g.fillRect(0,0,imageWidth,imageHeight);
		this.characterTemplateSet=this.getASCIICharacterTemplateSet();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	private boolean[][] getASCIICharacterTemplateSet()
	{
		boolean[][] ASCIICharacterTemplateSet=new boolean[maxASCII-minASCII][];
		for(int i=minASCII;i<maxASCII;i++)
		{
			Image image=new BufferedImage(300,300,2);
			Graphics g=image.getGraphics();
			g.setColor(Color.black);
			g.setFont(new Font(null,Font.BOLD,300));
			g.drawString(""+(char)i,0,239);
			g.drawString(""+(char)i,1,239);
			ASCIICharacterTemplateSet[i-minASCII]=this.getTemplate(image,characterWidth,characterHeight);
		}
		return ASCIICharacterTemplateSet;
	}
	private boolean[] getTemplate(Image image,int templateWidth,int templateHeight)
	{
		int width=image.getWidth(this),height=image.getHeight(this);
		int[] pixels=new int[width*height];
		PixelGrabber pixelGrabber=new PixelGrabber(image,0,0,width,height,pixels,0,width);
		try{pixelGrabber.grabPixels();}catch(Exception e){e.printStackTrace();}
		int minI=Integer.MAX_VALUE;
		int maxI=Integer.MIN_VALUE;
		int minJ=Integer.MAX_VALUE;
		int maxJ=Integer.MIN_VALUE;
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if(pixels[i*width+j]==(255<<24))
				{
					if(i<minI)minI=i;
					if(j<minJ)minJ=j;
					if(i>maxI)maxI=i;
					if(j>maxJ)maxJ=j;
				}
			}
		}
		int h=templateHeight,w=templateWidth;
		double m=(maxI-minI+0.0)/h;
		double n=(maxJ-minJ+0.0)/w;
		boolean[] template=new boolean[h*w];
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				int i1=(int)(minI+i*m+0.5);
				int j1=(int)(minJ+j*n+0.5);
				if(i1>=height||j1>=width)continue;
				if(pixels[i1*width+j1]==(255<<24))template[i*w+j]=true;
				else template[i*w+j]=false;
			}
		}
		return template;
	}
	private int getIndex(boolean[] templateSample,boolean[][] templateSet)
	{
		int index=0;
		int minDistance=Integer.MAX_VALUE;
		int l=templateSet.length;
		for(int i=0;i<l;i++)
		{
			int distance=this.getDistance(templateSample,templateSet[i]);
			if(distance<minDistance)
			{
				minDistance=distance;
				index=i;
			}
		}
		return index;
	}
	private int getDistance(boolean[] template1,boolean[] template2)
	{
		int distance=0;
		int l=template1.length;
		for(int i=0;i<l;i++)
		{
			if(template1[i]!=template2[i])distance++;
			else distance--;
		}
		return distance;
	}
	public void paint(Graphics g)
	{
		g.drawRect(imageX,imageY,imageWidth,imageHeight);
		g.drawImage(image,imageX,imageY,this);
	}
	public void actionPerformed(ActionEvent e)	
	{
		if(!isTiming)return;
		this.counter++;
		if(counter>1)
		{
			int index=this.getIndex(this.getTemplate(image,characterWidth,characterHeight),characterTemplateSet);
			this.textArea.setText(textArea.getText()+(char)(index+minASCII));
			g.setColor(Color.white);
			g.fillRect(0,0,imageWidth,imageHeight);
			this.repaint();
			this.isTiming=false;
			this.counter=0;
		}
	}
	public void mouseClicked(MouseEvent e)
	{
		this.image.flush();
		this.repaint();
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mousePressed(MouseEvent e)
	{
		this.counter=0;
		isTiming=false;
	}
	public void mouseDragged(MouseEvent e)
	{
		int Y=e.getY();
		int X=e.getX();
		g.setColor(Color.black);
		g.fillRect(X-imageX,Y-imageY,20,20);
		this.repaint();
	}
	public void mouseReleased(MouseEvent e)
	{
		isTiming=true;
	}
}