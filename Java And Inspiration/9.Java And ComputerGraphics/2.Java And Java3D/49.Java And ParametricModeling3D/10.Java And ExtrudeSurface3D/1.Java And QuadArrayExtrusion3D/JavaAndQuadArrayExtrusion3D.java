import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndQuadArrayExtrusion3D
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
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate();
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		Appearance Appearance1=new Appearance();
		Appearance1=new Appearance();
		Material Material1=new Material();
		Appearance1.setMaterial(Material1);
		Material1.setDiffuseColor(new Color3f(0f,1f,0f));
		Material1.setSpecularColor(new Color3f(0f,0f,1f));
		Material1.setShininess(4f);
		Point3d[] quadArrayCoordinates=new Point3d[]
		{
			new Point3d(-0.5,0.2,0.1),
			new Point3d(-0.5,0,0.1),
			new Point3d(0.5,0,0.1),
			new Point3d(0.5,0.2,0.1),
			new Point3d(-0.1,0,0.1),
			new Point3d(-0.1,-0.75,0.1),
			new Point3d(0.1,-0.75,0.1),
			new Point3d(0.1,0,0.1)
		};
		Vector3d extrusionVector=new Vector3d(0,0,-0.2);
		TransformGroup1.addChild(new QuadArrayExtrusion3D(quadArrayCoordinates,extrusionVector,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class QuadArrayExtrusion3D extends Shape3D
{
	public QuadArrayExtrusion3D(Point3d[] quadArrayCoordinates,Vector3d extrusionVector,Appearance appearance)
	{
		int c=0,l=quadArrayCoordinates.length;
		double dx=extrusionVector.x;
		double dy=extrusionVector.y;
		double dz=extrusionVector.z;
		QuadArray QuadArray1=new QuadArray(l*6,QuadArray.COORDINATES);
		for(int i=0;i<l;i+=4)
		{
			Point3d p00=quadArrayCoordinates[i+0];
			Point3d p01=quadArrayCoordinates[i+1];
			Point3d p02=quadArrayCoordinates[i+2];
			Point3d p03=quadArrayCoordinates[i+3];
			Point3d p10=new Point3d(p00.x+dx,p00.y+dy,p00.z+dz);
			Point3d p11=new Point3d(p01.x+dx,p01.y+dy,p01.z+dz);
			Point3d p12=new Point3d(p02.x+dx,p02.y+dy,p02.z+dz);
			Point3d p13=new Point3d(p03.x+dx,p03.y+dy,p03.z+dz);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p02);
			QuadArray1.setCoordinate(c++,p03);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p03);
			QuadArray1.setCoordinate(c++,p13);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p12);
			QuadArray1.setCoordinate(c++,p02);
			QuadArray1.setCoordinate(c++,p13);
			QuadArray1.setCoordinate(c++,p12);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p02);
			QuadArray1.setCoordinate(c++,p12);
			QuadArray1.setCoordinate(c++,p13);
			QuadArray1.setCoordinate(c++,p03);
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(QuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}