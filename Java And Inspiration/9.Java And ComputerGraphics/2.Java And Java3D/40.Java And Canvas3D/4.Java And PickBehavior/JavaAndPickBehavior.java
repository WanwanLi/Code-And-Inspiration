import java.awt.*;
import java.applet.Applet;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.picking.*;
import com.sun.j3d.utils.picking.behaviors.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndPickBehavior extends Applet
{
	PickCanvas PickCanvas1;
	Appearance Appearance1;
	public void init()
	{
		Canvas3D canvas3D=new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		this.setLayout(new BorderLayout());
		this.add(canvas3D);	
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		PickRotateBehavior PickRotateBehavior1=new PickRotateBehavior(BranchGroup1,canvas3D,BoundingSphere1,PickTool.GEOMETRY);
		BranchGroup1.addChild(PickRotateBehavior1);
		PickTranslateBehavior PickTranslateBehavior1=new PickTranslateBehavior(BranchGroup1,canvas3D,BoundingSphere1,PickTool.GEOMETRY);
		BranchGroup1.addChild(PickTranslateBehavior1);
		PickZoomBehavior PickZoomBehavior1=new PickZoomBehavior(BranchGroup1,canvas3D,BoundingSphere1,PickTool.GEOMETRY);
		BranchGroup1.addChild(PickZoomBehavior1);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(-1f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		BranchGroup1.addChild(TransformGroup1);
		for(int i=0;i<10;i++)
		{
			Transform3D transform3D=new Transform3D();
			transform3D.setTranslation(new Vector3d(Math.random()-0.5,Math.random()-0.5,Math.random()-0.5));
			TransformGroup TransformGroup2=new TransformGroup(transform3D);
			TransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			TransformGroup2.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
			ColorCube ColorCube1=new ColorCube(0.08f);
			PickTool.setCapabilities(ColorCube1,PickTool.INTERSECT_FULL);
			TransformGroup2.addChild(ColorCube1);
			TransformGroup1.addChild(TransformGroup2);
		}
		BranchGroup1.compile();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndPickBehavior(),400,400);
	}
}