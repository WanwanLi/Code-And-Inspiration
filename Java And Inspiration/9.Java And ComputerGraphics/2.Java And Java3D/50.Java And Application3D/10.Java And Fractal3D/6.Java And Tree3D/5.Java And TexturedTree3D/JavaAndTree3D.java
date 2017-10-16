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
public class JavaAndTree3D extends Applet
{
	public void init()
	{
		GraphicsConfiguration GraphicsConfiguration1=SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D=new Canvas3D(GraphicsConfiguration1);
		this.setLayout(new BorderLayout());
		this.add(canvas3D);	
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Color3f color3f=new Color3f(0f,0f,0f);
		Background Background1=new Background(color3f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		Point3d root=new Point3d(0,-0.5,0);
		int branchNumber=5,level=6;
		double branchDecay=0.7,radiusDecay=0.6;
		double size=0.4,radius=0.04,angle=Math.PI/6;
		Material Material1=new Material();
		Material1.setSpecularColor(new Color3f(0,0,0));
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new Tree3D("wood.jpg",root,size,radius,angle,branchNumber,branchDecay,radiusDecay,level,Material1));
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,0.1f,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		Sphere  skySphere=new Sphere(2f,Sphere.GENERATE_NORMALS_INWARD|Sphere.GENERATE_TEXTURE_COORDS,100);
		skySphere.getAppearance().setTexture(new TextureLoader("sky.jpg",null).getTexture());
		TransformGroup2.addChild(skySphere);
		TransformGroup1.addChild(TransformGroup2);
		transform3D=new Transform3D();
		transform3D.rotX(-Math.PI/2);
		transform3D.setTranslation(new Vector3f(0f,-0.5f,0f));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		TransformGroup3.addChild(new Image3D(4.5f,4.5f,"snow.jpg"));
		TransformGroup1.addChild(TransformGroup3);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndTree3D(),400,400);
	}
}
class Tree3D extends Shape3D
{
	private int level,n=10,c=0,k=0;
	private Point3d[] coordinates;
	private int[] coordinateIndices;
	private Vector3f[] normals;
	private TexCoord2f[] texCoords;
	private int branchNumber;
	private Vector3d branchDirection;
	private Vector3d branchRotateAxis;
	private double branchDecay,radiusDecay;
	private double size,angle,branchRotateAngle;
	private double branchRotateNoise=0.5,sprayRotateNoise=0.3, branchDecayNoise=0.2;
	public Tree3D(String imageName,Point3d root,double size,double radius,double angle,int branchNumber,double branchDecay,double radiusDecay,int level,Material material)
	{
		this.size=size;
		this.level=level;
		this.angle=angle;
		this.radiusDecay=radiusDecay;
		this.branchDecay=branchDecay;
		this.branchNumber=branchNumber;
		this.branchDirection=new Vector3d(0,size,0);
		this.branchRotateAxis=new Vector3d(0,0,1);
		this.branchRotateAngle=2*Math.PI/branchNumber;
		this.initCoordinates();
		this.getCoordinates(root,branchDirection,branchRotateAxis,radius,0);
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES|IndexedQuadArray.NORMALS|IndexedQuadArray.TEXTURE_COORDINATE_2,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedQuadArray1.setNormals(0,normals);
		IndexedQuadArray1.setNormalIndices(0,coordinateIndices);
		IndexedQuadArray1.setTextureCoordinates(0,0,texCoords);
		IndexedQuadArray1.setTextureCoordinateIndices(0,0,coordinateIndices);
		this.setGeometry(IndexedQuadArray1);
		this.setAppearance(this.getImageComponent2DAppearance(imageName,material));
	}
	public static Appearance getImageComponent2DAppearance(String imageName,Material material)
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
	private void initCoordinates()
	{
		this.countCoordinates(0);
		this.normals=new Vector3f[c];
		this.texCoords=new TexCoord2f[c];
		this.coordinates=new Point3d[c];c=0;
		this.coordinateIndices=new int[k];k=0;
	}
	private void countCoordinates(int l)
	{
		if(l>=level)return;
		c+=n;c+=n;k+=4*n;
		for(int i=0;i<branchNumber;i++)
		{
			this.countCoordinates(l+1);
		}
	}
	private void getCoordinates(Point3d p0,Vector3d v0,Vector3d d0,double r0,int l)
	{
		if(l>=level)return;
		Point3d p1=translate(p0,v0);
		double a=branchRotateAngle;
		double r1=r0*radiusDecay;
		int i0=addCoordinates(p0,v0,d0,r0,0.0f);
		int i1=addCoordinates(p1,v0,d0,r1,1.0f);
		this.addCoordinateIndices(i0,i1);
		for(int i=0;i<branchNumber;i++)
		{
			Vector3d d1=rotate(d0,v0,a*i+rand(branchRotateNoise));
			Vector3d v1=rotate(v0,d1,angle+rand(sprayRotateNoise));
			this.scale(v1,branchDecay+rand(branchDecayNoise));
			this.getCoordinates(p1,v1,d1,r1,l+1);
		}
	}
	private int addCoordinates(Point3d p,Vector3d v,Vector3d d,double r,float t)
	{
		int index=c;
		double a=2*Math.PI/(n-1);
		for(int j=0;j<n;j++)
		{
			Vector3d d1=rotate(d,v,a*j);
			this.normals[c]=normalize(d1);
			float s=1.0f*j/(n-1);
			this.texCoords[c]=new TexCoord2f(s,t);
			this.scale(d1,r);
			Point3d p1=translate(p,d1);
			this.coordinates[c++]=p1;
		}
		return index;
	}
	private void addCoordinateIndices(int i0,int i1)
	{
		for(int j=0;j<n;j++)
		{
			this.coordinateIndices[k++]=i0+(j+0)%n;
			this.coordinateIndices[k++]=i0+(j+1)%n;
			this.coordinateIndices[k++]=i1+(j+1)%n;
			this.coordinateIndices[k++]=i1+(j+0)%n;
		}
	}
	private Point3d translate(Point3d p,Vector3d v)
	{
		return new Point3d(p.x+v.x,p.y+v.y,p.z+v.z);
	}
	private double angleToY(double x,double y)
	{
		double r=Math.sqrt(x*x+y*y);
		return r==0?0:y>=0?Math.asin(x/r):Math.PI-Math.asin(x/r);
	}
	private void rotX(Vector3d v,double a)
	{
		double y=v.y;
		double z=v.z;
		v.y=y*Math.cos(a)-z*Math.sin(a);
		v.z=y*Math.sin(a)+z*Math.cos(a);
	}
	private void rotY(Vector3d v,double a)
	{
		double z=v.z;
		double x=v.x;
		v.z=z*Math.cos(a)-x*Math.sin(a);
		v.x=z*Math.sin(a)+x*Math.cos(a);
	}
	private void rotZ(Vector3d v,double a)
	{
		double x=v.x;
		double y=v.y;
		v.x=x*Math.cos(a)-y*Math.sin(a);
		v.y=x*Math.sin(a)+y*Math.cos(a);
	}
	private Vector3d rotate(Vector3d vector,Vector3d axis,double angle)
	{
		Vector3d v=new Vector3d(vector.x,vector.y,vector.z);
		Vector3d a=new Vector3d(axis.x,axis.y,axis.z);
		double rotZ=angleToY(a.x,a.y);
		this.rotZ(a,rotZ);
		double rotX=-angleToY(a.z,a.y);
		this.rotZ(v,rotZ);
		this.rotX(v,rotX);
		this.rotY(v,angle);
		this.rotX(v,-rotX);
		this.rotZ(v,-rotZ);
		return v;
	}
	private Vector3f normalize(Vector3d v)
	{
		Vector3f normal=new Vector3f((float)v.x,(float)v.y,(float)v.z);
		normal.normalize();
		return normal;
	}
	private void scale(Vector3d v,double a)
	{
		v.x*=a;
		v.y*=a;
		v.z*=a;
	}
	private double rand(double x)
	{
		return x*2.0*(Math.random()-0.5);
	}
}

class Image3D extends Shape3D
{
	public Image3D(double width,double height,String imageName)
	{
		Point3d p00=new Point3d(-width/2,-height/2,0);
		Point3d p01=new Point3d(-width/2,+height/2,0);
		Point3d p11=new Point3d(+width/2,+height/2,0);
		Point3d p10=new Point3d(+width/2,-height/2,0);
		TexCoord2f t00=new TexCoord2f(0f,0f);
		TexCoord2f t01=new TexCoord2f(0f,1f);
		TexCoord2f t11=new TexCoord2f(1f,1f);
		TexCoord2f t10=new TexCoord2f(1f,0f);
		Vector3f normal=new Vector3f(0f,0f,1f);
		QuadArray QuadArray1=new QuadArray(4,QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.TEXTURE_COORDINATE_2);
		QuadArray1.setCoordinate(3,p00);
		QuadArray1.setCoordinate(2,p01);
		QuadArray1.setCoordinate(1,p11);
		QuadArray1.setCoordinate(0,p10);
		QuadArray1.setTextureCoordinate(0,3,t00);
		QuadArray1.setTextureCoordinate(0,2,t01);
		QuadArray1.setTextureCoordinate(0,1,t11);
		QuadArray1.setTextureCoordinate(0,0,t10);
		QuadArray1.setNormal(3,normal);
		QuadArray1.setNormal(2,normal);
		QuadArray1.setNormal(1,normal);
		QuadArray1.setNormal(0,normal);
		this.setGeometry(QuadArray1);
		this.setAppearance(this.getImageComponent2DAppearance(imageName,false));
	}
	public static Appearance getImageComponent2DAppearance(String imageName,boolean hasMaterial)
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
		if(hasMaterial)imageComponent2DAppearance.setMaterial(new Material());
		return imageComponent2DAppearance;
	}
}
