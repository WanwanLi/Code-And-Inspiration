import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndDice
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
		TransformGroup1.addChild(new Dice(0.3f));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Dice extends Shape3D
{
	public Dice(float rf)
	{
		System.out.println(QuadArray.COORDINATES|QuadArray.TEXTURE_COORDINATE_2);
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(12,33,24);
		Point3f[] coordinates=new Point3f[]
		{
			new Point3f(0f,0f,0f),
			new Point3f(rf,0f,0f),
			new Point3f(rf,rf,0f),
			new Point3f(0f,rf,0f),
			new Point3f(0f,0f,rf),
			new Point3f(rf,0f,rf),
			new Point3f(rf,rf,rf),
			new Point3f(0f,rf,rf)
		};
		int[] coordinateIndices=new int[]
		{
			0,3,2,1,
			0,1,5,4,
			1,2,6,5,
			2,3,7,6,
			3,0,4,7,
			4,5,6,7
		};
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		TexCoord2f[] textureCoordinates=new TexCoord2f[]
		{
			new TexCoord2f(0f,1f),
			new TexCoord2f(1f/3,1f),
			new TexCoord2f(2f/3,1f),
			new TexCoord2f(1f,1f),

			new TexCoord2f(0f,1f/2),
			new TexCoord2f(1f/3,1f/2),
			new TexCoord2f(2f/3,1f/2),
			new TexCoord2f(1f,1f/2),

			new TexCoord2f(0f,0f),
			new TexCoord2f(1f/3,0f),
			new TexCoord2f(2f/3,0f),
			new TexCoord2f(1f,0f)
		};
		int[] textureCoordinateIndices=new int[]
		{
			0,1,5,4,
			1,2,6,5,
			2,3,7,6,
			5,6,10,9,
			6,7,11,10,
			4,5,9,8					
		};
		IndexedQuadArray1.setTextureCoordinates(0,0,textureCoordinates);
		IndexedQuadArray1.setTextureCoordinateIndices(0,0,textureCoordinateIndices);
		TextureLoader TextureLoader1=new TextureLoader("Dice.jpg",null);
		Appearance Appearance1=new Appearance();
		Appearance1.setTexture(TextureLoader1.getTexture());
		this.setAppearance(Appearance1);
		this.setGeometry(IndexedQuadArray1);
	}
}