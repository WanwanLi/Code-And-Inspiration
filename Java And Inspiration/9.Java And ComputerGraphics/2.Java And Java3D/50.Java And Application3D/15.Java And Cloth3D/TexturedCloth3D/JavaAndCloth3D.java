import java.io.*;
import java.awt.*;
import java.applet.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.imageio.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndCloth3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		TextureLoader TextureLoader1=new TextureLoader("stage.jpg",null);
		ImageComponent2D imageComponent2D=TextureLoader1.getImage();
		Background Background1=new Background(imageComponent2D);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,-1f,-1f);vector3f.normalize();
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
		Material Material1=new Material();
		Material1.setSpecularColor(new Color3f(0f,0f,0f));
		Vector3d force=new Vector3d(0,-1,0);
		double r=0.5,d=0.3,p=0.01,k=300.0,mass=1.0;int times=400;
		Cloth3D cloth3D=new Cloth3D(new Function_Plane(),-r,r,-r,r,mass);
		cloth3D.fixCoordinates(-d,d,-d,d);
		cloth3D.applyForce(force,k,times);
		cloth3D.setGeometry(cloth3D.getStriangleStripArray());
		cloth3D.setAppearance(cloth3D.getImageComponent2DAppearance("cloth.jpg",Material1));
		TransformGroup1.addChild(cloth3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Math3d
{
	public static Vector3d subtract(Point3d p0,Point3d p1)
	{
		double dx=p1.x-p0.x;
		double dy=p1.y-p0.y;
		double dz=p1.z-p0.z;
		return new Vector3d(dx,dy,dz);
	}
	public static void add(Vector3d v0,Vector3d v1)
	{
		v0.x+=v1.x;
		v0.y+=v1.y;
		v0.z+=v1.z;
	}
}
class Function_Plane implements Function
{
	public Point3d surface(double x,double z)
	{
		return new Point3d(x,0,z);
	}
}
class Function_SemiSphere implements Function
{
	double r;
	public Function_SemiSphere(double r)
	{
		this.r=r;
	}
	public Point3d surface(double x,double z)
	{
		double r=Math.sqrt(x*x+z*z);
		if(r>=this.r)return new Point3d(x,0,z);
		double r1=r/this.r,y=Math.sqrt(1-r1*r1);
		return new Point3d(x,this.r*y,z);
	}
}
interface Function
{
	Point3d surface(double u,double v);
}
class Cloth3D extends Shape3D
{
	private int n=80,m=80;
	private double u0,v0,du,dv,mass;
	private Point3d[] coordinates=new Point3d[m*n];
	private TexCoord2f[]  texCoords=new TexCoord2f[m*n];
	private boolean[] isFixedCoordinate=new boolean[m*n];
	private double[][] relaxedDistances=new double[m*n][4];
	public Cloth3D(Function function,double u0,double u1,double v0,double v1,double mass)
	{
		this.mass=mass*1000.0;
		this.u0=u0;this.v0=v0;
		this.du=(u1-u0)/(n-1);
		this.dv=(v1-v0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double u=u0+i*du;
			for(int j=0;j<n;j++)
			{
				double v=v0+j*dv;
				this.coordinates[i*n+j]=function.surface(v,u);
				this.texCoords[i*n+j]=new TexCoord2f(i*1.0f/m,j*1.0f/n);
			}
		}
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				this.isFixedCoordinate[i*n+j]=false;
				this.getRelaxedDistance(i,j);
			}
		}
	}
	public void fixCoordinates(double u0,double u1,double v0,double v1)
	{
		int i0=(int)((u0-this.u0)/this.du);
		int i1=(int)((u1-this.u0)/this.du);
		int j0=(int)((v0-this.v0)/this.dv);
		int j1=(int)((v1-this.v0)/this.dv);
		for(int i=i0;i<=i1;i++)
		{
			for(int j=j0;j<=j1;j++)
			{
				this.isFixedCoordinate[i*n+j]=true;
			}
		}
	}
	public void applyForce(Vector3d force,double k,int times)
	{
		for(int t=0;t<times;t++)
		{
			for(int i=0;i<m;i++)
			{
				for(int j=0;j<n;j++)
				{
					if(isFixedCoordinate[i*n+j])continue;
					Vector3d f=this.getForce(force,k,i,j);
					this.coordinates[i*n+j].x+=f.x/mass;
					this.coordinates[i*n+j].y+=f.y/mass;
					this.coordinates[i*n+j].z+=f.z/mass;
				}
			}
		}
	}
	private Vector3d getForce(Vector3d f,double k,int i,int j)
	{
		int i0=i-1,i1=i+1,j0=j-1,j1=j+1;
		Vector3d force=new Vector3d(f.x,f.y,f.z);
		Math3d.add(force,elasticForce(k,i,j,i0,j0,0,0));
		Math3d.add(force,elasticForce(k,i,j,i0,j1,0,1));
		Math3d.add(force,elasticForce(k,i,j,i1,j0,1,0));
		Math3d.add(force,elasticForce(k,i,j,i1,j1,1,1));
		return force;
	}
	private void getRelaxedDistance(int i,int j)
	{
		int i0=i-1,i1=i+1,j0=j-1,j1=j+1;
		this.relaxedDistances[i*n+j][0*2+0]=this.getDistance(i,j,i0,j0);
		this.relaxedDistances[i*n+j][0*2+1]=this.getDistance(i,j,i0,j1);
		this.relaxedDistances[i*n+j][1*2+0]=this.getDistance(i,j,i1,j0);
		this.relaxedDistances[i*n+j][1*2+1]=this.getDistance(i,j,i1,j1);
	}
	private double getDistance(int i0,int j0,int i1,int j1)
	{
		if(isOutOfRange(i1,j1))return 0.0;
		Point3d p0=this.coordinates[i0*n+j0];
		Point3d p1=this.coordinates[i1*n+j1];
		Vector3d distance=Math3d.subtract(p0,p1);
		return distance.length();
	}
	private Vector3d elasticForce(double k,int i0,int j0,int i1,int j1,int u,int v)
	{
		if(isOutOfRange(i1,j1))return new Vector3d(0,0,0);
		Point3d p0=this.coordinates[i0*n+j0];
		Point3d p1=this.coordinates[i1*n+j1];
		Vector3d dir=Math3d.subtract(p0,p1);
		double l0=relaxedDistances[i0*n+j0][u*2+v];
		double l=dir.length(),dl=l0-l; dir.normalize();
		return new Vector3d(-k*dl*dir.x,-k*dl*dir.y,-k*dl*dir.z);
	}
	private boolean isOutOfRange(int i,int j)
	{
		return i<0||i>=m||j<0||j>=n;
	}
	public GeometryArray getStriangleStripArray()
	{
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
		int[] stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
		IndexedTriangleStripArray IndexedTriangleStripArray1=new IndexedTriangleStripArray
		(
			coordinates.length,
			IndexedTriangleStripArray.COORDINATES|
			IndexedTriangleStripArray.NORMALS|
			IndexedTriangleStripArray.TEXTURE_COORDINATE_2,
			coordinateIndices.length,
			stripCounts
		);
		IndexedTriangleStripArray1.setCoordinates(0,coordinates);
		IndexedTriangleStripArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedTriangleStripArray1.setTextureCoordinates(0,0,texCoords);
		IndexedTriangleStripArray1.setTextureCoordinateIndices(0,0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedTriangleStripArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		return GeometryInfo1.getGeometryArray();
	}
	public Appearance getImageComponent2DAppearance(String imageName,Material material)
	{
		Appearance imageComponent2DAppearance=new Appearance();
		int imageWidth=1024,imageHeight=1024;
		BufferedImage BufferedImage1=new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_ARGB);
		BufferedImage BufferedImage2=null;
		try{BufferedImage2=ImageIO.read(new File(imageName));}catch(Exception e){}
		double scaleX=(imageWidth+0.0)/BufferedImage2.getWidth();
		double scaleY=(imageHeight+0.0)/BufferedImage2.getHeight();
		AffineTransform AffineTransform1=new AffineTransform();
		AffineTransform1.setToScale(scaleX,scaleY);
		AffineTransformOp AffineTransformOp1=new AffineTransformOp(AffineTransform1,AffineTransformOp.TYPE_BILINEAR);
		BufferedImage BufferedImage3=AffineTransformOp1.filter(BufferedImage2,null);
		ImageComponent2D imageComponent2D=new ImageComponent2D(ImageComponent2D.FORMAT_RGBA,BufferedImage3);
		Texture2D texture2D=new Texture2D(Texture.BASE_LEVEL,Texture.RGBA,imageComponent2D.getWidth(),imageComponent2D.getHeight());
		texture2D.setImage(0,imageComponent2D);
		texture2D.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		imageComponent2DAppearance.setTexture(texture2D);
		TextureAttributes TextureAttributes1=new TextureAttributes();
		TextureAttributes1.setTextureMode(TextureAttributes.COMBINE);
		imageComponent2DAppearance.setTextureAttributes(TextureAttributes1);
		imageComponent2DAppearance.setMaterial(material);
		return imageComponent2DAppearance;
	}
}
