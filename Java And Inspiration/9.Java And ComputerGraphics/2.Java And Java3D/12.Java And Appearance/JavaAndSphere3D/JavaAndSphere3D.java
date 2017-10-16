import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndSphere3D
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
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom();
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		MouseTranslate MouseTranslate1=new MouseTranslate();
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		TransformGroup1.addChild((new Sphere3D()).getTriangleFanArray3D());
		TransformGroup1.addChild((new Sphere3D()).getQuadArray3D());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Sphere3D
{
	int n=50;
	int m=25;
	final int X=0,Y=1,Z=2;
	float R=0.8f;
	final float PI=(float)Math.PI;
	float w=2*PI/(n-1);
	float r=PI/(m-1);
	float[][][] SphereCoordinates3D=new float[m][n][3];
	public Sphere3D()
	{
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				SphereCoordinates3D[i][j][X]=R*(float)Math.sin(r*i)*(float)Math.cos(w*j);
				SphereCoordinates3D[i][j][Y]=R*(float)Math.cos(r*i);
				SphereCoordinates3D[i][j][Z]=R*(float)Math.sin(r*i)*(float)Math.sin(w*j);
			}
		}
	
	}
	public Shape3D getTriangleFanArray3D()
	{
		int v=0;
		int[] count=new int[]{1+n,1+n};
		TriangleFanArray TriangleFanArray1=new TriangleFanArray((1+n)*2,1,count);
		TriangleFanArray1.setCoordinate(v++,new Point3f(SphereCoordinates3D[0][0][X],SphereCoordinates3D[0][0][Y],SphereCoordinates3D[0][0][Z]));
		for(int j=0;j<n;j++)TriangleFanArray1.setCoordinate(v++,new Point3f(SphereCoordinates3D[1][j][X],SphereCoordinates3D[1][j][Y],SphereCoordinates3D[1][j][Z]));
		TriangleFanArray1.setCoordinate(v++,new Point3f(SphereCoordinates3D[m-1][0][X],SphereCoordinates3D[m-1][0][Y],SphereCoordinates3D[m-1][0][Z]));
		for(int j=0;j<n;j++)TriangleFanArray1.setCoordinate(v++,new Point3f(SphereCoordinates3D[m-2][j][X],SphereCoordinates3D[m-2][j][Y],SphereCoordinates3D[m-2][j][Z]));
		PolygonAttributes PolygonAttributes1=new PolygonAttributes();
		PolygonAttributes1.setCullFace(0);
		System.out.println(PolygonAttributes.POLYGON_LINE);
		PolygonAttributes1.setPolygonMode(1);
		Appearance Appearance1=new Appearance();
		Appearance1=new Appearance();
		Appearance1.setPolygonAttributes(PolygonAttributes1);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(TriangleFanArray1);
		shape3D.setAppearance(Appearance1);
		return shape3D;		
	}
	public Shape3D getQuadArray3D()
	{
		int v=0;
		QuadArray QuadArray1=new QuadArray((m-1)*(n-1)*4,1);
		for(int i=1;i<m-2;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				Point3f P00=new Point3f(SphereCoordinates3D[i+0][j+0][X],SphereCoordinates3D[i+0][j+0][Y],SphereCoordinates3D[i+0][j+0][Z]);
				Point3f P01=new Point3f(SphereCoordinates3D[i+0][j+1][X],SphereCoordinates3D[i+0][j+1][Y],SphereCoordinates3D[i+0][j+1][Z]);
				Point3f P11=new Point3f(SphereCoordinates3D[i+1][j+1][X],SphereCoordinates3D[i+1][j+1][Y],SphereCoordinates3D[i+1][j+1][Z]);
				Point3f P10=new Point3f(SphereCoordinates3D[i+1][j+0][X],SphereCoordinates3D[i+1][j+0][Y],SphereCoordinates3D[i+1][j+0][Z]);			
				QuadArray1.setCoordinate(i*(n-1)*4+j*4+0,P00);
				QuadArray1.setCoordinate(i*(n-1)*4+j*4+1,P01);
				QuadArray1.setCoordinate(i*(n-1)*4+j*4+2,P11);
				QuadArray1.setCoordinate(i*(n-1)*4+j*4+3,P10);
			}
		}
		PolygonAttributes PolygonAttributes1=new PolygonAttributes();
		PolygonAttributes1.setCullFace(0);
		System.out.println(PolygonAttributes.POLYGON_LINE);
		PolygonAttributes1.setPolygonMode(1);
		Appearance Appearance1=new Appearance();
		Appearance1=new Appearance();
		Appearance1.setPolygonAttributes(PolygonAttributes1);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(QuadArray1);
		shape3D.setAppearance(Appearance1);
		return shape3D;		
	}
}