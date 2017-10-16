import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.applet.Applet;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import javax.imageio.*;
import com.sun.j3d.utils.image.*;
public class JavaAndSolarSystem extends Applet
{
	public void init()
	{
		System.out.print("SolarSystem>");
		Scanner scanner=new Scanner(System.in);
		String name=scanner.nextLine();
		Canvas3D canvas3D=new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		this.setLayout(new BorderLayout());
		this.add(canvas3D);	
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),500);
		OrbitBehavior OrbitBehavior1=new OrbitBehavior(canvas3D);
		OrbitBehavior1.setSchedulingBounds(BoundingSphere1);
		SimpleUniverse1.getViewingPlatform().setViewPlatformBehavior(OrbitBehavior1);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		boolean orbitIsVisible=true;
		SolarSystem SolarSystem1=new SolarSystem(BoundingSphere1,BranchGroup1,orbitIsVisible);
		SolarSystem1.trace(name);
		BranchGroup1.compile();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndSolarSystem(),400,400);
	}
}
class SolarSystem extends TransformGroup
{
	private BoundingSphere boundingSphere;
	private BranchGroup branchGroup;
	private boolean orbitIsVisible;
	private final int Mercury=1;
	private final int Venus=2;
	private final int Earth=3;
	private final int Mars=4;
	private final int Jupiter=5;
	private final int Saturn=6;
	private final int Uranus=7;
	private final int Neptune=8;
	private final int Moon=9;
	private int sphereNumber=10;
	private SphereInfo[] SphereInfoTable=new SphereInfo[sphereNumber];
	private void initSphereInfoTable()
	{
		this.SphereInfoTable[Mercury]=new SphereInfo("Mercury",0.045f,0.4,0.4,0.0,0.0,5000,8000);
		this.SphereInfoTable[Venus]=new SphereInfo("Venus",0.08f,0.6,0.55,0.0,0.0,8000,5000);
		this.SphereInfoTable[Earth]=new SphereInfo("Earth",0.1f,1.0,1.0,0.0,Math.PI/10,10000,3000);
		this.SphereInfoTable[Moon]=new SphereInfo("Moon",0.03f,0.2,0.2,0.0,0.0,4000,4000);
		this.SphereInfoTable[Mars]=new SphereInfo("Mars",0.1f,1.3,1.25,0.0,0.0,11000,4000);
		this.SphereInfoTable[Jupiter]=new SphereInfo("Jupiter",0.15f,1.8,1.75,0.0,Math.PI/6,13000,3000);
		this.SphereInfoTable[Saturn]=new SphereInfo("Saturn",0.12f,2.3,2.25,0.0,Math.PI/3,14000,2500);
		this.SphereInfoTable[Uranus]=new SphereInfo("Uranus",0.1f,2.8,2.75,0.0,Math.PI/2,15000,3000);
		this.SphereInfoTable[Neptune]=new SphereInfo("Neptune",0.09f,3.2,3.15,Math.PI/10,0.0,16000,4000);
	}
	public SolarSystem(BoundingSphere boundingSphere,BranchGroup branchGroup,boolean orbitIsVisible)
	{
		this.boundingSphere=boundingSphere;
		this.branchGroup=branchGroup;
		this.orbitIsVisible=orbitIsVisible;
		this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.initSphereInfoTable();
		this.addSun();
		this.add(Mercury,this);
		this.add(Earth,this);
		this.add(Venus,this);
		this.add(Mars,this);
		this.add(Jupiter,this);
		this.add(Saturn,this);
		this.add(Uranus,this);
		this.add(Neptune,this);
		this.addStars();
	}
	public void trace(String name)
	{
		this.branchGroup.addChild(this.trace(name,this));
	}
	private TransformGroup trace(String name,TransformGroup transformGroup)
	{
		double a=0.0,b=0.0,c=0.0,rotZ=0.0;int t=0,T=0;boolean isFix=false;
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		SphereInfo sphereInfo=null;
		int length=name.length();
		if(length>4)
		{
			String end=name.substring(length-4,length);
			if(end.equals(".fix")){name=name.substring(0,length-4);isFix=true;}
		}
		if(name.equals("Mercury"))sphereInfo=SphereInfoTable[Mercury];
		else if(name.equals("Venus"))sphereInfo=SphereInfoTable[Venus];
		else if(name.equals("Earth"))sphereInfo=SphereInfoTable[Earth];
		else if(name.equals("Moon"))
		{
			TransformGroup1=this.trace("Earth",TransformGroup1);
			TransformGroup1.addChild(transformGroup);
			sphereInfo=SphereInfoTable[Moon];
		}
		else if(name.equals("Mars"))sphereInfo=SphereInfoTable[Mars];
		else if(name.equals("Jupiter"))sphereInfo=SphereInfoTable[Jupiter];
		else if(name.equals("Saturn"))sphereInfo=SphereInfoTable[Saturn];
		else if(name.equals("Uranus"))sphereInfo=SphereInfoTable[Uranus];
		else if(name.equals("Neptune"))sphereInfo=SphereInfoTable[Neptune];
		else return this;
		a=sphereInfo.a;
		b=sphereInfo.b;
		c=sphereInfo.c;
		t=sphereInfo.t;
		if(isFix)
		{
			T=sphereInfo.T;
			rotZ=sphereInfo.rotZ;
		}
		Alpha alpha=new Alpha();
		alpha.setLoopCount(-1);
		alpha.setIncreasingAlphaDuration(t);
		alpha.setMode(Alpha.INCREASING_ENABLE);
		int l=50;
		float[] knots=new float[l];
		for(int i=0;i<l;i++)knots[i]=i/(l-1f);
		Point3f[] positions=this.getTracingOrbit(a,b,c,l);
		PositionPathInterpolator PositionPathInterpolator1=new PositionPathInterpolator(alpha,transformGroup,new Transform3D(),knots,positions);
		PositionPathInterpolator1.setSchedulingBounds(this.boundingSphere);
		this.branchGroup.addChild(PositionPathInterpolator1);
		if(name.equals("Moon"))T=0;
		if(T!=0)
		{
			Transform3D transform3D=new Transform3D();
			transform3D.rotZ(-rotZ);
			TransformGroup TransformGroup_rotZ=new TransformGroup(transform3D);
			TransformGroup_rotZ.addChild(transformGroup);
			alpha=new Alpha();
			alpha.setLoopCount(-1);
			alpha.setIncreasingAlphaDuration(T);
			alpha.setMode(Alpha.INCREASING_ENABLE);
			RotationInterpolator RotationInterpolator1=new RotationInterpolator(alpha,TransformGroup1,new Transform3D(),(float)Math.PI*2,0f);
			RotationInterpolator1.setSchedulingBounds(this.boundingSphere);
			this.branchGroup.addChild(RotationInterpolator1);
			TransformGroup1.addChild(TransformGroup_rotZ);
			transformGroup=TransformGroup1;
		}
		if(name.equals("Moon"))return TransformGroup1;
		return transformGroup;
	}
	private void addSun()
	{
		PointLight PointLight1=new PointLight(new Color3f(Color.white),new Point3f(0f,0f,0f),new Point3f(1f,0f,0f));
		PointLight1.setInfluencingBounds(boundingSphere);
		this.addChild(PointLight1);
		Sphere Sun=new Sphere(0.18f,Sphere.GENERATE_NORMALS|Sphere.GENERATE_TEXTURE_COORDS,100);
		TransformGroup TransformGroup_Sun=new TransformGroup();
		TransformGroup_Sun.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TransformGroup_Sun.addChild(Sun);
		this.addChild(TransformGroup_Sun);
		Appearance appearance=Sun.getAppearance();
		appearance.setTexture(new TextureLoader("SolarSystem/Sun.jpg",null).getTexture());
		TextureAttributes TextureAttributes1=new TextureAttributes();
		TextureAttributes1.setTextureMode(TextureAttributes.COMBINE);
		appearance.setTextureAttributes(TextureAttributes1);
		Material material=new Material();
		material.setEmissiveColor(new Color3f(Color.white));
		appearance.setMaterial(material);
		Alpha alpha=new Alpha();
		alpha.setLoopCount(-1);
		alpha.setIncreasingAlphaDuration(8000);
		alpha.setMode(Alpha.INCREASING_ENABLE);
		RotationInterpolator RotationInterpolator1=new RotationInterpolator(alpha,TransformGroup_Sun,new Transform3D(),0f,(float)Math.PI*2);
		RotationInterpolator1.setSchedulingBounds(this.boundingSphere);
		this.branchGroup.addChild(RotationInterpolator1);

	}
	private void add(int NO,TransformGroup transformGroup)
	{
		SphereInfo sphereInfo=SphereInfoTable[NO];
		String name=sphereInfo.name;
		float r=sphereInfo.r;
		double a=sphereInfo.a,b=sphereInfo.b;
		double c=sphereInfo.c,rotZ=sphereInfo.rotZ;
		int t=sphereInfo.t,T=sphereInfo.T;
		TransformGroup TransformGroup_PositionPath=new TransformGroup();
		TransformGroup_PositionPath.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TransformGroup TransformGroup_Rotation=new TransformGroup();
		TransformGroup_Rotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D transform3D=new Transform3D();
		transform3D.rotZ(rotZ);
		TransformGroup TransformGroup_rotZ=new TransformGroup(transform3D);
		TransformGroup_rotZ.addChild(TransformGroup_Rotation);
		TransformGroup_PositionPath.addChild(TransformGroup_rotZ);
		transformGroup.addChild(TransformGroup_PositionPath);
		Sphere Sphere1=new Sphere(r,Sphere.GENERATE_NORMALS|Sphere.GENERATE_TEXTURE_COORDS,100);
		if(NO==Saturn)
		{
			transform3D=new Transform3D();
			transform3D.rotX(Math.PI/2);
			TransformGroup transformGroup1=new TransformGroup(transform3D);
			transformGroup1.addChild(new Ring3D(0.2,0.13,"SolarSystem/SaturnRing.jpg"));
			TransformGroup_Rotation.addChild(transformGroup1);
		}
		else if(NO==Uranus)
		{
			transform3D=new Transform3D();
			transform3D.rotX(Math.PI/2);
			TransformGroup transformGroup1=new TransformGroup(transform3D);
			transformGroup1.addChild(new Ring3D(0.15,0.13,"SolarSystem/UranusRing.jpg"));
			TransformGroup_Rotation.addChild(transformGroup1);
		}
		TransformGroup_Rotation.addChild(Sphere1);
		Appearance Appearance1=Sphere1.getAppearance();
		Material Material1=new Material();
		Material1.setSpecularColor(new Color3f(0f,0f,0f));
		Appearance1.setMaterial(Material1);
		Appearance1.setTexture(new TextureLoader("SolarSystem/"+name+".jpg",null).getTexture());
		TextureAttributes TextureAttributes1=new TextureAttributes();
		TextureAttributes1.setTextureMode(TextureAttributes.COMBINE);
		Appearance1.setTextureAttributes(TextureAttributes1);
		Alpha alpha=new Alpha();
		alpha.setLoopCount(-1);
		alpha.setIncreasingAlphaDuration(t);
		alpha.setMode(Alpha.INCREASING_ENABLE);
		int l=50;
		float[] knots=new float[l];
		for(int i=0;i<l;i++)knots[i]=i/(l-1f);
		Point3f[] positions=this.getEllipseOrbit(a,b,c,l);
		PositionPathInterpolator PositionPathInterpolator1=new PositionPathInterpolator(alpha,TransformGroup_PositionPath,new Transform3D(),knots,positions);
		PositionPathInterpolator1.setSchedulingBounds(this.boundingSphere);
		this.branchGroup.addChild(PositionPathInterpolator1);
		if(this.orbitIsVisible)this.addOrbit(transformGroup,positions);
		alpha=new Alpha();
		alpha.setLoopCount(-1);
		alpha.setIncreasingAlphaDuration(T);
		alpha.setMode(Alpha.INCREASING_ENABLE);
		RotationInterpolator RotationInterpolator1=new RotationInterpolator(alpha,TransformGroup_Rotation,new Transform3D(),0f,(float)Math.PI*2);
		RotationInterpolator1.setSchedulingBounds(this.boundingSphere);
		this.branchGroup.addChild(RotationInterpolator1);
		if(NO==Earth)this.add(Moon,TransformGroup_PositionPath);
	}
	private void addStars()
	{
		Sphere Stars=new Sphere(10f,Sphere.GENERATE_NORMALS_INWARD|Sphere.GENERATE_TEXTURE_COORDS,100);
		Appearance appearance=Stars.getAppearance();
		appearance.setTexture(new TextureLoader("SolarSystem/Stars.jpg",null).getTexture());
		this.addChild(Stars);
	}
	Point3f[] getEllipseOrbit(double a,double b,double c,int l)
	{
		Point3f[] ellipseOrbit=new Point3f[l];
		double w=Math.PI*2/(l-1);
		for(int i=0;i<l;i++)
		{
			float x=(float)(a*Math.cos(w*i)*Math.cos(c));
			float y=(float)(a*Math.cos(w*i)*Math.sin(c));
			float z=(float)(b*Math.sin(w*i));
			ellipseOrbit[i]=new Point3f(x,y,z);
		}
		return ellipseOrbit;
	}
	Point3f[] getTracingOrbit(double a,double b,double c,int l)
	{
		Point3f[] ellipseOrbit=new Point3f[l];
		double w=Math.PI*2/(l-1);
		for(int i=0;i<l;i++)
		{
			float x=-(float)(a*Math.cos(w*i)*Math.cos(c));
			float y=-(float)(a*Math.cos(w*i)*Math.sin(c));
			float z=-(float)(b*Math.sin(w*i));
			ellipseOrbit[i]=new Point3f(x,y,z);
		}
		return ellipseOrbit;
	}
	private void addOrbit(TransformGroup transformGroup,Point3f[] orbitPoints)
	{
		int l=orbitPoints.length,n=0;
		int[] coordinateIndices=new int[2*l];
		for(int i=0;i<l-1;i++)
		{
			coordinateIndices[n++]=i;
			coordinateIndices[n++]=i+1;
		}
		coordinateIndices[n++]=l-1;
		coordinateIndices[n++]=0;
		IndexedLineArray IndexedLineArray1=new IndexedLineArray(l,IndexedLineArray.COORDINATES,2*l);
		IndexedLineArray1.setCoordinates(0,orbitPoints);
		IndexedLineArray1.setCoordinateIndices(0,coordinateIndices);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(IndexedLineArray1);
		transformGroup.addChild(shape3D);
	}
}
class SphereInfo
{
	public String name;
	public float r;
	public double a,b,c,rotZ;
	public int t,T;
	public SphereInfo(String name,float r,double a,double b,double c,double rotZ,int t,int T)
	{
		this.name=name;
		this.r=r;
		this.a=a;
		this.b=b;
		this.c=c;
		this.rotZ=rotZ;
		this.t=t;
		this.T=T;
	}
}
class Ring3D extends Shape3D
{
	public Ring3D(double R,double r,String imageName)
	{
		int l=30,n=0;
		double w=2*Math.PI/l;
		QuadArray QuadArray1=new QuadArray(4*l*2,QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.TEXTURE_COORDINATE_2);
		TexCoord2f t00=new TexCoord2f(0f,0f);
		TexCoord2f t01=new TexCoord2f(0f,1f);
		TexCoord2f t11=new TexCoord2f(1f,1f);
		TexCoord2f t10=new TexCoord2f(1f,0f);
		Vector3f normal=new Vector3f(0f,0f,1f);
		for(int i=0;i<l;i++)
		{
			double x00=r*Math.cos(w*(i+0));
			double x01=R*Math.cos(w*(i+0));
			double x10=r*Math.cos(w*(i+1));
			double x11=R*Math.cos(w*(i+1));
			double y00=r*Math.sin(w*(i+0));
			double y01=R*Math.sin(w*(i+0));
			double y10=r*Math.sin(w*(i+1));
			double y11=R*Math.sin(w*(i+1));
			Point3d p00=new Point3d(x00,y00,0);
			Point3d p01=new Point3d(x01,y01,0);
			Point3d p11=new Point3d(x11,y11,0);
			Point3d p10=new Point3d(x10,y10,0);
			QuadArray1.setCoordinate(n+0,p00);
			QuadArray1.setCoordinate(n+1,p01);
			QuadArray1.setCoordinate(n+2,p11);
			QuadArray1.setCoordinate(n+3,p10);
			QuadArray1.setTextureCoordinate(0,n+0,t00);
			QuadArray1.setTextureCoordinate(0,n+1,t01);
			QuadArray1.setTextureCoordinate(0,n+2,t11);
			QuadArray1.setTextureCoordinate(0,n+3,t10);
			QuadArray1.setNormal(n+0,normal);
			QuadArray1.setNormal(n+1,normal);
			QuadArray1.setNormal(n+2,normal);
			QuadArray1.setNormal(n+3,normal);
			n+=4;
		}
		normal=new Vector3f(0f,0f,-1f);
		for(int i=0;i<l;i++)
		{
			double x00=r*Math.cos(w*(i+0));
			double x01=R*Math.cos(w*(i+0));
			double x10=r*Math.cos(w*(i+1));
			double x11=R*Math.cos(w*(i+1));
			double y00=r*Math.sin(w*(i+0));
			double y01=R*Math.sin(w*(i+0));
			double y10=r*Math.sin(w*(i+1));
			double y11=R*Math.sin(w*(i+1));
			Point3d p00=new Point3d(x00,y00,0);
			Point3d p01=new Point3d(x01,y01,0);
			Point3d p11=new Point3d(x11,y11,0);
			Point3d p10=new Point3d(x10,y10,0);
			QuadArray1.setCoordinate(n+3,p00);
			QuadArray1.setCoordinate(n+2,p01);
			QuadArray1.setCoordinate(n+1,p11);
			QuadArray1.setCoordinate(n+0,p10);
			QuadArray1.setTextureCoordinate(0,n+3,t00);
			QuadArray1.setTextureCoordinate(0,n+2,t01);
			QuadArray1.setTextureCoordinate(0,n+1,t11);
			QuadArray1.setTextureCoordinate(0,n+0,t10);
			QuadArray1.setNormal(n+3,normal);
			QuadArray1.setNormal(n+2,normal);
			QuadArray1.setNormal(n+1,normal);
			QuadArray1.setNormal(n+0,normal);
			n+=4;
		}
		this.setGeometry(QuadArray1);
		this.setAppearance(this.getImageComponent2DAppearance(imageName,true));
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
		Material Material1=new Material();
		Material1.setSpecularColor(new Color3f(0f,0f,0f));
		if(hasMaterial)imageComponent2DAppearance.setMaterial(Material1);
		return imageComponent2DAppearance;
	}
}