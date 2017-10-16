import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.image.*;
public class JavaAndTexture3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background();
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
		Material1.setDiffuseColor(new Color3f(1f,0,0));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new Tetrahedron(0.5f));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Tetrahedron extends Shape3D
{
	public Tetrahedron(float r)
	{
		IndexedTriangleArray IndexedTriangleArray1=new IndexedTriangleArray(4,IndexedTriangleArray.COORDINATES|IndexedTriangleArray.TEXTURE_COORDINATE_3|IndexedTriangleArray.NORMALS,12);
		IndexedTriangleArray1.setCoordinate(0,new Point3f(0,0,0));
		IndexedTriangleArray1.setCoordinate(1,new Point3f(r,0,0));
		IndexedTriangleArray1.setCoordinate(2,new Point3f(0,r,0));
		IndexedTriangleArray1.setCoordinate(3,new Point3f(0,0,r));
		IndexedTriangleArray1.setTextureCoordinate(0,0,new TexCoord3f(0f,0f,0f));
		IndexedTriangleArray1.setTextureCoordinate(0,1,new TexCoord3f(1f,0f,0f));
		IndexedTriangleArray1.setTextureCoordinate(0,2,new TexCoord3f(0f,1f,0f));
		IndexedTriangleArray1.setTextureCoordinate(0,3,new TexCoord3f(0f,0f,1f));
		IndexedTriangleArray1.setNormal(0,new Vector3f(1/1.732f,1/1.732f,1/1.732f));
		IndexedTriangleArray1.setNormal(1,new Vector3f(-1,0,0));
		IndexedTriangleArray1.setNormal(2,new Vector3f(0,-1,0));
		IndexedTriangleArray1.setNormal(3,new Vector3f(0,0,-1));
		int[] coordinateIndices=new int[]
		{
			1,2,3,
			0,3,2,
			0,1,3,
			0,2,1
		};
		int[] normalIndices=new int[]
		{
			0,0,0,
			1,1,1,
			2,2,2,
			3,3,3
		};
		IndexedTriangleArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedTriangleArray1.setTextureCoordinateIndices(0,0,coordinateIndices);
		IndexedTriangleArray1.setNormalIndices(0,normalIndices);
		this.setGeometry(IndexedTriangleArray1);
		this.setAppearance(this.getTexture3DAppearance());
	}
	private Appearance getTexture3DAppearance()
	{
		int row=256,column=256,level=256;
		BufferedImage[] BufferedImages=new BufferedImage[level];
		for(int z=0;z<level;z++)
		{
			BufferedImages[z]=new BufferedImage(column,row,BufferedImage.TYPE_INT_ARGB);
			for(int y=0;y<row;y++)
			{
				for(int x=0;x<column;x++)
				{
					int red=z;
					int green=x;
					int blue=y;
					int RGB=(red<<16)|(green<<8)|blue;
					BufferedImages[z].setRGB(x,(column-1)-y,RGB);
				}
			}
		}
		ImageComponent3D imageComponent3D=new ImageComponent3D(ImageComponent3D.FORMAT_RGB,BufferedImages);
		Texture3D texture3D=new Texture3D(Texture3D.BASE_LEVEL,Texture3D.RGBA,imageComponent3D.getWidth(),imageComponent3D.getHeight(),imageComponent3D.getDepth());
		texture3D.setImage(0,imageComponent3D);
		Appearance appearance=new Appearance();
		appearance.setTexture(texture3D);
		appearance.setMaterial(new Material());
		return appearance;
	}
}