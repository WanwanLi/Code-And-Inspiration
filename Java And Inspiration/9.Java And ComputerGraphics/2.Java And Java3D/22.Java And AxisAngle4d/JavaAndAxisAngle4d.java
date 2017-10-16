import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndAxisAngle4d
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
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,0f));
		Appearance1.setMaterial(Material1);
		LineArray LineArray1=new LineArray(2,LineArray.COORDINATES);
		LineArray1.setCoordinate(0,new Point3f(-0.5f,-0.5f,-0.5f));
		LineArray1.setCoordinate(1,new Point3f(0.5f,0.5f,0.5f));
		Shape3D shape3D=new Shape3D(LineArray1,Appearance1);
		TransformGroup1.addChild(shape3D);
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(-0.5f,0f,0f));
		transform3D.setScale(0.1);
		TransformGroup TransformGroup_Rotation=new TransformGroup();
		TransformGroup TransformGroup2=new TransformGroup();
		for(int i=0;i<8;i++)
		{
			Transform3D Transform3D_Rotation=new Transform3D();
			Transform3D_Rotation.setRotation(new AxisAngle4d(0.5,0.5,0.5,Math.PI/4*i));
			TransformGroup_Rotation=new TransformGroup(Transform3D_Rotation);
			TransformGroup2=new TransformGroup(transform3D);
			TransformGroup2.addChild(new ColorCube());
			TransformGroup_Rotation.addChild(TransformGroup2);
			TransformGroup1.addChild(TransformGroup_Rotation);
		}
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}  