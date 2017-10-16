import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndFergusionSurface3D
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
		double k=-2;
		Point3d[][] ctrlPoints=
		{
			{new Point3d(-0.6,-0.1,-0.2),new Point3d(0.6,-0.1,-0.2)},
			{new Point3d(-0.6,-0.1,0.2),new Point3d(0.6,-0.1,0.2)}
		};
		Point3d[][] midPoints=
		{
			{new Point3d(-0.6,0.1,0),new Point3d(0.6,0.1,0)},
			{new Point3d(0,0.1,-0.2),new Point3d(0,0.1,0.2)}
		};
		Point3d[][] uCtrlVectors=
		{
			{sub(k,midPoints[0][0],ctrlPoints[0][0]),sub(k,midPoints[0][1],ctrlPoints[0][1])},
			{sub(k,ctrlPoints[1][0],midPoints[0][0]),sub(k,ctrlPoints[1][1],midPoints[0][1])}
		};
		Point3d[][] vCtrlVectors=
		{
			{sub(k,midPoints[1][0],ctrlPoints[0][0]),sub(k,ctrlPoints[0][1],midPoints[1][0])},
			{sub(k,midPoints[1][1],ctrlPoints[1][0]),sub(k,ctrlPoints[1][1],midPoints[1][1])}
		};
		Point3d[][] meshCtrlPoints=
		{
			{ctrlPoints[0][0],midPoints[1][0],ctrlPoints[0][1]},
			{midPoints[0][0],new Point3d(0,0.2,0),midPoints[0][1]},
			{ctrlPoints[1][0],midPoints[1][1],ctrlPoints[1][1]}
		};
		FergusionSurface3D FergusionSurface3D=new FergusionSurface3D(ctrlPoints,uCtrlVectors,vCtrlVectors);
		FergusionSurface3D.setAppearance(Appearance1);
		TransformGroup1.addChild(FergusionSurface3D);
		TransformGroup1.addChild(new Mesh3D(meshCtrlPoints));
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
class FergusionSurface3D extends Shape3D
{
	public FergusionSurface3D(Point3d[][] ctrlPoints,Point3d[][] uCtrlVectors,Point3d[][] vCtrlVectors)
	{
		int r=100,c=100,v=0;
		double du=1.0/(r-1),dv=1.0/(c-1);
		Point3d[] coordinates=new Point3d[r*c];
		int[] coordinateIndices=new int[(r-1)*(c-1)*4];
		for(int i=0;i<r;i++)
		{
			for(int j=0;j<c;j++)coordinates[i*c+j]=this.getCoordinate(ctrlPoints,uCtrlVectors,vCtrlVectors,i*du,j*dv);
		}
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
	private Point3d getCoordinate(Point3d ctrlPoint0,Point3d ctrlPoint1,Point3d ctrlVector0,Point3d ctrlVector1,double t)
	{
		double x=0,y=0,z=0;
		x+=F0(ctrlPoint0,t).x;
		y+=F0(ctrlPoint0,t).y;
		z+=F0(ctrlPoint0,t).z;

		x+=F1(ctrlPoint1,t).x;
		y+=F1(ctrlPoint1,t).y;
		z+=F1(ctrlPoint1,t).z;

		x+=G0(ctrlVector0,t).x;
		y+=G0(ctrlVector0,t).y;
		z+=G0(ctrlVector0,t).z;

		x+=G1(ctrlVector1,t).x;
		y+=G1(ctrlVector1,t).y;
		z+=G1(ctrlVector1,t).z;
		return new Point3d(x,y,z);
	}
	private Point3d getCoordinate(Point3d[][] ctrlPoints,Point3d[][] uCtrlVectors,Point3d[][] vCtrlVectors,double u,double v)
	{
		Point3d zeroVector=new Point3d(0,0,0);
		Point3d ctrlPoint0=getCoordinate(ctrlPoints[0][0],ctrlPoints[1][0],uCtrlVectors[0][0],uCtrlVectors[1][0],u);
		Point3d ctrlPoint1=getCoordinate(ctrlPoints[0][1],ctrlPoints[1][1],uCtrlVectors[0][1],uCtrlVectors[1][1],u);
		Point3d ctrlVector0=getCoordinate(vCtrlVectors[0][0],vCtrlVectors[1][0],zeroVector,zeroVector,u);
		Point3d ctrlVector1=getCoordinate(vCtrlVectors[0][1],vCtrlVectors[1][1],zeroVector,zeroVector,u);
		return getCoordinate(ctrlPoint0,ctrlPoint1,ctrlVector0,ctrlVector1,v);
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