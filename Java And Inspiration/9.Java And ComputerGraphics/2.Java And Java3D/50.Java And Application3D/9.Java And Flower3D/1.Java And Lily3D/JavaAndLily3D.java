import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndLily3D
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
		double PI_2=Math.PI/2,PI2=Math.PI*2;
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(0.3);
		TransformGroup1.setTransform(transform3D);
		TransformGroup1.addChild(new Lily3D());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Math3d
{
	public static double rand(double x)
	{
		return 2*x*(Math.random()-0.5);
	}
	public static double random(double min,double max)
	{
		return min+(max-min)*Math.random();
	}
	public static Vector3d difference(Point3d p1,Point3d p2)
	{
		return new Vector3d(p2.x-p1.x,p2.y-p1.y,p2.z-p1.z);
	}
	public static Point3d sphereSurface(Vector3d r,double u,double v)
	{
		double x=r.x*Math.cos(u)*Math.cos(v);
		double y=r.y*Math.sin(u);
		double z=r.z*Math.cos(u)*Math.sin(v);
		return new Point3d(x,y,z);
	}
	public static Vector3d sphereNormal(Vector3d r,double u,double v)
	{
		double x=r.x*Math.cos(u)*Math.cos(v);
		double y=r.y*Math.sin(u);
		double z=r.z*Math.cos(u)*Math.sin(v);
		double du=0.001,dv=du;
		double dxu=r.x*Math.cos(u+du)*Math.cos(v)-x;
		double dxv=r.x*Math.cos(u)*Math.cos(v+dv)-x;
		double dyu=r.y*Math.sin(u+du)-y;
		double dyv=0;
		double dzu=r.z*Math.cos(u+du)*Math.sin(v)-z;
		double dzv=r.z*Math.cos(u)*Math.sin(v+dv)-z;
		Vector3d v1=new Vector3d(dxu,dyu,dzu);
		Vector3d v2=new Vector3d(dxv,dyv,dzv);
		Vector3d normal=new Vector3d();
		normal.cross(v1,v2);
		normal.normalize();
		return normal;
	}
	public static Point3d planeSurface(Vector3d k,double u,double v)
	{
		double x=k.x*u;
		double y=k.y*v;
		double z=k.z;
		return new Point3d(x,y,z);
	}
	public static Point3d columnSurface(Vector3d r,double u,double v)
	{
		double x=r.x*Math.cos(v);
		double y=r.y*u;
		double z=r.z*Math.sin(v);
		return new Point3d(x,y,z);
	}
	public static Vector3d columnNormal(Vector3d r,double u,double v)
	{
		double x=-r.x*Math.sin(v);
		double y=0;
		double z=r.z*Math.cos(v);
		return new Vector3d(x,y,z);
	}
	public static Color3f RGB(float hue)
	{
		float red=0f,green=0f,blue=0f,high=0f,mid=0f,low;
		float saturation=1,brightness=1;
		float range=saturation*brightness;
		high=brightness;
		low=brightness-range;
		float H=6*hue%6;
		int n=(int)H;
		float h=H-n;
		mid=(n%2==0?low+h*range:low+(1-h)*range);
		switch(n)
		{
			case 0:red=high;green=mid;blue=low;break;
			case 1:red=mid;green=high;blue=low;break;
			case 2:red=low;green=high;blue=mid;break;
			case 3:red=low;green=mid;blue=high;break;
			case 4:red=mid;green=low;blue=high;break;
			case 5:red=high;green=low;blue=mid;break;
		}
		return new Color3f(red,green,blue);
	}
	public static Color3f HSB(float hue,float saturation,float brightness)
	{
		float red=0f,green=0f,blue=0f,high=0f,mid=0f,low;
		float range=saturation*brightness;
		high=brightness;
		low=brightness-range;
		float H=6*hue%6;
		int n=(int)H;
		float h=H-n;
		mid=(n%2==0?low+h*range:low+(1-h)*range);
		switch(n)
		{
			case 0:red=high;green=mid;blue=low;break;
			case 1:red=mid;green=high;blue=low;break;
			case 2:red=low;green=high;blue=mid;break;
			case 3:red=low;green=mid;blue=high;break;
			case 4:red=mid;green=low;blue=high;break;
			case 5:red=high;green=low;blue=mid;break;
		}
		return new Color3f(red,green,blue);
	}
}
class Function_Leaf implements Function
{
	public Vector3d displace(double u,double v)
	{
		double x=u*0.5*Math.sin(v*3.14-0.4);
		double y=1.12*(v-0.5)*Math.sin(3.14*(u+0.5));
		double z=-0.25*v*v*Math.exp(-200*u*u)+0.6*Math.sin(v*3.14+2)+0.3*Math.sin(u*3.14+2);
		z+=0.01*(1-2*Math.abs(v-0.5))*(1-2*Math.abs(u))*Math.sin((u+0.5)*3.14*24);
		return new Vector3d(x,y,z);
	}
	public Point3d surface(double u,double v)
	{
		Vector3d k=new Vector3d(1.0,2.0,0.0);
		Point3d P=Math3d.planeSurface(k,u,v);
		Vector3d D=this.displace(u,v);
		double x=P.x+D.x;
		double y=P.y+D.y;
		double z=P.z+D.z;
		return new Point3d(x,y,z);
	}
	public Color3f color(double u,double v)
	{
		double hue=60.0+20.0*(1-v),m=2*24;
		return Math3d.RGB((float)(hue/360.0));
	}
}
class Function_Torus implements Function
{
	public double deform(double u,double v)
	{
		return u<0?0.0:2.5*Math.abs(Math.sin(2*u));
	}
	public Point3d surface(double u,double v)
	{
		Vector3d r=new Vector3d(0.05,1.5,0.05);
		Point3d P=Math3d.sphereSurface(r,u,v);
		Vector3d N=Math3d.sphereNormal(r,u,v);
		double g=0.1*this.deform(u,v);
		double x=P.x+N.x*g;
		double y=P.y+N.y*g;
		double z=P.z+N.z*g;
		return new Point3d(x,y<1?y:1,z);
	}
	public Color3f color(double u,double v)
	{
		double hue=90.0-30.0*Math.sin(2*u);
		return Math3d.RGB((float)(hue/360.0));
	}
}
class Function_Ovary implements Function
{
	public double displace(double u)
	{
		return -Math.sin(1.57+u)/2;
	}
	public Point3d surface(double u,double v)
	{
		Vector3d r=new Vector3d(1.0,2.0,1.0);
		Point3d P=Math3d.sphereSurface(r,u,v);
		double x=P.x;
		double y=P.y;
		double z=P.z+displace(u);
		return new Point3d(x,y,z);
	}
	public Color3f color(double u,double v)
	{
		double hue=70.0-10.0*Math.sin(2*u+1.57);
		return Math3d.RGB((float)(hue/360.0));
	}
}
class Function_Style implements Function
{
	public double deform(double u,double v)
	{
		return  Math.sin(3.14*u-1);
	}
	public double displace(double u,double v)
	{
		return 0.4*Math.sin(3.14*u+1);
	}
	public Point3d surface(double u,double v)
	{
		Vector3d r=new Vector3d(0.1,4.0,0.1);
		Point3d P=Math3d.columnSurface(r,u,v);
		Vector3d N=Math3d.columnNormal(r,u,v);
		double g=this.deform(u,v);
		double d=this.displace(u,v);
		double x=P.x+N.x*g+d;
		double y=P.y+N.y*g;
		double z=P.z+N.z*g+d;
		return new Point3d(x,y,z);
	}
	public Color3f color(double u,double v)
	{
		double hue=70.0-10.0*Math.sin(2*u+1.57);
		return Math3d.RGB((float)(hue/360.0));
	}
}
class Function_Lily implements Function
{
	public Vector3d displace(double u,double v)
	{
		double x=u*0.6*Math.sin(v*3.14+1)+0.05*u*Math.sin(10*v*3.14);
		double y=1.12*(v-0.5)*Math.sin(3.14*(u+0.5))+0.5;
		double z=-0.1*Math.exp(-25*(u-0.05)*(u-0.05))-0.1*Math.exp(-25*(u+0.05)*(u+0.05));
		z+=0.01*(1-2*Math.abs(v-0.5))*(1-2*Math.abs(u))*Math.sin((u+0.5)*3.14*24)-0.8*Math.sin(v*3.14+3);
		return new Vector3d(x,y,z);
	}
	public Point3d surface(double u,double v)
	{
		Vector3d k=new Vector3d(1.0,2.5,0.0);
		Point3d P=Math3d.planeSurface(k,u,v);
		Vector3d D=this.displace(u,v);
		double x=P.x+D.x;
		double y=P.y+D.y;
		double z=P.z+D.z;
		return new Point3d(x*0.8,y,z);
	}
	public Color3f color(double u,double v)
	{
		double u1=Math3d.random(-0.4,0.4);
		double v1=0.8*Math.random();
		float hue=356.0f/360.0f;
		//double saturation=100+260*2*Math.abs(u);
		double saturation=100+260*1.414*Math.sqrt(Math.abs(u));
		saturation+=300.0*Math.abs(Math.sin(u-u1)*Math.sin(v-v1));
		float brightness=1.0f;
		return Math3d.HSB(hue,1.0f-(float)(saturation/360.0),brightness);
	}
}
class Function_Anther implements Function
{
	public double deform(double u,double v)
	{
		return Math.sin(v/2)*Math.abs(Math.cos(u));
	}
	public double noise(double u,double v)
	{
		double q3=Math3d.random(2,5);
		double q4=Math3d.random(0,Math.PI/2);
		double q5=Math3d.random(0,Math.PI*2);
		return -1.5*Math.exp(-1.5*((u-q4)*(u-q4)+(v-q5)*(v-q5))/q3)/q3;
	}
	public Point3d surface(double u,double v)
	{
		Vector3d r=new Vector3d(0.25,1.25,0.4);
		Point3d P=Math3d.sphereSurface(r,u,v);
		Vector3d N=Math3d.sphereNormal(r,u,v);
		double g=0.25*deform(u,v)+0.02*noise(u,v);
		double x=P.x+N.x*g;
		double y=P.y+N.y*g;
		double z=P.z+N.z*g;
		return new Point3d(x,y,z);
	}
	public Color3f color(double u,double v)
	{
		return new Color3f(1.0f,0f,0f);
	}
}
interface Function
{
	Point3d surface(double u,double v);
	Color3f color(double u,double v);
}
class Surface3D extends Shape3D
{
	public int n=100,m=100;
	public Point3d[] coordinates=new Point3d[m*n];
	public Color3f[] colors=new Color3f[m*n];
	public Surface3D(Function function,double u0,double u1,double v0,double v1)
	{
		double du=(u1-u0)/(n-1),dv=(v1-v0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double v=v0+i*dv;
			for(int j=0;j<n;j++)
			{
				double u=u0+j*du;
				this.coordinates[i*n+j]=function.surface(u,v);
				this.colors[i*n+j]=function.color(u,v);
			}
		}
		this.setGeometry(this.getStriangleStripArray());
		this.setAppearance(this.getDefaultAppearance());
	}
	public Surface3D(Function function,double u0,double u1,double v0,double v1,int doubleSurface)
	{
		double du=(u1-u0)/(n-1),dv=(v1-v0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double v=v0+i*dv;
			for(int j=0;j<n;j++)
			{
				double u=u0+j*du;
				this.coordinates[i*n+j]=function.surface(u,v);
				this.colors[i*n+j]=function.color(u,v);
			}
		}
		this.setGeometry(this.getStriangleStripArray(doubleSurface));
		this.setAppearance(this.getDefaultAppearance());
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
				coordinateIndices[v++]=i*n+j;
				coordinateIndices[v++]=(i-1)*n+j;
			}
		}
		int[] stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setColors(colors);
		GeometryInfo1.setColorIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		return GeometryInfo1.getGeometryArray();
	}
	GeometryArray getStriangleStripArray(int doubleSurface)
	{
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
		GeometryInfo1.setColors(colors);
		GeometryInfo1.setColorIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		return GeometryInfo1.getGeometryArray();
	}
	Appearance getDefaultAppearance()
	{
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setSpecularColor(new Color3f(0.2f,0.2f,0.2f));
		Appearance1.setMaterial(Material1);
		return Appearance1;
	}
}
class Stamen3D extends SharedGroup
{
	public Stamen3D()
	{
		Transform3D transform3D=new Transform3D();
		transform3D.rotX(Math.PI/2);
		transform3D.setScale(0.3);
		transform3D.setTranslation(new Vector3d(-0.3,0.3,-0.15));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Surface3D(new Function_Style(),0,1,0,Math.PI*2));
		this.addChild(TransformGroup1);

		transform3D=new Transform3D();
		transform3D.rotZ(Math.PI/4);
		transform3D.setScale(0.15);
		transform3D.setTranslation(new Vector3d(-0.45,0.45,1.05));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		transform3D=new Transform3D();
		transform3D.rotY(-Math.PI/2);
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		TransformGroup3.addChild(new Surface3D(new Function_Anther(),-Math.PI/2,Math.PI/2,0,Math.PI*2));
		TransformGroup2.addChild(TransformGroup3);
		this.addChild(TransformGroup2);
	}
}
class Pistil3D extends SharedGroup
{
	public Pistil3D()
	{
		Transform3D transform3D=new Transform3D();
		transform3D.rotX(Math.PI/2);
		transform3D.setScale(new Vector3d(0.3,0.4,0.3));
		transform3D.setTranslation(new Vector3d(-0.1,0.1,-0.15));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Surface3D(new Function_Style(),0,1,0,Math.PI*2));
		this.addChild(TransformGroup1);

		transform3D=new Transform3D();
		//transform3D.rotX(Math.PI/2);
		transform3D.setScale(new Vector3d(0.1,0.05,0.1));
		transform3D.setTranslation(new Vector3d(-0.2,0.2,1.5));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Surface3D(new Function_Ovary(),-Math.PI/2,Math.PI/2,0,Math.PI*2));
		this.addChild(TransformGroup2);
	}
}
class Lily3D extends TransformGroup
{
	public Lily3D()
	{
		SharedGroup SharedGroup1=new SharedGroup();
		SharedGroup1.addChild(new Surface3D(new Function_Lily(),-0.5,0.5,0,1.0,1));
		int m=3;double rotZ=2*Math.PI/m;
		for(int i=0;i<m;i++)
		{
			Transform3D transform3D=new Transform3D();
			transform3D.rotZ(i*rotZ);
			TransformGroup transformGroup=new TransformGroup(transform3D);
			transformGroup.addChild(new Link(SharedGroup1));
			this.addChild(transformGroup);
		}
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0,0,-0.1));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		for(int i=0;i<m;i++)
		{
			transform3D=new Transform3D();
			transform3D.rotZ(i*rotZ+Math.PI/3);
			TransformGroup transformGroup=new TransformGroup(transform3D);
			transformGroup.addChild(new Link(SharedGroup1));
			TransformGroup1.addChild(transformGroup);
		}
		this.addChild(TransformGroup1);

		Stamen3D Stamen=new Stamen3D();
		Pistil3D Pistil=new Pistil3D();
		m=6;rotZ=2*Math.PI/m;
		for(int i=0;i<m;i++)
		{
			transform3D=new Transform3D();
			transform3D.rotZ(i*rotZ);
			TransformGroup transformGroup=new TransformGroup(transform3D);
			transformGroup.addChild(new Link(Stamen));
			this.addChild(transformGroup);
		}
		this.addChild(new Link(Pistil));

		transform3D=new Transform3D();
		transform3D.rotX(Math.PI/2);
		transform3D.setScale(new Vector3d(2.0,0.6,2.0));
		transform3D.setTranslation(new Vector3d(0,0,-0.85));
		TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Surface3D(new Function_Torus(),-Math.PI/2,Math.PI/2,0,Math.PI*2));
		this.addChild(TransformGroup1);
	}
}




