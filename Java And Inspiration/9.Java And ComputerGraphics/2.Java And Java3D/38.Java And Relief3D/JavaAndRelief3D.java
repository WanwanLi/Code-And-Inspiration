import java.io.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.imageio.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
public class JavaAndRelief3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f lightDirection=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,lightDirection);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new Relief3D(0.6,0.8,0.0001,0.1,0.8,Appearance1,"Image.jpg"));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Relief3D extends Shape3D
{
	private int imageWidth;
	private int imageHeight;
	private int[] pixels;
	public Relief3D(double width,double height,double depth,double thickness,double scale,Appearance appearance,String imageName)
	{
		this.getReliefImage(imageName,scale);
		Point3d P00=new Point3d(-width/2,-height/2,0);
		Point3d P01=new Point3d(-width/2,+height/2,0);
		Point3d P11=new Point3d(+width/2,+height/2,0);
		Point3d P10=new Point3d(+width/2,-height/2,0);
		QuadArray QuadArray1=new QuadArray((imageHeight-1)*(imageWidth-1)*4,QuadArray.COORDINATES|QuadArray.NORMALS);
		int index=0;
		for(int i=0;i<imageHeight-1;i++)
		{
			for(int j=0;j<imageWidth-1;j++)
			{
				Point3d p00=this.getBilinearInterpolatedPoint(P00,P01,P11,P10,imageHeight,imageWidth,i+0,j+0);
				Point3d p10=this.getBilinearInterpolatedPoint(P00,P01,P11,P10,imageHeight,imageWidth,i+1,j+0);
				Point3d p11=this.getBilinearInterpolatedPoint(P00,P01,P11,P10,imageHeight,imageWidth,i+1,j+1);
				Point3d p01=this.getBilinearInterpolatedPoint(P00,P01,P11,P10,imageHeight,imageWidth,i+0,j+1);
				QuadArray1.setCoordinate(index++,this.getReliefCoordinate(p00,i+0,j+0,depth,thickness));
				QuadArray1.setCoordinate(index++,this.getReliefCoordinate(p10,i+1,j+0,depth,thickness));
				QuadArray1.setCoordinate(index++,this.getReliefCoordinate(p11,i+1,j+1,depth,thickness));
				QuadArray1.setCoordinate(index++,this.getReliefCoordinate(p01,i+0,j+1,depth,thickness));

			}
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(QuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
	private Point3d getBilinearInterpolatedPoint(Point3d p00,Point3d p01,Point3d p11,Point3d p10,int row,int column,int i,int j)
	{
		double U=(j+0.0)/(column-1.0);
		double V=(row-1.0-i)/(row-1.0);
		Point3d pU0=ADD(MUL(p00,1-U),MUL(p10,U));
		Point3d pU1=ADD(MUL(p01,1-U),MUL(p11,U));
		Point3d pUV=ADD(MUL(pU0,1-V),MUL(pU1,V));
		return pUV;
	}
	private Point3d MUL(Point3d p,double k)
	{
		return new Point3d(p.x*k,p.y*k,p.z*k);
	}
	private Point3d ADD(Point3d p0,Point3d p1)
	{
		return new Point3d(p0.x+p1.x,p0.y+p1.y,p0.z+p1.z);
	}
	private Point3d getReliefCoordinate(Point3d p,int i,int j,double depth,double thickness)
	{
		double z=pixels[i*imageWidth+j]*depth+thickness;
		if(i==0||i==imageHeight-1||j==0||j==imageWidth-1)z=0;
		return new Point3d(p.x,p.y,z);
	}
	private void getReliefImage(String imageName,double scale)
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
		}
		catch(Exception e){System.err.println(e);}
		this.getGrayPixels(1,1,1);
		this.getScaledPixels(scale);
	}
	private void getGrayPixels(double r,double g,double b)
	{
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{

				int alpha=this.getAlpha(pixels[i*imageWidth+j]);
				int red=this.getRed(pixels[i*imageWidth+j]);
				int green=this.getGreen(pixels[i*imageWidth+j]);
				int blue=this.getBlue(pixels[i*imageWidth+j]);
				int gray=(int)((r*red+g*green+b*blue)/(r+g+b));
				this.pixels[i*imageWidth+j]=gray;
			}
		}
	}
	private void getScaledPixels(double scale)
	{
		if(scale<0)return;
		int scaledImageWidth=(int)(imageWidth*scale);
		int scaledImageHeight=(int)(imageHeight*scale);
		int[] scaledPixels=new int[scaledImageWidth*scaledImageHeight];
		for(int i=0;i<scaledImageHeight;i++)
		{
			for(int j=0;j<scaledImageWidth;j++)
			{
				double x=(i+0.0)/scale;
				double y=(j+0.0)/scale;
				scaledPixels[i*scaledImageWidth+j]=this.getBilinearInterpolatedGray(x,y);
			}
		}
		this.pixels=scaledPixels;
		this.imageWidth=scaledImageWidth;
		this.imageHeight=scaledImageHeight;
	}
	private int getBilinearInterpolatedGray(double x,double y)
	{
		int i0=(int)x;
		int i1=i0+1;
		int j0=(int)y;
		int j1=j0+1;
		if(i1>=imageHeight||j1>=imageWidth||i0>=imageHeight||j0>=imageWidth)return 0;
		int g00=this.pixels[i0*imageWidth+j0];
		int g01=this.pixels[i0*imageWidth+j1];
		int g11=this.pixels[i1*imageWidth+j1];
		int g10=this.pixels[i1*imageWidth+j0];
		double U=(x-i0)/1.0;
		double V=(y-j0)/1.0;
		double gU0=g00*(1-U)+g10*U;
		double gU1=g01*(1-U)+g11*U;
		double gUV=gU0*(1-V)+gU1*V;
		return round(gUV);
	}
	private int getAlpha(int pixel)
	{
		return pixel>>24;
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
	private int round(double x)
	{
		int x0=(int)x;
		double dx=x-x0;
		return (dx>0.4?(x0+1):x0);
	}
}