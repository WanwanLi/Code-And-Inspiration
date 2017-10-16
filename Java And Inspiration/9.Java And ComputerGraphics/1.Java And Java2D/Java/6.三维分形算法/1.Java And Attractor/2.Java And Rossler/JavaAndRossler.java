import java.applet.*;
import java.awt.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndRossler extends Applet
{
	public void init()
	{
		GraphicsConfiguration GraphicsConfiguration1=SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D=new Canvas3D(GraphicsConfiguration1);
		this.setLayout(new BorderLayout());
		this.add(canvas3D);	
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),1000);
		Color3f color3f=new Color3f(0f,0f,0f);
		Background Background1=new Background(color3f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		color3f=new Color3f(1f,0f,0f);
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
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(0.05);
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Rossler());
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndRossler(),400,400);
	}
}
class Rossler extends Shape3D
{
	private double a=0.2;
	private double b=0.2;
	private double c=5.7;
	private int n=3850;
	private double h=0.02;
	public Rossler()
	{
		LineArray LineArray1=new LineArray(2*n,LineArray.COORDINATES);
		int index=0;
		double x0=0.1,y0=0,z0=0,x1=x0,y1=y0,z1=z0;
		for(int i=0;i<n;i++)
		{
			LineArray1.setCoordinate(index++,new Point3d(x0,y0,z0));
			x1=x0+h*dx(x0,y0,z0);
			y1=y0+h*dy(x0,y0,z0);
			z1=z0+h*dz(x0,y0,z0);
			LineArray1.setCoordinate(index++,new Point3d(x1,y1,z1));
			x0=x1;
			y0=y1;
			z0=z1;
		}
		this.setGeometry(LineArray1);
	}
	private double dx(double x,double y,double z)
	{
		return -y-z;
	}
	private double dy(double x,double y,double z)
	{
		return x+a*y;
	}
	private double dz(double x,double y,double z)
	{
		return b+z*(x-c);
	}
}