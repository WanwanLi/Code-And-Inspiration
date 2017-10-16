import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndExtrusion3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),100);
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
		Appearance1=new Appearance();
		Material Material1=new Material();
		Appearance1.setMaterial(Material1);
		Material1.setDiffuseColor(new Color3f(1f,0.8f,0f));
		Point3d[] polygonCoordinates=new Point3d[]
		{
			new Point3d(-0.5,0.2,0.1),
			new Point3d(-0.5,0,0.1),
			new Point3d(-0.1,0,0.1),
			new Point3d(-0.1,-0.75,0.1),
			new Point3d(0.1,-0.75,0.1),
			new Point3d(0.1,0,0.1),
			new Point3d(0.5,0,0.1),
			new Point3d(0.5,0.2,0.1)
		};
		Vector3d extrusionVector=new Vector3d(0,0,-0.2);
		TransformGroup1.addChild(new Extrusion3D(polygonCoordinates,extrusionVector,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Extrusion3D extends Shape3D
{
	public Extrusion3D(Point3d[] polygonCoordinates,Vector3d extrusionVector,Appearance appearance)
	{
		int n=polygonCoordinates.length,v=0;
		Point3d[] coordinates=new Point3d[2*n];
		for(int i=0;i<n;i++)
		{
			coordinates[i]=polygonCoordinates[i];
			double x=polygonCoordinates[i].x+extrusionVector.x;
			double y=polygonCoordinates[i].y+extrusionVector.y;
			double z=polygonCoordinates[i].z+extrusionVector.z;
			coordinates[n+i]=new Point3d(x,y,z);
		}
		int[] coordinateIndices=new int[n+4*n+n];
		for(int i=0;i<n;i++)coordinateIndices[v++]=i;
		for(int i=0;i<n-1;i++)
		{
			coordinateIndices[v++]=i+0;
			coordinateIndices[v++]=i+n+0;
			coordinateIndices[v++]=i+n+1;
			coordinateIndices[v++]=i+1;
		}
		{
			coordinateIndices[v++]=n-1;
			coordinateIndices[v++]=n-1+n;
			coordinateIndices[v++]=n;
			coordinateIndices[v++]=0;
		}
		for(int i=n-1;i>=0;i--)coordinateIndices[v++]=n+i;
		int[] stripCounts=new int[1+n+1];v=0;
		stripCounts[v++]=n;
		for(int i=0;i<n;i++)stripCounts[v++]=4;
		stripCounts[v++]=n;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}