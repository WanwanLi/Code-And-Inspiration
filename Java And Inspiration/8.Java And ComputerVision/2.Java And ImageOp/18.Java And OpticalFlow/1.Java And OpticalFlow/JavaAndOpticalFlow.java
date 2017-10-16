import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndOpticalFlow
{
	public static void main(String[] args)
	{
		Frame_OpticalFlow Frame_OpticalFlow1=new Frame_OpticalFlow();
		Frame_OpticalFlow1.setVisible(true);
	}
}
class Frame_OpticalFlow extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	double[][] opticalFlow;
	public Frame_OpticalFlow()
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
			String imageName0="..\\..\\..\\JavaAndImageProcessing.jpg";
			String imageName1="..\\JavaAndImageProcessing1.jpg";
*/
			String imageName0="..\\car1.jpg";
			String imageName1="..\\car2.jpg";
			Image2=Toolkit.getDefaultToolkit().getImage(imageName1);
			int[] pixels=getImagePixels(imageName0);
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			pixels=getImagePixels(imageName1);
			//opticalFlow=ImageOp1.getOpticalFlow(pixels);
			opticalFlow=ImageOp1.getOpticalFlow(pixels,0,0);
/*
			int sgm=2,row=2*sgm+1,column=row;
			double u1=0,u2=0,o1=sgm,o2=sgm,p=0,zoom=0.15;
			double[][] GaussFilter=ImageOp1.getGaussFilter(u1,u2,o1,o2,p,zoom,row,column);
			ImageOp1.normalizeFilter(GaussFilter);
			ImageOp1.filter(GaussFilter);
*/
			Image1=this.createImage(ImageOp1.getMemoryImageSource());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public int[] getImagePixels(String imageName) throws Exception
	{
		Image image=Toolkit.getDefaultToolkit().getImage(imageName);
		MediaTracker MediaTracker1=new MediaTracker(this);
		MediaTracker1.addImage(image,0);
		MediaTracker1.waitForID(0);
		imageWidth=image.getWidth(this);
		imageHeight=image.getHeight(this);
		int[] pixels=new int[imageWidth*imageHeight];
		PixelGrabber PixelGrabber1=new PixelGrabber(image,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
		PixelGrabber1.grabPixels();
		return pixels;
	}
	public void drawOpticalFlow(Graphics g,double[][] opticalFlow,int x,int y,int w,int h,Color c)
	{
		g.setColor(c);
		int step=8;
		for(int i=0;i<h-1;i+=step)
		{
			for(int j=0;j<w-1;j+=step)
			{
				double[] uv=opticalFlow[i*w+j];
				double k=10.0;
				int x0=x+j,y0=y+i;
				int dx=(int)(uv[0]*k);
				int dy=(int)(uv[1]*k);
				int x1=x0+dx,y1=y0+dy;
				g.drawLine(x0,y0,x1,y1);
				g.fillRect(x1,y1,2,2);
			}
		}
	}
	public void paint(Graphics g)
	{
		if(Image1!=null)g.drawImage(Image1,100,100,this);
		if(Image1!=null)this.drawOpticalFlow(g,opticalFlow,100,100,imageWidth,imageHeight,Color.red);
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
	public double[][] getOpticalFlow(int[] pixels)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		double[][] uv=new double[ImageHeight*ImageWidth][2];
		for(int i=0;i<ImageHeight-1;i++)
		{
			for(int j=0;j<ImageWidth-1;j++)
			{
				int p0=Pixels[i*ImageWidth+j];
				int pX=Pixels[i*ImageWidth+j+1];
				int pY=Pixels[(i+1)*ImageWidth+j];
				int pT=pixels[i*ImageWidth+j];
				double Ix=getBrightnessDifference(ColorModel1,p0,pX);
				double Iy=getBrightnessDifference(ColorModel1,p0,pY);
				double It=getBrightnessDifference(ColorModel1,p0,pT);
				double[] M=new double[]
				{
					Ix*Ix,Ix*Iy,
					Ix*Iy,Iy*Iy
				};
				M=inverseOfMatrix2x2_(M);
				double[] V=new double[]{-Ix*It,-Iy*It};
				uv[i*ImageWidth+j]=mulVec2(M,V);

			}
		}
		return uv;
	}
	public double[][] getOpticalFlow(int[] pixels,int r,int c)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		double[][] uv=new double[ImageHeight*ImageWidth][2];
		for(int i=r;i<ImageHeight-1-r;i++)
		{
			for(int j=c;j<ImageWidth-1-c;j++)
			{
				int p0=Pixels[i*ImageWidth+j];
				int pX=Pixels[i*ImageWidth+j+1];
				int pY=Pixels[(i+1)*ImageWidth+j];
				int pT=pixels[i*ImageWidth+j];
				double[] M=getOpticalFlowMatrix(ColorModel1,pixels,r,c,i,j);
				double[] V=new double[]{M[4],M[5]};
			//	M=inverseOfMatrix2x2(M);
			//	uv[i*ImageWidth+j]=mulVec2(M,V);
				uv[i*ImageWidth+j]=getOpticalFlowVector(M);
//if(1%10==0||j%10==0)System.out.println(uv[i*ImageWidth+j][0]+","+uv[i*ImageWidth+j][1]);
			}
		}
		return uv;
	}
	private double[] getOpticalFlowMatrix(ColorModel ColorModel1,int[] pixels,int r,int c,int i,int j)
	{
		double[] I={0,0,0,0,0,0};
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				int p0=Pixels[u*ImageWidth+v];
				int pX=Pixels[u*ImageWidth+v+1];
				int pY=Pixels[(u+1)*ImageWidth+v];
				int pT=pixels[u*ImageWidth+v];
				double Ix=getBrightnessDifference(ColorModel1,p0,pX);
				double Iy=getBrightnessDifference(ColorModel1,p0,pY);
				double It=getBrightnessDifference(ColorModel1,p0,pT);
				I[0]+=Ix*Ix;
				I[1]+=Ix*Iy;
				I[2]+=Ix*Iy;
				I[3]+=Iy*Iy;
				I[4]-=Ix*It;
				I[5]-=Iy*It;
			}
		}
		return I;
	}
	private double[] getOpticalFlowVector(double[] opticalFlowMatrix)
	{
		double IxIx=opticalFlowMatrix[0];
		double IxIy=opticalFlowMatrix[1];
		double IyIy=opticalFlowMatrix[3];
		double IxIt=-opticalFlowMatrix[4];
		double IyIt=-opticalFlowMatrix[5];
		double u=-IyIy*IxIt+IxIy*IyIt;
		double v=-IxIx*IyIt+IxIy*IxIt;
		double t=IxIx*IyIy-IxIy*IxIy;
		if(t==0)return new double[]{0,0};
		return new double[]{u/t,v/t};
	}
	private double[] inverseOfMatrix2x2_(double[] M)
	{
		double a=M[0],b=M[1],c=M[2],d=M[3];
		double m=c/a,p=d-b*m,n=m/p;
		return new double[]
		{
			(1+m*n)/a,-n/a,
			         -m/p,  1/p
		};
	}
	private double[] inverseOfMatrix2x2(double[] M)
	{
		double a=M[0],b=M[1];
		double c=M[2],d=M[3];
		double k=a*d-b*c;
	//	if(k==0.0)return new double[]{0,0,0,0};
		return new double[]
		{
			  d/k,-b/k,
			-c/k,  a/k
		};
	}
	private double[] mulVec2(double[] M,double[] V)
	{
		return new double[]
		{
			M[0]*V[0]+M[1]*V[1],
			M[2]*V[0]+M[3]*V[1]
		};
	}
	private double getDistance(ColorModel colorModel,int pixel0,int pixel1)
	{
		double dr=colorModel.getRed(pixel0)-colorModel.getRed(pixel1);
		double dg=colorModel.getGreen(pixel0)-colorModel.getGreen(pixel1);
		double db=colorModel.getBlue(pixel0)-colorModel.getBlue(pixel1);
		return Math.sqrt(dr*dr+dg*dg+db*db);
	}
	private double getBrightnessDifference(ColorModel colorModel,int pixel0,int pixel1)
	{
		double I0=getBrightness(colorModel,pixel0);
		double I1=getBrightness(colorModel,pixel1);
		//return Math.abs(I1-I0);
		return I1-I0;
	}
	private double getBrightness(ColorModel colorModel,int pixel)
	{
		int r=colorModel.getRed(pixel);
		int g=colorModel.getGreen(pixel);
		int b=colorModel.getBlue(pixel);
		int m=max(r,g);
		return max(m,b);
	//	return Math.sqrt(r*r+g*g+b*b);
	}
	private int max(int a,int b)
	{
		return a>b?a:b;
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