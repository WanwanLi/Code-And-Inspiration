import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
public class JavaAndTextureAnalyzer
{
	public static void main(String[] args)
	{
		Frame_TextureAnalyzer Frame_TextureAnalyzer1=new Frame_TextureAnalyzer();
		Frame_TextureAnalyzer1.setVisible(true);
	}
}
class Frame_TextureAnalyzer extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_TextureAnalyzer()
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
			String dir="..\\Texture\\";
			String imageName=dir+"Texture_Mixture2.JPG";
			try{this.Image1=ImageIO.read(new File(imageName));}
			catch(Exception e){e.printStackTrace();}
			String[] textureImageNames=new String[]
			{
				dir+"Texture1.JPG",
				dir+"Texture2.JPG",
				dir+"Texture3.JPG",
				dir+"Texture4.JPG",
				dir+"Texture5.JPG"
			};
			TextureAnalyzer TextureAnalyzer1=new TextureAnalyzer(textureImageNames);
			int[] result=TextureAnalyzer1.analyze(imageName);
			this.imageWidth=result[result.length-2];
			this.imageHeight=result[result.length-1];
			ImageOp ImageOp1=new ImageOp(result,imageWidth,imageHeight);
			Color[] colors=new Color[]
			{
				new Color(234,196,147),
				Color.gray,
				new Color(92,29,0),
				Color.red,
				Color.blue
			};
			ImageOp1.map(colors);
			this.Image2=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		if(Image1!=null)g.drawImage(Image1,100,100,this);
		if(Image2!=null)g.drawImage(Image2,700,100,this);
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
	public void map(Color[] colors)
	{
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int index=Pixels[i*ImageWidth+j];
				int alpha=255;
				int red=colors[index].getRed();
				int green=colors[index].getGreen();
				int blue=colors[index].getBlue();
				this.Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}
class TextureAnalyzer
{
	int[][] texturePixels;
	private int size=10,K=20;
	public TextureAnalyzer(String[] textureImageNames)
	{
		int l=textureImageNames.length;
		this.texturePixels=new int[l][];
		int k=0;
		for(int i=0;i<l;i++)
		{
			Image image=null;
			try{image=ImageIO.read(new File(textureImageNames[i]));}
			catch(Exception e){e.printStackTrace();}
			int width=image.getWidth(null);
			int height=image.getHeight(null);
			this.texturePixels[i]=new int[width*height+2];
			PixelGrabber PixelGrabber1=new PixelGrabber(image,0,0,width,height,texturePixels[i],0,width);
			try{PixelGrabber1.grabPixels();}catch(Exception e){e.printStackTrace();}
			this.texturePixels[i][width*height+0]=width;
			this.texturePixels[i][width*height+1]=height;
		}
	}
	public int[] analyze(String imageName)
	{
		Image image=null;
		try{image=ImageIO.read(new File(imageName));}
		catch(Exception e){e.printStackTrace();}
		int width=image.getWidth(null);
		int height=image.getHeight(null);
		int maxWidth=width-size;
		int maxHeight=height-size;
		int[] pixels=new int[width*height];
		int[] result=new int[width*height+2];
		PixelGrabber PixelGrabber1=new PixelGrabber(image,0,0,width,height,pixels,0,width);
		try{PixelGrabber1.grabPixels();}catch(Exception e){e.printStackTrace();}
		for(int i=0;i<height;i+=size)
		{
			for(int j=0;j<width;j+=size)
			{
				int index=getMinDistanceIndex(pixels,width,height,i,j);
				this.setResult(result,width,height,i,j,index);
			}
		}
		result[width*height]=width;
		result[width*height+1]=height;
		return result;
	}
	private int getMinDistanceIndex(int[] pixels,int width,int height,int i0,int j0)
	{
		int index=0,minD=Integer.MAX_VALUE;
		for(int n=0;n<texturePixels.length;n++)
		{
			int textureWidth=texturePixels[n][texturePixels[n].length-2];
			int textureHeight=texturePixels[n][texturePixels[n].length-1];
			int maxWidth=textureWidth-size;
			int maxHeight=textureHeight-size;
			for(int k=0;k<K;k++)
			{
				int d=0;
				int x=(int)(maxWidth*Math.random());
				int y=(int)(maxHeight*Math.random());
				for(int i=0;i<size;i++)
				{
					if(i0+i>=height)break;
					for(int j=0;j<size;j++)
					{
						if(j0+j>=width)break;
						int p1=pixels[(i0+i)*width+(j0+j)];
						int p2=texturePixels[n][(y+i)*textureWidth+(x+j)];
						d+=distance(p1,p2);
					}
				}
				if(d<minD){minD=d;index=n;}
			}
		}
		return index;
	}
	private void setResult(int[] result,int width,int height,int i0,int j0,int index)
	{
		for(int i=0;i<size;i++)
		{
			if(i0+i>=height)break;
			for(int j=0;j<size;j++)
			{
				if(j0+j>=width)break;
				result[(i0+i)*width+(j0+j)]=index;
			}
		}
	}
	private int distance(int pixel1,int pixel2)
	{
		if(pixel1==pixel2)return 0;
		int r1=pixel1>>16;
		int g1=pixel1>>8;
		int b1=pixel1>>0;
		int r2=pixel2>>16;
		int g2=pixel2>>8;
		int b2=pixel2>>0;
		return Math.abs(r2-r1)+Math.abs(g2-g1)+Math.abs(b2-b1);
	}
}