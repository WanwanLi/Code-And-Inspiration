import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndSkinSurface3D
{
	public static void main(String[] args)
	{
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
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom();
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate();
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,1f));
		Appearance1.setMaterial(Material1);
		double[] X={-0.5,-0.2,0.1,0.4};
		double[][] Z=
		{
			{-0.4,0.4},
			{-0.3,0.35},
			{-0.3,0.4},
			{-0.2,0.1}
		};
		int m=4,n=80;
		Point3d[][] curvePoints=new Point3d[m][n];
		for(int i=0;i<m;i++)
		{
			double dz=(Z[i][1]-Z[i][0])/(n-1);
			for(int j=0;j<n;j++)
			{
				double x=X[i];
				double z=Z[i][0]+j*dz;
				double y=F(i,z);
				curvePoints[i][j]=new Point3d(x,y,z);
			}
		}
		SkinSurface3D SkinSurface3D=new SkinSurface3D(curvePoints);
		SkinSurface3D.setAppearance(Appearance1);
		TransformGroup1.addChild(SkinSurface3D);
	//	TransformGroup1.addChild(new Mesh3D(curvePoints));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	static Point3d sub(double k,Point3d p0,Point3d p1)
	{
		return new Point3d(k*(p1.x-p0.x),k*(p1.y-p0.y),k*(p1.z-p0.z));
	}
	public static double F(int i,double x)
	{
		switch(i)
		{
			case 0:return 0.2*G(x,0,0.2);
			case 1:return 0.1*Math.sin(20*x);
			case 2:return 2.5*x*x;
			case 3:return 0.5*Math.exp(2*x);
		}
		return 0;
	}
	public static double G(double x,double x0,double d)
	{
		double u=(x-x0)/d;
		double k=Math.sqrt(2*Math.PI)*d;
		return Math.exp(-u*u/2)/k;
	}
}
class Mesh3D  extends Shape3D
{
	public Mesh3D(Point3d[][] meshPoints)
	{
		int m=meshPoints.length,n=meshPoints[0].length,v=0;
		Point3d[] coordinates=new Point3d[m*n];
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)coordinates[i*n+j]=meshPoints[i][j];
		int[] coordinateIndices=new int[(m-1)*(n-1)*4+(m-1)*2+(n-1)*2];
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+1)*n+(j+0);
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+0)*n+(j+1);
			}
		}
		for(int i=0;i<m-1;i++)
		{
			int j=n-1;
			{
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+1)*n+(j+0);
			}
		}
		{
			int i=m-1;
			for(int j=0;j<n-1;j++)
			{
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+0)*n+(j+1);
			}
		}
		IndexedLineArray IndexedLineArray1=new IndexedLineArray(coordinates.length,IndexedLineArray.COORDINATES,coordinateIndices.length);
		IndexedLineArray1.setCoordinates(0,coordinates);
		IndexedLineArray1.setCoordinateIndices(0,coordinateIndices);
		this.setGeometry(IndexedLineArray1);
	}
}

class SkinSurface3D extends Shape3D
{
	public SkinSurface3D(Point3d[][] curvePoints)
	{
		int m=80,n=curvePoints[0].length,v=0;
		Point3d[][] newCurvePoints=transpose(curvePoints);
		Point3d[][] newCoordinates=new Point3d[n][];
		for(int j=0;j<n;j++)
		{
			double[] knots=getRiesenfeldKnots(newCurvePoints[j]);
			Vector3d[] ctrlVectors=getCtrlVectors(newCurvePoints[j],knots);
			newCoordinates[j]=getCoordinates(newCurvePoints[j],ctrlVectors,knots,m);
		}
		Point3d[] coordinates=new Point3d[m*n];
		for(int i=0;i<m;i++)for(int j=0;j<n;j++)coordinates[i*n+j]=newCoordinates[j][i];
		int[] coordinateIndices=new int[(m-1)*n*2];
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
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
	private Point3d F0(Point3d ctrlPoint0,double t)
	{
		double k=1-3*t*t+2*t*t*t;
		double x=ctrlPoint0.x*k;
		double y=ctrlPoint0.y*k;
		double z=ctrlPoint0.z*k;
		return new Point3d(x,y,z);
	}
	private Point3d F1(Point3d ctrlPoint1,double t)
	{
		double k=3*t*t-2*t*t*t;
		double x=ctrlPoint1.x*k;
		double y=ctrlPoint1.y*k;
		double z=ctrlPoint1.z*k;
		return new Point3d(x,y,z);
	}
	private Vector3d G0(Vector3d ctrlVector0,double t)
	{
		double k=t-2*t*t+t*t*t;
		double x=ctrlVector0.x*k;
		double y=ctrlVector0.y*k;
		double z=ctrlVector0.z*k;
		return new Vector3d(x,y,z);
	}
	private Vector3d G1(Vector3d ctrlVector1,double t)
	{
		double k=-t*t+t*t*t;
		double x=ctrlVector1.x*k;
		double y=ctrlVector1.y*k;
		double z=ctrlVector1.z*k;
		return new Vector3d(x,y,z);
	}
	private Point3d getCoordinate(Point3d ctrlPoint0,Point3d ctrlPoint1,Vector3d ctrlVector0,Vector3d ctrlVector1,double d,double t)
	{
		double x=0,y=0,z=0;
		x+=F0(ctrlPoint0,t).x;
		y+=F0(ctrlPoint0,t).y;
		z+=F0(ctrlPoint0,t).z;

		x+=F1(ctrlPoint1,t).x;
		y+=F1(ctrlPoint1,t).y;
		z+=F1(ctrlPoint1,t).z;

		x+=d*G0(ctrlVector0,t).x;
		y+=d*G0(ctrlVector0,t).y;
		z+=d*G0(ctrlVector0,t).z;

		x+=d*G1(ctrlVector1,t).x;
		y+=d*G1(ctrlVector1,t).y;
		z+=d*G1(ctrlVector1,t).z;
		return new Point3d(x,y,z);
	}
	private Point3d D(Point3d[] ctrlPoints,int i)
	{
		double dx=ctrlPoints[i+1].x-ctrlPoints[i].x;
		double dy=ctrlPoints[i+1].y-ctrlPoints[i].y;
		double dz=ctrlPoints[i+1].z-ctrlPoints[i].z;
		return new Point3d(dx,dy,dz);
	}
	private double d(double[] knots,int i)
	{
		return knots[i+1]-knots[i];
	}
	private Vector3d getBessellCtrlVector(Point3d[] ctrlPoints,double[] knots,int i)
	{
		int n=ctrlPoints.length;
		if(i==0)
		{
			Vector3d v=getBessellCtrlVector(ctrlPoints,knots,1);
			double d=d(knots,0);
			Point3d p=D(ctrlPoints,0);
			double x=2.0*p.x/d-v.x;
			double y=2.0*p.y/d-v.y;
			double z=2.0*p.z/d-v.z;
			double r=Math.sqrt(x*x+y*y+z*z);
			return new Vector3d(x/r,y/r,z/r);
		}
		else if(i==n-1)
		{
			Vector3d v=getBessellCtrlVector(ctrlPoints,knots,n-2);
			double d=d(knots,n-2);
			Point3d p=D(ctrlPoints,n-2);
			double x=2.0*p.x/d-v.x;
			double y=2.0*p.y/d-v.y;
			double z=2.0*p.z/d-v.z;
			double r=Math.sqrt(x*x+y*y+z*z);
			return new Vector3d(x/r,y/r,z/r);
		}
		else
		{
			double d0=d(knots,i-1);
			double d1=d(knots,i);
			Point3d p0=D(ctrlPoints,i-1);
			Point3d p1=D(ctrlPoints,i);
			double k0=d1/(d0+d1)/d0;
			double k1=d0/(d0+d1)/d1;
			double x=k0*p0.x+k1*p1.x;
			double y=k0*p0.y+k1*p1.y;
			double z=k0*p0.z+k1*p1.z;
			double r=Math.sqrt(x*x+y*y+z*z);
			return new Vector3d(x/r,y/r,z/r);
		}
	}
	private Vector3d[] getCtrlVectors(Point3d[] ctrlPoints,double[] knots)
	{
		int l=ctrlPoints.length;
		Vector3d[] ctrlVectors=new Vector3d[l];
		for(int i=0;i<l;i++)ctrlVectors[i]=getBessellCtrlVector(ctrlPoints,knots,i);
		return ctrlVectors;
	}
	private Point3d[] getCoordinates(Point3d[] ctrlPoints,Vector3d[] ctrlVectors,double[] knots,int curvePointsLength)
	{
		int l=ctrlPoints.length,c=0;
		Point3d[] coordinates=new Point3d[curvePointsLength];
		double[] u=knots;
		double d,t=0.0,dt=1.0/(curvePointsLength-1);
		for(int i=0;i<l-1;i++)
		{
			Point3d p0=ctrlPoints[i+0];
			Point3d p1=ctrlPoints[i+1];
			Vector3d v0=ctrlVectors[i+0];
			Vector3d v1=ctrlVectors[i+1];
			for(;t<u[i+1]&&c<curvePointsLength;t+=dt,c++)
			{
				d=u[i+1]-u[i];
				coordinates[c]=getCoordinate(p0,p1,v0,v1,d,(t-u[i])/d);
			}
		}
		return coordinates;
	}
	public static double[] getUniformKnots(int ctrlPointsLength)
	{
		int l=ctrlPointsLength;
		double[] knots=new double[l];
		double du=1.0/(l-1);
		for(int i=0;i<l;i++)knots[i]=i*du;
		return knots;
	}
	public  static double distance(Point3d point0,Point3d point1)
	{
		double dx=point1.x-point0.x;
		double dy=point1.y-point0.y;
		double dz=point1.z-point0.z;
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
	}
	public  static double[] getRiesenfeldKnots(Point3d[] ctrlPoints)
	{
		int l=ctrlPoints.length;
		double[] knots=new double[l];
		double[] u=new double[l];u[0]=0;
		for(int i=1;i<l;i++)u[i]=u[i-1]+distance(ctrlPoints[i-1],ctrlPoints[i]);
		for(int i=0;i<l;i++)knots[i]=u[i]/u[l-1];
		return knots;
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
