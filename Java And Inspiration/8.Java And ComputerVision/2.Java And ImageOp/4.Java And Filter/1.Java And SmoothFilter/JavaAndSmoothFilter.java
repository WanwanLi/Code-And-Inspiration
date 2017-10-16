import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndSmoothFilter
{
	public static void main(String[] args)
	{
		Frame_SmoothFilter Frame_SmoothFilter1=new Frame_SmoothFilter();
		Frame_SmoothFilter1.setVisible(true);
	}
}
class Frame_SmoothFilter extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_SmoothFilter()
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
			Image1=Toolkit.getDefaultToolkit().getImage("..\\..\\..\\JavaAndImageProcessing.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			double[] smoothFilter=new double[]
			{
				0.075,0.125,0.075,
				0.125,0.200,0.125,
				0.075,0.125,0.075
			};
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			ImageOp1.filter(smoothFilter);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		if(Image1!=null)g.drawImage(Image1,100,100,this);
		if(Image2!=null)g.drawImage(Image2,100+imageWidth+100,100,this);
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
	public void filter(double[] Filter)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		int[] pixels=new int[ImageWidth*ImageHeight];
		for(int i=1;i<ImageHeight-1;i++)
		{
			for(int j=1;j<ImageWidth-1;j++)
			{
				double alpha=this.getFilterAlpha(ColorModel1,Pixels,Filter,i,j);
				double red=this.getFilterRed(ColorModel1,Pixels,Filter,i,j);
				double green=this.getFilterGreen(ColorModel1,Pixels,Filter,i,j);
				double blue=this.getFilterBlue(ColorModel1,Pixels,Filter,i,j);
				pixels[i*ImageWidth+j]=this.getARGBvalue(alpha,red,green,blue);
			}
		}
		this.Pixels=pixels;
	}
	public double getFilterAlpha(ColorModel colorModel,int[] pixels,double[] filter,int i,int j)
	{
		int alpha__=colorModel.getAlpha(pixels[i*ImageWidth+j]);
		int alpha_0=colorModel.getAlpha(pixels[i*ImageWidth+(j-1)]);
		int alpha_1=colorModel.getAlpha(pixels[i*ImageWidth+(j+1)]);
		int alpha0_=colorModel.getAlpha(pixels[(i-1)*ImageWidth+j]);
		int alpha1_=colorModel.getAlpha(pixels[(i+1)*ImageWidth+j]);
		int alpha00=colorModel.getAlpha(pixels[(i-1)*ImageWidth+(j-1)]);
		int alpha01=colorModel.getAlpha(pixels[(i-1)*ImageWidth+(j+1)]);
		int alpha10=colorModel.getAlpha(pixels[(i+1)*ImageWidth+(j-1)]);
		int alpha11=colorModel.getAlpha(pixels[(i+1)*ImageWidth+(j+1)]);
		double filterAlpha=alpha00*filter[0]+alpha0_*filter[1]+alpha01*filter[2]+alpha_0*filter[3]+alpha__*filter[4]+alpha_1*filter[5]+alpha10*filter[6]+alpha1_*filter[7]+alpha11*filter[8];
		return filterAlpha;
	}
	public double getFilterRed(ColorModel colorModel,int[] pixels,double[] filter,int i,int j)
	{
		int red__=colorModel.getRed(pixels[i*ImageWidth+j]);
		int red_0=colorModel.getRed(pixels[i*ImageWidth+(j-1)]);
		int red_1=colorModel.getRed(pixels[i*ImageWidth+(j+1)]);
		int red0_=colorModel.getRed(pixels[(i-1)*ImageWidth+j]);
		int red1_=colorModel.getRed(pixels[(i+1)*ImageWidth+j]);
		int red00=colorModel.getRed(pixels[(i-1)*ImageWidth+(j-1)]);
		int red01=colorModel.getRed(pixels[(i-1)*ImageWidth+(j+1)]);
		int red10=colorModel.getRed(pixels[(i+1)*ImageWidth+(j-1)]);
		int red11=colorModel.getRed(pixels[(i+1)*ImageWidth+(j+1)]);
		double filterRed=red00*filter[0]+red0_*filter[1]+red01*filter[2]+red_0*filter[3]+red__*filter[4]+red_1*filter[5]+red10*filter[6]+red1_*filter[7]+red11*filter[8];
		return filterRed;
	}
	public double getFilterGreen(ColorModel colorModel,int[] pixels,double[] filter,int i,int j)
	{
		int green__=colorModel.getGreen(pixels[i*ImageWidth+j]);
		int green_0=colorModel.getGreen(pixels[i*ImageWidth+(j-1)]);
		int green_1=colorModel.getGreen(pixels[i*ImageWidth+(j+1)]);
		int green0_=colorModel.getGreen(pixels[(i-1)*ImageWidth+j]);
		int green1_=colorModel.getGreen(pixels[(i+1)*ImageWidth+j]);
		int green00=colorModel.getGreen(pixels[(i-1)*ImageWidth+(j-1)]);
		int green01=colorModel.getGreen(pixels[(i-1)*ImageWidth+(j+1)]);
		int green10=colorModel.getGreen(pixels[(i+1)*ImageWidth+(j-1)]);
		int green11=colorModel.getGreen(pixels[(i+1)*ImageWidth+(j+1)]);
		double filterGreen=green00*filter[0]+green0_*filter[1]+green01*filter[2]+green_0*filter[3]+green__*filter[4]+green_1*filter[5]+green10*filter[6]+green1_*filter[7]+green11*filter[8];
		return filterGreen;
	}
	public double getFilterBlue(ColorModel colorModel,int[] pixels,double[] filter,int i,int j)
	{
		int blue__=colorModel.getBlue(pixels[i*ImageWidth+j]);
		int blue_0=colorModel.getBlue(pixels[i*ImageWidth+(j-1)]);
		int blue_1=colorModel.getBlue(pixels[i*ImageWidth+(j+1)]);
		int blue0_=colorModel.getBlue(pixels[(i-1)*ImageWidth+j]);
		int blue1_=colorModel.getBlue(pixels[(i+1)*ImageWidth+j]);
		int blue00=colorModel.getBlue(pixels[(i-1)*ImageWidth+(j-1)]);
		int blue01=colorModel.getBlue(pixels[(i-1)*ImageWidth+(j+1)]);
		int blue10=colorModel.getBlue(pixels[(i+1)*ImageWidth+(j-1)]);
		int blue11=colorModel.getBlue(pixels[(i+1)*ImageWidth+(j+1)]);
		double filterBlue=blue00*filter[0]+blue0_*filter[1]+blue01*filter[2]+blue_0*filter[3]+blue__*filter[4]+blue_1*filter[5]+blue10*filter[6]+blue1_*filter[7]+blue11*filter[8];
		return filterBlue;
	}
	public void smooth()
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		int[] pixels=new int[ImageWidth*ImageHeight];
		for(int i=1;i<ImageHeight-1;i++)
		{
			for(int j=1;j<ImageWidth-1;j++)
			{
				double alpha=this.getAverageAlpha(ColorModel1,Pixels,i,j);
				double red=this.getAverageRed(ColorModel1,Pixels,i,j);
				double green=this.getAverageGreen(ColorModel1,Pixels,i,j);
				double blue=this.getAverageBlue(ColorModel1,Pixels,i,j);
				pixels[i*ImageWidth+j]=this.getARGBvalue(alpha,red,green,blue);
			}
		}
		this.Pixels=pixels;
	}
	public double getAverageAlpha(ColorModel colorModel,int[] pixels,int i,int j)
	{
		int alpha=colorModel.getAlpha(pixels[i*ImageWidth+j]);
		int alpha_0=colorModel.getAlpha(pixels[i*ImageWidth+(j-1)]);
		int alpha_1=colorModel.getAlpha(pixels[i*ImageWidth+(j+1)]);
		int alpha0_=colorModel.getAlpha(pixels[(i-1)*ImageWidth+j]);
		int alpha1_=colorModel.getAlpha(pixels[(i+1)*ImageWidth+j]);
		int alpha00=colorModel.getAlpha(pixels[(i-1)*ImageWidth+(j-1)]);
		int alpha01=colorModel.getAlpha(pixels[(i-1)*ImageWidth+(j+1)]);
		int alpha10=colorModel.getAlpha(pixels[(i+1)*ImageWidth+(j-1)]);
		int alpha11=colorModel.getAlpha(pixels[(i+1)*ImageWidth+(j+1)]);
		double averageAlpha=(alpha+alpha_0+alpha_1+alpha0_+alpha1_+alpha00+alpha01+alpha10+alpha11)/9;
		return averageAlpha;
	}
	public double getAverageRed(ColorModel colorModel,int[] pixels,int i,int j)
	{
		int red=colorModel.getRed(pixels[i*ImageWidth+j]);
		int red_0=colorModel.getRed(pixels[i*ImageWidth+(j-1)]);
		int red_1=colorModel.getRed(pixels[i*ImageWidth+(j+1)]);
		int red0_=colorModel.getRed(pixels[(i-1)*ImageWidth+j]);
		int red1_=colorModel.getRed(pixels[(i+1)*ImageWidth+j]);
		int red00=colorModel.getRed(pixels[(i-1)*ImageWidth+(j-1)]);
		int red01=colorModel.getRed(pixels[(i-1)*ImageWidth+(j+1)]);
		int red10=colorModel.getRed(pixels[(i+1)*ImageWidth+(j-1)]);
		int red11=colorModel.getRed(pixels[(i+1)*ImageWidth+(j+1)]);
		double averageRed=(red+red_0+red_1+red0_+red1_+red00+red01+red10+red11)/9;
		return averageRed;
	}
	public double getAverageGreen(ColorModel colorModel,int[] pixels,int i,int j)
	{
		int green=colorModel.getGreen(pixels[i*ImageWidth+j]);
		int green_0=colorModel.getGreen(pixels[i*ImageWidth+(j-1)]);
		int green_1=colorModel.getGreen(pixels[i*ImageWidth+(j+1)]);
		int green0_=colorModel.getGreen(pixels[(i-1)*ImageWidth+j]);
		int green1_=colorModel.getGreen(pixels[(i+1)*ImageWidth+j]);
		int green00=colorModel.getGreen(pixels[(i-1)*ImageWidth+(j-1)]);
		int green01=colorModel.getGreen(pixels[(i-1)*ImageWidth+(j+1)]);
		int green10=colorModel.getGreen(pixels[(i+1)*ImageWidth+(j-1)]);
		int green11=colorModel.getGreen(pixels[(i+1)*ImageWidth+(j+1)]);
		double averageGreen=(green+green_0+green_1+green0_+green1_+green00+green01+green10+green11)/9;
		return averageGreen;
	}
	public double getAverageBlue(ColorModel colorModel,int[] pixels,int i,int j)
	{
		int blue=colorModel.getBlue(pixels[i*ImageWidth+j]);
		int blue_0=colorModel.getBlue(pixels[i*ImageWidth+(j-1)]);
		int blue_1=colorModel.getBlue(pixels[i*ImageWidth+(j+1)]);
		int blue0_=colorModel.getBlue(pixels[(i-1)*ImageWidth+j]);
		int blue1_=colorModel.getBlue(pixels[(i+1)*ImageWidth+j]);
		int blue00=colorModel.getBlue(pixels[(i-1)*ImageWidth+(j-1)]);
		int blue01=colorModel.getBlue(pixels[(i-1)*ImageWidth+(j+1)]);
		int blue10=colorModel.getBlue(pixels[(i+1)*ImageWidth+(j-1)]);
		int blue11=colorModel.getBlue(pixels[(i+1)*ImageWidth+(j+1)]);
		double averageBlue=(blue+blue_0+blue_1+blue0_+blue1_+blue00+blue01+blue10+blue11)/9;
		return averageBlue;
	}
	private int getARGBvalue(double alpha,double red,double green,double blue)
	{
		if(alpha<0)alpha=0;
		if(red<0)red=0;
		if(green<0)green=0;
		if(blue<0)blue=0;
		if(alpha>255)alpha=255;
		if(red>255)red=255;
		if(green>255)green=255;
		if(blue>255)blue=255;
		return ((int)alpha<<24)|((int)red<<16)|((int)green<<8)|(int)blue;
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}