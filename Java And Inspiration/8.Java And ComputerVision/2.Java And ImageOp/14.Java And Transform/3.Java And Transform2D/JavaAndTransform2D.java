import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
public class JavaAndTransform2D
{
	public static void main(String[] args)
	{
		Frame_Transform2D frame_Transform2D=new Frame_Transform2D();
		frame_Transform2D.setVisible(true);
	}
}
class Frame_Transform2D extends Frame
{
	public Image Image1,Image2;
	int imageWidth,imageHeight;
	public Frame_Transform2D()
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
			ImageOp1.setToTranslation(50.7,50.7);
			ImageOp1.setToScale(2,2);
			ImageOp1.setToRotation(Math.PI/12,imageWidth/2,imageHeight/2);
			ImageOp1.setToScale(0.7,0.7);
			ImageOp1.setToTranslation(150,100);
			ImageOp1.setToShear(0,0.05);
			ImageOp1.setToRotation(Math.PI/12,150,100);
			ImageOp1.setToTwirl(Math.PI/12,imageWidth/2,imageHeight/2,imageHeight/3);
			ImageOp1.setToWave(1.2,10,imageHeight,imageWidth);
			ImageOp1.setToLens(1.1,imageWidth/2,imageHeight/2,imageWidth/4);
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
class Transform2D
{
	private double x;
	private double y;
	public Transform2D(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	public void setToTranslation(double tx,double ty)
	{
		this.x-=tx;
		this.y-=ty;
	}
	public void setToScale(double sx,double sy)
	{
		this.x/=sx;
		this.y/=sy;
	}
	public void setToRotation(double theta)
	{
		double X=this.x*Math.cos(-theta)-this.y*Math.sin(-theta);
		double Y=this.x*Math.sin(-theta)+this.y*Math.cos(-theta);
		this.x=X;
		this.y=Y;
	}
	public void setToRotation(double theta,double cx,double cy)
	{
		this.setToTranslation(cx,cy);
		this.setToRotation(theta);
		this.setToTranslation(-cx,-cy);
	}
	public void setToShear(double shx,double shy)
	{
		double X=this.x-this.y*shx;
		double Y=this.y-this.x*shy;
		this.x=X;
		this.y=Y;
	}
	public void setTransform(double m00,double m10,double m01,double m11,double m02,double m12)
	{
		double X=this.x*m00+this.y*m01+m02;
		double Y=this.x*m10+this.y*m11+m12;
		this.x=X;
		this.y=Y;
	}
	public void setToTwirl(double theta,double cx,double cy,double maxr)
	{
		double dx=this.x-cx;
		double dy=this.y-cy;
		double r=Math.sqrt(dx*dx+dy*dy);
		if(dx==0&&dy==0)return;
		double a=Math.atan(dy/dx)-theta*(maxr-r)/maxr;
		if(dx<0)a+=Math.PI;
		this.x=cx+r*Math.cos(a);
		this.y=cy+r*Math.sin(a);
	}
	public void setToWave(double rx,double ry,double lx,double ly)
	{
		double kx=Math.PI*2/lx;
		double ky=Math.PI*2/ly;
		double X=this.x-Math.sin(kx*this.y)*rx;
		double Y=this.y-Math.sin(ky*this.x)*ry;
		this.x=X;
		this.y=Y;
	}
	public void setToLens(double p,double cx,double cy,double maxr)
	{
		double dx=this.x-cx;
		double dy=this.y-cy;
		double r=Math.sqrt(dx*dx+dy*dy);
		if(r==0||r>=maxr)return;
		double z=Math.sqrt(maxr*maxr-r*r);
		double thetaX=Math.atan(dx/z)/p;
		double thetaY=Math.atan(dy/z)/p;
		this.x=cx+z*Math.tan(thetaX);
		this.y=cy+z*Math.tan(thetaY);
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
	public void setToTranslation(double tx,double ty)
	{
		this.createARGBInterpolator2D();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Transform2D transform2D=new Transform2D(j,i);
				transform2D.setToTranslation(tx,ty);
				int alpha=Interpolator2D_Alpha.getImageInterpolatedValue(transform2D);
				int red=Interpolator2D_Red.getImageInterpolatedValue(transform2D);
				int green=Interpolator2D_Green.getImageInterpolatedValue(transform2D);
				int blue=Interpolator2D_Blue.getImageInterpolatedValue(transform2D);
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public void setToScale(double sx,double sy)
	{
		this.createARGBInterpolator2D();
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
	public void setToRotation(double theta)
	{
		this.createARGBInterpolator2D();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Transform2D transform2D=new Transform2D(j,i);
				transform2D.setToRotation(theta);
				int alpha=Interpolator2D_Alpha.getImageInterpolatedValue(transform2D);
				int red=Interpolator2D_Red.getImageInterpolatedValue(transform2D);
				int green=Interpolator2D_Green.getImageInterpolatedValue(transform2D);
				int blue=Interpolator2D_Blue.getImageInterpolatedValue(transform2D);
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public void setToRotation(double theta,double cx,double cy)
	{
		this.createARGBInterpolator2D();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Transform2D transform2D=new Transform2D(j,i);
				transform2D.setToRotation(theta,cx,cy);
				int alpha=Interpolator2D_Alpha.getImageInterpolatedValue(transform2D);
				int red=Interpolator2D_Red.getImageInterpolatedValue(transform2D);
				int green=Interpolator2D_Green.getImageInterpolatedValue(transform2D);
				int blue=Interpolator2D_Blue.getImageInterpolatedValue(transform2D);
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public void setToShear(double shx,double shy)
	{
		this.createARGBInterpolator2D();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Transform2D transform2D=new Transform2D(j,i);
				transform2D.setToShear(shx,shy);
				int alpha=Interpolator2D_Alpha.getImageInterpolatedValue(transform2D);
				int red=Interpolator2D_Red.getImageInterpolatedValue(transform2D);
				int green=Interpolator2D_Green.getImageInterpolatedValue(transform2D);
				int blue=Interpolator2D_Blue.getImageInterpolatedValue(transform2D);
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public void setToTwirl(double theta,double cx,double cy,double maxr)
	{
		this.createARGBInterpolator2D();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Transform2D transform2D=new Transform2D(j,i);
				transform2D.setToTwirl(theta,cx,cy,maxr);
				int alpha=Interpolator2D_Alpha.getImageInterpolatedValue(transform2D);
				int red=Interpolator2D_Red.getImageInterpolatedValue(transform2D);
				int green=Interpolator2D_Green.getImageInterpolatedValue(transform2D);
				int blue=Interpolator2D_Blue.getImageInterpolatedValue(transform2D);
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public void setToWave(double rx,double ry,double lx,double ly)
	{
		this.createARGBInterpolator2D();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Transform2D transform2D=new Transform2D(j,i);
				transform2D.setToWave(rx,ry,lx,ly);
				int alpha=Interpolator2D_Alpha.getImageInterpolatedValue(transform2D);
				int red=Interpolator2D_Red.getImageInterpolatedValue(transform2D);
				int green=Interpolator2D_Green.getImageInterpolatedValue(transform2D);
				int blue=Interpolator2D_Blue.getImageInterpolatedValue(transform2D);
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public void setToLens(double p,double cx,double cy,double maxr)
	{
		this.createARGBInterpolator2D();
		for(int i=0;i<ImageHeight;i++)
		{
			for(int j=0;j<ImageWidth;j++)
			{
				Transform2D transform2D=new Transform2D(j,i);
				transform2D.setToLens(p,cx,cy,maxr);
				int alpha=Interpolator2D_Alpha.getImageInterpolatedValue(transform2D);
				int red=Interpolator2D_Red.getImageInterpolatedValue(transform2D);
				int green=Interpolator2D_Green.getImageInterpolatedValue(transform2D);
				int blue=Interpolator2D_Blue.getImageInterpolatedValue(transform2D);
				Pixels[i*ImageWidth+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
		}
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(ImageWidth,ImageHeight,Pixels,0,ImageWidth);
		return MemoryImageSource1;
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
class FunctionGenerator
{
	private double[] x;
	private double[] y;
	private int n;
	public FunctionGenerator(double[] x,double[] y,int n)
	{
		this.n=n;
		this.x=new double[n];
		this.y=new double[n];
		for(int i=0;i<n;i++)
		{
			this.x[i]=x[i];
			this.y[i]=y[i];
		}
	}
	private void sortX()
	{
		for(int i=0;i<n-1;i++)
		{
			int k=i;
			double minX=x[i];
			for(int j=i+1;j<n;j++)
			{
				if(x[j]<minX)
				{
					k=j;
					minX=x[j];
				}
			}
			if(k!=i)
			{
				double xi=x[i];
				x[i]=x[k];
				x[k]=xi;
				double yi=y[i];
				y[i]=y[k];
				y[k]=yi;
			}
		}
	}
	public void printXY()
	{
		for(int i=0;i<n;i++)System.out.println("x["+i+"]="+x[i]+"\t"+"y["+i+"]="+y[i]);
	}
	private void sortY()
	{
		
	}
}