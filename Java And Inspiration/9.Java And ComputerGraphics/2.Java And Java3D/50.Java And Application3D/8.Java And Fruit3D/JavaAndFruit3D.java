import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndFruit3D
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
		Transform3D transform3D=new Transform3D();
		transform3D.rotZ(Math.PI/2);
		TransformGroup1.setTransform(transform3D);
		TransformGroup1.addChild(new Surface3D(new Function_Carambole(),-Math.PI/2,Math.PI/2,0,Math.PI*2));
		transform3D=new Transform3D();
		transform3D.setScale(0.3);
		//TransformGroup1.setTransform(transform3D);
		//TransformGroup1.addChild(new Surface3D(new Function_Apple(),-Math.PI/2,Math.PI/2,0,Math.PI*2));
		//TransformGroup1.addChild(new Surface3D(new Function_Peach(),-Math.PI/2,Math.PI/2,0,Math.PI*2));
		//TransformGroup1.addChild(new Surface3D(new Function_Pear(),-Math.PI/2,Math.PI/2,0,Math.PI*2));
		//TransformGroup1.addChild(new Surface3D(new Function_Mango(),-Math.PI/2,Math.PI/2,0,Math.PI*2));
		//TransformGroup1.addChild(new Surface3D(new Function_Banana(),-Math.PI/2,Math.PI/2,0,Math.PI*2));
		//TransformGroup1.addChild(new Surface3D(new Function_Orange(),-Math.PI/2,Math.PI/2,0,Math.PI*2));
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
}
class Function_Carambole implements Function
{
	public double deform(double u,double v)
	{
		return Math.sin(5*v+2*Math.sin(u));
	}
	public double noise(double u,double v)
	{
		double d=0.025,du=u+Math3d.rand(d),dv=v+Math3d.rand(d);
		return Math.abs(Math.sin(10*du)*Math.sin(10*dv+2*Math.sin(du)));
	}
	public Point3d deformedSurface(Vector3d r,double u,double v)
	{
		Point3d P=Math3d.sphereSurface(r,u,v);
		Vector3d N=Math3d.sphereNormal(r,u,v);
		double g=r.x/4*this.deform(u,v);
		double x=P.x+N.x*g;
		double y=P.y+N.y*g;
		double z=P.z+N.z*g;
		return new Point3d(x,y,z);
	}
	public Vector3d deformedNormal(Vector3d r,double u,double v)
	{
		double du=0.001,dv=du;
		Point3d P=this.deformedSurface(r,u,v);
		Point3d U=this.deformedSurface(r,u+du,v);
		Point3d V=this.deformedSurface(r,u,v+dv);
		Vector3d dPu=Math3d.difference(P,U);
		Vector3d dPv=Math3d.difference(P,V);
		Vector3d normal=new Vector3d();
		normal.cross(dPu,dPv);
		normal.normalize();
		return normal;
	}
	public Point3d surface(double u,double v)
	{
		Vector3d r=new Vector3d(0.5,1.0,0.5);
		Point3d P=this.deformedSurface(r,u,v);
		Vector3d N=this.deformedNormal(r,u,v);
		double g=r.x/100*this.noise(u,v);
		double x=P.x+N.x*g;
		double y=P.y+N.y*g;
		double z=P.z+N.z*g;
		return new Point3d(x,y,z);
	}
	public Color3f color(double u,double v)
	{
		return new Color3f(0.8f,1f,0f);
	}
}
class Function_Apple implements Function
{
	public double deform(double u,double v)
	{
		return -Math.exp(2*u)-Math.exp(-2*u);
	}
	public double scale(double t)
	{
		return 1.0+0.25*t;
	}
	public Point3d surface(double u,double v)
	{
		Vector3d r=new Vector3d(1.0,1.25,1.0);
		Point3d P=Math3d.sphereSurface(r,u,v);
		Vector3d N=Math3d.sphereNormal(r,u,v);
		double g=0.05*this.deform(u,v);
		double x=P.x+N.x*g;
		double y=P.y+N.y*g;
		double z=P.z+N.z*g;
		double s=this.scale(y);
		return new Point3d(x*s,y,z*s);
	}
	public Color3f color(double u,double v)
	{
		double u1=Math3d.random(-0.2,0.2),v1=0.5*Math.random();
		double hue=50.0-30.0*Math.abs(Math.sin(u-u1)*Math.sin(v-v1));
		return Math3d.RGB((float)(hue/360.0));
	}
}
class Function_Peach implements Function
{
	public double deform(double u,double v)
	{
		return 0.025*(Math.exp(2*u)-Math.exp(-2*u))+0.5*Math.sin(v/2)*Math.abs(Math.cos(u));
	}
	public double scale(double t)
	{
		return 1.0+0.25*t;
	}
	public Point3d surface(double u,double v)
	{
		Vector3d r=new Vector3d(1.0,1.0,1.0);
		Point3d P=Math3d.sphereSurface(r,u,v);
		Vector3d N=Math3d.sphereNormal(r,u,v);
		double g=this.deform(u,v);
		double x=P.x+N.x*g;
		double y=P.y+N.y*g;
		double z=P.z+N.z*g;
		double s=this.scale(y);
		return new Point3d(x,y,z);
	}
	public Color3f color(double u,double v)
	{
		double hue=50.0-30.0*Math.sin(u);
		return Math3d.RGB((float)(hue/360.0));
	}
}
class Function_Pear implements Function
{
	public double deform(double u,double v)
	{
		return Math.exp(2*u)-Math.exp(-2*u);
	}
	public Point3d surface(double u,double v)
	{
		Vector3d r=new Vector3d(1.0,1.0,1.0);
		Point3d P=Math3d.sphereSurface(r,u,v);
		Vector3d N=Math3d.sphereNormal(r,u,v);
		double g=0.05*this.deform(u,v);
		double x=P.x+N.x*g;
		double y=P.y+N.y*g;
		double z=P.z+N.z*g;
		return new Point3d(x,y,z);
	}
	public Color3f color(double u,double v)
	{
		return new Color3f(1f,1f,0f);
	}
}
class Function_Mango implements Function
{
	public double displace(double u)
	{
		return u*Math.sin(Math.abs(u))/2;
	}
	public Point3d surface(double u,double v)
	{
		Vector3d r=new Vector3d(1.5,4.5,1.0);
		double x=r.x*(Math.cos(u)*Math.cos(v)+displace(u));
		double y=r.y*Math.sin(u);
		double z=r.z*Math.cos(u)*Math.sin(v);
		return new Point3d(x,y,z);
	}
	public Color3f color(double u,double v)
	{
		double u1=Math3d.random(-0.2,0.2),v1=0.5*Math.random();
		double hue=60.0,u2=u-u1,v2=v-v1;
		if(0<=u2&&u2<=1.57)if(0<=v2&&v2<=1.57)
		hue=60.0+20.0*Math.sin(2*u2)*Math.sin(2*v2);
		return Math3d.RGB((float)(hue/360.0));
	}
}
class Function_Banana implements Function
{
	public double deform(double u,double v)
	{
		if(u<0)return (Math.exp(u)+Math.exp(-u))/10-(Math.PI/2+u)*Math.abs(Math.sin(2*v+Math.PI/2));
		else return (Math.exp(u)+Math.exp(-u))/10-(Math.PI/2-u)*Math.abs(Math.sin(2*v+Math.PI/2));
	}
	public double displace(double u)
	{
		return 2*Math.sin(u+1);
	}
	public Point3d surface(double u,double v)
	{
		Vector3d r=new Vector3d(1.0,5.0,1.0);
		Point3d P=Math3d.sphereSurface(r,u,v);
		Vector3d N=Math3d.sphereNormal(r,u,v);
		double g=0.1*this.deform(u,v);
		double x=P.x+N.x*g+displace(u);
		double y=P.y+N.y*g;
		double z=P.z+N.z*g;
		return new Point3d(x,y,z);
	}
	public Color3f color(double u,double v)
	{
		double hue=70.0-10.0*Math.sin(2*u+1.57);
		return Math3d.RGB((float)(hue/360.0));
	}
}
class Function_Orange implements Function
{
	public double deform(double u,double v)
	{
		return -Math.exp(2*u)-Math.exp(-2*u);
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
		Vector3d r=new Vector3d(1.0,0.9,1.0);
		Point3d P=Math3d.sphereSurface(r,u,v);
		Vector3d N=Math3d.sphereNormal(r,u,v);
		double g=0.015*deform(u,v)+0.01*noise(u,v);
		double x=P.x+N.x*g;
		double y=P.y+N.y*g;
		double z=P.z+N.z*g;
		return new Point3d(x,y,z);
	}
	public Color3f color(double u,double v)
	{
		return new Color3f(1.0f,0.6f,0f);
	}
}
interface Function
{
	Point3d surface(double u,double v);
	Color3f color(double u,double v);
}
class Surface3D extends Shape3D
{
	public int n=200,m=200;
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
	Appearance getDefaultAppearance()
	{
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setSpecularColor(new Color3f(0.2f,0.2f,0.2f));
		Appearance1.setMaterial(Material1);
		return Appearance1;
	}
}
