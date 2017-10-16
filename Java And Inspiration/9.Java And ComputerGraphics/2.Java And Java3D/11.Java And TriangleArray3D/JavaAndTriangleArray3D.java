import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndTriangleArray3D
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
		int n=50;
		int h=5;
		final int X=0,Y=1,Z=2;
		float R=0.8f;
		final float PI=(float)Math.PI;
		float w=2*PI/n;
		float[][][] TriangleArray3D=new float[h][n][3];
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<n;j++)
			{
				TriangleArray3D[i][j][X]=R*(float)Math.cos(w*j);
				TriangleArray3D[i][j][Y]=i*0.2f;
				TriangleArray3D[i][j][Z]=R*(float)Math.sin(w*j);
			}
		}
		System.out.println(GeometryArray.COORDINATES|GeometryArray.NORMALS);
		TriangleArray TriangleArray1=new TriangleArray(h*n*6,3);
		for(int i=0;i<h-1;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				Point3f P00=new Point3f(TriangleArray3D[i+0][j+0][X],TriangleArray3D[i+0][j+0][Y],TriangleArray3D[i+0][j+0][Z]);
				Point3f P10=new Point3f(TriangleArray3D[i+1][j+0][X],TriangleArray3D[i+1][j+0][Y],TriangleArray3D[i+1][j+0][Z]);
				Point3f P01=new Point3f(TriangleArray3D[i+0][j+1][X],TriangleArray3D[i+0][j+1][Y],TriangleArray3D[i+0][j+1][Z]);
				Point3f P11=new Point3f(TriangleArray3D[i+1][j+1][X],TriangleArray3D[i+1][j+1][Y],TriangleArray3D[i+1][j+1][Z]);
				Vector3f vector1=new Vector3f(P01.x-P00.x,P01.y-P00.y,P01.z-P00.z);
				Vector3f vector2=new Vector3f(P10.x-P00.x,P10.y-P00.y,P10.z-P00.z);
				Vector3f normal=new Vector3f();
				normal.cross(vector2,vector1);
				normal.normalize();
				TriangleArray1.setCoordinate(i*n*6+j*6+0,P00);
				TriangleArray1.setCoordinate(i*n*6+j*6+1,P10);
				TriangleArray1.setCoordinate(i*n*6+j*6+2,P01);
				TriangleArray1.setNormal(i*n*6+j*6+0,normal);
				TriangleArray1.setNormal(i*n*6+j*6+1,normal);
				TriangleArray1.setNormal(i*n*6+j*6+2,normal);
				vector1=new Vector3f(P11.x-P01.x,P11.y-P01.y,P11.z-P01.z);
				vector2=new Vector3f(P10.x-P01.x,P10.y-P01.y,P10.z-P01.z);
				normal.cross(vector2,vector1);
				normal.normalize();
				TriangleArray1.setCoordinate(i*n*6+j*6+3,P01);
				TriangleArray1.setCoordinate(i*n*6+j*6+4,P10);
				TriangleArray1.setCoordinate(i*n*6+j*6+5,P11);
				TriangleArray1.setNormal(i*n*6+j*6+3,normal);
				TriangleArray1.setNormal(i*n*6+j*6+4,normal);
				TriangleArray1.setNormal(i*n*6+j*6+5,normal);
			}
			Point3f P00=new Point3f(TriangleArray3D[i+0][n-1][X],TriangleArray3D[i+0][n-1][Y],TriangleArray3D[i+0][n-1][Z]);
			Point3f P10=new Point3f(TriangleArray3D[i+1][n-1][X],TriangleArray3D[i+1][n-1][Y],TriangleArray3D[i+1][n-1][Z]);
			Point3f P01=new Point3f(TriangleArray3D[i+0][0][X],TriangleArray3D[i+0][0][Y],TriangleArray3D[i+0][0][Z]);
			Point3f P11=new Point3f(TriangleArray3D[i+1][0][X],TriangleArray3D[i+1][0][Y],TriangleArray3D[i+1][0][Z]);	
			Vector3f vector1=new Vector3f(P01.x-P00.x,P01.y-P00.y,P01.z-P00.z);
			Vector3f vector2=new Vector3f(P10.x-P00.x,P10.y-P00.y,P10.z-P00.z);
			Vector3f normal=new Vector3f();
			normal.cross(vector2,vector1);
			normal.normalize();
			TriangleArray1.setCoordinate(i*n*6+(n-1)*6+0,P00);
			TriangleArray1.setCoordinate(i*n*6+(n-1)*6+1,P10);
			TriangleArray1.setCoordinate(i*n*6+(n-1)*6+2,P01);
			TriangleArray1.setNormal(i*n*6+(n-1)*6+0,normal);
			TriangleArray1.setNormal(i*n*6+(n-1)*6+1,normal);
			TriangleArray1.setNormal(i*n*6+(n-1)*6+2,normal);
			vector1=new Vector3f(P11.x-P01.x,P11.y-P01.y,P11.z-P01.z);
			vector2=new Vector3f(P10.x-P01.x,P10.y-P01.y,P10.z-P01.z);
			normal.cross(vector2,vector1);
			normal.normalize();
			TriangleArray1.setCoordinate(i*n*6+(n-1)*6+3,P01);
			TriangleArray1.setCoordinate(i*n*6+(n-1)*6+4,P10);
			TriangleArray1.setCoordinate(i*n*6+(n-1)*6+5,P11);
			TriangleArray1.setNormal(i*n*6+(n-1)*6+3,normal);
			TriangleArray1.setNormal(i*n*6+(n-1)*6+4,normal);
			TriangleArray1.setNormal(i*n*6+(n-1)*6+5,normal);

		}
		PolygonAttributes PolygonAttributes1=new PolygonAttributes();
		PolygonAttributes1.setCullFace(0);
		System.out.println(PolygonAttributes.POLYGON_LINE);
		PolygonAttributes1.setPolygonMode(1);
		Appearance Appearance1=new Appearance();
		Appearance1=new Appearance();
		Appearance1.setPolygonAttributes(PolygonAttributes1);
		Material Material1=new Material();
		Appearance1.setMaterial(Material1);
		Material1.setDiffuseColor(new Color3f(0f,1f,0f));
		Material1.setSpecularColor(new Color3f(0f,0f,1f));
		Material1.setShininess(4f);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(TriangleArray1);
		shape3D.setAppearance(Appearance1);
		TransformGroup1.addChild(shape3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}