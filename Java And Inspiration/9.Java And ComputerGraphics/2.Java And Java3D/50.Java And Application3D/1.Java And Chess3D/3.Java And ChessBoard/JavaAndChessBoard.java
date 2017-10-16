import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
public class JavaAndChessBoard
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
		TransformGroup1.addChild(new ChessBoard());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
}  
class ChessBoard extends TransformGroup
{
	public ChessBoard()
	{
		float unit=0.12f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.white));
		Appearance1.setMaterial(Material1);
		Appearance Appearance2=new Appearance();
		Material Material2=new Material();
		Material2.setDiffuseColor(new Color3f(Color.orange));
		Appearance2.setMaterial(Material2);
		int m=8;
		int n=8;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				Transform3D transform3D=new Transform3D();
				transform3D.setTranslation(new Vector3f(j*unit,0f,(i-4)*unit));
				TransformGroup TransformGroup1=new TransformGroup(transform3D);
				TransformGroup1.addChild(new Block(unit/2,unit/4,unit/2,((i+j)%2==0?Appearance1:Appearance2)));
				this.addChild(TransformGroup1);
			}
		}
	}
}
class Block extends Shape3D
{
	public Block(float xdim,float ydim,float zdim,Appearance appearance)
	{
		int m=2;
		int n=4;
		int index=0;
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(m*n,1,4*m*n+8);
		Point3f[] coordinates=new Point3f[]
		{
			new Point3f(-xdim,ydim,zdim),
			new Point3f(-xdim,-ydim,zdim),
			new Point3f(-xdim,-ydim,-zdim),
			new Point3f(-xdim,ydim,-zdim),
			new Point3f(xdim,ydim,zdim),
			new Point3f(xdim,-ydim,zdim),
			new Point3f(xdim,-ydim,-zdim),
			new Point3f(xdim,ydim,-zdim)
		};
		IndexedQuadArray1.setCoordinates(0,coordinates);
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				int[] coordinateIndices=new int[]
				{
					((i+0)%m)*n+((j+0)%n),
					((i+1)%m)*n+((j+0)%n),
					((i+1)%m)*n+((j+1)%n),
					((i+0)%m)*n+((j+1)%n)
				};
				IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
				index+=4;
			}
		}
		int[] coordinateIndices=new int[]
		{
			3,2,1,0,
			4,5,6,7
		};
		IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}