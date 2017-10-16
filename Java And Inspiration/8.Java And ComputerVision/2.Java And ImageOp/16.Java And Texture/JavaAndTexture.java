import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
public class JavaAndTexture
{
	public static void main(String[] args)
	{
		Frame_Texture Frame_Texture1=new Frame_Texture();
		Frame_Texture1.setVisible(true);
	}
}
class Frame_Texture extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight,i=0,j=0;
	public Frame_Texture()
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
			String textureImageName="Texture\\Texture1.JPG";
			this.imageWidth=800;
			this.imageHeight=400;
			ImageOp ImageOp1=new ImageOp(imageWidth,imageHeight);
			ImageOp1.generateTexture(textureImageName);
			Image1=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		if(Image1!=null)g.drawImage(Image1,100,100,this);
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
	public ImageOp(int imageWidth,int imageHeight)
	{
		this.ImageWidth=imageWidth;
		this.ImageHeight=imageHeight;
		this.Pixels=new int[imageWidth*imageHeight];
	}
	public void generateTexture(String textureImageName)
	{
		Image textureImage=null;
		try{textureImage=ImageIO.read(new File(textureImageName));}
		catch(Exception e){e.printStackTrace();}
		int textureWidth=textureImage.getWidth(null);
		int textureHeight=textureImage.getHeight(null);
		int size=min(textureWidth,textureHeight)/5,edge=size/3,length=size+2*edge;
		int maxWidth=textureWidth-length;
		int maxHeight=textureHeight-length;
		int[] texturePixels=new int[textureWidth*textureHeight];
		PixelGrabber PixelGrabber1=new PixelGrabber(textureImage,0,0,textureWidth,textureHeight,texturePixels,0,textureWidth);
		try{PixelGrabber1.grabPixels();}catch(Exception e){e.printStackTrace();}
		int count=100;
		this.generateTexturePixels(texturePixels,textureWidth,textureHeight,size,edge,length);
		for(int x0=length-edge;x0<ImageWidth-length;x0+=length-edge)
		{
			this.generateTexturePixels_x0(texturePixels,textureWidth,textureHeight,size,edge,length,count,x0);
		}
		for(int y0=length-edge;y0<ImageHeight-length;y0+=length-edge)
		{
			this.generateTexturePixels_y0(texturePixels,textureWidth,textureHeight,size,edge,length,count,y0);
		}
		for(int x0=length-edge;x0<ImageWidth-length;x0+=length-edge)
		{
			for(int y0=length-edge;y0<ImageHeight-length;y0+=length-edge)
			{
				this.generateTexturePixels(texturePixels,textureWidth,textureHeight,size,edge,length,count,x0,y0);
			}
		}

	}
	private void generateTexturePixels(int[] texturePixels,int textureWidth,int textureHeight,int size,int edge,int length)
	{
		int maxWidth=textureWidth-length;
		int maxHeight=textureHeight-length;
		int x=(int)(maxWidth*Math.random());
		int y=(int)(maxHeight*Math.random());
		for(int i=0;i<length;i++)for(int j=0;j<length;j++)this.Pixels[i*ImageWidth+j]=texturePixels[(y+i)*textureWidth+(x+j)];
	}
	private void generateTexturePixels_x0(int[] texturePixels,int textureWidth,int textureHeight,int size,int edge,int length,int count,int x0)
	{
		int y0=0;
		int maxWidth=textureWidth-length;
		int maxHeight=textureHeight-length;
		int[] rightEdgePixels=new int[length*edge];
		for(int i=0;i<length;i++)for(int j=0;j<edge;j++)rightEdgePixels[i*edge+j]=Pixels[(i+y0)*ImageWidth+(j+x0)];
		int matchX=0,matchY=0;
		int[] matchLeftEdge=new int[length];
		int minDistance=Integer.MAX_VALUE;
		for(int k=0;k<count;k++)
		{
			int x=(int)(maxWidth*Math.random());
			int y=(int)(maxHeight*Math.random());
			int newDistance=0;
			int[] leftEdge=new int[length];
			for(int i=0;i<length;i++)
			{
				int minD=Integer.MAX_VALUE;
				for(int j=0;j<edge;j++)
				{
					int p1=texturePixels[(y+i)*textureWidth+(x+j)];
					int p2=rightEdgePixels[i*edge+j];
					int d=distance(p1,p2);
					if(d<minD){minD=d;leftEdge[i]=j;}
					if(d==0)break;
				}
				newDistance+=minD;
			}
			if(newDistance<minDistance)
			{
				minDistance=newDistance;
				matchX=x;matchY=y;
				for(int i=0;i<length;i++)matchLeftEdge[i]=leftEdge[i];
			}
		}
		for(int i=0;i<length;i++)
		{
			if(y0+i>ImageHeight)break;
			for(int j=matchLeftEdge[i];j<length;j++)
			{
				if(x0+j>ImageWidth)break;
				this.Pixels[(y0+i)*ImageWidth+(x0+j)]=texturePixels[(matchY+i)*textureWidth+(matchX+j)];
			}
		}
	}
	private void generateTexturePixels_y0(int[] texturePixels,int textureWidth,int textureHeight,int size,int edge,int length,int count,int y0)
	{
		int x0=0;
		int maxWidth=textureWidth-length;
		int maxHeight=textureHeight-length;
		int[] bottomEdgePixels=new int[edge*length];
		for(int i=0;i<edge;i++)for(int j=0;j<length;j++)bottomEdgePixels[i*length+j]=Pixels[(i+y0)*ImageWidth+(j+x0)];
		int matchX=0,matchY=0;
		int[] matchTopEdge=new int[length];
		int minDistance=Integer.MAX_VALUE;
		for(int k=0;k<count;k++)
		{
			int x=(int)(maxWidth*Math.random());
			int y=(int)(maxHeight*Math.random());
			int newDistance=0;
			int[] topEdge=new int[length];
			for(int j=0;j<length;j++)
			{
				int minD=Integer.MAX_VALUE;
				for(int i=0;i<edge;i++)
				{
					int p1=texturePixels[(y+i)*textureWidth+(x+j)];
					int p2=bottomEdgePixels[i*length+j];
					int d=distance(p1,p2);
					if(d<minD){minD=d;topEdge[j]=i;}
					if(d==0)break;
				}
				newDistance+=minD;
			}
			if(newDistance<minDistance)
			{
				minDistance=newDistance;
				matchX=x;matchY=y;
				for(int j=0;j<length;j++)matchTopEdge[j]=topEdge[j];
			}
		}
		for(int j=0;j<length;j++)
		{
			if(x0+j>ImageWidth)break;
			for(int i=matchTopEdge[j];i<length;i++)
			{
				if(y0+i>ImageHeight)break;
				this.Pixels[(y0+i)*ImageWidth+(x0+j)]=texturePixels[(matchY+i)*textureWidth+(matchX+j)];
			}
		}
	}
	private void generateTexturePixels(int[] texturePixels,int textureWidth,int textureHeight,int size,int edge,int length,int count,int x0,int y0)
	{
		int maxWidth=textureWidth-length;
		int maxHeight=textureHeight-length;
		int[] rightEdgePixels=new int[length*edge];
		for(int i=0;i<length;i++)for(int j=0;j<edge;j++)rightEdgePixels[i*edge+j]=Pixels[(i+y0)*ImageWidth+(j+x0)];
		int[] bottomEdgePixels=new int[edge*length];
		for(int i=0;i<edge;i++)for(int j=0;j<length;j++)bottomEdgePixels[i*length+j]=Pixels[(i+y0)*ImageWidth+(j+x0)];
		int matchX=0,matchY=0;
		int[] matchLeftEdge=new int[length];
		int[] matchTopEdge=new int[length];
		int minDistance=Integer.MAX_VALUE;
		for(int k=0;k<count;k++)
		{
			int x=(int)(maxWidth*Math.random());
			int y=(int)(maxHeight*Math.random());
			int newDistance=0;
			int[] leftEdge=new int[length];
			for(int i=0;i<length;i++)
			{
				int minD=Integer.MAX_VALUE;
				for(int j=0;j<edge;j++)
				{
					int p1=texturePixels[(y+i)*textureWidth+(x+j)];
					int p2=rightEdgePixels[i*edge+j];
					int d=distance(p1,p2);
					if(d<minD){minD=d;leftEdge[i]=j;}
					if(d==0)break;
				}
				newDistance+=minD;
			}
			int[] topEdge=new int[length];
			for(int j=0;j<length;j++)
			{
				int minD=Integer.MAX_VALUE;
				for(int i=0;i<edge;i++)
				{
					int p1=texturePixels[(y+i)*textureWidth+(x+j)];
					int p2=bottomEdgePixels[i*length+j];
					int d=distance(p1,p2);
					if(d<minD){minD=d;topEdge[j]=i;}
					if(d==0)break;
				}
				newDistance+=minD;
			}
			if(newDistance<minDistance)
			{
				minDistance=newDistance;
				matchX=x;matchY=y;
				for(int i=0;i<length;i++)matchLeftEdge[i]=leftEdge[i];
				for(int j=0;j<length;j++)matchTopEdge[j]=topEdge[j];
			}
		}
		for(int i=0;i<length;i++)
		{
			if(y0+i>ImageHeight)break;
			for(int j=matchLeftEdge[i];j<length;j++)
			{
				if(x0+j>ImageWidth)break;
				if(i>=matchTopEdge[j])
				{
					this.Pixels[(y0+i)*ImageWidth+(x0+j)]=texturePixels[(matchY+i)*textureWidth+(matchX+j)];
				}
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
	private int min(int a,int b)
	{
		return (a<=b?a:b);
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}
