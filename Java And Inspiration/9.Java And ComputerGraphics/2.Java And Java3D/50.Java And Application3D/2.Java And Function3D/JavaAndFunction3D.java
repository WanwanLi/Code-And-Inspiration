import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndFunction3D
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
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
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
		Material1.setDiffuseColor(new Color3f(1f,0.8f,0f));
		Appearance1.setMaterial(Material1);
		Function3D function3D=new Function3D(0,1.25,0.01,Appearance1);
		TransformGroup1.addChild(function3D);
	//	TransformGroup1.addChild(new Vector3D(function3D.F(Math.PI/8),function3D.D(Math.PI/8)));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Vector3D extends TransformGroup
{
	public Vector3D(Point3d p,Vector3d v)
	{
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,0.8f,0f));
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(Material1);
		double length=Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
		double rotZ=Math.atan(v.y)-Math.PI/2;
		double rotY=-Math.atan(v.z);
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0f,length/2,0f));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Cylinder((float)(length/100),(float)(length),Appearance1));
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0f,length,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Cone((float)(length/30),(float)(length/5),Appearance1));
		TransformGroup TransformGroup3=new TransformGroup();
		TransformGroup3.addChild(TransformGroup1);
		TransformGroup3.addChild(TransformGroup2);
		transform3D=new Transform3D();
		transform3D.rotZ(rotZ);
		TransformGroup TransformGroup4=new TransformGroup(transform3D);
		TransformGroup4.addChild(TransformGroup3);
		transform3D=new Transform3D();
		transform3D.rotY(rotY);
		TransformGroup TransformGroup5=new TransformGroup(transform3D);
		TransformGroup5.addChild(TransformGroup4);
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(p.x,p.y,p.z));
		TransformGroup TransformGroup6=new TransformGroup(transform3D);
		TransformGroup6.addChild(TransformGroup5);
		this.addChild(TransformGroup6);
	}
}
class Function3D extends Shape3D
{
	double minX,maxX;
	int m=300;
	int n=50;
	public Point3d F1(double t)
	{
		double w=2;
		return new Point3d(t,Math.sin(t*w),Math.cos(t*w));
	}
	public Point3d F(double t)
	{
		double w=100;
		double x=t*Math.sin(w*t)*(Math.PI/30);
		double y=t*0.01*w;
		double z=0.5*t*Math.cos(t*0.5*w);
		return new Point3d(x,y,z);
	}
	public Vector3d D(double t,double dt)
	{
		Point3d p1=F(t);
		Point3d p2=F(t+dt);
		return new Vector3d((p2.x-p1.x)/dt,(p2.y-p1.y)/dt,(p2.z-p1.z)/dt);
	}
	public double angleToY(double x,double y)
	{
		double r=Math.sqrt(x*x+y*y);
		return r==0?0:y>=0?Math.asin(x/r):Math.PI-Math.asin(x/r);
	}
	double lengthOfVector(Vector3d v)
	{
		return Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
	}
	public Vector3d crossVector(Vector3d v0,Vector3d v1)
	{
		double x=v0.y*v1.z-v1.y*v0.z;
		double y=v0.z*v1.x-v1.z*v0.x;
		double z=v0.x*v1.y-v1.x*v0.y;
		return new Vector3d(x,y,z);
	}
	public double angleToVector(Vector3d v0,Vector3d v1)
	{
		double l0=lengthOfVector(v0);
		double l1=lengthOfVector(v1);
		return Math.acos((v0.x*v1.x+v0.y*v1.y+v0.z*v1.z)/(l0*l1));
	}
	public void rotateX(Vector3d v,double a)
	{
		double y=v.y;
		double z=v.z;
		v.y=y*Math.cos(a)-z*Math.sin(a);
		v.z=y*Math.sin(a)+z*Math.cos(a);
	}
	public void rotateY(Vector3d v,double a)
	{
		double z=v.z;
		double x=v.x;
		v.z=z*Math.cos(a)-x*Math.sin(a);
		v.x=z*Math.sin(a)+x*Math.cos(a);
	}
	public void rotateZ(Vector3d v,double a)
	{
		double x=v.x;
		double y=v.y;
		v.x=x*Math.cos(a)-y*Math.sin(a);
		v.y=x*Math.sin(a)+y*Math.cos(a);
	}
	public void rotate(Vector3d vector,Vector3d axis,double angle)
	{
		Vector3d v=new Vector3d(vector.x,vector.y,vector.z);
		Vector3d a=new Vector3d(axis.x,axis.y,axis.z);
		double rot_Z=angleToY(a.x,a.y);
		rotateZ(a,rot_Z);
		double rot_X=-angleToY(a.z,a.y);
		rotateZ(v,rot_Z);
		rotateX(v,rot_X);
		rotateY(v,angle);
		rotateX(v,-rot_X);
		rotateZ(v,-rot_Z);
		vector.x=v.x;
		vector.y=v.y;
		vector.z=v.z;
	}
	public Function3D(double minT,double maxT,double R,Appearance appearance)
	{
		Point3d[] coordinates=new Point3d[(m+2)*n];
		int[] coordinateIndices=new int[(m+1)*n*2];
		int[] stripCounts=new int[m+1];int v=0;
		double angle=2*Math.PI/(n-1),dt=(maxT-minT)/(m-1);
		double dx,dy,dz,dX,dY,dZ,rotX,rotZ,t,Fx,Fy,Fz;
		Vector3d[] d=new Vector3d[n];
		for(int j=0;j<n;j++)
		{
			double x=R*Math.cos(angle*j);
			double y=0;
			double z=R*Math.sin(angle*j);
			d[j]=new Vector3d(x,y,z);
		}
		Vector3d dF0=new Vector3d(0,1,0);
		for(int j=0;j<n;j++)coordinates[v++]=new Point3d(F(minT).x,F(minT).y,F(minT).z);
		for(int i=0;i<m;i++)
		{
			t=minT+i*dt;
			Vector3d dF=D(t,dt);
			angle=angleToVector(dF0,dF);
			Vector3d axis=crossVector(dF0,dF);
			dF0=dF;
			for(int j=0;j<n;j++)
			{
				rotate(d[j],axis,angle);
				coordinates[v++]=new Point3d(F(t).x+d[j].x,F(t).y+d[j].y,F(t).z+d[j].z);
			}
		}
		for(int j=0;j<n;j++)coordinates[v++]=new Point3d(F(maxT).x,F(maxT).y,F(maxT).z);
		v=0;
		for(int i=1;i<m+2;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		for(int i=0;i<m+1;i++)stripCounts[i]=2*n;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());		
	}
}