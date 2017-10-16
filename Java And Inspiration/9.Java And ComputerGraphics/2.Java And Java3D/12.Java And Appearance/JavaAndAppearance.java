import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndAppearance
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

		PolygonAttributes PolygonAttributes1=new PolygonAttributes();
		PolygonAttributes1.setCullFace(PolygonAttributes.CULL_BACK);
		PolygonAttributes1.setCullFace(PolygonAttributes.CULL_NONE);
		PolygonAttributes1.setPolygonMode(PolygonAttributes.POLYGON_POINT);
		PointAttributes PointAttributes1=new PointAttributes();
		PointAttributes1.setPointSize(0.5f);
		PointAttributes1.setPointAntialiasingEnable(true);

		PolygonAttributes PolygonAttributes2=new PolygonAttributes();
		PolygonAttributes2.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(0.1f);
		LineAttributes1.setLinePattern(LineAttributes.PATTERN_SOLID);
		LineAttributes1.setLinePattern(LineAttributes.PATTERN_DASH);
		LineAttributes1.setLinePattern(LineAttributes.PATTERN_DOT);
		LineAttributes1.setLinePattern(LineAttributes.PATTERN_DASH_DOT);
		LineAttributes1.setLineAntialiasingEnable(true);

		PolygonAttributes PolygonAttributes3=new PolygonAttributes();
		PolygonAttributes3.setPolygonMode(PolygonAttributes.POLYGON_FILL);
		ColoringAttributes ColoringAttributes1=new ColoringAttributes();
		ColoringAttributes1.setColor(1f,0f,0f);
		RenderingAttributes RenderingAttributes1=new RenderingAttributes();
		RenderingAttributes1.setIgnoreVertexColors(true);

		PolygonAttributes PolygonAttributes4=new PolygonAttributes();
		PolygonAttributes4.setPolygonMode(PolygonAttributes.POLYGON_FILL);
		ColoringAttributes ColoringAttributes2=new ColoringAttributes();
		ColoringAttributes2.setShadeModel(ColoringAttributes.SHADE_FLAT);
		ColoringAttributes2.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
		RenderingAttributes RenderingAttributes2=new RenderingAttributes();
		RenderingAttributes2.setIgnoreVertexColors(false);

		Appearance Appearance1=new Appearance();
		Appearance1.setPolygonAttributes(PolygonAttributes1);
		Appearance1.setPointAttributes(PointAttributes1);

		Appearance1.setPolygonAttributes(PolygonAttributes2);
		Appearance1.setLineAttributes(LineAttributes1);
		Appearance1.setPolygonAttributes(PolygonAttributes3);
		Appearance1.setColoringAttributes(ColoringAttributes1);
		Appearance1.setRenderingAttributes(RenderingAttributes1);
		Appearance1.setPolygonAttributes(PolygonAttributes4);
		Appearance1.setColoringAttributes(ColoringAttributes2);
		Appearance1.setRenderingAttributes(RenderingAttributes2);

		Appearance1.setMaterial(new Material());
		TransformGroup1.addChild(new Sphere3D(0.3,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Sphere3D extends TransformGroup
{
	public Sphere3D(double R,Appearance appearance)
	{
		int m=25,n=50;
		double u=Math.PI/(m-1);
		double v=2*Math.PI/(n-1);
		double du=0.000001*u;
		double dv=0.000001*v;
		Point3d[][] coordinates=new Point3d[m][n];
		Vector3f[][] normals=new Vector3f[m][n];
		Color3f[][] colors=new Color3f[m][n];
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				double x=R*Math.sin(u*i)*Math.cos(v*j);
				double y=R*Math.cos(u*i);
				double z=R*Math.sin(u*i)*Math.sin(v*j);
				coordinates[i][j]=new Point3d(x,y,z);
				double dxu=R*Math.sin(u*i+du)*Math.cos(v*j)-x;
				double dyu=R*Math.cos(u*i+du)-y;
				double dzu=R*Math.sin(u*i+du)*Math.sin(v*j)-z;
				double dxv=R*Math.sin(u*i)*Math.cos(v*j+dv)-x;
				double dyv=0;
				double dzv=R*Math.sin(u*i)*Math.sin(v*j+dv)-z;
				Vector3f v1=new Vector3f((float)dxu,(float)dyu,(float)dzu);
				Vector3f v2=new Vector3f((float)dxv,(float)dyv,(float)dzv);
				Vector3f normal=new Vector3f();
				normal.cross(v2,v1);
				normal.normalize();
				normals[i][j]=normal;
				colors[i][j]=new Color3f((float)(i/(m-1.0)),0f,0f);
			}
		}
		int c=0;
		int[] count=new int[]{1+n,1+n};
		TriangleFanArray TriangleFanArray1=new TriangleFanArray((1+n)*2,TriangleFanArray.COORDINATES|TriangleFanArray.NORMALS|TriangleFanArray.COLOR_3,count);
		TriangleFanArray1.setCoordinate(c,coordinates[0][0]);
		TriangleFanArray1.setNormal(c,new Vector3f(0,1,0));
		TriangleFanArray1.setColor(c++,colors[0][0]);
		for(int j=n-1;j>=0;j--)
		{
			TriangleFanArray1.setCoordinate(c,coordinates[1][j]);
			TriangleFanArray1.setNormal(c,normals[1][j]);
			TriangleFanArray1.setColor(c++,colors[1][j]);
		}
		TriangleFanArray1.setCoordinate(c,coordinates[m-1][0]);
		TriangleFanArray1.setNormal(c,new Vector3f(0,-1,0));
		TriangleFanArray1.setColor(c++,colors[m-1][0]);
		for(int j=0;j<n;j++)
		{
			TriangleFanArray1.setCoordinate(c,coordinates[m-2][j]);
			TriangleFanArray1.setNormal(c,normals[m-2][j]);
			TriangleFanArray1.setColor(c++,colors[m-2][j]);
		}
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(TriangleFanArray1);
		shape3D.setAppearance(appearance);
		this.addChild(shape3D);	
		c=0;
		QuadArray QuadArray1=new QuadArray((m-1)*(n-1)*4,QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.COLOR_3);
		for(int i=1;i<m-2;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				QuadArray1.setCoordinate(c+0,coordinates[i+0][j+0]);
				QuadArray1.setCoordinate(c+1,coordinates[i+0][j+1]);
				QuadArray1.setCoordinate(c+2,coordinates[i+1][j+1]);
				QuadArray1.setCoordinate(c+3,coordinates[i+1][j+0]);
				QuadArray1.setNormal(c+0,normals[i+0][j+0]);
				QuadArray1.setNormal(c+1,normals[i+0][j+1]);
				QuadArray1.setNormal(c+2,normals[i+1][j+1]);
				QuadArray1.setNormal(c+3,normals[i+1][j+0]);
				QuadArray1.setColor(c+0,colors[i+0][j+0]);
				QuadArray1.setColor(c+1,colors[i+0][j+1]);
				QuadArray1.setColor(c+2,colors[i+1][j+1]);
				QuadArray1.setColor(c+3,colors[i+1][j+0]);
				c+=4;
			}
		}
		shape3D=new Shape3D();
		shape3D.setGeometry(QuadArray1);
		shape3D.setAppearance(appearance);
		this.addChild(shape3D);
	}
}