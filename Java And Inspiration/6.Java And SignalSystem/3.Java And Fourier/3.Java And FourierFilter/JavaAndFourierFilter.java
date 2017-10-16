import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndFourierFilter
{
	public static void main(String[] args)
	{
		Frame_FourierFilter Frame_FourierFilter1=new Frame_FourierFilter();
		Frame_FourierFilter1.setVisible(true);
	}
}
class Frame_FourierFilter extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_FourierFilter()
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
			ImageOp ImageOp1=new ImageOp(pixels,imageWidth,imageHeight);
			double sx=((2<<8)+0.0)/imageWidth;
			double sy=((2<<8)+0.0)/imageHeight;
			ImageOp1.setToScale(sx,sy);
//			ImageOp1.lowpassFourierFilter(600);
			ImageOp1.highpassFourierFilter(200);
			Image2=this.createImage(ImageOp1.getMemoryImageSource());
			this.imageWidth*=sx;
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
	public Interpolator2D Interpolator2D_Alpha;
	public Interpolator2D Interpolator2D_Red;
	public Interpolator2D Interpolator2D_Green;
	public Interpolator2D Interpolator2D_Blue;
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
/*
	public void grey(double r,double g,double b)
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=ColorModel1.getAlpha(Pixels[i*ImageWidth+j]);
				int red=ColorModel1.getRed(Pixels[i*ImageWidth+j]);
				int green=ColorModel1.getGreen(Pixels[i*ImageWidth+j]);
				int blue=ColorModel1.getBlue(Pixels[i*ImageWidth+j]);
				double Grey=(r*red+g*green+b*blue)/(r+g+b);
				double Alpha=alpha;
				double Red=Grey;
				double Green=Grey;
				double Blue=Grey;
				this.Pixels[i*ImageWidth+j]=this.getARGBvalue(Alpha,Grey,Grey,Grey);
			}
		}
	}
*/
	public void lowpassFourierFilter(int radius)
	{
		int[] pixels_Red=new int[ImageHeight*ImageWidth];
		int[] pixels_Green=new int[ImageHeight*ImageWidth];
		int[] pixels_Blue=new int[ImageHeight*ImageWidth];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				pixels_Red[i*ImageWidth+j]=this.getRed(Pixels[i*ImageWidth+j]);
				pixels_Green[i*ImageWidth+j]=this.getGreen(Pixels[i*ImageWidth+j]);
				pixels_Blue[i*ImageWidth+j]=this.getBlue(Pixels[i*ImageWidth+j]);
			}
		}
		FastFourierTransform2D FourierTransform=new FastFourierTransform2D(pixels_Red,ImageWidth,ImageHeight);
		Complex[][] A=FourierTransform.getComplexArray();
		for(int i=0;i<ImageHeight;i++)for(int j=0;j<ImageWidth;j++)A[i][j]=Complex.MUL(A[i][j],lowpass(i,j,radius));
		FourierTransform.inverse();
		pixels_Red=FourierTransform.getPixels();
		FourierTransform=new FastFourierTransform2D(pixels_Green,ImageWidth,ImageHeight);
		A=FourierTransform.getComplexArray();
		for(int i=0;i<ImageHeight;i++)for(int j=0;j<ImageWidth;j++)A[i][j]=Complex.MUL(A[i][j],lowpass(i,j,radius));
		FourierTransform.inverse();
		pixels_Green=FourierTransform.getPixels();
		FourierTransform=new FastFourierTransform2D(pixels_Blue,ImageWidth,ImageHeight);
		A=FourierTransform.getComplexArray();
		for(int i=0;i<ImageHeight;i++)for(int j=0;j<ImageWidth;j++)A[i][j]=Complex.MUL(A[i][j],lowpass(i,j,radius));
		FourierTransform.inverse();
		pixels_Blue=FourierTransform.getPixels();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=this.getAlpha(Pixels[i*ImageWidth+j]);
				int red=pixels_Red[i*ImageWidth+j];
				int green=pixels_Green[i*ImageWidth+j];
				int blue=pixels_Blue[i*ImageWidth+j];
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public void highpassFourierFilter(int radius)
	{
		int[] pixels_Red=new int[ImageHeight*ImageWidth];
		int[] pixels_Green=new int[ImageHeight*ImageWidth];
		int[] pixels_Blue=new int[ImageHeight*ImageWidth];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				pixels_Red[i*ImageWidth+j]=this.getRed(Pixels[i*ImageWidth+j]);
				pixels_Green[i*ImageWidth+j]=this.getGreen(Pixels[i*ImageWidth+j]);
				pixels_Blue[i*ImageWidth+j]=this.getBlue(Pixels[i*ImageWidth+j]);
			}
		}
		FastFourierTransform2D FourierTransform=new FastFourierTransform2D(pixels_Red,ImageWidth,ImageHeight);
		Complex[][] A=FourierTransform.getComplexArray();
		for(int i=0;i<ImageHeight;i++)for(int j=0;j<ImageWidth;j++)A[i][j]=Complex.MUL(A[i][j],highpass(i,j,radius));
		FourierTransform.inverse();
		pixels_Red=FourierTransform.getPixels();
		FourierTransform=new FastFourierTransform2D(pixels_Green,ImageWidth,ImageHeight);
		A=FourierTransform.getComplexArray();
		for(int i=0;i<ImageHeight;i++)for(int j=0;j<ImageWidth;j++)A[i][j]=Complex.MUL(A[i][j],highpass(i,j,radius));
		FourierTransform.inverse();
		pixels_Green=FourierTransform.getPixels();
		FourierTransform=new FastFourierTransform2D(pixels_Blue,ImageWidth,ImageHeight);
		A=FourierTransform.getComplexArray();
		for(int i=0;i<ImageHeight;i++)for(int j=0;j<ImageWidth;j++)A[i][j]=Complex.MUL(A[i][j],highpass(i,j,radius));
		FourierTransform.inverse();
		pixels_Blue=FourierTransform.getPixels();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				int alpha=this.getAlpha(Pixels[i*ImageWidth+j]);
				int red=pixels_Red[i*ImageWidth+j];
				int green=pixels_Green[i*ImageWidth+j];
				int blue=pixels_Blue[i*ImageWidth+j];
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	private Complex lowpass(int row,int column,int radius)
	{
		boolean b=(row*row+column*column)<radius*radius;
		return (b?(new Complex(1,0)):(new Complex(0,0)));
	}
	private Complex highpass(int row,int column,double radius)
	{
		boolean b=(row*row+column*column)>radius*radius;
		return (b?(new Complex(1,0)):(new Complex(0,0)));
	}
	public void setToScale(double sx,double sy)
	{
		this.createARGBInterpolator2D();
		this.ImageWidth*=sx;
		this.ImageHeight*=sy;
		this.Pixels=new int[ImageHeight*ImageWidth];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Transform2D transform2D=new Transform2D(j,i);
				transform2D.setToScale(sx,sy);
				int alpha=Interpolator2D_Alpha.getImageInterpolatedValue(transform2D);
				int red=Interpolator2D_Red.getImageInterpolatedValue(transform2D);
				int green=Interpolator2D_Green.getImageInterpolatedValue(transform2D);
				int blue=Interpolator2D_Blue.getImageInterpolatedValue(transform2D);
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	private void createARGBInterpolator2D()
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		int[] Alpha=new int[ImageWidth*ImageHeight];
		int[] Red=new int[ImageWidth*ImageHeight];
		int[] Green=new int[ImageWidth*ImageHeight];
		int[] Blue=new int[ImageWidth*ImageHeight];
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Alpha[i*ImageWidth+j]=ColorModel1.getAlpha(Pixels[i*ImageWidth+j]);
				Red[i*ImageWidth+j]=ColorModel1.getRed(Pixels[i*ImageWidth+j]);
				Green[i*ImageWidth+j]=ColorModel1.getGreen(Pixels[i*ImageWidth+j]);
				Blue[i*ImageWidth+j]=ColorModel1.getBlue(Pixels[i*ImageWidth+j]);
			}
		}
		this.Interpolator2D_Alpha=new Interpolator2D(Alpha,ImageWidth,ImageHeight);
		this.Interpolator2D_Red=new Interpolator2D(Red,ImageWidth,ImageHeight);
		this.Interpolator2D_Green=new Interpolator2D(Green,ImageWidth,ImageHeight);
		this.Interpolator2D_Blue=new Interpolator2D(Blue,ImageWidth,ImageHeight);
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
	private int getAlpha(int pixel)
	{
		return (pixel>>24)&0xff;
	}
	private int getRed(int pixel)
	{
		return (pixel>>16)&0xff;
	}
	private int getGreen(int pixel)
	{
		return (pixel>>8)&0xff;
	}
	private int getBlue(int pixel)
	{
		return pixel&0xff;
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
	}
}
class FastFourierTransform2D
{
	Complex[][] A;
	public FastFourierTransform2D(int[] pixels,int imageWidth,int imageHeight)
	{
		int m=imageHeight;
		int n=imageWidth;
		Complex[][] a=new Complex[m][n];
		this.A=new Complex[m][];
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)a[i][j]=new Complex(pixels[i*n+j]+0.0,0.0);
		for(int i=0;i<m;i++)this.A[i]=this.getFastFourierTransformedArray(a[i],n);
		Complex[] b=new Complex[m];
		for(int j=0;j<n;j++)
		{
			for(int i=0;i<m;i++)b[i]=A[i][j];
			Complex[] c=this.getFastFourierTransformedArray(b,m);
			for(int i=0;i<m;i++)this.A[i][j]=c[i];
		}
	}
	public Complex[][] getComplexArray(){return this.A;}
	public void inverse()
	{
		int m=A.length;
		int n=A[0].length;
		for(int i=0;i<m;i++)this.A[i]=this.getInverseTransformedArray(A[i],n);
		Complex[] b=new Complex[m];
		for(int j=0;j<n;j++)
		{
			for(int i=0;i<m;i++)b[i]=A[i][j];
			Complex[] c=this.getInverseTransformedArray(b,m);
			for(int i=0;i<m;i++)this.A[i][j]=c[i];
		}
	}
	public int[] getPixels()
	{
		int imageWidth=A[0].length;
		int imageHeight=A.length;
		int[] pixels=new int[imageHeight*imageWidth];
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				double length=A[i][j].length();
				length/=(imageHeight*imageWidth);
				if(length>255)length=255;
				pixels[i*imageWidth+j]=(int)length;
			}
		}
		return pixels;
	}
	private Complex[] getFastFourierTransformedArray(Complex[] a,int n)
	{
		if(n==2)
		{
			Complex[] A=new Complex[2];
			Complex a0=Complex.MUL(a[0],Complex.e(0));
			Complex a1_0=Complex.MUL(a[1],Complex.e(0));
			Complex a1_PI=Complex.MUL(a[1],Complex.e(Math.PI));
			A[0]=Complex.ADD(a0,a1_0);
			A[1]=Complex.ADD(a0,a1_PI);
			return A;
		}
		Complex[] b=new Complex[n/2];
		Complex[] c=new Complex[n/2];
		for(int i=0;i<n/2;i++)
		{
			b[i]=a[i*2];
			c[i]=a[i*2+1];
		}
		Complex[] A=new Complex[n];
		Complex[] B=new Complex[n/2];
		Complex[] C=new Complex[n/2];
		B=this.getFastFourierTransformedArray(b,n/2);
		C=this.getFastFourierTransformedArray(c,n/2);
		for(int i=0;i<n/2;i++)
		{
			A[i]=Complex.ADD(B[i],Complex.MUL(C[i],Complex.e2PI(n,i)));
			A[i+n/2]=Complex.ADD(B[i],Complex.MUL(C[i],Complex.e2PI(n,i+n/2)));
		}
		return A;
	}
	private Complex[] getInverseTransformedArray(Complex[] a,int n)
	{
		if(n==2)
		{
			Complex[] A=new Complex[2];
			Complex a0=Complex.MUL(a[0],Complex.e(0));
			Complex a1_0=Complex.MUL(a[1],Complex.e(0));
			Complex a1_PI=Complex.MUL(a[1],Complex.e(-Math.PI));
			A[0]=Complex.ADD(a0,a1_0);
			A[1]=Complex.ADD(a0,a1_PI);
			return A;
		}
		Complex[] b=new Complex[n/2];
		Complex[] c=new Complex[n/2];
		for(int i=0;i<n/2;i++)
		{
			b[i]=a[i*2];
			c[i]=a[i*2+1];
		}
		Complex[] A=new Complex[n];
		Complex[] B=new Complex[n/2];
		Complex[] C=new Complex[n/2];
		B=this.getInverseTransformedArray(b,n/2);
		C=this.getInverseTransformedArray(c,n/2);
		for(int i=0;i<n/2;i++)
		{
			A[i]=Complex.ADD(B[i],Complex.MUL(C[i],Complex.e2PI(-n,i)));
			A[i+n/2]=Complex.ADD(B[i],Complex.MUL(C[i],Complex.e2PI(-n,i+n/2)));
		}
		return A;
	}
}
class Complex
{
	public double real;
	public double imaginary;
	public Complex(double real,double imaginary)
	{
		this.real=real;
		this.imaginary=imaginary;
	}
	public void add(Complex c)
	{
		this.real+=c.real;
		this.imaginary+=c.imaginary;
	}
	public void sub(Complex c)
	{
		this.real-=c.real;
		this.imaginary-=c.imaginary;
	}
	public void mul(double d)
	{
		this.real*=d;
		this.imaginary*=d;
	}
	public static Complex ADD(Complex c0,Complex c1)
	{
		return new Complex(c0.real+c1.real,c0.imaginary+c1.imaginary);
	}
	public static Complex SUB(Complex c0,Complex c1)
	{
		return new Complex(c0.real-c1.real,c0.imaginary-c1.imaginary);
	}
	public static Complex MUL(Complex c0,Complex c1)
	{
		double real=c0.real*c1.real-c0.imaginary*c1.imaginary;
		double imaginary=c0.real*c1.imaginary+c0.imaginary*c1.real;
		return new Complex(real,imaginary);
	}
	public static Complex MUL(Complex c,double d)
	{
		return new Complex(c.real*d,c.imaginary*d);
	}
	public static Complex e(double w)
	{
		return new Complex(Math.cos(w),Math.sin(w));
	}
	public static Complex e2PI(int n,int exp)
	{
		double w=2.0*Math.PI/n*exp;
		return new Complex(Math.cos(w),Math.sin(w));
	}
	private boolean isClose(double d0,double d1)
	{
		double d=Math.abs(d0-d1);
		double e=1E-8;
		return (d<e);
	}
	public boolean equals(Complex c)
	{
		return (isClose(this.real,c.real)&&isClose(this.imaginary,c.imaginary));
	}
	public double length()
	{
		return Math.sqrt(real*real+imaginary*imaginary);
	}
}
class Transform2D
{
	private double x;
	private double y;
	public Transform2D(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	public void setToScale(double sx,double sy)
	{
		this.x/=sx;
		this.y/=sy;
	}
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
}
class Interpolator2D
{
	private int[] color;
	private int imageWidth,imageHeight;
	public Interpolator2D(int[] color,int imageWidth,int imageHeight)
	{
		this.color=color;
		this.imageWidth=imageWidth;
		this.imageHeight=imageHeight;
	}
	public int getImageInterpolatedValue(Transform2D transform2D)
	{
		double x=transform2D.getX();
		double y=transform2D.getY();
		if(isValid(x,y))return round(this.getLinearInterpolatedValue(x,y));
		else return 0;
	}
	private boolean isValid(double x,double y)
	{
		return (x>=0&&x<imageWidth-1&&y>=0&&y<imageHeight-1);
	}
	private int round(double x)
	{
		int roundX=(int)x;
		if(x-roundX>0.4)roundX++;
		return roundX;
	}
	private double linear(double x)
	{
		if(-1<x&&x<=1)return 1-Math.abs(x);
		else return 0;
	}
	private double linear2D(double x,double z)
	{
		return linear(x)*linear(z);
	}
	public double getLinearInterpolatedValue(double x,double y)
	{
		double linearInterpolatedValue=0;
		int x0=(int)x,y0=(int)y;
		int x1=x0+1,y1=y0+1;
		for(int i=y0;i<=y1;i++)
		{
			for(int j=x0;j<=x1;j++)
			{
				linearInterpolatedValue+=color[i*imageWidth+j]*linear2D(j-x,i-y);
			}
		}
		return linearInterpolatedValue;
	}
	public double getBiLinearInterpolatedValue(double x,double y)
	{
		int x0=(int)x,y0=(int)y;
		int x1=x0+1,y1=y0+1;
		double v=x-x0;
		double u=y-y0;
		int c00=color[y0*imageWidth+x0];
		int c01=color[y0*imageWidth+x1];
		int c10=color[y1*imageWidth+x0];
		int c11=color[y1*imageWidth+x1];
		return (1-u)*(1-v)*c00+(1-u)*(v)*c01+(u)*(1-v)*c10+(u)*(v)*c11;
	}
}