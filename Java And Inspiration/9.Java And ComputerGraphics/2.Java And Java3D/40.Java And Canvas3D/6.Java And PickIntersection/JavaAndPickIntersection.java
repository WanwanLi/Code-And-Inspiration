import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.picking.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndPickIntersection extends Applet implements MouseListener
{
	PickCanvas PickCanvas1;
	PointArray PointArray1;
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
		DirectionalLight DirectionalLight1=new DirectionalLight(new Color3f(1f,1f,1f),new Vector3f(-1f,0f,-1f));
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		BranchGroup1.addChild(TransformGroup1);
		int n=20;
		this.PointArray1=new PointArray(n,PointArray.COORDINATES|PointArray.COLOR_4);
		this.PointArray1.setCapability(PointArray.ALLOW_COORDINATE_READ);
		this.PointArray1.setCapability(PointArray.ALLOW_FORMAT_READ);
		this.PointArray1.setCapability(PointArray.ALLOW_COLOR_READ);
		this.PointArray1.setCapability(PointArray.ALLOW_COLOR_WRITE);
		this.PointArray1.setCapability(PointArray.ALLOW_COUNT_READ);
		Point3f[] coordinates=new Point3f[n];
		Color4f[] colors=new Color4f[n];
		for(int i=0;i<n;i++)
		{
			coordinates[i]=new Point3f((float)(Math.random()-0.5),(float)(Math.random()-0.5),(float)(Math.random()-0.5));
			colors[i]=new Color4f((float)Math.random(),(float)Math.random(),(float)Math.random(),1f);
		}
		this.PointArray1.setCoordinates(0,coordinates);
		this.PointArray1.setColors(0,colors);
		Appearance Appearance1=new Appearance();
		PointAttributes PointAttributes1=new PointAttributes();
		PointAttributes1.setPointSize(30f);
		PointAttributes1.setPointAntialiasingEnable(true);
		Appearance1.setPointAttributes(PointAttributes1);
		TransformGroup1.addChild(new Shape3D(this.PointArray1,Appearance1));
		this.PickCanvas1=new PickCanvas(canvas3D,BranchGroup1);
		this.PickCanvas1.setTolerance(10);
		BranchGroup1.compile();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndPickIntersection(),400,400);
	}
	public void mouseClicked(MouseEvent e)
	{
		this.PickCanvas1.setShapeLocation(e);
		PickResult[] PickResults=this.PickCanvas1.pickAll();
		for(int i=0;PickResults!=null&&i<PickResults.length;i++)
		{
			PickIntersection PickIntersection1=PickResults[i].getIntersection(0);
			Point3d coordinate=PickIntersection1.getClosestVertexCoordinates();
			int[] indices=PickIntersection1.getPrimitiveCoordinateIndices();
			System.out.println("indices["+indices[0]+"].coordinate=("+coordinate.x+","+coordinate.y+","+coordinate.z+")");		
			Color4f color4f=new Color4f();
			this.PointArray1.getColor(indices[0],color4f);
			color4f.x=1f-color4f.x;
			color4f.y=1f-color4f.y;
			color4f.z=1f-color4f.z;
			if(color4f.w==1f)color4f.w=0.5f;
			else color4f.w=1f;
			this.PointArray1.setColor(indices[0],color4f);
		}	
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
}