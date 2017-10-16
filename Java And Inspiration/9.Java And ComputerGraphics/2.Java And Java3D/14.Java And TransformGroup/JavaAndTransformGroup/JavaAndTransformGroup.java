import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndTransformGroup
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Color3f color3f=new Color3f(0f,0f,0f);
		Background Background1=new Background(color3f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,0f,0f));
		Appearance1.setMaterial(Material1);
		BranchGroup1.addChild(new Sphere(0.1f,Appearance1));

		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
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

		Appearance Appearance2=new Appearance();
		Material Material2=new Material();
		Material2.setDiffuseColor(new Color3f(0f,1f,0f));
		Appearance2.setMaterial(Material2);
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0,-0.45,0));
		transform3D.setScale(new Vector3d(1,0.5,1.5));
		transform3D.setRotation(new AxisAngle4d(1,1,1,Math.PI/4));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);

		transform3D=new Transform3D();
		transform3D.setRotation(new AxisAngle4d(1,0,0,Math.PI/4));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);

		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0,0.25,0));
		TransformGroup TransformGroup4=new TransformGroup(transform3D);		

		TransformGroup4.addChild(new Cone(0.05f,0.1f,Appearance2));
		TransformGroup3.addChild(TransformGroup4);
		TransformGroup1.addChild(TransformGroup3);

		TransformGroup2.addChild(new Sphere(0.05f,Appearance2));
		TransformGroup1.addChild(TransformGroup2);
		TransformGroup1.addChild(new Sphere(0.15f,Appearance2));

		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
 