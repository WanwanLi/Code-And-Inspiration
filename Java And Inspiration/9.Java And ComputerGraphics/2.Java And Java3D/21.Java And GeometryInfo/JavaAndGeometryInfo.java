import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndGeometryInfo
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
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,0f));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new HyperbolicParaboloid3D(0.2f,0.1f,-0.5f,-0.5f,0.5f,0.5f));
//		TransformGroup1.addChild(new HyperbolicParaboloid3D(Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
class HyperbolicParaboloid3D extends Shape3D
{
	private float Y(float x,float z,float p,float q)
	{
		return 0.5f*(x*x/p-z*z/q);
	}
	private Vector3f unitVector3f(float x,float y,float z)
	{
		float l=(float)Math.sqrt(x*x+y*y+z*z);
		return new Vector3f(x/l,y/l,z/l);
	}
	public HyperbolicParaboloid3D(float p,float q,float x0,float z0,float x1,float z1)
	{
		int n=100,m=100;
		Point3f[] coordinates=new Point3f[m*n];
		float dx=(x1-x0)/n,dz=(z1-z0)/m;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				float x=x0+i*dx;
				float z=z0+j*dz;
				float y=Y(x,z,p,q);
				coordinates[i*n+j]=new Point3f(x,y,z);
			}
		}
		Color3f[] colors=new Color3f[m*n];
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				float x=x0+i*dx;
				float z=z0+j*dz;
				float y=2+10*f(x,z);
				if(y>1)y=1;
				if(y<0)y=0;
				colors[i*n+j]=new Color3f(0,y,(n-1.0f-j)/(n-1));
			}
		}
		Vector3f[] normals=new Vector3f[m*n];
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				float x=x0+i*dx;
				float z=z0+j*dz;
				float dYx=Y(x+dx,z,p,q)-Y(x,z,p,q);
				float dYz=Y(x,z+dz,p,q)-Y(x,z,p,q);
				normals[i*n+j]=unitVector3f(-dYx/dx,1,-dYz/dz);
			}
		}
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
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setColors(colors);
		GeometryInfo1.setColorIndices(coordinateIndices);
		GeometryInfo1.setNormals(normals);
		GeometryInfo1.setNormalIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(new Material());
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(Appearance1);
	}
	private float f(float x,float z)
	{
		return 0.2f*(2f*(float)(Math.cos(x*x)*Math.sin(z*z)/Math.exp(0.25*(x*x+z*z)))-1f);
	}
	public HyperbolicParaboloid3D(Appearance appearance)
	{
		int n=40,m=40;
		Point3f[] coordinates=new Point3f[m*n];
		int v=0;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				float x=(i-m/2)*0.2f;
				float z=(j-n/2)*0.2f;
				float y=f(x,z);
				coordinates[v++]=new Point3f(x,y,z);
			}
		}
		int[] coordinateIndices=new int[2*(m-1)*n];
		v=0;
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
		this.setAppearance(appearance);
	}
}