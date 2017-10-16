import java.awt.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndExponentialFog
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(new Color3f(Color.black));
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		float front=2f,back=6f;
		color3f=new Color3f(1f,1f,1f);
		float dencity=0.05f;
		ExponentialFog ExponentialFog1=new ExponentialFog(color3f,dencity);
		ExponentialFog1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(ExponentialFog1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		Appearance Appearance2=new Appearance();
		Material Material2=new Material();
		Material2.setDiffuseColor(new Color3f(1f,1f,0f));
		Appearance2.setMaterial(Material2);
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<5;j++)
			{
				Transform3D transform3D=new Transform3D();
				transform3D.setTranslation(new Vector3f(j*0.5f-1f,-0.5f+0.2f*i,-i*2.0f));
				TransformGroup TransformGroup2=new TransformGroup(transform3D);
				TransformGroup2.addChild(new Cylinder(0.1f,0.4f,Appearance2));
				TransformGroup1.addChild(TransformGroup2);
			}
		}
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
 