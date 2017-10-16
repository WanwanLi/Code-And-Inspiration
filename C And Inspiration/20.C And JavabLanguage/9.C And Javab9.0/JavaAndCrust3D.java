import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndCrust3D
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
		int m=80,n=50;
		double minX=-0.85,maxX=0.85,minZ=-0.6,maxZ=0.6;
		double dx=(maxX-minX)/(n-1);
		double dz=(maxZ-minZ)/(m-1);
		Point3d[][] points=new Point3d[m][n];
		double[][] outerSizes=new double[m][n];
		double[][] innerSizes=new double[m][n];
		double u1=0,u2=0,o1=0.5,o2=0.5,p=0,w=20;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				double x=minX+j*dx;
				double z=minZ+i*dz;
				double y=G(x,z,u1,u2,o1,o2,p);
				points[i][j]=new Point3d(x,y,z);
				outerSizes[i][j]=0.01+0.2*G(x,u1,o1)*G(z,u2,o2);
				innerSizes[i][j]=0.01+0.01*(Math.sin(w*x)*Math.sin(w*z));

			}
		}
		Crust3D Crust3D=new Crust3D(points,outerSizes,innerSizes,Appearance1);
		TransformGroup1.addChild(Crust3D);
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
class Crust3D extends Shape3D
{
	public Vector3d D(Point3d p1,Point3d p2)
	{
		return new Vector3d(p2.x-p1.x,p2.y-p1.y,p2.z-p1.z);
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
	public void unitVector(Vector3d vector)
	{
		double length=lengthOfVector(vector);
		vector.x/=length;
		vector.y/=length;
		vector.z/=length;
	}
	public Crust3D(Point3d[][] points,double[][] outerSizes,double[][] innerSizes,Appearance appearance)
	{
		int m=points.length-1,n=points[0].length-1,v=0;
		Point3d[] coordinates=new Point3d[m*n*2];
		int[][] indices=new int[m*n][2];
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				Vector3d v10=D(points[i][j],points[i+1][j+0]);
				Vector3d v01=D(points[i][j],points[i+0][j+1]);
				Vector3d normal=crossVector(v10,v01);unitVector(normal);
				double x0=points[i][j].x+outerSizes[i][j]*normal.x;
				double y0=points[i][j].y+outerSizes[i][j]*normal.y;
				double z0=points[i][j].z+outerSizes[i][j]*normal.z;
				coordinates[v]=new Point3d(x0,y0,z0);indices[i*n+j][0]=v++;
				double x1=points[i][j].x-innerSizes[i][j]*normal.x;
				double y1=points[i][j].y-innerSizes[i][j]*normal.y;
				double z1=points[i][j].z-innerSizes[i][j]*normal.z;
				coordinates[v]=new Point3d(x1,y1,z1);indices[i*n+j][1]=v++;
			}
		}
		int[] coordinateIndices=new int[(m-1)*n*2*2+n*2*2+m*2*2];v=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=indices[i*n+j][1];
				coordinateIndices[v++]=indices[(i-1)*n+j][1];

			}
		}
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=indices[(i-1)*n+j][0];
				coordinateIndices[v++]=indices[i*n+j][0];
			}
		}
		{
			int i=0;
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=indices[i*n+j][1];
				coordinateIndices[v++]=indices[i*n+j][0];
			}
		}
		{
			int i=m-1;
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=indices[i*n+j][0];
				coordinateIndices[v++]=indices[i*n+j][1];
			}
		}
		for(int i=0;i<m;i++)
		{
			int j=0;
			{
				coordinateIndices[v++]=indices[i*n+j][0];
				coordinateIndices[v++]=indices[i*n+j][1];
			}
		}
		for(int i=0;i<m;i++)
		{
			int j=n-1;
			{
				coordinateIndices[v++]=indices[i*n+j][1];
				coordinateIndices[v++]=indices[i*n+j][0];
			}
		}
		int[] stripCounts=new int[(m-1)*2+4];v=0;
		for(int i=0;i<m-1;i++)stripCounts[v++]=2*n;
		for(int i=0;i<m-1;i++)stripCounts[v++]=2*n;
		stripCounts[v++]=2*n;
		stripCounts[v++]=2*n;
		stripCounts[v++]=2*m;
		stripCounts[v++]=2*m;
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