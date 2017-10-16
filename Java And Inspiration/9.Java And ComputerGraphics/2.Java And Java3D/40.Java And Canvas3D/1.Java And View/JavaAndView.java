import java.io.*;
import java.awt.*;
import java.applet.Applet;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndView extends Applet
{
	public void init()
	{	
		GraphicsConfiguration GraphicsConfiguration1=SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D=new Canvas3D(GraphicsConfiguration1);
		this.setLayout(new GridLayout(2,2));
		this.add(canvas3D);
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(Color.orange);
		Vector3f vector3f=new Vector3f(-1f,0f,-1f);
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
		Material1.setDiffuseColor(new Color3f(1f,0.8f,0f));
		Appearance1.setMaterial(Material1);
		Font3D font3D=new Font3D(new Font("Î¢ÈíÑÅºÚ",50,Font.BOLD),new FontExtrusion());
		Text3D text3D=new Text3D(font3D,"Java3D");
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(0.3,0.3,1.3));
		transform3D.setTranslation(new Vector3f(-0.5f,0f,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Shape3D(text3D,Appearance1));
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);


		canvas3D=new Canvas3D(GraphicsConfiguration1);
		this.add(canvas3D);	
		SimpleUniverse1.addBranchGraph(this.getViewBranchGroup(canvas3D,new Point3d(3,0,0),new Point3d(0,0,0),new Vector3d(0,1,0)));

		canvas3D=new Canvas3D(GraphicsConfiguration1);
		this.add(canvas3D);	
		SimpleUniverse1.addBranchGraph(this.getViewBranchGroup(canvas3D,new Point3d(0,0,3),new Point3d(0,0,0),new Vector3d(0,1,0)));

		canvas3D=new Canvas3D(GraphicsConfiguration1);
		this.add(canvas3D);	
		SimpleUniverse1.addBranchGraph(this.getViewBranchGroup(canvas3D,new Point3d(0,3,0),new Point3d(0,0,0),new Vector3d(0,0,-1)));
	}

	public BranchGroup getViewBranchGroup(Canvas3D canvas3D,Point3d Point3d_eye,Point3d Point3d_center,Vector3d Vector3d_up)
	{
		Transform3D transform3D=new Transform3D();
		transform3D.lookAt(Point3d_eye,Point3d_center,Vector3d_up);
		transform3D.invert();
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		ViewPlatform ViewPlatform1=new ViewPlatform();
		View View1=new View();
		View1.setProjectionPolicy(View.PARALLEL_PROJECTION);
		View1.addCanvas3D(canvas3D);
		View1.attachViewPlatform(ViewPlatform1);
		View1.setPhysicalBody(new PhysicalBody());
		View1.setPhysicalEnvironment(new PhysicalEnvironment());
		TransformGroup1.addChild(ViewPlatform1);
		BranchGroup BranchGroup1=new BranchGroup();
		BranchGroup1.addChild(TransformGroup1);
		return BranchGroup1;
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndView(),400,400);
	}
}
