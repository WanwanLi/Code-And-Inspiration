import java.awt.*;
import java.applet.Applet;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndOrbitBehavior extends Applet
{
	public void init()
	{
		Canvas3D canvas3D=new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		this.setLayout(new BorderLayout());
		this.add(canvas3D);	
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		OrbitBehavior OrbitBehavior1=new OrbitBehavior(canvas3D);
		OrbitBehavior1.setSchedulingBounds(BoundingSphere1);
		SimpleUniverse1.getViewingPlatform().setViewPlatformBehavior(OrbitBehavior1);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(1f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		BranchGroup1.addChild(TransformGroup1);
		System.out.println(Sphere.GENERATE_NORMALS|Sphere.GENERATE_TEXTURE_COORDS);
		Sphere Sphere1=new Sphere(0.3f,3,100);
		Appearance Appearance1=Sphere1.getAppearance();
		Appearance1.setMaterial(new Material());
		Appearance1.setTexture(new TextureLoader("earth.jpg",null).getTexture());
		TextureAttributes TextureAttributes1=new TextureAttributes();
		TextureAttributes1.setTextureMode(TextureAttributes.COMBINE);
		Appearance1.setTextureAttributes(TextureAttributes1);
		TransformGroup1.addChild(Sphere1);

		System.out.println(Sphere.GENERATE_NORMALS_INWARD|Sphere.GENERATE_TEXTURE_COORDS);
		Sphere Sphere2=new Sphere(3f,6,100);
		Appearance Appearance2=Sphere2.getAppearance();
		Appearance2.setTexture(new TextureLoader("stars.jpg",null).getTexture());
		TransformGroup1.addChild(Sphere2);

		BranchGroup1.compile();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndOrbitBehavior(),400,400);
	}
}