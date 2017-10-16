import java.applet.*;
import java.awt.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndTree3D extends Applet
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
		Point3d root=new Point3d(0,0,0);
		int branchNumber=5,level=6;
		double size=0.2,angle=Math.PI/6;
		double[] sizeOfEachBranch=new double[branchNumber];
		double[] angleOfEachBranch=new double[branchNumber];
		for(int i=0;i<branchNumber;i++){sizeOfEachBranch[i]=0.6;angleOfEachBranch[i]=(i*2*Math.PI/branchNumber);}
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(1f);
		ColoringAttributes ColoringAttributes1=new ColoringAttributes();
		ColoringAttributes1.setColor(0f,0.8f,0f);
		Appearance Appearance1=new Appearance();
		Appearance1.setLineAttributes(LineAttributes1);
		Appearance1.setColoringAttributes(ColoringAttributes1);
		TransformGroup1.addChild(new Tree3D(root,size,angle,sizeOfEachBranch,angleOfEachBranch,level,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndTree3D(),400,400);
	}
}
class Tree3D extends Shape3D
{
	private int level=5,c=1,k=0;
	private Point3d[] coordinates;
	private int[] coordinateIndices;
	private int branchNumber;
	private double[] sizeOfEachBranch;
	private double[] angleOfEachBranch;
	private double size,angle;
	private Vector3d branchDirection;
	private Vector3d branchRotateAxis;
	public Tree3D(Point3d root,double size,double angle,double[] sizeOfEachBranch,double[] angleOfEachBranch,int level,Appearance appearance)
	{
		this.size=size;
		this.level=level;
		this.angle=angle;
		this.sizeOfEachBranch=sizeOfEachBranch;
		this.angleOfEachBranch=angleOfEachBranch;
		this.branchNumber=sizeOfEachBranch.length;
		this.branchDirection=new Vector3d(0,size,0);
		this.branchRotateAxis=new Vector3d(0,0,1);
		this.initTree3DCoordinates(root,0);
		this.getTree3DCoordinates(root,0,branchDirection,branchRotateAxis,0);
		IndexedLineArray IndexedLineArray1=new IndexedLineArray(coordinates.length,LineArray.COORDINATES,coordinateIndices.length);
		IndexedLineArray1.setCoordinates(0,coordinates);
		IndexedLineArray1.setCoordinateIndices(0,coordinateIndices);
		this.setGeometry(IndexedLineArray1);
		this.setAppearance(appearance);
	}
	private void initTree3DCoordinates(Point3d p0,int n)
	{
		this.newTree3DCoordinates(0);
		this.coordinates=new Point3d[c];
		this.coordinateIndices=new int[k];
		this.coordinates[0]=p0;c=1;k=0;
	}
	private void newTree3DCoordinates(int n)
	{
		if(n>=level)return;
		c++;k++;k++;
		for(int i=0;i<branchNumber;i++)this.newTree3DCoordinates(n+1);
	}
	private void getTree3DCoordinates(Point3d p0,int i0,Vector3d v0,Vector3d d0,int n)
	{
		if(n>=level)return;
		Point3d p1=translate(p0,v0);
		int i1=c++;
		this.coordinates[i1]=p1;
		this.coordinateIndices[k++]=i0;
		this.coordinateIndices[k++]=i1;
		for(int i=0;i<branchNumber;i++)
		{
			Vector3d d1=rotate(d0,v0,angleOfEachBranch[i]);
			Vector3d v1=rotate(v0,d1,angle);
			this.scale(v1,sizeOfEachBranch[i]);
			this.getTree3DCoordinates(p1,i1,v1,d1,n+1);
		}
	}
	private Point3d translate(Point3d p,Vector3d v)
	{
		return new Point3d(p.x+v.x,p.y+v.y,p.z+v.z);
	}
	private double angleToY(double x,double y)
	{
		double r=Math.sqrt(x*x+y*y);
		return r==0?0:y>=0?Math.asin(x/r):Math.PI-Math.asin(x/r);
	}
	private void rotX(Vector3d v,double a)
	{
		double y=v.y;
		double z=v.z;
		v.y=y*Math.cos(a)-z*Math.sin(a);
		v.z=y*Math.sin(a)+z*Math.cos(a);
	}
	private void rotY(Vector3d v,double a)
	{
		double z=v.z;
		double x=v.x;
		v.z=z*Math.cos(a)-x*Math.sin(a);
		v.x=z*Math.sin(a)+x*Math.cos(a);
	}
	private void rotZ(Vector3d v,double a)
	{
		double x=v.x;
		double y=v.y;
		v.x=x*Math.cos(a)-y*Math.sin(a);
		v.y=x*Math.sin(a)+y*Math.cos(a);
	}
	private Vector3d rotate(Vector3d vector,Vector3d axis,double angle)
	{
		Vector3d v=new Vector3d(vector.x,vector.y,vector.z);
		Vector3d a=new Vector3d(axis.x,axis.y,axis.z);
		double rotZ=angleToY(a.x,a.y);
		this.rotZ(a,rotZ);
		double rotX=-angleToY(a.z,a.y);
		this.rotZ(v,rotZ);
		this.rotX(v,rotX);
		this.rotY(v,angle);
		this.rotX(v,-rotX);
		this.rotZ(v,-rotZ);
		return v;
	}
	private void scale(Vector3d v,double a)
	{
		v.x*=a;
		v.y*=a;
		v.z*=a;
	}
}
