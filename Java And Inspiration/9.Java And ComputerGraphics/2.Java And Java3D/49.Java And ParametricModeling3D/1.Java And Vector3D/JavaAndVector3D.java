import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndVector3D
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
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,0.8f,0f));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new Vector3D(new Point3d(0.1,0,-0.1),new Vector3d(1,1,1)));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static Point3d F(double t)
	{
		double w=100;
		double x=t*Math.sin(w*t)*(Math.PI/30);
		double y=t*0.01*w;
		double z=0.5*t*Math.cos(t*0.5*w);
		return new Point3d(x,y,z);
	}
	public Point3d F1(double t)
	{
		double w=2;
		return new Point3d(t,Math.sin(t*w),Math.cos(t*w));
	}
}
class Vector3D extends TransformGroup
{
	public Vector3D(Point3d p,Vector3d v)
	{
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,0.8f,0f));
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(Material1);
		double length=Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
		double rotZ=Math.atan(v.y)-Math.PI/2;
		double rotY=-Math.atan(v.z);
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0f,length/2,0f));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Cylinder((float)(length/100),(float)(length),Appearance1));
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0f,length,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Cone((float)(length/30),(float)(length/5),Appearance1));
		TransformGroup TransformGroup3=new TransformGroup();
		TransformGroup3.addChild(TransformGroup1);
		TransformGroup3.addChild(TransformGroup2);
		transform3D=new Transform3D();
		transform3D.rotZ(rotZ);
		TransformGroup TransformGroup4=new TransformGroup(transform3D);
		TransformGroup4.addChild(TransformGroup3);
		transform3D=new Transform3D();
		transform3D.rotY(rotY);
		TransformGroup TransformGroup5=new TransformGroup(transform3D);
		TransformGroup5.addChild(TransformGroup4);
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(p.x,p.y,p.z));
		TransformGroup TransformGroup6=new TransformGroup(transform3D);
		TransformGroup6.addChild(TransformGroup5);
		this.addChild(TransformGroup6);
	}
}
