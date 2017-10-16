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
public class JavaAndSculpture3D
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
		String imageName="Sculpture3D\\8.jpg"; double width=1.5,height=1.5,tilt=-1,slant=-1; int time=100;
		Function_Image Function_Image1=new Function_Image(imageName,width,height,time,new Vector3d(0,0,1));
		TransformGroup1.addChild(new Surface3D(Function_Image1,0,1,0,1,false,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Function_Image implements Function
{
	private int[] pixels;
	private double[][] depths;
	private double width,height;
	private int imageHeight,imageWidth;
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
			double albedo=1500000000;
			double[] brightness = this.getBrightnessValues();
			ImageShape3D ImageShape3d=new ImageShape3D(albedo, brightness, imageWidth, imageHeight);
			//ImageShape3d.setLightPosition(0,0,0);
			//ImageShape3d.setCameraParameters(100,100,300);
			ImageShape3d.setCameraParameters(128,128,300);
			this.depths=ImageShape3d.getDepthMap(-0.005,400,100);
			//this.depths=ImageShape3d.getDepthMap(-0.005,400,200);
			this.width=width; this.height=height;
		}
		catch(Exception e){System.err.println(e);}
	}
	private double[] getBrightnessValues()
	{
		ColorModel ColorModel1=ColorModel.getRGBdefault();
		double[] brightness=new double[imageHeight*imageWidth];
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				brightness[i*imageWidth+j]=this.getBrightness(ColorModel1,i,j);
			}
		}
		return brightness;
	}
	private double getBrightness(ColorModel colorModel,int i,int j)
	{
		double red=colorModel.getRed(pixels[i*imageWidth+j]);
		double green=colorModel.getGreen(pixels[i*imageWidth+j]);
		double blue=colorModel.getBlue(pixels[i*imageWidth+j]);
		return Math.max(red,Math.max(green,blue));
	}
	private double interpolate(double u,double v)
	{
		int j0=(int)(v*imageWidth);
		int i0=(int)(u*imageHeight);
		int j1=j0+1, i1=i0+1; 
		double U=u*imageHeight-i0;
		double V=v*imageWidth-j0;
		while(i0>=imageHeight)i0--;
		while(j0>=imageWidth)j0--;
		while(i1>=imageHeight)i1--;
		while(j1>=imageWidth)j1--;
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
