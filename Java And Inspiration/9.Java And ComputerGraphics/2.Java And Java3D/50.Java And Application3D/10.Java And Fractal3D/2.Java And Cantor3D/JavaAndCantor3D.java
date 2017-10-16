import java.applet.*;
import java.awt.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndCantor3D extends Applet
{
	public void init()
	{
		GraphicsConfiguration GraphicsConfiguration1=SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D=new Canvas3D(GraphicsConfiguration1);
		this.setLayout(new BorderLayout());
		this.add(canvas3D);	
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Color3f color3f=new Color3f(0f,0f,0f);
		Background Background1=new Background(color3f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		float size=0.5f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		int maxDepth=4;
		TransformGroup1.addChild(new Cantor3D(size,Appearance1,maxDepth));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndCantor3D(),400,400);
	}
}
class Cantor3D extends TransformGroup
{
	int MaxDepth;
	Appearance Appearance1;
	public Cantor3D(float size,Appearance appearance,int maxDepth)
	{
		MaxDepth=maxDepth;
		Appearance1=appearance;
		this.createCantor3D(size,new Vector3f(0f,0f,0f),0);
	}
	private void createCantor3D(float size,Vector3f vector3f,int depth)
	{
		if(depth>=MaxDepth)
		{
			Transform3D transform3D=new Transform3D();
			transform3D.setTranslation(vector3f);
			TransformGroup TransformGroup1=new TransformGroup(transform3D);
			TransformGroup1.addChild(new Box(size,size,size,Appearance1));
			this.addChild(TransformGroup1);
			return;
		}
		createCantor3D(size/3,new Vector3f(vector3f.x-size/2,vector3f.y-size/2,vector3f.z-size/2),depth+1);
		createCantor3D(size/3,new Vector3f(vector3f.x-size/2,vector3f.y-size/2,vector3f.z+size/2),depth+1);
		createCantor3D(size/3,new Vector3f(vector3f.x-size/2,vector3f.y+size/2,vector3f.z-size/2),depth+1);
		createCantor3D(size/3,new Vector3f(vector3f.x-size/2,vector3f.y+size/2,vector3f.z+size/2),depth+1);
		createCantor3D(size/3,new Vector3f(vector3f.x+size/2,vector3f.y-size/2,vector3f.z-size/2),depth+1);
		createCantor3D(size/3,new Vector3f(vector3f.x+size/2,vector3f.y-size/2,vector3f.z+size/2),depth+1);
		createCantor3D(size/3,new Vector3f(vector3f.x+size/2,vector3f.y+size/2,vector3f.z-size/2),depth+1);
		createCantor3D(size/3,new Vector3f(vector3f.x+size/2,vector3f.y+size/2,vector3f.z+size/2),depth+1);
	}
}