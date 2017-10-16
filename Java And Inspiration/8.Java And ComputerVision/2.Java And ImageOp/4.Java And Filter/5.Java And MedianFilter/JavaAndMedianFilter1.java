import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndMedianFilter
{
	public static void main(String[] args)
	{
		Frame_MedianFilter Frame_MedianFilter1=new Frame_MedianFilter();
		Frame_MedianFilter1.setVisible(true);
	}
}
class Frame_MedianFilter extends Frame
{
	public Image Image1,Image2;
	private int imageWidth,imageHeight;
	public Frame_MedianFilter()
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
			Image1=Toolkit.getDefaultToolkit().getImage("..\\..\\JavaAndImageProcessing.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] Pixels=new int[imageWidth*imageHeight];
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,Pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			ColorModel ColorModel1=ColorModel.getRGBdefault();
			for(int i=1;i<imageHeight-1;i++)
			{
				for(int j=1;j<imageWidth-1;j++)
				{
					int alpha=ColorModel1.getAlpha(Pixels[i*imageWidth+j]);
					int red=this.getMedianRed(ColorModel1,Pixels,i,j);
					int green=this.getMedianGreen(ColorModel1,Pixels,i,j);
					int blue=this.getMedianBlue(ColorModel1,Pixels,i,j);
					pixels[i*imageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
				}
			}
			MemoryImageSource MemoryImageSource1=new MemoryImageSource(imageWidth,imageHeight,pixels,0,imageWidth);
			Image2=this.createImage(MemoryImageSource1);
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		if(Image1!=null)g.drawImage(Image1,100,100,this);
		if(Image2!=null)g.drawImage(Image2,100+imageWidth+100,100,this);
	}
	private int Median(int y0,int y1,int y2,int y3,int y4,int y5,int y6,int y7,int y8)
	{
		int[] y=new int[]{y0,y1,y2,y3,y4,y5,y6,y7,y8};
		for(int i=0;i<8;i++)
		{
			int k=i,min=y[i];
			for(int j=i+1;j<9;j++)if(y[j]<min){k=j;min=y[j];}
			if(k!=i)
			{
				int yi=y[i];
				y[i]=y[k];
				y[k]=yi;
			}
		}
		return y[4];
		
	}
	public int getMedianRed(ColorModel colorModel,int[] pixels,int i,int j)
	{
		int red=colorModel.getRed(pixels[i*imageWidth+j]);
		int red_0=colorModel.getRed(pixels[i*imageWidth+(j-1)]);
		int red_1=colorModel.getRed(pixels[i*imageWidth+(j+1)]);
		int red0_=colorModel.getRed(pixels[(i-1)*imageWidth+j]);
		int red1_=colorModel.getRed(pixels[(i+1)*imageWidth+j]);
		int red00=colorModel.getRed(pixels[(i-1)*imageWidth+(j-1)]);
		int red01=colorModel.getRed(pixels[(i-1)*imageWidth+(j+1)]);
		int red10=colorModel.getRed(pixels[(i+1)*imageWidth+(j-1)]);
		int red11=colorModel.getRed(pixels[(i+1)*imageWidth+(j+1)]);
		return Median(red,red_0,red_1,red0_,red1_,red00,red01,red10,red11);
	}
	public int getMedianGreen(ColorModel colorModel,int[] pixels,int i,int j)
	{
		int green=colorModel.getGreen(pixels[i*imageWidth+j]);
		int green_0=colorModel.getGreen(pixels[i*imageWidth+(j-1)]);
		int green_1=colorModel.getGreen(pixels[i*imageWidth+(j+1)]);
		int green0_=colorModel.getGreen(pixels[(i-1)*imageWidth+j]);
		int green1_=colorModel.getGreen(pixels[(i+1)*imageWidth+j]);
		int green00=colorModel.getGreen(pixels[(i-1)*imageWidth+(j-1)]);
		int green01=colorModel.getGreen(pixels[(i-1)*imageWidth+(j+1)]);
		int green10=colorModel.getGreen(pixels[(i+1)*imageWidth+(j-1)]);
		int green11=colorModel.getGreen(pixels[(i+1)*imageWidth+(j+1)]);
		return Median(green,green_0,green_1,green0_,green1_,green00,green01,green10,green11);
	}
	public int getMedianBlue(ColorModel colorModel,int[] pixels,int i,int j)
	{
		int blue=colorModel.getBlue(pixels[i*imageWidth+j]);
		int blue_0=colorModel.getBlue(pixels[i*imageWidth+(j-1)]);
		int blue_1=colorModel.getBlue(pixels[i*imageWidth+(j+1)]);
		int blue0_=colorModel.getBlue(pixels[(i-1)*imageWidth+j]);
		int blue1_=colorModel.getBlue(pixels[(i+1)*imageWidth+j]);
		int blue00=colorModel.getBlue(pixels[(i-1)*imageWidth+(j-1)]);
		int blue01=colorModel.getBlue(pixels[(i-1)*imageWidth+(j+1)]);
		int blue10=colorModel.getBlue(pixels[(i+1)*imageWidth+(j-1)]);
		int blue11=colorModel.getBlue(pixels[(i+1)*imageWidth+(j+1)]);
		return Median(blue,blue_0,blue_1,blue0_,blue1_,blue00,blue01,blue10,blue11);
	}
}