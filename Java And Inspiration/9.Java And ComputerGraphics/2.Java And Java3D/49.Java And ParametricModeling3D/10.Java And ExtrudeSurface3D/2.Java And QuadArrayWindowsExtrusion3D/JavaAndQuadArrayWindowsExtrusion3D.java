import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndQuadArrayWindowsExtrusion3D
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
		Material1.setDiffuseColor(new Color3f(0f,1f,1f));
		Appearance1.setMaterial(Material1);
		Point3d[] outSideQuadArrayCoordinates=new Point3d[]
		{
			new Point3d(0,0,0),
			new Point3d(0.2,0,0),
			new Point3d(0.2,0.2,0),
			new Point3d(0,0.2,0)
		};
		Point3d[] inSideQuadArrayCoordinates=new Point3d[]
		{
			new Point3d(0.05,0.05,0),
			new Point3d(0.15,0.05,0),
			new Point3d(0.15,0.15,0),
			new Point3d(0.05,0.15,0)
		};
		TransformGroup1.addChild(new QuadArrayWindowsExtrusion3D(outSideQuadArrayCoordinates,inSideQuadArrayCoordinates,new Vector3d(0,0,-0.1),Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class QuadArrayWindowsExtrusion3D extends Shape3D
{
	public QuadArrayWindowsExtrusion3D(Point3d[] outSideQuadArrayCoordinates,Point3d[] inSideQuadArrayCoordinates,Vector3d extrusionVector,Appearance appearance)
	{
		int c=0,l=outSideQuadArrayCoordinates.length;
		double dx=extrusionVector.x;
		double dy=extrusionVector.y;
		double dz=extrusionVector.z;
		QuadArray QuadArray1=new QuadArray(l*16,QuadArray.COORDINATES);
		for(int i=0;i<l;i+=4)
		{
			Point3d op00=outSideQuadArrayCoordinates[i+0];
			Point3d op01=outSideQuadArrayCoordinates[i+1];
			Point3d op02=outSideQuadArrayCoordinates[i+2];
			Point3d op03=outSideQuadArrayCoordinates[i+3];
			Point3d ip00=inSideQuadArrayCoordinates[i+0];
			Point3d ip01=inSideQuadArrayCoordinates[i+1];
			Point3d ip02=inSideQuadArrayCoordinates[i+2];
			Point3d ip03=inSideQuadArrayCoordinates[i+3];
			Point3d op10=new Point3d(op00.x+dx,op00.y+dy,op00.z+dz);
			Point3d op11=new Point3d(op01.x+dx,op01.y+dy,op01.z+dz);
			Point3d op12=new Point3d(op02.x+dx,op02.y+dy,op02.z+dz);
			Point3d op13=new Point3d(op03.x+dx,op03.y+dy,op03.z+dz);
			Point3d ip10=new Point3d(ip00.x+dx,ip00.y+dy,ip00.z+dz);
			Point3d ip11=new Point3d(ip01.x+dx,ip01.y+dy,ip01.z+dz);
			Point3d ip12=new Point3d(ip02.x+dx,ip02.y+dy,ip02.z+dz);
			Point3d ip13=new Point3d(ip03.x+dx,ip03.y+dy,ip03.z+dz);
			QuadArray1.setCoordinate(c++,op00);
			QuadArray1.setCoordinate(c++,op01);
			QuadArray1.setCoordinate(c++,ip01);
			QuadArray1.setCoordinate(c++,ip00);
			QuadArray1.setCoordinate(c++,op01);
			QuadArray1.setCoordinate(c++,op02);
			QuadArray1.setCoordinate(c++,ip02);
			QuadArray1.setCoordinate(c++,ip01);
			QuadArray1.setCoordinate(c++,op02);
			QuadArray1.setCoordinate(c++,op03);
			QuadArray1.setCoordinate(c++,ip03);
			QuadArray1.setCoordinate(c++,ip02);
			QuadArray1.setCoordinate(c++,op03);
			QuadArray1.setCoordinate(c++,op00);
			QuadArray1.setCoordinate(c++,ip00);
			QuadArray1.setCoordinate(c++,ip03);
			QuadArray1.setCoordinate(c++,op13);
			QuadArray1.setCoordinate(c++,op12);
			QuadArray1.setCoordinate(c++,ip12);
			QuadArray1.setCoordinate(c++,ip13);
			QuadArray1.setCoordinate(c++,op12);
			QuadArray1.setCoordinate(c++,op11);
			QuadArray1.setCoordinate(c++,ip11);
			QuadArray1.setCoordinate(c++,ip12);
			QuadArray1.setCoordinate(c++,op11);
			QuadArray1.setCoordinate(c++,op10);
			QuadArray1.setCoordinate(c++,ip10);
			QuadArray1.setCoordinate(c++,ip11);
			QuadArray1.setCoordinate(c++,op10);
			QuadArray1.setCoordinate(c++,op13);
			QuadArray1.setCoordinate(c++,ip13);
			QuadArray1.setCoordinate(c++,ip10);
			QuadArray1.setCoordinate(c++,op00);
			QuadArray1.setCoordinate(c++,op10);
			QuadArray1.setCoordinate(c++,op11);
			QuadArray1.setCoordinate(c++,op01);
			QuadArray1.setCoordinate(c++,op01);
			QuadArray1.setCoordinate(c++,op11);
			QuadArray1.setCoordinate(c++,op12);
			QuadArray1.setCoordinate(c++,op02);
			QuadArray1.setCoordinate(c++,op02);
			QuadArray1.setCoordinate(c++,op12);
			QuadArray1.setCoordinate(c++,op13);
			QuadArray1.setCoordinate(c++,op03);
			QuadArray1.setCoordinate(c++,op03);
			QuadArray1.setCoordinate(c++,op13);
			QuadArray1.setCoordinate(c++,op10);
			QuadArray1.setCoordinate(c++,op00);
			QuadArray1.setCoordinate(c++,ip00);
			QuadArray1.setCoordinate(c++,ip01);
			QuadArray1.setCoordinate(c++,ip11);
			QuadArray1.setCoordinate(c++,ip10);
			QuadArray1.setCoordinate(c++,ip01);
			QuadArray1.setCoordinate(c++,ip02);
			QuadArray1.setCoordinate(c++,ip12);
			QuadArray1.setCoordinate(c++,ip11);
			QuadArray1.setCoordinate(c++,ip02);
			QuadArray1.setCoordinate(c++,ip03);
			QuadArray1.setCoordinate(c++,ip13);
			QuadArray1.setCoordinate(c++,ip12);
			QuadArray1.setCoordinate(c++,ip03);
			QuadArray1.setCoordinate(c++,ip00);
			QuadArray1.setCoordinate(c++,ip10);
			QuadArray1.setCoordinate(c++,ip13);
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(QuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}