import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndCircularWindowExtrusion3D
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
		TransformGroup1.addChild(new CircularWindowExtrusion3D(0.5,0.2,20,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class CircularWindowExtrusion3D extends Shape3D
{
	public CircularWindowExtrusion3D(double r,double t,int n,Appearance appearance)
	{
		int c=0;
		double a=0.5*Math.PI/n;
		QuadArray QuadArray1=new QuadArray(4*n*12+4*4,QuadArray.COORDINATES);
		Point3d[] p02=new Point3d[]
		{
			new Point3d(r,r,0),
			new Point3d(-r,r,0),
			new Point3d(-r,-r,0),
			new Point3d(r,-r,0)
		};
		Point3d[] p12=new Point3d[]
		{
			new Point3d(r,r,-t),
			new Point3d(-r,r,-t),
			new Point3d(-r,-r,-t),
			new Point3d(r,-r,-t)
		};
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<n;j++)
			{
				double w=j*a+i*Math.PI/2;
				Point3d p00=new Point3d(r*Math.cos(w),r*Math.sin(w),0);
				Point3d p10=new Point3d(r*Math.cos(w),r*Math.sin(w),-t);
				w+=a;
				Point3d p01=new Point3d(r*Math.cos(w),r*Math.sin(w),0);
				Point3d p11=new Point3d(r*Math.cos(w),r*Math.sin(w),-t);
				QuadArray1.setCoordinate(c++,p00);
				QuadArray1.setCoordinate(c++,p02[i]);
				QuadArray1.setCoordinate(c++,p01);
				QuadArray1.setCoordinate(c++,p00);
				QuadArray1.setCoordinate(c++,p10);
				QuadArray1.setCoordinate(c++,p11);
				QuadArray1.setCoordinate(c++,p12[i]);
				QuadArray1.setCoordinate(c++,p10);
				QuadArray1.setCoordinate(c++,p00);
				QuadArray1.setCoordinate(c++,p01);
				QuadArray1.setCoordinate(c++,p11);
				QuadArray1.setCoordinate(c++,p10);
			}
		}
		QuadArray1.setCoordinate(c++,p02[0]);
		QuadArray1.setCoordinate(c++,p12[0]);
		QuadArray1.setCoordinate(c++,p12[1]);
		QuadArray1.setCoordinate(c++,p02[1]);
		QuadArray1.setCoordinate(c++,p02[1]);
		QuadArray1.setCoordinate(c++,p12[1]);
		QuadArray1.setCoordinate(c++,p12[2]);
		QuadArray1.setCoordinate(c++,p02[2]);
		QuadArray1.setCoordinate(c++,p02[2]);
		QuadArray1.setCoordinate(c++,p12[2]);
		QuadArray1.setCoordinate(c++,p12[3]);
		QuadArray1.setCoordinate(c++,p02[3]);
		QuadArray1.setCoordinate(c++,p02[3]);
		QuadArray1.setCoordinate(c++,p12[3]);
		QuadArray1.setCoordinate(c++,p12[0]);
		QuadArray1.setCoordinate(c++,p02[0]);
		GeometryInfo GeometryInfo1=new GeometryInfo(QuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}