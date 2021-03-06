import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndTransform3D
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
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
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
		TransformGroup TransformGroup2=new TransformGroup();
		TransformGroup2.addChild(new Sphere(0.2f,Appearance2));
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,-0.45f,0f));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		Appearance Appearance3=new Appearance();
		Material Material3=new Material();
		Material3.setDiffuseColor(new Color3f(1f,0f,0f));
		Appearance3.setMaterial(Material3);
		TransformGroup3.addChild(new Box(0.5f,0.05f,0.5f,Appearance3));
		transform3D.setTranslation(new Vector3f(0f,0.4f,0f));
		TransformGroup TransformGroup4=new TransformGroup(transform3D);
		Appearance Appearance4=new Appearance();
		Material Material4=new Material();
		Material4.setDiffuseColor(new Color3f(0f,0f,1f));
		Appearance4.setMaterial(Material4);
		TransformGroup4.addChild(new Box(0.4f,0.05f,0.4f,Appearance4));
		transform3D.setTranslation(new Vector3f(0.3f,0f,0.3f));
		TransformGroup TransformGroup5=new TransformGroup(transform3D);
		Appearance Appearance5=new Appearance();
		Material Material5=new Material();
		Material5.setDiffuseColor(new Color3f(1f,1f,1f));
		Appearance5.setMaterial(Material5);
		TransformGroup5.addChild(new Cylinder(0.05f,0.8f,Appearance5));
		transform3D.setTranslation(new Vector3f(-0.3f,0f,0.3f));
		TransformGroup TransformGroup6=new TransformGroup(transform3D);
		Appearance Appearance6=new Appearance();
		Material Material6=new Material();
		Material6.setDiffuseColor(new Color3f(0f,1f,1f));
		Appearance6.setMaterial(Material6);
		TransformGroup6.addChild(new Cylinder(0.05f,0.8f,Appearance6));
		transform3D.setTranslation(new Vector3f(-0.3f,0f,-0.3f));
		TransformGroup TransformGroup7=new TransformGroup(transform3D);
		Appearance Appearance7=new Appearance();
		Material Material7=new Material();
		Material7.setDiffuseColor(new Color3f(1f,1f,0f));
		Appearance7.setMaterial(Material7);
		TransformGroup7.addChild(new Cylinder(0.05f,0.8f,Appearance7));
		transform3D.setTranslation(new Vector3f(0.3f,0f,-0.3f));
		TransformGroup TransformGroup8=new TransformGroup(transform3D);
		Appearance Appearance8=new Appearance();
		Material Material8=new Material();
		Material8.setDiffuseColor(new Color3f(1f,0f,1f));
		Appearance8.setMaterial(Material8);
		TransformGroup8.addChild(new Cylinder(0.05f,0.8f,Appearance8));
		transform3D.setTranslation(new Vector3f(0.0f,0.85f,0.0f));
		TransformGroup TransformGroup9=new TransformGroup(transform3D);
		Appearance Appearance9=new Appearance();
		Material Material9=new Material();
		Material9.setDiffuseColor(new Color3f(0.1f,1.0f,0.5f));
		Appearance9.setMaterial(Material9);
		TransformGroup9.addChild(new Cone(0.3f,0.8f,Appearance9));
		transform3D.setTranslation(new Vector3f(0.3f,0.6f,0.3f));
		TransformGroup TransformGroup10=new TransformGroup(transform3D);
		Appearance Appearance10=new Appearance();
		Material Material10=new Material();
		Material10.setDiffuseColor(new Color3f(0.8f,0.8f,0.8f));
		Appearance10.setMaterial(Material10);
		TransformGroup10.addChild(new Cone(0.05f,0.3f,Appearance10));
		transform3D.setTranslation(new Vector3f(-0.3f,0.6f,0.3f));
		TransformGroup TransformGroup11=new TransformGroup(transform3D);
		Appearance Appearance11=new Appearance();
		Material Material11=new Material();
		Material11.setDiffuseColor(new Color3f(0.0f,0.5f,0.5f));
		Appearance11.setMaterial(Material11);
		TransformGroup11.addChild(new Cone(0.05f,0.3f,Appearance11));
		transform3D.setTranslation(new Vector3f(-0.3f,0.6f,-0.3f));
		TransformGroup TransformGroup12=new TransformGroup(transform3D);
		Appearance Appearance12=new Appearance();
		Material Material12=new Material();
		Material12.setDiffuseColor(new Color3f(0.5f,0.5f,0.0f));
		Appearance12.setMaterial(Material12);
		TransformGroup12.addChild(new Cone(0.05f,0.3f,Appearance12));
		transform3D.setTranslation(new Vector3f(0.3f,0.6f,-0.3f));
		TransformGroup TransformGroup13=new TransformGroup(transform3D);
		Appearance Appearance13=new Appearance();
		Material Material13=new Material();
		Material13.setDiffuseColor(new Color3f(0.5f,0.0f,0.5f));
		Appearance13.setMaterial(Material13);
		TransformGroup13.addChild(new Cone(0.05f,0.3f,Appearance13));
		transform3D.rotY(Math.PI/6);
		TransformGroup TransformGroup14=new TransformGroup(transform3D);
		TransformGroup14.addChild(new ColorCube(0.1));
		transform3D.setTranslation(new Vector3f(0f,-0.2f,0f));
		TransformGroup TransformGroup15=new TransformGroup(transform3D);
		TransformGroup15.addChild(TransformGroup14);
		TransformGroup1.addChild(TransformGroup2);
		TransformGroup1.addChild(TransformGroup3);
		TransformGroup1.addChild(TransformGroup4);
		TransformGroup1.addChild(TransformGroup5);
		TransformGroup1.addChild(TransformGroup6);
		TransformGroup1.addChild(TransformGroup7);
		TransformGroup1.addChild(TransformGroup8);
		TransformGroup1.addChild(TransformGroup9);
		TransformGroup1.addChild(TransformGroup10);
		TransformGroup1.addChild(TransformGroup11);
		TransformGroup1.addChild(TransformGroup12);
		TransformGroup1.addChild(TransformGroup13);
		TransformGroup1.addChild(TransformGroup15);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
 