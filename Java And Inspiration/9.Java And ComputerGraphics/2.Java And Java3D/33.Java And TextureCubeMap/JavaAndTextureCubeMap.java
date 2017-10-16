import java.awt.*;
import java.awt.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.image.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndTextureCubeMap
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
		TransformGroup1.addChild(new Dodecahedron(0.25f));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Dodecahedron extends Shape3D
{
	public Dodecahedron(float r)
	{
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		float f=0.5f*(float)(Math.sqrt(5)+1);
		Point3f[] coordinates=new Point3f[]
		{
			new Point3f(r,r,r),
			new Point3f(0f,r/f,r*f),
			new Point3f(r*f,0f,r/f),
			new Point3f(r/f,r*f,0f),

			new Point3f(-r,r,r),
			new Point3f(0f,-r/f,r*f),
			new Point3f(r,-r,r),
			new Point3f(r*f,0f,-r/f),

			new Point3f(r,r,-r),
			new Point3f(-r/f,r*f,0f),
			new Point3f(-r*f,0f,r/f),
			new Point3f(-r,-r,r),

			new Point3f(r/f,-r*f,0f),
			new Point3f(r,-r,-r),
			new Point3f(0f,r/f,-r*f),
			new Point3f(-r,r,-r),

			new Point3f(-r/f,-r*f,0f),
			new Point3f(-r*f,0f,-r/f),
			new Point3f(0f,-r/f,-r*f),
			new Point3f(-r,-r,-r)
		};
		int[] coordinateIndices=new int[]
		{
			0,1,5,6,2,
			0,2,7,8,3,
			0,3,9,4,1,
			1,4,10,11,5,

			2,6,12,13,7,
			3,8,14,15,9,
			5,11,16,12,6,
			7,13,18,14,8,

			9,15,17,10,4,
			19,16,11,10,17,
			19,17,15,14,18,
			19,18,13,12,16
		};
		int[] stripCounts=new int[12];
		for(int i=0;i<12;i++)stripCounts[i]=5;
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		(new NormalGenerator()).generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(this.getTextureCubeMapAppearance(new String[]{"0.jpg","1.jpg","2.jpg","3.jpg","4.jpg","5.jpg"}));
	}
	private Appearance getTextureCubeMapAppearance(String[] picturesNames)
	{
		ImageComponent2D[] ImageComponent2Ds=new ImageComponent2D[6];
		for(int i=0;i<6;i++)ImageComponent2Ds[i]=(new TextureLoader(picturesNames[i%picturesNames.length],null)).getImage();
		TextureCubeMap TextureCubeMap1=new TextureCubeMap(Texture.BASE_LEVEL,Texture.RGBA,2<<9);
		TextureCubeMap1.setImage(0,TextureCubeMap.NEGATIVE_X,ImageComponent2Ds[0]);
		TextureCubeMap1.setImage(0,TextureCubeMap.NEGATIVE_Y,ImageComponent2Ds[1]);
		TextureCubeMap1.setImage(0,TextureCubeMap.NEGATIVE_Z,ImageComponent2Ds[2]);
		TextureCubeMap1.setImage(0,TextureCubeMap.POSITIVE_X,ImageComponent2Ds[3]);
		TextureCubeMap1.setImage(0,TextureCubeMap.POSITIVE_Y,ImageComponent2Ds[4]);
		TextureCubeMap1.setImage(0,TextureCubeMap.POSITIVE_Z,ImageComponent2Ds[5]);
		TextureCubeMap1.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		TextureCubeMap1.setMinFilter(Texture.BASE_LEVEL_LINEAR);
		TextureCubeMap1.setEnable(true);float scale=2f;
		TexCoordGeneration TexCoordGeneration1=new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,TexCoordGeneration.TEXTURE_COORDINATE_3);
		TexCoordGeneration1.setPlaneR(new Vector4f(scale,0,0,0));
		TexCoordGeneration1.setPlaneS(new Vector4f(0,scale,0,0));
		TexCoordGeneration1.setPlaneT(new Vector4f(0,0,scale,0));
		Appearance textureCubeMapAppearance=new Appearance();
		textureCubeMapAppearance.setTexture(TextureCubeMap1);
		textureCubeMapAppearance.setTexCoordGeneration(TexCoordGeneration1);
		textureCubeMapAppearance.setCapability(Appearance.ALLOW_TEXGEN_WRITE);
		return textureCubeMapAppearance;
	}
}





