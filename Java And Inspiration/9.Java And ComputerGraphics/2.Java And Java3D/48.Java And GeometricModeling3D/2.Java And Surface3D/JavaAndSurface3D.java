import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndSurface3D
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
		Transform3D Transform3D=new Transform3D();
		Transform3D.setScale(new Vector3d(0.2,0.2,0.2));
		TransformGroup1.setTransform(Transform3D);
	//	TransformGroup1.addChild(new Surface3D(new Function_Ca(),-1.5,1.5,-1,1,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_Cos(),-1.5,1.5,-1,1,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_Sqrt_Abs_Sin_16(),-1.5,1.5,-1,1,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_Sin_Min(),-1.5,1.5,-1,1,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_Int_SinX_SinZ_x_z(),-3.2,3.2,-2.2,2.2,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_Log_SinX_SinZ_2(),-3.2,3.2,-2.2,2.2,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_HyperbolicParaboloid(),-1.5,1.5,-1,1,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ElectricField(),-3.2,3.2,-2.2,2.2,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_WaveField(),-3.2,3.2,-2.2,2.2,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_Wrinkle(),-3.2,3.2,-2.2,2.2,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_Mobius(),0,Math.PI,0,4*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_MobiusStrip(),0,2*Math.PI,-1,1,0,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_MobiusCompound(),0,2*Math.PI,0,2*Math.PI,0,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_Spiral(),0,Math.PI,0,6*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_Helix(),0,-2*Math.PI,0,5*Math.PI/2,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClineStrip(),0,-2*Math.PI,0,2*Math.PI,Appearance1));
		TransformGroup1.addChild(new Surface3D(new Function_ClineBottle(),0,2*Math.PI,0,2*Math.PI,0,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_SpiralCompound(),0,2*Math.PI,0,2*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_MinimalSurface1(),-1,1,-1,1,0,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_MinimalSurface2(),-Math.PI,Math.PI,-1,1,0,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_MinimalSurface3(),-Math.PI,Math.PI,-Math.PI,Math.PI,0,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_MinimalSurface4(),0,-2*Math.PI,-3*Math.PI,3*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_MinimalSurface5(),0,-2*Math.PI,0,1.3,0,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface1(),-2*Math.PI,2*Math.PI,-2*Math.PI,2*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface2(),-2*Math.PI,2*Math.PI,-2*Math.PI,2*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface3(),-2*Math.PI,2*Math.PI,-2*Math.PI,2*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface4(),-2*Math.PI,2*Math.PI,-2*Math.PI,2*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface5(),-2*Math.PI,2*Math.PI,-2*Math.PI,2*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface6(),-2*Math.PI,2*Math.PI,-2*Math.PI,2*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface7(),0,2*Math.PI,0,2*Math.PI,0,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface8(),0,5*Math.PI,0,1.2*Math.PI,0,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface9(),-10*Math.PI,10*Math.PI,-10*Math.PI,10*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface10(),-2*Math.PI,2*Math.PI,-2*Math.PI,2*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface11(),-4*Math.PI,4*Math.PI,-4*Math.PI,4*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface12(),-4*Math.PI,4*Math.PI,-4*Math.PI,4*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface13(),Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface14(),0,Math.PI,0,2*Math.PI,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface15(),0,2,-6,6,0,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface16(),-1.15,1.15,-1.15,1.15,0,Appearance1));
	//	TransformGroup1.addChild(new Surface3D(new Function_ClassicSurface17(),-1.15,1.15,-1.15,1.15,0,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Math3d
{
	public static double sa(double x)
	{
		return x==0?1:Math.sin(x)/x;
	}
	public static double ca(double x)
	{
		return x==0?Double.MAX_VALUE:Math.cos(x)/x;
	}
}
class Function_Sa implements Function
{
	public Point3d surface(double x,double z)
	{
		double D=Math.sqrt(x*x+z*z);
		double A=1.5,W=10;
		double y=A*Math3d.sa(W*D);
		return new Point3d(x,y,z);
	}
}
class Function_Ca implements Function
{
	public Point3d surface(double x,double z)
	{
		double D=Math.sqrt(x*x+z*z);
		double A=1.5,W=10;
		double y=A*Math3d.ca(W*D);
		return new Point3d(x,y,z);
	}
}
class Function_Cos implements Function
{
	public Point3d surface(double x,double z)
	{
		double D=Math.sqrt(x*x+z*z);
		double A=0.5,W=10;
		double y=A*Math.cos(W*D);
		return new Point3d(x,y,z);
	}
}
class Function_Sqrt_Abs_Sin_16 implements Function
{
	public Point3d surface(double x,double z)
	{
		double y=Math.sqrt(Math.abs(x))+0.3*Math.pow(Math.sin(10*z),16);
		return new Point3d(x,y,z);
	}
}
class Function_Sin_Min implements Function
{
	public Point3d surface(double x,double z)
	{
		double y=0.2*Math.sin(20*Math.min(x*x,z*z));
		return new Point3d(x,y,z);
	}
}
class Function_Int_SinX_SinZ_x_z implements Function
{
	public Point3d surface(double x,double z)
	{
		double y=(int)(Math.sin(x)+Math.sin(z)+x+z);
		return new Point3d(x,y,z);
	}
}
class Function_Log_SinX_SinZ_2 implements Function
{
	public Point3d surface(double x,double z)
	{
		double w=4;
		double y=Math.log(Math.sin(w*x)+Math.sin(w*z)+2.0);
		return new Point3d(x,y,z);
	}
}
class Function_HyperbolicParaboloid implements Function
{
	public Point3d surface(double x,double z)
	{
		double y=0.5*(x*x/0.2-z*z/0.1);
		return new Point3d(x,y,z);
	}
}
class Function_WaveField implements Function
{
	public Point3d surface(double x,double z)
	{
		double y=2*Math.cos(x*x)*Math.sin(z*z)/Math.exp(0.25*(x*x+z*z))-1;
		return new Point3d(x,y,z);
	}
}
class Function_Wrinkle implements Function
{
	public Point3d surface(double x,double z)
	{
		double k=4,A=0.5;
		double y=A*(k+Math.sin(x))*Math.cos(k*z);
		return new Point3d(x,y,z);
	}
}
class Function_ElectricField implements Function
{
	public Point3d surface(double x,double z)
	{
		double Q=0.2;
		double D=1.0;
		double y=Q/Math.sqrt((x-D)*(x-D)+z*z)-Q/Math.sqrt((x+D)*(x+D)+z*z);
		return new Point3d(x,y,z);
	}
}
class Function_Mobius implements Function
{
	public Point3d surface(double u,double v)
	{
		double R=2,r=1.5;
		double m=Math.cos(v/2)*Math.sin(u)-Math.sin(v/2)*Math.sin(2*u);
		double  n=Math.sin(v/2)*Math.sin(u)+Math.cos(v/2)*Math.sin(2*u);
		double x=(R+m)*Math.cos(v);
		double y=r*n;
		double z=(R+m)*Math.sin(v);
		return new Point3d(x,y,z);
	}
}
class Function_MobiusStrip implements Function
{
	public Point3d surface(double u,double v)
	{
		double x=(1+0.5*v*Math.cos(0.5*u))*Math.cos(u);
		double y=0.5*v*Math.sin(0.5*u);
		double z=(1+0.5*v*Math.cos(0.5*u))*Math.sin(u);
		return new Point3d(x,y,z);
	}
}
class Function_Helix implements Function
{
	public Point3d surface(double u,double v)
	{
		double R=1.2*v,r=2.5;
		double x=R*Math.sin(u)*Math.sin(u)*Math.sin(v);
		double y=R*r*Math.sin(u)*Math.cos(u);
		double z=R*Math.sin(u)*Math.sin(u)*Math.cos(v);
		return new Point3d(x,y,z);
	}
}
class Function_MobiusCompound implements Function
{
	public Point3d surface(double u,double v)
	{
		double c=4+3*Math.sin(v)*Math.cos(u)-3*Math.sin(2*v)*Math.sin(u)/2;
		double x=c*Math.cos(u);
		double y=5*(Math.sin(u)*Math.sin(v)+Math.cos(u)*Math.sin(2*v));
		double z=c*Math.sin(u);
		return new Point3d(x,y,z);
	}
}
class Function_ClineStrip implements Function
{
	public Point3d surface(double u,double v)
	{
		double r=0.2,w=1;
		double c=1+r*Math.cos(u);
		double x=c*Math.cos(v);
		double Y=r*Math.sin(u);
		double Z=c*Math.sin(v);
		double a=x+w*0.5*Math.PI;
		double y=Y*Math.cos(a)-Z*Math.sin(a);
		double z=Y*Math.sin(a)+Z*Math.cos(a);
		return new Point3d(x,y,z);
	}
}
class Function_ClineBottle implements Function
{
	public Point3d surface(double u,double v)
	{
		double a=6.0,b=16.0,c=4.0;
		double r=c*(1-Math.cos(u)/2);
		double x=a*Math.cos(u)*(1+Math.sin(u))+r*Math.cos(u)*Math.cos(v);
		if(u>Math.PI)x=a*Math.cos(u)*(1+Math.sin(u))+r*Math.cos(v+Math.PI);
		double y=b*Math.sin(u)+r*Math.sin(u)*Math.cos(v);
		if(u>Math.PI)y=b*Math.sin(u);
		double z=r*Math.sin(v);
		return new Point3d(x,y,z);
	}
}
class Function_Spiral implements Function
{
	public Point3d surface(double u,double v)
	{
		double e=0.05;
		double x=e*v*Math.sin(u)*Math.cos(v);
		double y=e*v*Math.cos(u)+v*Math.PI*e;
		double z=e*v*Math.sin(u)*Math.sin(v);
		return new Point3d(x,y,z);
	}
}
class Function_SpiralCompound implements Function
{
	public Point3d surface(double u,double v)
	{
		double R=4,r=0.2,w=11,k=2;
		double x=r*(R-Math.cos(u)+Math.sin(w*v))*Math.cos(k*v);
		double y=r*(Math.sin(w*(v+Math.PI/2))+Math.sin(u));
		double z=r*(R-Math.cos(u)+Math.sin(w*v))*Math.sin(k*v);
		return new Point3d(x,y,z);
	}
}
class Function_MinimalSurface1 implements Function
{
	public Point3d surface(double u,double v)
	{
		double x=u-u*u*u/3+u*v*v*Math.PI;
		double y=u*u-v*v*Math.PI;
		double z=v-v*v*v/3+v*u*u;
		return new Point3d(x,y,z);
	}
}
class Function_MinimalSurface2 implements Function
{
	public Point3d surface(double u,double v)
	{
		double x=Math.sinh(v)*Math.sin(u);
		double y=0.3*u;
		double z=-Math.sinh(v)*Math.cos(u);
		return new Point3d(x,y,z);
	}
}
class Function_MinimalSurface3 implements Function
{
	public Point3d surface(double u,double v)
	{
		double R=2.2;
		double r=0.5;
		double k=11.0404;
		double w=0.30404;
		double x=R*2*Math.cosh(v/2)*Math.cos(u);
		double y=r*(2*Math.cosh(v/2)*Math.sin(u)*Math.sin(w*x+k)+2*v*Math.cos(w*x+k));
		double z=r*(2*Math.cosh(v/2)*Math.sin(u)*Math.cos(w*x+k)-2*v*Math.sin(w*x+k));
		return new Point3d(x,y,z);
	}
}
class Function_MinimalSurface4 implements Function
{
	public Point3d surface(double u,double v)
	{
		double r=0.5+0.7*Math.cos(u);
		double x=r*Math.cos(v);
		double y=Math.sin(u)+v/2.7;
		double z=r*Math.sin(v);
		return new Point3d(x,y,z);
	}
}
class Function_MinimalSurface5 implements Function
{
	public Point3d surface(double u,double v)
	{
		int n=5;
		double p=2.0*n-1.0;
		double x=v*Math.cos(v)-Math.pow(v,p)*Math.cos(p*u)/p;
		double y=v*Math.sin(v)+Math.pow(v,p)*Math.sin(p*u)/p;
		double z=2*Math.pow(v,n)*Math.cos(n*u)/n;
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface1 implements Function
{
	public Point3d surface(double x,double z)
	{
		double A=0.1,w=20;
		double y=A*Math.sin(z*Math.cos(w*x)+z*Math.sin(w*x));
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface2 implements Function
{
	public Point3d surface(double x,double z)
	{
		double A=0.3,B=4.0;
		double y=A*Math.cos(B*Math.cos(x)+Math.sin(z)+B*Math.cos(z)+Math.sin(x));
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface3 implements Function
{
	public Point3d surface(double x,double z)
	{
		double a=1.3,b=1.4,c=0.1;
		double y=a*x*x*z-b*x*x*x*x-c*z*z*z;
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface4 implements Function
{
	public Point3d surface(double x,double z)
	{
		double A=1.5;
		double y=A*Math.sin(x+Math.cos(z));
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface5 implements Function
{
	public Point3d surface(double x,double z)
	{
		double A=0.5;
		double y=A*Math.sin(x*z);
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface6 implements Function
{
	public Point3d surface(double u,double v)
	{
		double x=u+v;
		double z=v-u;
		double y=Math.pow(Math.sin(u),16)*3*Math.pow(Math.cos(v),16)-Math.pow(u*u+v*v,0.55);
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface7 implements Function
{
	public Point3d surface(double u,double v)
	{
		double x=v*Math.sin(u)*Math.cos(v);
		double y=v*Math.sin(u)*Math.sin(v);
		double z=1.5*v*Math.cos(u);
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface8 implements Function
{
	public Point3d surface(double u,double v)
	{
		double x=u*Math.cos(Math.cos(u))*Math.cos(v);
		double y=u*Math.cos(Math.cos(u))*Math.sin(v);
		double z=u*Math.sin(Math.cos(u));
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface9 implements Function
{
	public Point3d surface(double x,double z)
	{
		double y=0.04*Math.pow(Math.pow(Math.sin(x)*Math.cos(z)*x*z,2),0.4);
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface10 implements Function
{
	public Point3d surface(double x,double z)
	{
		double A=1.8,w=1.3;
		double y=A*Math.cos(w*x)*Math.cos(w*z);
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface11 implements Function
{
	public Point3d surface(double x,double z)
	{
		double y=1.2*(Math.sin(x+Math.sin(z))+Math.cos(x+Math.cos(z)));
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface12 implements Function
{
	public Point3d surface(double x,double z)
	{
		double y=0.02*Math.cos(x)*z*z;
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface13 implements Function
{
	public Point3d surface(double u,double v)
	{
		double x0=-3,x1=3,z0=-3,z1=3;
		double w=10;
		double x=x0+(x1-x0)*u;
		double z=z0+(z1-z0)*v;
		double y=20.0*Math.cos(w*x)*Math.cos(w*z)*u*(1-u)*v*(1-v);
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface14 implements Function
{
	public Point3d surface(double u,double v)
	{
		double h=0.05,w=20;
		double x=Math.sin(u)*Math.cos(v)+h*Math.cos(w*v);
		double y=Math.cos(u);
		double z=Math.sin(u)*Math.sin(v)+h*Math.cos(w*u);
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface15 implements Function
{
	public Point3d surface(double u,double v)
	{
		double x=Math.pow(u,2)*Math.cos(2*v)/2;
		double y=-v-Math.pow(u,2)*Math.sin(2*v)/2;
		double z=2*u*Math.cos(v);
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface16 implements Function
{
	public Point3d surface(double u,double v)
	{
		double x=u-Math.pow(u,7)/7+3*Math.pow(u,5)*Math.pow(v,2)-5*Math.pow(u,3)*Math.pow(v,4)+u*Math.pow(v,6);
		double y=-v-v*Math.pow(u,6)+5*Math.pow(u,4)*Math.pow(v,3)-3*Math.pow(u,2)*Math.pow(v,5)+Math.pow(v,7)/7;
		double z=Math.pow(u,4)/2-3*Math.pow(u,2)*Math.pow(v,2)+Math.pow(v,4)/2;
		return new Point3d(x,y,z);
	}
}
class Function_ClassicSurface17 implements Function
{
	public Point3d surface(double x,double z)
	{
		double A=1.5,r=0.05;
		double d=Math.sqrt(x*x+z*z);
		double y=A*Math.max(0,1-d/r);
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
	public Surface3D(Function function,Appearance appearance)
	{
		for(int i=0;i<m;i++)
		{
			double u=(0.0+i)/(m-1);
			for(int j=0;j<n;j++)
			{
				double v=(0.0+j)/(n-1);
				this.coordinates[i*n+j]=function.surface(u,v);
			}
		}
		this.setGeometry(this.getStriangleStripArray());
		this.setAppearance(appearance);
	}
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
			}
		}
		this.setGeometry(this.getStriangleStripArray());
		this.setAppearance(appearance);
	}
	public Surface3D(Function function,double u0,double u1,double v0,double v1,int doubleSurface,Appearance appearance)
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
		this.setGeometry(this.getStriangleStripArray(doubleSurface));
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
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		return GeometryInfo1.getGeometryArray();
	}
}
