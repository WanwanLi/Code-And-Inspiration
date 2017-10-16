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
	int imageWidth,imageHeight;
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
			Image1=Toolkit.getDefaultToolkit().getImage("JavaAndImageProcessing.jpg");
			MediaTracker MediaTracker1=new MediaTracker(this);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			imageWidth=Image1.getWidth(this);
			imageHeight=Image1.getHeight(this);
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			int sgm=1,row=2*sgm+1,column=row;
			double u1=0,u2=0,o1=sgm,o2=sgm,p=0,zoom=0.15;
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			double[][] GaussFilter=ImageOp1.getGaussFilter(u1,u2,o1,o2,p,zoom,row,column);
			ImageOp1.normalizeFilter(GaussFilter);
		//	ImageOp1.filter(GaussFilter);
			ImageOp1.medianFilter(row,column);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		if(Image2!=null)g.drawImage(Image2,100,100,this);
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
	public void filter(double[][] Filter)
	{
		int row=Filter.length;
		int column=Filter[0].length;
		if(row%2==0)row++;
		if(column%2==0)column++;
		int r=row/2,c=column/2;
		int[] pixels=new int[ImageWidth*ImageHeight];
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int i=r;i<ImageHeight-r;i++)
		{
			for(int j=c;j<ImageWidth-c;j++)
			{
				double alpha=this.getFilterAlpha(ColorModel1,Pixels,Filter,r,c,i,j);
				double red=this.getFilterRed(ColorModel1,Pixels,Filter,r,c,i,j);
				double green=this.getFilterGreen(ColorModel1,Pixels,Filter,r,c,i,j);
				double blue=this.getFilterBlue(ColorModel1,Pixels,Filter,r,c,i,j);
				pixels[i*ImageWidth+j]=this.getARGBvalue(alpha,red,green,blue);
			}
		}
		this.Pixels=pixels;
	}
	public void medianFilter(int row,int column)
	{
		if(row%2==0)row++;
		if(column%2==0)column++;
		int r=row/2,c=column/2;
		double[] midFilter=new double[row*column];
		int[] pixels=new int[ImageWidth*ImageHeight];
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int i=r;i<ImageHeight-r;i++)
		{
			for(int j=c;j<ImageWidth-c;j++)
			{
				double alpha=this.getMidFilterAlpha(ColorModel1,Pixels,midFilter,r,c,i,j);
				double red=this.getMidFilterRed(ColorModel1,Pixels,midFilter,r,c,i,j);
				double green=this.getMidFilterGreen(ColorModel1,Pixels,midFilter,r,c,i,j);
				double blue=this.getMidFilterBlue(ColorModel1,Pixels,midFilter,r,c,i,j);
				pixels[i*ImageWidth+j]=this.getARGBvalue(alpha,red,green,blue);
			}
		}
		this.Pixels=pixels;
	}
	private double getMidFilterAlpha(ColorModel colorModel,int[] pixels,double[] midFilter,int r,int c,int i,int j)
	{
		return 255.0;
	}
	private double getMidFilterRed(ColorModel colorModel,int[] pixels,double[] midFilter,int r,int c,int i,int j)
	{
		int column=c*2+1;
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				midFilter[(u-(i-r))*column+v-(j-c)]=colorModel.getRed(pixels[u*ImageWidth+v]);
			}
		}
		return Median(midFilter);
	}
	private double getMidFilterGreen(ColorModel colorModel,int[] pixels,double[] midFilter,int r,int c,int i,int j)
	{
		int column=c*2+1;
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				midFilter[(u-(i-r))*column+v-(j-c)]=colorModel.getGreen(pixels[u*ImageWidth+v]);
			}
		}
		return Median(midFilter);
	}
	private double getMidFilterBlue(ColorModel colorModel,int[] pixels,double[] midFilter,int r,int c,int i,int j)
	{
		int column=c*2+1;
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				midFilter[(u-(i-r))*column+v-(j-c)]=colorModel.getBlue(pixels[u*ImageWidth+v]);
			}
		}
		return Median(midFilter);
	}
	private int getMidIndex(double[] array,int begin,int end)
	{
		if(begin>=end)return end;
		double pivot=array[begin];
		int low=begin+1;
		int high=end;
		while(low<high)
		{
			while(low<high&&array[high]>=pivot)high--;
			while(low<high&&array[low]<=pivot)low++;
			if(low<high)this.swap(array,low,high);
		}
		if(array[begin]>array[low])this.swap(array,begin,low);
		return low;
	}
	public double getMedian(double[] array,int begin,int end)
	{
		if(begin>=end)return array[end];
		int low=begin;
		int high=end;
		int mid=high/2;
		int index=-1;
		while(index!=mid)
		{
			index=getMidIndex(array,low,high);
			if(index<mid)low=index+1==mid?mid:index;
			else if(index>mid)high=index-1==mid?mid:index;
			else break;
		}
		return array[mid];
	}
	private void swap(double[] array,int i,int j)
	{
		double arrayi=array[i];
		array[i]=array[j];
		array[j]=arrayi;
	}
	private double Median1(double[] y)
	{
		int length=y.length;
		return this.getMedian(y,0,length-1);
	}
	private double Median(double[] y)
	{
		int length=y.length;
		for(int i=0;i<length-1;i++)
		{
			int k=i;
			double min=y[i];
			for(int j=i+1;j<length;j++)if(y[j]<min){k=j;min=y[j];}
			if(k!=i)
			{
				double yi=y[i];
				y[i]=y[k];
				y[k]=yi;
			}
		}
		return y[length/2];
	}
	private double getFilterAlpha(ColorModel colorModel,int[] pixels,double[][] filter,int r,int c,int i,int j)
	{
		double filterAlpha=0;
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				filterAlpha+=colorModel.getAlpha(pixels[u*ImageWidth+v])*filter[u-(i-r)][v-(j-c)];
			}
		}
		return filterAlpha;
	}
	private double getFilterRed(ColorModel colorModel,int[] pixels,double[][] filter,int r,int c,int i,int j)
	{
		double filterRed=0;
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				filterRed+=colorModel.getRed(pixels[u*ImageWidth+v])*filter[u-(i-r)][v-(j-c)];
			}
		}
		return filterRed;
	}
	private double getFilterGreen(ColorModel colorModel,int[] pixels,double[][] filter,int r,int c,int i,int j)
	{
		double filterGreen=0;
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				filterGreen+=colorModel.getGreen(pixels[u*ImageWidth+v])*filter[u-(i-r)][v-(j-c)];
			}
		}
		return filterGreen;
	}
	private double getFilterBlue(ColorModel colorModel,int[] pixels,double[][] filter,int r,int c,int i,int j)
	{
		double filterBlue=0;
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				filterBlue+=colorModel.getBlue(pixels[u*ImageWidth+v])*filter[u-(i-r)][v-(j-c)];
			}
		}
		return filterBlue;
	}
	public double Gauss(double x,double y,double u1,double u2,double o1,double o2,double p)
	{
		double u=(x-u1)/o1;
		double v=(y-u2)/o2;
		double w=1-p*p;
		return Math.exp(-(u*u-2*p*u*v+v*v)/(2*w))/(2*Math.PI*o1*o2*Math.sqrt(w));
	}
	public double[][] getGaussFilter(double u1,double u2,double o1,double o2,double p,double zoom,int row,int column)
	{
		if(row%2==0)row++;
		if(column%2==0)column++;
		double maxX=zoom*o1;
		double minX=-zoom*o1;
		double maxY=zoom*o2;
		double minY=-zoom*o2;
		double dx=(maxX-minX)/(column-1);
		double dy=(maxY-minY)/(row-1);
		double[][] GaussFilter=new double[row][column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				double x=minX+j*dx;
				double y=minY+i*dy;
				GaussFilter[i][j]=Gauss(x,y,u1,u2,o1,o2,p);
			}
		}
		return GaussFilter;
	}
	public double[][] getEverageFilter(int row,int column)
	{
		if(row%2==0)row++;
		if(column%2==0)column++;
		double[][] everageFilter=new double[row][column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				everageFilter[i][j]=1.0/(row*column);
			}
		}
		return everageFilter;
	}
	public void normalizeFilter(double[][] filter)
	{
		int row=filter.length;
		int column=filter[0].length;
		double weight=0;
		for(int i=0;i<row;i++)for(int j=0;j<column;j++)weight+=filter[i][j];
		for(int i=0;i<row;i++)for(int j=0;j<column;j++)filter[i][j]/=weight;
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