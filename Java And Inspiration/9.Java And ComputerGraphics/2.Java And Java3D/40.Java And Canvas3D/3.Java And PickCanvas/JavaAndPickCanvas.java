import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.applet.Applet;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.picking.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndPickCanvas extends Applet implements MouseListener
{
	PickCanvas PickCanvas1;
	Appearance Appearance1;
	public void init()
	{
		Canvas3D canvas3D=new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		canvas3D.addMouseListener(this);
		this.setLayout(new BorderLayout());
		this.add(canvas3D);	
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
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
		Appearance Appearance0=new Appearance();
		Material Material0=new Material();
		Material0.setDiffuseColor(new Color3f(1f,0f,1f));
		Appearance0.setMaterial(Material0);
		Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,1f,0f));
		Appearance1.setMaterial(Material1);
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(-0.1f,0.0f,-1.5f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		Box Box2=new Box(0.3f,0.5f,0.2f,Primitive.ENABLE_GEOMETRY_PICKING|Primitive.ENABLE_APPEARANCE_MODIFY|Primitive.GENERATE_NORMALS,Appearance0);
		TransformGroup2.addChild(Box2);
		TransformGroup1.addChild(TransformGroup2);
		transform3D.setTranslation(new Vector3f(0.2f,0.0f,0.1f));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		Sphere Sphere3=new Sphere(0.3f,Primitive.ENABLE_GEOMETRY_PICKING|Primitive.ENABLE_APPEARANCE_MODIFY|Primitive.GENERATE_NORMALS,Appearance0);
		TransformGroup3.addChild(Sphere3);
		TransformGroup1.addChild(TransformGroup3);
		transform3D.setTranslation(new Vector3f(-0.3f,0.0f,-0.3f));
		TransformGroup TransformGroup4=new TransformGroup(transform3D);
		Cylinder Cylinder4=new Cylinder(0.4f,0.4f,Primitive.ENABLE_GEOMETRY_PICKING|Primitive.ENABLE_APPEARANCE_MODIFY|Primitive.GENERATE_NORMALS,Appearance0);
		TransformGroup4.addChild(Cylinder4);
		TransformGroup1.addChild(TransformGroup4);
		BranchGroup1.compile();
		PickCanvas1=new PickCanvas(canvas3D,BranchGroup1);
		PickCanvas1.setMode(PickTool.GEOMETRY);
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndPickCanvas(),400,400);
	}
	public void mouseClicked(MouseEvent e)
	{
		PickCanvas1.setShapeLocation(e);
		PickResult[] pickResults=PickCanvas1.pickAll();
		for(int i=0;pickResults!=null&&i<pickResults.length;i++)
		{
			Node node=pickResults[i].getObject();
			if(node instanceof Shape3D)((Shape3D)node).setAppearance(Appearance1);
		}
	}
	public void mousePressed(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
}