import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndTorus
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
		Material1.setDiffuseColor(new Color3f(1f,0f,0f));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new Torus(0.3f,0.1f,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}  
class Torus extends Shape3D
{
	public Torus(float R,float r,Appearance appearance)
	{
		int m=20,n=40;
		double w=2*Math.PI/m;
		double cosw=Math.cos(w);
		double sinw=Math.sin(w);
		double dx=R*(1-cosw);
		double dy=-R*sinw;
		double[] transformMatrix=new double[]
		{
			cosw,-sinw,0,dx,
			sinw, cosw,0,dy,
			     0,       0,1,  0,
			     0,       0,0,  1
		};
		Transform3D circleTransform3D=new Transform3D(transformMatrix);
		Point3d[] circlePoint3d=new Point3d[m];
		circlePoint3d[0]=new Point3d(R+r,0,0);
		for(int i=1;i<m;i++)
		{
			circlePoint3d[i]=new Point3d();
			circleTransform3D.transform(circlePoint3d[i-1],circlePoint3d[i]);
		}
		Transform3D rotYTransform3D=new Transform3D();
		rotYTransform3D.rotY(2*Math.PI/n);
		System.out.println(IndexedQuadArray.COORDINATES);
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(n*m,1,4*m*n);
		int index=0;
		for(int i=0;i<n;i++)
		{
			IndexedQuadArray1.setCoordinates(i*m,circlePoint3d);
			for(int j=0;j<m;j++)
			{
				rotYTransform3D.transform(circlePoint3d[j]);
				int[] coordinateIndices=new int[]
				{
					((i+0)%n)*m+((j+0)%m),
					((i+1)%n)*m+((j+0)%m),
					((i+1)%n)*m+((j+1)%m),
					((i+0)%n)*m+((j+1)%m)
				};
				IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
				index+=4;
			}
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
}