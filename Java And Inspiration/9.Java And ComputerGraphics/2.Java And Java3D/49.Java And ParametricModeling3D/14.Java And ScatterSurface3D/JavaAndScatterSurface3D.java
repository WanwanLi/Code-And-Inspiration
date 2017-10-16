import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndScatterSurface3D
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
		Point3d[] scatterPoints=new Point3d[]
		{
			new Point3d(-1.0,0.0,-1.0),
			new Point3d(1.0,0.3,-1.0),
			new Point3d(-0.3,-0.5,-0.3),
			new Point3d(1.0,0.1,1.0),
			new Point3d(-1.0,0.4,1.0)
		};
		double r=0.4,x0=-2*r,x1=2*r,z0=-r,z1=r;
		TransformGroup1.addChild(new ScatterSurface3D(scatterPoints,x0,x1,z0,z1,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class ScatterSurface3D extends Shape3D
{
	public int n=100,m=100;
	public Point3d[] coordinates=new Point3d[m*n];
	public ScatterSurface3D(Point3d[] scatterPoints,double x0,double x1,double z0,double z1,Appearance appearance)
	{
		double dx=(x1-x0)/(n-1),dz=(z1-z0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double z=z0+i*dz;
			for(int j=0;j<n;j++)
			{
				double x=x0+j*dx;
				this.coordinates[i*n+j]=this.getInterpolatedPoint(scatterPoints,x,z);
			}
		}
		this.setGeometry(this.getStriangleStripArray());
		this.setAppearance(appearance);
	}
	private Point3d getInterpolatedPoint(Point3d[] scatterPoints,double x,double z)
	{
		double y=0,d=0;
		int l=scatterPoints.length;
		double[] r=new double[l];
		for(int i=0;i<l;i++)
		{
			double dx=scatterPoints[i].x-x;
			double dz=scatterPoints[i].z-z;
			r[i]=dx*dx+dz*dz;
			if(r[i]==0)return new Point3d(x,scatterPoints[i].y,z);
			r[i]=1.0/r[i];
			d+=r[i];
		}
		for(int i=0;i<l;i++)y+=scatterPoints[i].y*(r[i]/d);
		return new Point3d(x,y,z);
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
