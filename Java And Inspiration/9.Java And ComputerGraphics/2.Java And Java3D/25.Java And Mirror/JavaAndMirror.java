import java.awt.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndMirror
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		AmbientLight AmbientLight1=new AmbientLight(true,new Color3f(Color.green));
		AmbientLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(AmbientLight1);
		PointLight PointLight1=new PointLight(new Color3f(Color.green),new Point3f(3f,3f,3f),new Point3f(1f,0f,0f));
		PointLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(PointLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,0.8f,0f));
		Appearance1.setMaterial(Material1);
		Mirror Mirror1=new Mirror(new Point3d(0,-0.5,0.5),new Point3d(1,-0.5,-0.5),new Point3d(0,0.5,-0.5),new Point3d(-1,0.5,0.5));
		TransformGroup1.addChild(Mirror1);
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(0.15);
		transform3D.setTranslation(new Vector3f(0.2f,0f,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		Font3D font3D=new Font3D(new Font("Î¢ÈíÑÅºÚ",Font.BOLD,1),new FontExtrusion());
		Text3D text3D=new Text3D(font3D,"Java3D");
		TransformGroup2.addChild(new Shape3D(text3D,Appearance1));
		SharedGroup SharedGroup1=new SharedGroup();
		SharedGroup1.addChild(TransformGroup2);
		TransformGroup1.addChild(new Link(SharedGroup1));
		TransformGroup TransformGroup3=new TransformGroup(Mirror1.getReflectionTransform3D());
		TransformGroup3.addChild(new Link(SharedGroup1));
		TransformGroup1.addChild(TransformGroup3);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}  
class Mirror extends Shape3D
{
	private Vector3d MirrorNormal;
	public Mirror(Point3d p0,Point3d p1,Point3d p2,Point3d p3)
	{
		this.MirrorNormal=this.getNormalVector(p0,p1,p2);
		System.out.println(QuadArray.COORDINATES);
		QuadArray QuadArray1=new QuadArray(4,1);
		QuadArray1.setCoordinate(0,p0);
		QuadArray1.setCoordinate(1,p1);
		QuadArray1.setCoordinate(2,p2);
		QuadArray1.setCoordinate(3,p3);
		Appearance Appearance1=new Appearance();
		System.out.println(TransparencyAttributes.BLENDED);
		Appearance1.setTransparencyAttributes(new TransparencyAttributes(2,0.7f));
		this.setAppearance(Appearance1);
		this.setGeometry(QuadArray1);
	}
	public Transform3D getReflectionTransform3D()
	{
		double x=MirrorNormal.x;
		double y=MirrorNormal.y;
		double z=MirrorNormal.z;
		double w=Math.acos(z/Math.sqrt(x*x+y*y+z*z));
		Transform3D Transform3D_Rotation=new Transform3D();
		Transform3D_Rotation.setRotation(new AxisAngle4d(y,-x,0,w));
		Transform3D Transform3D_Reflection=new Transform3D();
		Transform3D_Reflection.setScale(new Vector3d(1,1,-1));
		Transform3D transform3D=new Transform3D();
		transform3D.mulInverse(Transform3D_Rotation);
		transform3D.mul(Transform3D_Reflection);
		transform3D.mul(Transform3D_Rotation);
		return transform3D;
	}
	private Vector3d getNormalVector(Point3d p0,Point3d p1,Point3d p2)
	{
		double x01=p1.x-p0.x;
		double y01=p1.y-p0.y;
		double z01=p1.z-p0.z;
		double x02=p2.x-p0.x;
		double y02=p2.y-p0.y;
		double z02=p2.z-p0.z;
		double x=y01*z02-y02*z01;
		double y=z01*x02-z02*x01;
		double z=x01*y02-x02*y01;
		return new Vector3d(x,y,z);
	}
}


