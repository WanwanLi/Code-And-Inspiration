import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndHermiteSurface3D
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
		Point3d[][] ctrlPoints=
		{
			{
				new Point3d(-0.6,-0.1,-0.2),
				new Point3d(-0.4,-0.25,-0.3),
				new Point3d(-0.1,-0.25,-0.3),
				new Point3d(0.3,-0.55,-0.2),
				new Point3d(0.5,-0.42,-0.3),
				new Point3d(0.65,-0.3,-0.2)
			},
			{
				new Point3d(-0.6,0.1,0.1),
				new Point3d(-0.4,0.25,0.1),
				new Point3d(-0.1,0.25,0.1),
				new Point3d(0.3,-0.25,0.1),
				new Point3d(0.5,-0.2,0.1),
				new Point3d(0.65,0.3,0.1)
			},
			{
				new Point3d(-0.6,-0.1,0.2),
				new Point3d(-0.4,-0.25,0.3),
				new Point3d(-0.1,-0.25,0.3),
				new Point3d(0.3,-0.55,0.2),
				new Point3d(0.5,-0.42,0.3),
				new Point3d(0.65,-0.3,0.2)
			},
		};
		double[] uKnots=HermiteSurface3D.getUniformKnots(ctrlPoints.length);
		double[] vKnots=HermiteSurface3D.getUniformKnots(ctrlPoints[0].length);
		HermiteSurface3D HermiteSurface3D=new HermiteSurface3D(ctrlPoints,uKnots,vKnots);
		HermiteSurface3D.setAppearance(Appearance1);
		TransformGroup1.addChild(HermiteSurface3D);
		TransformGroup1.addChild(new Mesh3D(ctrlPoints));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	static Point3d sub(double k,Point3d p0,Point3d p1)
	{
		return new Point3d(k*(p1.x-p0.x),k*(p1.y-p0.y),k*(p1.z-p0.z));
	}
}
class HermiteSurface3D extends Shape3D
{
	public HermiteSurface3D(Point3d[][] ctrlPoints,double[] uKnots,double[] vKnots)
	{
		int v=0,r=80,c=80;
		Point3d[][] vCtrlVectors=getCtrlVectors(ctrlPoints,vKnots);
		Point3d[][] newCtrlPoints=transpose(ctrlPoints);
		Point3d[][] newCtrlVectors=getCtrlVectors(newCtrlPoints,uKnots);
		Point3d[][] uCtrlVectors=transpose(newCtrlVectors);
		Point3d[] coordinates=getCoordinates(ctrlPoints,uCtrlVectors,vCtrlVectors,uKnots,vKnots,r,c);
		int[] coordinateIndices=new int[(r-1)*(c-1)*4];
		for(int i=0;i<r-1;i++)
		{
			for(int j=0;j<c-1;j++)
			{
				coordinateIndices[v++]=(i+0)*c+(j+0);
				coordinateIndices[v++]=(i+1)*c+(j+0);
				coordinateIndices[v++]=(i+1)*c+(j+1);
				coordinateIndices[v++]=(i+0)*c+(j+1);
			}
		}
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES|IndexedQuadArray.NORMALS,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
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
	private Point3d G0(Point3d ctrlVector0,double t)
	{
		double k=t-2*t*t+t*t*t;
		double x=ctrlVector0.x*k;
		double y=ctrlVector0.y*k;
		double z=ctrlVector0.z*k;
		return new Point3d(x,y,z);
	}
	private Point3d G1(Point3d ctrlVector1,double t)
	{
		double k=-t*t+t*t*t;
		double x=ctrlVector1.x*k;
		double y=ctrlVector1.y*k;
		double z=ctrlVector1.z*k;
		return new Point3d(x,y,z);
	}
	private Point3d getCoordinate(Point3d ctrlPoint0,Point3d ctrlPoint1,Point3d ctrlVector0,Point3d ctrlVector1,double d,double t)
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
	private Point3d getBessellCtrlVector(Point3d[] ctrlPoints,double[] knots,int i)
	{
		int n=ctrlPoints.length;
		if(i==0)
		{
			Point3d v=getBessellCtrlVector(ctrlPoints,knots,1);
			double d=d(knots,0);
			Point3d p=D(ctrlPoints,0);
			double x=2.0*p.x/d-v.x;
			double y=2.0*p.y/d-v.y;
			double z=2.0*p.z/d-v.z;
			double r=Math.sqrt(x*x+y*y+z*z);
			return new Point3d(x/r,y/r,z/r);
		}
		else if(i==n-1)
		{
			Point3d v=getBessellCtrlVector(ctrlPoints,knots,n-2);
			double d=d(knots,n-2);
			Point3d p=D(ctrlPoints,n-2);
			double x=2.0*p.x/d-v.x;
			double y=2.0*p.y/d-v.y;
			double z=2.0*p.z/d-v.z;
			double r=Math.sqrt(x*x+y*y+z*z);
			return new Point3d(x/r,y/r,z/r);
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
			return new Point3d(x/r,y/r,z/r);
		}
	}
	private Point3d[][] getCtrlVectors(Point3d[][] ctrlPoints,double[] knots)
	{
		int m=ctrlPoints.length,n=ctrlPoints[0].length;
		Point3d[][] ctrlVectors=new Point3d[m][n];
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)ctrlVectors[i][j]=getBessellCtrlVector(ctrlPoints[i],knots,j);
		}
		return ctrlVectors;
	}
	private Point3d getCoordinate(Point3d[][] ctrlPoints,Point3d[][] uCtrlVectors,Point3d[][] vCtrlVectors,int i,int j,double dU,double dV,double u,double v)
	{
		Point3d zeroVector=new Point3d(0,0,0);
		Point3d ctrlPoint0=getCoordinate(ctrlPoints[i+0][j+0],ctrlPoints[i+1][j+0],uCtrlVectors[i+0][j+0],uCtrlVectors[i+1][j+0],dU,u);
		Point3d ctrlPoint1=getCoordinate(ctrlPoints[i+0][j+1],ctrlPoints[i+1][j+1],uCtrlVectors[i+0][j+1],uCtrlVectors[i+1][j+1],dU,u);
		Point3d ctrlVector0=getCoordinate(vCtrlVectors[i+0][j+0],vCtrlVectors[i+1][j+0],zeroVector,zeroVector,dU,u);
		Point3d ctrlVector1=getCoordinate(vCtrlVectors[i+0][j+1],vCtrlVectors[i+1][j+1],zeroVector,zeroVector,dU,u);
		return getCoordinate(ctrlPoint0,ctrlPoint1,ctrlVector0,ctrlVector1,dV,v);
	}
	private Point3d[] getCoordinates(Point3d[][] ctrlPoints,Point3d[][] uCtrlVectors,Point3d[][] vCtrlVectors,double[] uKnots,double[] vKnots,int row,int column)
	{
		int m=ctrlPoints.length,n=ctrlPoints[0].length,r=0,c=0;
		Point3d[] coordinates=new Point3d[row*column];
		double dU,u=0.0,du=1.0/(row-1),dV,v=0.0,dv=1.0/(column-1);
		for(int i=0;i<m-1;i++)
		{
			for(;u<uKnots[i+1]&&r<row;u+=du,r++)
			{
				dU=uKnots[i+1]-uKnots[i];c=0;v=0;
				for(int j=0;j<n-1;j++)
				{
					for(;v<vKnots[j+1]&&c<column;v+=dv,c++)
					{
						dV=vKnots[j+1]-vKnots[j];
						coordinates[r*column+c]=getCoordinate(ctrlPoints,uCtrlVectors,vCtrlVectors,i,j,dU,dV,(u-uKnots[i])/dU,(v-vKnots[j])/dV);
					}
				}
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