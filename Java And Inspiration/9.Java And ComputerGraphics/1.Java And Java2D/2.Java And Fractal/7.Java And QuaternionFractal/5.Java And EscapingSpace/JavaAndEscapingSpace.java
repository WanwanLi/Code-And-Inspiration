import java.applet.*;
import java.awt.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndEscapingSpace extends Applet
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
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new EscapingSpace(0.4f,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndEscapingSpace(),400,400);
	}
}
class EscapingSpace extends Shape3D
{
	public int m=400;
	public int n=200;
	double u,w;
	double a;
	Point3f center;
	float x,y,z,r;
	float size;
	float[] radius=new float[m*n];
	public EscapingSpace(Point3f center,float size,Appearance appearance)
	{
		int v=0;
		this.size=size;
		this.center=center;
		float dr=size/500f;
		Point3f[] coordinates=new Point3f[m*n];
		for(int i=0;i<m;i++)
		{
			u=i*Math.PI/(m-1);
			for(int j=0;j<n;j++)
			{
				r=0;
				w=j*2*Math.PI/n;
				this.getXYZ();
				dr=size/5f;
				while(dr>size/500f)
				{
					while(this.isInBoundary()&&this.isRoot()){r+=dr;this.getXYZ();}
					r-=dr;
					this.getXYZ();
					dr/=2;
				}
				coordinates[v++]=new Point3f(x,y,z);
			}
		};
		v=0;
		int[] coordinateIndices=new int[4*m*n];
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=((i+0)%m)*n+(j+0)%n;
				coordinateIndices[v++]=((i+0)%m)*n+(j+1)%n;
				coordinateIndices[v++]=((i+1)%m)*n+(j+1)%n;
				coordinateIndices[v++]=((i+1)%m)*n+(j+0)%n;
			}
		}
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
	public EscapingSpace(float size,Appearance appearance)
	{

		float x0,x1;
		x=-size;
		float dx=size/5;
		while(dx>size/500)
		{
			while(!isRoot())x+=dx;
			x-=dx;
			dx/=2;
		}
		x0=x+2*dx;
		x=size;
		dx=size/5;
		while(dx>size/500)
		{
			while(!isRoot())x-=dx;
			x+=dx;
			dx/=2;
		}
		x1=x-2*dx;
		dx=(x1-x0)/(m-1);
		this.size=size;
		float dr=size/5;
		Point3f[] coordinates=new Point3f[m*n];
		y=z=0f;
		int v=0;
		for(int i=0;i<m;i++)
		{
			a=0.0;
			x=x0+i*dx;
			y=z=r=0f;
			this.center=new Point3f(x,y,z);
			for(int j=0;j<n;j++)
			{
				r=0;
				a=j*2*Math.PI/n;
				this.getYZ();
				dr=size/5f;
				while(dr>size/500f)
				{
					while(this.isInBoundary()&&this.isRoot()){r+=dr;this.getYZ();}
					r-=dr;
					this.getYZ();
					dr/=2;
				}
				coordinates[v++]=new Point3f(x,y,z);
			}
		};
		v=0;
		int[] coordinateIndices=new int[4*(m-1)*n];
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=((i+0)%m)*n+(j+0)%n;
				coordinateIndices[v++]=((i+0)%m)*n+(j+1)%n;
				coordinateIndices[v++]=((i+1)%m)*n+(j+1)%n;
				coordinateIndices[v++]=((i+1)%m)*n+(j+0)%n;
			}
		}
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
	private boolean isInBoundary()
	{
		return (x>center.x-size&&x<center.x+size&&y>center.y-size&&y<center.y+size&&z>center.z-size&&z<center.z+size);
	}
	private boolean isRoot()
	{
		float R=0.4f;
		return (x*x+y*y+z*z<R*R);
	}
	private void getXYZ()
	{
		this.x=center.x+(float)(r*Math.sin(u)*Math.cos(w));
		this.y=center.y+(float)(r*Math.cos(u));
		this.z=center.z+(float)(r*Math.sin(u)*Math.sin(w));
	}
	private void getYZ()
	{
		y=center.y+r*(float)(Math.cos(a));
		z=center.z+r*(float)(Math.sin(a));
	}
}












