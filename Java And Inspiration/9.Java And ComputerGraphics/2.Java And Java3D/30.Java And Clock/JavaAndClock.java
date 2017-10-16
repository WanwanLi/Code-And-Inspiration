import java.awt.Color;
import java.util.Enumeration;
import java.util.Calendar;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndClock
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(Color.orange);
		Vector3f vector3f=new Vector3f(0f,1f,-1f);
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
		float r=0.5f;
		Appearance  Appearance1=new Appearance();
		Appearance1.setTexture((new TextureLoader("Clock.jpg",null)).getTexture());
		 Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(1,1,0.05));
		transform3D.setTranslation(new Vector3f(0f,0f,-0.025f));
		TransformGroup TransformGroupBack=new TransformGroup(transform3D);
		TransformGroupBack.addChild(new Sphere(r,2,100,Appearance1));
		TransformGroup1.addChild(TransformGroupBack);
		Appearance  Appearance2=new Appearance();
		Material Material2=new Material();
		Material2.setDiffuseColor(new Color3f(Color.orange));
		Appearance2.setMaterial(Material2);
		TransformGroup TransformGroupHour=new TransformGroup();
		TransformGroupHour.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TransformGroupHour.addChild(new Arrow(0.8f*r,0.1f*r,0.05f*r,Appearance2));
		TransformGroup TransformGroupMinute=new TransformGroup();
		TransformGroupMinute.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TransformGroupMinute.addChild(new Arrow(r,0.05f*r,0.05f*r,Appearance2));
		TransformGroup TransformGroupSecond=new TransformGroup();
		TransformGroupSecond.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TransformGroupSecond.addChild(new Arrow(r,0.02f*r,0.05f*r,Appearance2));
		Behavior Behavior1=new Behavior_Clock(TransformGroupHour,TransformGroupMinute,TransformGroupSecond);
		Behavior1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(Behavior1);
		TransformGroup1.addChild(TransformGroupHour);
		TransformGroup1.addChild(TransformGroupMinute);
		TransformGroup1.addChild(TransformGroupSecond);

		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 

class Behavior_Clock extends Behavior
{
	TransformGroup TransformGroupHour;
	TransformGroup TransformGroupMinute;
	TransformGroup TransformGroupSecond;
	public Behavior_Clock(TransformGroup transformGroupHour,TransformGroup transformGroupMinute,TransformGroup transformGroupSecond)
	{
		TransformGroupHour=transformGroupHour;
		TransformGroupMinute=transformGroupMinute;
		TransformGroupSecond=transformGroupSecond;
	}
	public void initialize(){this.wakeupOn(new WakeupOnElapsedTime(500));}
	public void processStimulus(Enumeration enumeration)
	{
		double hour=Calendar.getInstance().get(Calendar.HOUR);
		double minute=Calendar.getInstance().get(Calendar.MINUTE);
		double second=Calendar.getInstance().get(Calendar.SECOND);
		Transform3D transform3D=new Transform3D();
		transform3D.rotZ(-2*Math.PI*(hour+minute/60)/12+Math.PI/2);
		TransformGroupHour.setTransform(transform3D);
		transform3D.rotZ(-2*Math.PI*(minute+second/60)/60+Math.PI/2);
		TransformGroupMinute.setTransform(transform3D);
		transform3D.rotZ(-2*Math.PI*second/60+Math.PI/2);
		TransformGroupSecond.setTransform(transform3D);
		this.wakeupOn(new WakeupOnElapsedTime(500));
	}
}

class Arrow extends Shape3D
{
	public Arrow(float l,float h,float d,Appearance appearance)
	{
		Point3f[] coordinates=new Point3f[]
		{
			new Point3f(0f,0f,d),
			new Point3f(l,0f,0f),
			new Point3f(h,h,0f),
			new Point3f(h,-h,0f),
			new Point3f(0f,0f,-d)
		};

		int[] coordinateIndices=new int[]
		{
			0,1,2,
			0,3,1,
			4,1,3,
			4,2,1
		};
		System.out.println(IndexedTriangleArray.COORDINATES|IndexedTriangleArray.NORMALS);
		IndexedTriangleArray IndexedTriangleArray1=new IndexedTriangleArray(coordinates.length,3,coordinateIndices.length);
		Vector3f v1=new Vector3f();
		v1.sub(coordinates[1],coordinates[0]);
		v1.normalize();
		Vector3f v2=new Vector3f();
		v2.sub(coordinates[2],coordinates[0]);
		v2.normalize();
		Vector3f v=new Vector3f();
		v.cross(v1,v2);
		IndexedTriangleArray1.setNormal(0,v);
		v.y=-v.y;
		IndexedTriangleArray1.setNormal(1,v);
		v.z=-v.z;
		IndexedTriangleArray1.setNormal(2,v);
		v.y=-v.y;
		IndexedTriangleArray1.setNormal(3,v);
		int[] normalIndices=new int[]
		{
			0,0,0,
			1,1,1,
			2,2,2,
			3,3,3
		};
		IndexedTriangleArray1.setCoordinates(0,coordinates);
		IndexedTriangleArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedTriangleArray1.setNormalIndices(0,normalIndices);
		this.setAppearance(appearance);
		this.setGeometry(IndexedTriangleArray1);
	}
}




