import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndMeshline3D
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
		int m=20,n=20;
		double minX=-0.85,maxX=0.85,minZ=-0.6,maxZ=0.6;
		double dx=(maxX-minX)/(n-1);
		double dz=(maxZ-minZ)/(m-1);
		Point3d[][] points=new Point3d[m][n];
		double size=0.005;
		double u1=0,u2=0,o1=0.5,o2=0.5,p=0;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				double x=minX+j*dx;
				double z=minZ+i*dz;
				double y=G(x,z,u1,u2,o1,o2,p);
				points[i][j]=new Point3d(x,y,z);
			}
		}
		Meshline3D Meshline3D=new Meshline3D(points,size,Appearance1);
		TransformGroup1.addChild(Meshline3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static Point3d F(double t)
	{
		double w=100;
		double x=t*Math.sin(w*t)*(Math.PI/30);
		double y=t*0.01*w;
		double z=0.5*t*Math.cos(t*0.5*w);
		return new Point3d(x,y,z);
	}
	public static double G(double x,double x0,double d)
	{
		double u=(x-x0)/d;
		double k=Math.sqrt(2*Math.PI)*d;
		return Math.exp(-u*u/2)/k;
	}
	public static double G(double x,double y,double u1,double u2,double o1,double o2,double p)
	{
		double u=(x-u1)/o1;
		double v=(y-u2)/o2;
		double w=1-p*p;
		return Math.exp(-(u*u-2*p*u*v+v*v)/(2*w))/(2*Math.PI*o1*o2*Math.sqrt(w));
	}
	public Point3d F1(double t)
	{
		double w=2;
		return new Point3d(t,Math.sin(t*w),Math.cos(t*w));
	}
}
class Meshline3D extends TransformGroup
{
	public Meshline3D(Point3d[][] points,double size,Appearance appearance)
	{
		Point3d[][] newPoints=transpose(points);
		int m=points.length,n=points[0].length,v=0;
		double[] sizes=new double[n],newSizes=new double[m];
		for(int j=0;j<n;j++)sizes[j]=size;
		for(int i=0;i<m;i++)newSizes[i]=size;
		for(int i=0;i<m;i++)this.addChild(new Pipeline3D(points[i],sizes,appearance));
		for(int j=0;j<n;j++)this.addChild(new Pipeline3D(newPoints[j],newSizes,appearance));
	}
	private Point3d[][] transpose(Point3d[][] pointsMatrix)
	{
		int m=pointsMatrix.length,n=pointsMatrix[0].length;
		Point3d[][] newMatrix=new Point3d[n][m];
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<m;j++)newMatrix[i][j]=pointsMatrix[j][i];
		}
		return newMatrix;
	}
}
class Pipeline3D extends Shape3D
{
	public Vector3d D(Point3d p1,Point3d p2)
	{
		return new Vector3d(p2.x-p1.x,p2.y-p1.y,p2.z-p1.z);
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
		if(angle==0)return;
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
	public Pipeline3D(Point3d[] points,double[] sizes,Appearance appearance)
	{
		int m=points.length,n=10,v=0;
		Point3d[] coordinates=new Point3d[(m+2)*n];
		int[] coordinateIndices=new int[(m+1)*n*2];
		double angle=2*Math.PI/(n-1);
		Vector3d dF0=new Vector3d(0,1,0),axis=dF0;
		Vector3d[] d=new Vector3d[n];
		for(int j=0;j<n;j++)
		{
			double x=Math.cos(angle*j);
			double y=0;
			double z=Math.sin(angle*j);
			d[j]=new Vector3d(x,y,z);
		}
		for(int j=0;j<n;j++)coordinates[v++]=points[0];
		for(int i=0;i<m;i++)
		{
			Point3d P=points[i];
			double R=sizes[i];
			if(i==m-1)angle=0;
			else
			{
				Vector3d dF=D(points[i],points[i+1]);
				angle=angleToVector(dF0,dF);
				axis=crossVector(dF0,dF);
				dF0=dF;
			}
			for(int j=0;j<n;j++)
			{
				rotate(d[j],axis,angle);
				coordinates[v++]=new Point3d(P.x+R*d[j].x,P.y+R*d[j].y,P.z+R*d[j].z);
			}
		}
		for(int j=0;j<n;j++)coordinates[v++]=points[m-1];
		v=0;
		for(int i=1;i<m+2;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[m+1];
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