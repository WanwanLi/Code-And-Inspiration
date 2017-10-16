import java.io.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.imageio.*;
import javax.vecmath.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.behaviors.mouse.*;
public class JavaAndLandscape3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,0f));
		Appearance1.setMaterial(Material1);
		String imageName="Landscape\\3.jpg"; double width=1.5,height=1.0,tilt=-1,slant=-1; int time=100;
		Function_Image Function_Image1=new Function_Image(imageName,width,height,time,new Vector3d(0,0,1));
		TransformGroup1.addChild(new Surface3D(Function_Image1,0,1,0,1,false,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Math3d
{
	public static double sqrt2(double x,double y)
	{
		return Math.sqrt(x*x+y*y);
	}
	public static double[][][] gradDir(double[][] E)
	{
		double L, error= 1e-2;
		double[][][] G=new double[2][E.length][E[0].length]; 
		for(int i=0;i<E.length;i++)
		{
			for(int j=0;j<E[0].length;j++)
			{
				if(j-1< 0||i-1< 0)G[0][i][j]=G[1][i][j]=0;
				else
				{
					G[0][i][j] = E[i][j] - E[i][(j-1)];
					G[1][i][j] = E[i][j] - E[i-1][j];
					L= sqrt2(G[0][i][j],G[1][i][j]);
					G[0][i][j] /= L+ error;
					G[1][i][j] /= L+ error;
				}
				
			}
		}
		return G;
	}
	public static double max(double[][] E)
	{
		double m=Double.MIN_VALUE;
		for(int i=0;i<E.length;i++)
		{
			for(int j=0;j<E[0].length;j++)
			{
				m=Math.max(m,E[i][j]);
			}
		}
		return m;
	}
	public static double mean(double[][] E)
	{
		int n=0; double sum=0.0;
		for(int i=0;i<E.length;i++)
		{
			for(int j=0;j<E[0].length;j++)
			{
				sum+=E[i][j];n++;
			}
		}
		return sum/n;
	}
	public static double mean2(double[][] E)
	{
		int n=0; double sum2=0.0;
		for(int i=0;i<E.length;i++)
		{
			for(int j=0;j<E[0].length;j++)
			{
				sum2+=E[i][j]*E[i][j];n++;
			}
		}
		return sum2/n;
	}
}
class Function_Image implements Function
{
	private int[] pixels;
	private double[][] depths;
	private double width,height;
	private int imageWidth,imageHeight;
	public Function_Image(String imageName,double width,double height,int time,Vector3d lightDirection)
	{
		try
		{
			Frame Frame1=new Frame();
			Image Image1=Toolkit.getDefaultToolkit().getImage(imageName);
			MediaTracker MediaTracker1=new MediaTracker(Frame1);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			this.imageWidth=Image1.getWidth(Frame1);
			this.imageHeight=Image1.getHeight(Frame1);
			this.pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
			this.width=width; this.height=height;
			//this.depths=this.getShapeFromShading(time,lightDirection);
			this.depths=this.getShapeFromShading(time,0,0);
		}
		catch(Exception e){System.err.println(e);}
	}
	private double[][] getShapeFromShading(int time,Vector3d light)
	{
		if(light.x == 0 && light.y == 0) light.x = light.y = 0.01;
		double Ps= light.x/light.z, Qs = light.y/light.z;
		double p,q,pq,PQs,fZ,dfZ,Eij,Wn=0.0001*0.0001,Y,K;
		double[][] Si=new double[imageHeight][imageWidth];
		double[][] Zn=new double[imageHeight][imageWidth];
		double[][] Si1=new double[imageHeight][imageWidth];
		double[][] Zn1=new double[imageHeight][imageWidth];
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				Zn1[i][j] = 0.0;
				Si1[i][j] = 1.0;
			}
		}
		for(int t=0;t<time;t++)
		{
			for(int i=0;i<imageHeight;i++)
			{
				for(int j=0;j<imageWidth;j++)
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
			for(int i=0;i<imageHeight;i++)
			{
				for(int j=0;j<imageWidth;j++)
				{
					Zn1[i][j] = Zn[i][j];
					Si1[i][j] = Si[i][j];
				}
			}
System.out.println(t+"....");
		}
		double scale=1e-3;
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				Zn[i][j] *= scale;
			}
		}
		return Zn;
	}
	private double[] getIlluminationInfo(double[][] brightness)
	{
		double PI=Math.PI;
		double Max = Math3d.max(brightness);
System.out.println(Max);
		double E = Math3d.mean(brightness);
		double E2 = Math3d.mean2(brightness);
System.out.println(E+" , "+E2);
		double[][][] G = Math3d.gradDir(brightness);
		double Mex = Math3d.mean(G[0]);
		double Mey = Math3d.mean(G[1]);
		double tilt = Math.atan(Mey/Mex); if (tilt < 0)tilt+=PI;
		double gamma = Math.sqrt(6 *PI*PI*E2- 48 * E*E);
System.out.println(6 *PI*PI*E2- 48 * E*E);
System.out.println("gamma = "+gamma );
		double albedo = gamma/PI, slant = Math.acos(Math.min(4*E/gamma,1));
System.out.println("ga="+4*E/gamma);
System.out.println("acos(ga)="+slant);
		return new double[]{albedo,tilt,slant};
	}
	private double getReflectanceValue(double normalX,double normalY,double tilt,double slant)
	{
		double p=normalX,q=normalY,a=tilt,b=slant;
		double r=p*Math.cos(a)*Math.sin(b)+q*Math.sin(a)*Math.sin(b);
		return Math.max(0.0,(Math.cos(b)+r)/Math.sqrt(1+p*p+q*q));
	}
	private double getDerivativeValue(double normalX,double normalY,double lightX,double lightY)
	{
		double p=normalX,q=normalY,ix=lightX,iy=lightY,l2=1+p*p+q*q;
		double D=(p+q)*(ix*p + iy*q + 1)/(Math.sqrt(l2*l2*l2)*Math.sqrt(1 + ix*ix + iy*iy));
		return D-(ix+iy)/(Math.sqrt(l2)*Math.sqrt(1 + ix*ix + iy*iy));
	}
	private double[][] getShapeFromShading(int time,double tilt,double slant)
	{
		double[][] E=this.getBrightnessValues();
		if(tilt<0&&slant<0)
		{
			double[] info = getIlluminationInfo(E);
			tilt=info[1]; slant=info[2];
System.out.println((tilt/Math.PI)+" PI "+ (slant/Math.PI) +" PI ");
		}
		double Ix=Math.cos(tilt)*Math.tan(slant);
		double Iy=Math.sin(tilt)*Math.tan(slant);
		double[][] Z=new double[imageHeight][imageWidth];
		double[][] P=new double[imageHeight][imageWidth];
		double[][] Q=new double[imageHeight][imageWidth];
		for(int t=0;t<0*time;t++)
		{
			for(int i=0;i<imageHeight;i++)
			{
				for(int j=0;j<imageWidth;j++)
				{
					if(j-1< 0||i-1< 0)P[i][j]=Q[i][j]=0;
					else
					{
						P[i][j] = Z[i][j] - Z[i][j-1];
						Q[i][j] = Z[i][j] - Z[i-1][j];
					}
					double R = getReflectanceValue(P[i][j],Q[i][j],tilt,slant);
					double D = getDerivativeValue(P[i][j],Q[i][j],Ix,Iy);
					Z[i][j] -= (E[i][j]-R)/(D+0.001);
				}
			}
			if(t%10==0)System.out.println("Iterated "+t+" times....");
		}
		double scale=1e-6;
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				Z[i][j] = Math.abs(Z[i][j]*scale);
			}
		}
		return E;
	}
	private double[][] getBrightnessValues()
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		double[][] B=new double[imageHeight][imageWidth];
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				B[i][j]=this.getBrightness(ColorModel1,i,j);
			}
		}
		return B;
	}
	private double getBrightness(ColorModel colorModel,int i,int j)
	{
		double red=colorModel.getRed(pixels[i*imageWidth+j]);
		double green=colorModel.getGreen(pixels[i*imageWidth+j]);
		double blue=colorModel.getBlue(pixels[i*imageWidth+j]);
		return Math.max(red,Math.max(green,blue))/255.0/10;
	}
	private double sqrt(double x){return Math.sqrt(x);}
	private double interpolate(double u,double v)
	{
		int j0=(int)(v*imageWidth);
		int i0=(int)(u*imageHeight);
		int j1=j0+1, i1=i0+1; 
		double U=u*imageHeight-i0;
		double V=v*imageWidth-j0;
		if(i1>=imageHeight||j1>=imageWidth)return 0;
		if(i0>=imageHeight||j0>=imageWidth)return 0;
		double b00=this.depths[i0][j0];
		double b01=this.depths[i0][j1];
		double b11=this.depths[i1][j1];
		double b10=this.depths[i1][j0];
		double bU0=b00*(1-U)+b10*U;
		double bU1=b01*(1-U)+b11*U;
		double bUV=bU0*(1-V)+bU1*V;
		return bUV;
	}
	public Point3d surface(double u,double v)
	{
		double x=width*(u-0.5);
		double z=height*(v-0.5);
		double y=interpolate(u,v);
		return new Point3d(x,y,z);
	}
}
interface Function
{
	Point3d surface(double u,double v);
}
class Surface3D extends Shape3D
{
	public int n=200,m=200;
	public Point3d[] coordinates=new Point3d[m*n];
	public Surface3D(Function function,double u0,double u1,double v0,double v1,boolean isDoubleSurface,Appearance appearance)
	{
		double du=(u1-u0)/(n-1),dv=(v1-v0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double v=v0+i*dv;
			for(int j=0;j<n;j++)
			{
				double u=u0+j*du;
				this.coordinates[i*n+j]=function.surface(u,v);
			}
		}
		this.setGeometry(this.getStriangleStripArray(isDoubleSurface));
		this.setAppearance(appearance);
	}
	GeometryArray getStriangleStripArray()
	{
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		int[] coordinateIndices=new int[2*(m-1)*n];
		int v=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		return GeometryInfo1.getGeometryArray();
	}
	GeometryArray getStriangleStripArray(boolean isDoubleSurface)
	{
		if(!isDoubleSurface)return this.getStriangleStripArray();
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		int[] coordinateIndices=new int[4*(m-1)*n];
		int v=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;

			}
		}
		for(int i=1;i<m;i++)
		{
			for(int j=n-1;j>=0;j--)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[2*(m-1)];
		for(int i=0;i<2*(m-1);i++)stripCounts[i]=2*n;
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		return GeometryInfo1.getGeometryArray();
	}

}
