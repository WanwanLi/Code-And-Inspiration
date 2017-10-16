import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndGaussFilter
{
	public static void main(String[] args)
	{
		Frame_GaussFilter Frame_GaussFilter1=new Frame_GaussFilter();
		Frame_GaussFilter1.setVisible(true);
	}
}
class Frame_GaussFilter extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_GaussFilter()
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
			int[] pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			int sgm=2,row=2*sgm+1,column=row;
			double u1=0,u2=0,o1=sgm,o2=sgm,p=0,zoom=1.5;
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			double[][] depthImage=ImageOp1.getShapeFromShading(100,0,0);
			double[][] GaussFilter=ImageOp1.getGaussFilter(u1,u2,o1,o2,p,zoom,row,column);
			ImageOp1.normalizeFilter(GaussFilter);
			ImageOp1.filter(GaussFilter);
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
	public double[][] getShapeFromShading(int time,double a,double b)
	{
		double Ps=Math.cos(a)*Math.sin(b)/Math.cos(b);
		double Qs=Math.sin(a)*Math.sin(b)/Math.cos(b);
		double p,q,pq,PQs,fZ,dfZ,Eij,Wn=0.0001*0.0001,Y,K;
		double[][] Si=new double[ImageHeight][ImageWidth];
		double[][] Zn=new double[ImageHeight][ImageWidth];
		double[][] Si1=new double[ImageHeight][ImageWidth];
		double[][] Zn1=new double[ImageHeight][ImageWidth];
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Zn1[i][j] = 0.0;
				Si1[i][j] = 0.01;
			}
		}
		for(int t=0;t<time;t++)
		{
			for(int i=0;i<ImageHeight;i++)
			{
				for(int j=0;j<ImageWidth;j++)
				{
					if(j-1< 0||i-1< 0)p = q = 0.0;
					else
					{
						p = Zn1[i][j] - Zn1[i][(j-1)];
						q = Zn1[i][j] - Zn1[i-1][j];
					}
					pq = 1.0 + p*p + q*q;
					PQs = 1.0 + Ps*Ps + Qs*Qs;
					Eij = this.getBrightness(ColorModel1,i,j);
					fZ = -1.0*(Eij - Math.max(0.0,(1+p*Ps+q*Qs)/(sqrt(pq)*sqrt(PQs))));
					dfZ = -1.0*((Ps+Qs)/(sqrt(pq)*sqrt(PQs))-(p+q)*(1.0+p*Ps+q*Qs)/(sqrt(pq*pq*pq)*sqrt(PQs))) ;
					Y = fZ + dfZ*Zn1[i][j];
					K = Si1[i][j]*dfZ/(Wn+dfZ*Si1[i][j]*dfZ);
					Si[i][j] = (1.0 - K*dfZ)*Si1[i][j]; 
					Zn[i][j] = Zn1[i][j] + K*(Y-dfZ*Zn1[i][j]);
				}
			}
			for(int i=0;i<ImageHeight;i++)
			{
				for(int j=0;j<ImageWidth;j++)
				{
					Zn1[i][j] = Zn[i][j];
					Si1[i][j] = Si[i][j];
				}
			}
System.out.println(t+"....");
		}
		return Zn;
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
	private double getBrightness(ColorModel colorModel,int i,int j)
	{
		double red=colorModel.getRed(Pixels[i*ImageWidth+j])/255.0;
		double green=colorModel.getGreen(Pixels[i*ImageWidth+j])/255.0;
		double blue=colorModel.getBlue(Pixels[i*ImageWidth+j])/255.0;
		return Math.max(red,Math.max(green,blue));
	}
	private double sqrt(double x){return Math.sqrt(x);}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}