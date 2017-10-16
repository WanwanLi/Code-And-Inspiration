import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndLaplacePyramid
{
	public static void main(String[] args)
	{
		Frame_LaplacePyramid Frame_LaplacePyramid1=new Frame_LaplacePyramid();
		Frame_LaplacePyramid1.setVisible(true);
	}
}
class Frame_LaplacePyramid extends Frame
{
	public Image Image1,Image2;
	private Image[] pyramidImages1;
	private Image[] pyramidImages2;
	int imageWidth,imageHeight;
	int imageWidth0,imageHeight0;
	public Frame_LaplacePyramid()
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
			int sgm=2,row=2*sgm+1,column=row;
			double u1=0,u2=0,o1=sgm,o2=sgm,p=0,zoom=0.15;
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);

			double[][] GaussFilter=ImageOp1.getGaussFilter(u1,u2,o1,o2,p,zoom,row,column);
			ImageOp1.normalizeFilter(GaussFilter);
			int level=7;
			int[][] pyramidPixels=ImageOp1.getLaplacePyramidPixels(GaussFilter,level);
			int[] imageWidths=ImageOp1.getDecGeometricSeries(imageWidth,level);
			int[] imageHeights=ImageOp1.getDecGeometricSeries(imageHeight,level);
			this.pyramidImages1=this.createImages(ImageOp1.getMemoryImageSources(pyramidPixels,imageWidths,imageHeights));
		}
		catch(Exception e){e.printStackTrace();}
	}
	public Image[] createImages(MemoryImageSource[] memoryImageSources)
	{
		int length=memoryImageSources.length;
		Image[] images=new Image[length];
		for(int i=0;i<length;i++)images[i]=this.createImage(memoryImageSources[i]);
		return images;
	}
	private void drawExpandedPyramidImages(Graphics g,Image[] images,int x,int y,int imageWidth,int imageHeight)
	{
		int dx=2;
		for(int i=0;i<images.length;i++)
		{
			if(images[i]!=null)g.drawImage(images[i],x,y,this);
			x+=imageWidth+dx;
			imageWidth*=2;
		}
	}
	private void drawReducedPyramidImages(Graphics g,Image[] images,int x,int y,int imageWidth,int imageHeight)
	{
		int dx=2;
		for(int i=0;i<images.length;i++)
		{
			if(images[i]!=null)g.drawImage(images[i],x,y,this);
			x+=imageWidth+dx;
			imageWidth/=2;
		}
	}
	public void paint(Graphics g)
	{
		this.drawReducedPyramidImages(g,pyramidImages1,100,100,imageWidth,imageHeight);
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
	private int getFilteredPixel(ColorModel colorModel,int[] pixels,int imageWidth,int imageHeight,double[][] Filter,int r,int c,int i,int j)
	{
		int ImageWidth0=ImageWidth;
		int ImageHeight0=ImageHeight;
		this.ImageWidth=imageWidth;
		this.ImageHeight=imageHeight;
		double alpha,red,green,blue;
		alpha=this.getFilterAlpha(colorModel,pixels,Filter,r,c,i,j);
		red=this.getFilterRed(colorModel,pixels,Filter,r,c,i,j);
		green=this.getFilterGreen(colorModel,pixels,Filter,r,c,i,j);
		blue=this.getFilterBlue(colorModel,pixels,Filter,r,c,i,j);
		this.ImageWidth=ImageWidth0;
		this.ImageHeight=ImageHeight0;
		return this.getARGBvalue(alpha,red,green,blue);
	}
	public int[][] getExpandedPyramidPixels(double[][] Filter,int level)
	{
		if(level==0)return null;
		int row=Filter.length;
		int column=Filter[0].length;
		if(row%2==0)row++;
		if(column%2==0)column++;
		int r=row/2,c=column/2;
		int[][] PyramidPixels=new int[level][];
		PyramidPixels[0]=new int[ImageHeight*ImageWidth];
		for(int i=0;i<ImageHeight*ImageWidth;i++)PyramidPixels[0][i]=Pixels[i];
		int imageWidth0=ImageWidth;
		int imageHeight0=ImageHeight;
		int imageHeight=ImageHeight*2;
		int imageWidth=ImageWidth*2;
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int k=1;k<level;k++)
		{
			PyramidPixels[k]=new int[imageHeight*imageWidth];
			for(int i=0;i<imageHeight;i++)
			{
				for(int j=0;j<imageWidth;j++)
				{
					PyramidPixels[k][i*imageWidth+j]=this.getFilteredPixel(ColorModel1,PyramidPixels[k-1],imageWidth0,imageHeight0,Filter,r,c,i/2,j/2);
				}
			}
			imageWidth0=imageWidth;
			imageHeight0=imageHeight;
			imageHeight*=2;
			imageWidth*=2;
		}
		return PyramidPixels;
	}
	public int[][] getReducedPyramidPixels(double[][] Filter,int level)
	{
		if(level==0)return null;
		int row=Filter.length;
		int column=Filter[0].length;
		if(row%2==0)row++;
		if(column%2==0)column++;
		int r=row/2,c=column/2;
		int[][] PyramidPixels=new int[level][];
		PyramidPixels[0]=new int[ImageHeight*ImageWidth];
		for(int i=0;i<ImageHeight*ImageWidth;i++)PyramidPixels[0][i]=Pixels[i];
		int imageWidth0=ImageWidth;
		int imageHeight0=ImageHeight;
		int imageHeight=ImageHeight/2;
		int imageWidth=ImageWidth/2;
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int k=1;k<level;k++)
		{
			PyramidPixels[k]=new int[imageHeight*imageWidth];
			for(int i=0;i<imageHeight;i++)
			{
				for(int j=0;j<imageWidth;j++)
				{
					PyramidPixels[k][i*imageWidth+j]=this.getFilteredPixel(ColorModel1,PyramidPixels[k-1],imageWidth0,imageHeight0,Filter,r,c,i*2,j*2);
				}
			}
			imageWidth0=imageWidth;
			imageHeight0=imageHeight;
			imageHeight/=2;
			imageWidth/=2;
		}
		return PyramidPixels;
	}
	public int[][] getLaplacePyramidPixels(double[][] GaussFilter,int level)
	{
		if(level==0)return null;
		int[][] GaussPyramidPixels=this.getReducedPyramidPixels(GaussFilter,level);
		int[] imageWidths=this.getDecGeometricSeries(ImageWidth,level);
		int[] imageHeights=this.getDecGeometricSeries(ImageHeight,level);
		int row=GaussFilter.length;
		int column=GaussFilter[0].length;
		if(row%2==0)row++;
		if(column%2==0)column++;
		int r=row/2,c=column/2,k;
		int[][] PyramidPixels=new int[level][];
		int imageWidth0=ImageWidth;
		int imageHeight0=ImageHeight;
		int imageHeight=ImageHeight;
		int imageWidth=ImageWidth;
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(k=0;k<level-1;k++)
		{
			PyramidPixels[k]=new int[imageHeight*imageWidth];
			for(int i=0;i<imageHeight;i++)
			{
				for(int j=0;j<imageWidth;j++)
				{
					int GaussPyramidPixel1=this.getFilteredPixel(ColorModel1,GaussPyramidPixels[k+1],imageWidths[k+1],imageHeights[k+1],GaussFilter,r,c,i/2,j/2);
					PyramidPixels[k][i*imageWidth+j]=subPixel(ColorModel1,GaussPyramidPixels[k][i*imageWidth+j],GaussPyramidPixel1);
				}
			}
			imageWidth0=imageWidth;
			imageHeight0=imageHeight;
			imageHeight/=2;
			imageWidth/=2;
		}
		PyramidPixels[k]=new int[imageHeight*imageWidth];
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				PyramidPixels[k][i*imageWidth+j]=GaussPyramidPixels[k][i*imageWidth+j];
			}
		}
		return PyramidPixels;
	}
	public int[] getIncGeometricSeries(int a0,int length)
	{
		int[] a=new int[length];
		a[0]=a0;
		for(int i=1;i<length;i++)a[i]=a[i-1]*2;
		return a;
	}
	public int[] getDecGeometricSeries(int a0,int length)
	{
		int[] a=new int[length];
		a[0]=a0;
		for(int i=1;i<length;i++)a[i]=a[i-1]/2;
		return a;
	}
	private double getFilterAlpha(ColorModel colorModel,int[] pixels,double[][] filter,int r,int c,int i,int j)
	{
		double filterAlpha=0;
		for(int u=i-r;u<=i+r;u++)
		{
			int u1=u<0?0:u>=ImageHeight?ImageHeight-1:u;
			for(int v=j-c;v<=j+c;v++)
			{
				int v1=v<0?0:v>=ImageWidth?ImageWidth-1:v;
				filterAlpha+=colorModel.getAlpha(pixels[u1*ImageWidth+v1])*filter[u-(i-r)][v-(j-c)];
			}
		}
		return 255;
	}
	private double getFilterRed(ColorModel colorModel,int[] pixels,double[][] filter,int r,int c,int i,int j)
	{
		double filterRed=0;
		for(int u=i-r;u<=i+r;u++)
		{
			int u1=u<0?0:u>=ImageHeight?ImageHeight-1:u;
			for(int v=j-c;v<=j+c;v++)
			{
				int v1=v<0?0:v>=ImageWidth?ImageWidth-1:v;
				filterRed+=colorModel.getRed(pixels[u1*ImageWidth+v1])*filter[u-(i-r)][v-(j-c)];
			}
		}
		return filterRed;
	}
	private double getFilterGreen(ColorModel colorModel,int[] pixels,double[][] filter,int r,int c,int i,int j)
	{
		double filterGreen=0;
		for(int u=i-r;u<=i+r;u++)
		{
			int u1=u<0?0:u>=ImageHeight?ImageHeight-1:u;
			for(int v=j-c;v<=j+c;v++)
			{
				int v1=v<0?0:v>=ImageWidth?ImageWidth-1:v;
				filterGreen+=colorModel.getGreen(pixels[u1*ImageWidth+v1])*filter[u-(i-r)][v-(j-c)];
			}
		}
		return filterGreen;
	}
	private double getFilterBlue(ColorModel colorModel,int[] pixels,double[][] filter,int r,int c,int i,int j)
	{
		double filterBlue=0;
		for(int u=i-r;u<=i+r;u++)
		{
			int u1=u<0?0:u>=ImageHeight?ImageHeight-1:u;
			for(int v=j-c;v<=j+c;v++)
			{
				int v1=v<0?0:v>=ImageWidth?ImageWidth-1:v;
				filterBlue+=colorModel.getBlue(pixels[u1*ImageWidth+v1])*filter[u-(i-r)][v-(j-c)];
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
	private int subPixel(ColorModel colorModel,int pixel0,int pixel1)
	{
		int red0=colorModel.getRed(pixel0);
		int green0=colorModel.getGreen(pixel0);
		int blue0=colorModel.getBlue(pixel0);
		int red1=colorModel.getRed(pixel1);
		int green1=colorModel.getGreen(pixel1);
		int blue1=colorModel.getBlue(pixel1);
		int alpha=255,red=red0-red1,green=green0-green1,blue=blue0-blue1;
		return this.getARGBvalue(alpha,red,green,blue);
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
	private boolean isInImageRange(int i,int j)
	{
		return (0<=i&&i<ImageHeight&&0<=j&&j<ImageWidth);
	}
	private boolean isInImageRange(int r,int c,int i,int j)
	{
		return (0<=i-r&&i+r<ImageHeight&&0<=j-c&&j+c<ImageWidth);
	}
	public MemoryImageSource[] getMemoryImageSources(int[][] imagePixels,int[] imageWidths,int[] imageHeights)
	{
		int length=imagePixels.length;
		MemoryImageSource[] MemoryImageSources=new MemoryImageSource[length];
		for(int i=0;i<length;i++)
		{
			MemoryImageSources[i]=new MemoryImageSource(imageWidths[i],imageHeights[i],imagePixels[i],0,imageWidths[i]);
		}
		return MemoryImageSources;
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}