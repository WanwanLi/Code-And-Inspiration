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
public class JavaAndScenary3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);vector3f.normalize();
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		vector3f=new Vector3f(0f,-0.5f,1f);vector3f.normalize();
		DirectionalLight1=new DirectionalLight(color3f,vector3f);
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
		Transform3D transform3D=new Transform3D();
		transform3D.rotX(Math.PI/40);
		transform3D.setScale(new Vector3d(0.5,0.5,0.5));
		TransformGroup1.setTransform(transform3D);
		String imageName="grass.jpg";
		Function_Hill function_Hill=new Function_Hill(0.8,-1.5,1.5,-1,1,0.4,40);
		TransformGroup1.addChild(new Surface3D(imageName,function_Hill,-1.5,1.5,-1,1,Material1));
		function_Hill.inverse=1;
		TransformGroup1.addChild(new Surface3D(imageName,function_Hill,-1.5,1.5,-1,1,0,Material1));
		Function_Wave function_Wave=new Function_Wave(0.01,-6.0,6.0,-5.0,5.0,20,10);
		Appearance Appearance1=new Appearance();
		Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0.2f,0.4f,1f));
		Appearance1.setMaterial(Material1);
		TransparencyAttributes TransparencyAttributes1=new TransparencyAttributes(TransparencyAttributes.BLENDED,0.3f);
		Appearance1.setTransparencyAttributes(TransparencyAttributes1);
		Appearance1.setMaterial(Material1);
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0,0.05,0));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Surface3D(function_Wave,-4.0,4.0,-3.0,8.0,Appearance1));
		TransformGroup1.addChild(TransformGroup2);
		Sphere skySphere=new Sphere(6f,Sphere.GENERATE_NORMALS_INWARD|Sphere.GENERATE_TEXTURE_COORDS,100);
		skySphere.getAppearance().setTexture(new TextureLoader("sky.jpg",null).getTexture());
		transform3D=new Transform3D();
		transform3D.rotY(Math.PI/2);
		TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(skySphere);
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Function_Hill implements Function
{
	public int inverse=0;
	public int hillsNumber=0;
	public TexCoord2f[] centers;
	public TexCoord3f[] features;
	public Function_Hill(double H,double x0,double x1,double z0,double z1,double e,int n)
	{
		this.hillsNumber=n;
		this.centers=new TexCoord2f[n];
		this.features=new TexCoord3f[n];
		double W=(x1-x0)/10,D=(z1-z0)/10;
		for(int i=0;i<n;i++)
		{
			float x=this.random(x0,x1);
			float z=this.random(z0,z1);
			centers[i]=new TexCoord2f(x,z);
			float w=this.random(D,W);
			float d=this.random(D/2,D);
			float h=this.random(H/10,H);
			if(x-x0<e||x1-x<e||z-z0<e||z1-z<e)h=0;
			features[i]=new TexCoord3f(w,d,h);
		}
	}
	public Point3d surface(double x,double z)
	{
		double y=0.0;
		for(int i=0;i<this.hillsNumber;i++)
		{
			y+=Gauss2D(x,z,centers[i],features[i]);
		}
		if(inverse!=0)y=-y;
		return new Point3d(x,y,z);
	}
	public double Gauss2D(double x,double z,TexCoord2f center,TexCoord3f feature)
	{
		double u=(x-center.x)/feature.x;
		double v=(z-center.y)/feature.y;
		return Math.exp(-u*u-v*v)*feature.z;
	}
	public float random(double x0,double x1)
	{
		return (float)(x0+(x1-x0)*Math.random());
	}
}
class Function_Wave implements Function
{
	public double decay=0.85;
	public double frequency=0.2;
	public double amplitude=0.2;
	public int wavesNumber=10;
	public TexCoord2f[] centers;
	public Function_Wave(double A,double x0,double x1,double z0,double z1,double w,int n)
	{
		this.frequency=w;
		this.amplitude=A;
		this.wavesNumber=n;
		this.centers=new TexCoord2f[n];
		for(int i=0;i<n;i++)
		{
			float x=this.random(x0,x1);
			float z=this.random(z0,z1);
			centers[i]=new TexCoord2f(x,z);
		}
	}
	public Point3d surface(double x,double z)
	{
		double y=0.0;
		for(int i=0;i<this.wavesNumber;i++)
		{
			y+=Wave2D(x,z,centers[i]);
		}
		return new Point3d(x,y,z);
	}
	public double Wave2D(double x,double z,TexCoord2f center)
	{
		double u=x-center.x,v=z-center.y;
		double r=Math.sqrt(u*u+v*v);
		double A=this.amplitude;
		double w=this.frequency;
		double y=A*Math.cos(w*r);
		return y*Math.exp(-r*this.decay);
	}
	public float random(double x0,double x1)
	{
		return (float)(x0+(x1-x0)*Math.random());
	}
}
interface Function
{
	Point3d surface(double u,double v);
}
class Surface3D extends Shape3D
{
	public int n=100,m=100;
	public Point3d[] coordinates=new Point3d[m*n];
	public TexCoord2f[]  texCoords=new TexCoord2f[m*n];
	public Surface3D(Function function,double u0,double u1,double v0,double v1,Appearance appearance)
	{
		double du=(u1-u0)/(n-1),dv=(v1-v0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double v=v0+i*dv;
			for(int j=0;j<n;j++)
			{
				double u=u0+j*du;
				this.coordinates[i*n+j]=function.surface(u,v);
				this.texCoords[i*n+j]=new TexCoord2f(i*1.0f/m,j*1.0f/n);
			}
		}
		this.setGeometry(this.getStriangleStripArray());
		this.setAppearance(appearance);
	}
	public Surface3D(String imageName,Function function,double u0,double u1,double v0,double v1,Material material)
	{
		double du=(u1-u0)/(n-1),dv=(v1-v0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double v=v0+i*dv;
			for(int j=0;j<n;j++)
			{
				double u=u0+j*du;
				this.coordinates[i*n+j]=function.surface(u,v);
				this.texCoords[i*n+j]=new TexCoord2f(i*1.0f/m,j*1.0f/n);
			}
		}
		this.setGeometry(this.getStriangleStripArray());
		this.setAppearance(this.getImageComponent2DAppearance(imageName,material));
	}
	public Surface3D(String imageName,Function function,double u0,double u1,double v0,double v1,int doubleSurface,Material material)
	{
		double du=(u1-u0)/(n-1),dv=(v1-v0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double v=v0+i*dv;
			for(int j=0;j<n;j++)
			{
				double u=u0+j*du;
				this.coordinates[i*n+j]=function.surface(u,v);
				this.texCoords[i*n+j]=new TexCoord2f(i*1.0f/m,j*1.0f/n);
			}
		}
		this.setGeometry(this.getStriangleStripArray(doubleSurface));
		this.setAppearance(this.getImageComponent2DAppearance(imageName,material));
	}
	public GeometryArray getStriangleStripArray()
	{
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
	public GeometryArray getStriangleStripArray(int doubleSurface)
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
		int[] stripCounts=new int[2*(m-1)];
		for(int i=0;i<2*(m-1);i++)stripCounts[i]=2*n;
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
