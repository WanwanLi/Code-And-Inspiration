import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndJHandWritingReader
{
	public static void main(String[] args)
	{
		JHandWritingReader JHandWritingReader1=new JHandWritingReader();
		JHandWritingReader.setVisible(true);
	}
}
class JHandWritingReader extends JFrame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_HandWriting()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		try
		{
/*
			Image1=Toolkit.getDefaultToolkit().getImage("..\\..\\JavaAndImageProcessing.jpg");
			Image2=Toolkit.getDefaultToolkit().getImage("JavaAndHandWriting.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image2,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image2.getWidth(this);
			imageHeight=Image2.getHeight(this);
			pixels=new int[imageWidth*imageHeight];
			PixelGrabber1=new PixelGrabber(Image2,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			int[] compareResults=ImageOp1.compare(pixels,imageWidth,imageHeight);
			i=compareResults[0];
			j=compareResults[1];
			System.out.println("MinMatchingDistance("+i+","+j+")="+compareResults[2]);*/
			this.getASCIICharacterTemplate();
		}
		catch(Exception e){e.printStackTrace();}
	}
	Image image,image1;
	private boolean[][] getASCIICharacterTemplate()
	{
		int minASCII=33,maxASCII=127;
		int characterWidth=10,characterHeight=20;
int h=characterHeight,w=characterWidth;
		boolean[][] ASCIICharacterTemplate=new boolean[maxASCII-minASCII][];
		for(int i=minASCII;i<maxASCII;i++)
		{
			this.image=new BufferedImage(300,300,2);
			Graphics g=image.getGraphics();
			g.setColor(Color.black);
			g.setFont(new Font(null,Font.BOLD,300));
			g.drawString(""+(char)i,0,250);
System.out.println((char)i);
			ASCIICharacterTemplate[i-minASCII]=this.getTemplate(image,characterWidth,characterHeight);

		}
		int width=800,height=700;
		this.image1=new BufferedImage(width,height,2);
		Graphics g=image1.getGraphics();
		g.setColor(Color.blue);
for(int k=0;k<maxASCII-minASCII;k++)
{
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				int x=j+(k%20)*30,y=i+k/20*30;
				if(ASCIICharacterTemplate[k][i*w+j])g.drawLine(x,y,x,y);
			}
		}			
}
		return ASCIICharacterTemplate;
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
				if(pixels[i1*width+j1]==(255<<24))template[i*w+j]=true;
				else template[i*w+j]=false;
			}
		}
		return template;
	}
	public void paint(Graphics g)
	{
		g.setColor(Color.red);/*
		if(Image1!=null)g.drawImage(Image1,100,100,this);
		if(Image1!=null)g.drawRect(j+100,i+100,imageWidth,imageHeight);
		if(Image2!=null)g.drawImage(Image2,600,100,this);*/
		g.drawImage(image,600,100,this);
		g.drawImage(image1,0,100,this);
	}
}
class ImageOp
{
	public int ImageWidth;
	public int ImageHeight;
	public int[] Pixels;
	public static final String StandardCharacterImageLib="Standard Character Image Lib";
	public ImageOp(int[] pixels,int imageWidth,int imageHeight)
	{
		this.ImageWidth=imageWidth;
		this.ImageHeight=imageHeight;
		this.Pixels=new int[imageWidth*imageHeight];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				this.Pixels[i*ImageWidth+j]=pixels[i*ImageWidth+j];
			}
		}
	}
	public char toChar(int[] pixels)
	{
		return 'c';
	}
	public int[] compare(int[] pixels,int imageWidth,int imageHeight)
	{
		int[] compareResults=new int[3];
		int[] GreyValues=this.getGreyValues(Pixels,ImageWidth,ImageHeight);
		int[] greyValues=this.getGreyValues(pixels,imageWidth,imageHeight);
		int minMatchingDistance=11235813;
		if(ImageWidth>imageWidth&&ImageHeight>imageHeight)
		{
			for(int i=0;i<=ImageHeight-imageHeight;i++)
			{
				for(int j=0;j<=ImageWidth-imageWidth;j++)
				{
					int matchingDistance=this.getMatchingDistance(GreyValues,i,j,greyValues,imageWidth,imageHeight);
					if(matchingDistance<minMatchingDistance)
					{
						compareResults[0]=i;
						compareResults[1]=j;
						minMatchingDistance=matchingDistance;
					}
				}
			}
		}
		compareResults[2]=minMatchingDistance;
		return compareResults;
	}
	private int getMatchingDistance(int[] GreyValues,int i0,int j0,int[] greyValues,int imageWidth,int imageHeight)
	{
		int matchingDistance=0;
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				int greyDifference=GreyValues[(i+i0)*ImageWidth+(j+j0)]-greyValues[i*imageWidth+j];
				matchingDistance+=greyDifference*greyDifference;
			}
		}
		return matchingDistance;
	}
	private int[] getGreyValues(int[] pixels,int imageWidth,int imageHeight)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		int[] greyValues=new int[imageWidth*imageHeight];
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				int red=ColorModel1.getRed(pixels[i*imageWidth+j]);
				int green=ColorModel1.getGreen(pixels[i*imageWidth+j]);
				int blue=ColorModel1.getBlue(pixels[i*imageWidth+j]);
				int grey=(red+green+blue)/3;
				greyValues[i*imageWidth+j]=grey;
			}
		}
		return greyValues;
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}