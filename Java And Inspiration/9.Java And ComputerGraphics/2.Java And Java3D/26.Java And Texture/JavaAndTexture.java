import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndTexture
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		TextureLoader TextureLoader1=new TextureLoader("InternationalChess4.0.jpg",null);
		ImageComponent2D imageComponent2D=TextureLoader1.getImage();
		Background Background1=new Background(imageComponent2D);
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
		System.out.println(Primitive.GENERATE_TEXTURE_COORDS);
		System.out.println(Sphere.GENERATE_NORMALS|Sphere.GENERATE_TEXTURE_COORDS);
		Sphere Sphere1=new Sphere(0.5f,3,50);
		Appearance Appearance1=Sphere1.getAppearance();
		Appearance1.setMaterial(new Material());
		Texture Texture1=TextureLoader1.getTexture();
		Appearance1.setTexture(Texture1);
		TextureAttributes TextureAttributes1=new TextureAttributes();
		System.out.println(TextureAttributes.COMBINE);
		TextureAttributes1.setTextureMode(6);
		Appearance1.setTextureAttributes(TextureAttributes1);
		TransformGroup1.addChild(Sphere1);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
 