import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndRegion
{
	public static void main(String[] args)
	{
		Frame_Region Frame_Region1=new Frame_Region();
		Frame_Region1.setVisible(true);
	}
}
class Frame_Region extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_Region()
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
			Image1=Toolkit.getDefaultToolkit().getImage("..\\JavaAndBinary.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			ImageOp1.region(Color.white,200);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		g.setColor(Color.red);
		if(Image1!=null)g.drawImage(Image1,100,100,this);
		if(Image2!=null)g.drawImage(Image2,600,100,this);
	}
}
class StringQueue
{
	private String stringQueue;
	public StringQueue()
	{
		this.stringQueue="";
	}
	public void enQueue(int i,int j)
	{
		this.stringQueue+=i+","+j+";";
	}
	public int[] deQueue()
	{
		int n=0;
		String i="",j="";
		char c=stringQueue.charAt(n++);
		while(c!=',')
		{
			i+=c;
			c=stringQueue.charAt(n++);
		}
		c=stringQueue.charAt(n++);
		while(c!=';')
		{
			j+=c;
			c=stringQueue.charAt(n++);
		}
		int[] ij=new int[2];
		ij[0]=Integer.parseInt(i);
		ij[1]=Integer.parseInt(j);
		this.stringQueue=stringQueue.substring(n,stringQueue.length());
		return ij;
	}
	public void show()
	{
		System.out.println(stringQueue);
	}
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
}
class ImageOp
{
	public int ImageWidth;
	public int ImageHeight;
	public int[] Pixels;
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
	private int[] getBinaryValues(int thresholdValue)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		int[] binaryValues=new int[ImageWidth*ImageHeight];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int red=ColorModel1.getRed(Pixels[i*ImageWidth+j]);
				int green=ColorModel1.getGreen(Pixels[i*ImageWidth+j]);
				int blue=ColorModel1.getBlue(Pixels[i*ImageWidth+j]);
				int grey=(red+green+blue)/3;
				if(grey<thresholdValue)binaryValues[i*ImageWidth+j]=1;
				else binaryValues[i*ImageWidth+j]=0;
			}
		}
		return binaryValues;
	}
	public void binary(Color foregroundColor,Color backgroundColor,int thresholdValue)
	{
		int[] binaryValues=this.getBinaryValues(thresholdValue);
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=255;
				int red=backgroundColor.getRed();
				int green=backgroundColor.getGreen();
				int blue=backgroundColor.getBlue();
				if(binaryValues[i*ImageWidth+j]==1)
				{
					red=foregroundColor.getRed();
					green=foregroundColor.getGreen();
					blue=foregroundColor.getBlue();
				}
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	private boolean isInImageRange(int i,int j)
	{
		return (0<=i&&i<ImageHeight&&0<=j&&j<ImageWidth);
	}
	private void floodFill(int i,int j,int label,int[] regionValues)
	{
		StringQueue q=new StringQueue();
		q.enQueue(i,j);
		while(q.isNotEmpty())
		{
			int[] ij=q.deQueue();
			int  I=ij[0],J=ij[1],I0=I-1,I1=I+1,J0=J-1,J1=J+1;
			if(isInImageRange(I,J0)&&regionValues[I*ImageWidth+J0]==1){q.enQueue(I,J0);regionValues[I*ImageWidth+J0]=label;}
			if(isInImageRange(I,J1)&&regionValues[I*ImageWidth+J1]==1){q.enQueue(I,J1);regionValues[I*ImageWidth+J1]=label;}
			if(isInImageRange(I0,J)&&regionValues[I0*ImageWidth+J]==1){q.enQueue(I0,J);regionValues[I0*ImageWidth+J]=label;}
			if(isInImageRange(I1,J)&&regionValues[I1*ImageWidth+J]==1){q.enQueue(I1,J);regionValues[I1*ImageWidth+J]=label;}
			if(isInImageRange(I0,J0)&&regionValues[I0*ImageWidth+J0]==1){q.enQueue(I0,J0);regionValues[I0*ImageWidth+J0]=label;}
			if(isInImageRange(I0,J1)&&regionValues[I0*ImageWidth+J1]==1){q.enQueue(I0,J1);regionValues[I0*ImageWidth+J1]=label;}
			if(isInImageRange(I1,J0)&&regionValues[I1*ImageWidth+J0]==1){q.enQueue(I1,J0);regionValues[I1*ImageWidth+J0]=label;}
			if(isInImageRange(I1,J1)&&regionValues[I1*ImageWidth+J1]==1){q.enQueue(I1,J1);regionValues[I1*ImageWidth+J1]=label;}
		}
	}
	private int labelTheRegions(int[] regionValues)
	{
		int label=2;
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				if(regionValues[i*ImageWidth+j]==1)
				{
					regionValues[i*ImageWidth+j]=label;
					this.floodFill(i,j,label,regionValues);
					label++;
				}
			}
		}
		return label;
	}
	public void region(Color backgroundColor,int thresholdValue)
	{
		int[] binaryValues=this.getBinaryValues(thresholdValue);
		int[] regionValues=new int[ImageWidth*ImageHeight];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				regionValues[i*ImageWidth+j]=binaryValues[i*ImageWidth+j];
			}
		}
		int label=this.labelTheRegions(regionValues);
		int[] pixels=new int[label];
		pixels[0]=(255<<24)|(backgroundColor.getRed()<<16)|(backgroundColor.getGreen()<<8)|backgroundColor.getBlue();
		for(int i=1;i<label;i++)
		{
			int alpha=255;
			int red=(int)(250*Math.random());
			int green=(int)(250*Math.random());
			int blue=(int)(250*Math.random());
			pixels[i]=(alpha<<24)|(red<<16)|(green<<8)|blue;
		}
		for(int i=0;i<ImageHeight;i++)for(int j=0;j<ImageWidth;j++)Pixels[i*ImageWidth+j]=pixels[regionValues[i*ImageWidth+j]];
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}