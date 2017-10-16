import java.applet.*;
import java.awt.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndHill3D extends Applet
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
		Point3f p0=new Point3f(-1f,0f,1f);
		Point3f p1=new Point3f(1f,0f,1f);
		Point3f p2=new Point3f(0f,0f,-1f);
		float size=0.5f;
		int maxDepth=5;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new Hill3D(p0,p1,p2,size,maxDepth,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndHill3D(),400,400);
	}
}
class Node
{
	public Object object;
	public Node next;
	public Node(Object object)
	{
		this.object=object;
		this.next=null;
	}
}
class LinkedList
{
	Node FirstNode;
	Node LastNode;
	public int length;
	public LinkedList()
	{
		FirstNode=LastNode=null;
		length=0;
	}
	public void add(Object object)
	{
		Node node=new Node(object);
		if(length==0)FirstNode=LastNode=node;
		else
		{
			LastNode.next=node;
			LastNode=node;
		}
		length++;
	}
	public void addIntegers(int[] integers)
	{
		for(int i=0;i<integers.length;i++)this.add(integers[i]);
	}
	public Object[] getAllObjects()
	{
		Object[] allObjects=new Object[length];
		int i=0;
		for(Node n=FirstNode;i<length;n=n.next,i++)allObjects[i]=n.object;
		return allObjects;
	}
	public int[] getAllInt()
	{
		int[] allInt=new int[length];
		int i=0;
		for(Node n=FirstNode;i<length;n=n.next,i++)allInt[i]=(Integer)n.object;
		return allInt;
	}
	public Point3f[] getAllPoint3f()
	{
		Point3f[] allPoint3f=new Point3f[length];
		int i=0;
		for(Node n=FirstNode;i<length;n=n.next,i++)allPoint3f[i]=(Point3f)n.object;
		return allPoint3f;
	}
}
class Hill3D extends Shape3D
{
	int MaxDepth;
	LinkedList CoordinatesLinkedList;
	LinkedList CoordinateIndicesLinkedList;
	private Point3f getRandomDisplacementMiddlePoint3f(Point3f p0,Point3f p1,float size)
	{
		return new Point3f((p0.x+p1.x)/2,(p0.y+p1.y)/2+size,(p0.z+p1.z)/2);
	}
	private void getHill3DCoordinatesAndCoordinateIndices(Point3f p0,Point3f p1,Point3f p2,float size01,float size12,float size20,int depth)
	{
		Point3f p01=getRandomDisplacementMiddlePoint3f(p0,p1,size01);
		Point3f p12=getRandomDisplacementMiddlePoint3f(p1,p2,size12);
		Point3f p20=getRandomDisplacementMiddlePoint3f(p2,p0,size20);
		if(depth>=MaxDepth)
		{
			int length=CoordinatesLinkedList.length;
			CoordinatesLinkedList.add(p0);//0
			CoordinatesLinkedList.add(p1);//1
			CoordinatesLinkedList.add(p2);//2
			CoordinatesLinkedList.add(p01);//3
			CoordinatesLinkedList.add(p12);//4
			CoordinatesLinkedList.add(p20);//5
			int[] coordinateIndices=new int[]
			{
				length+3,length+4,length+5,
				length+3,length+1,length+4,
				length+4,length+2,length+5,
				length+5,length+0,length+3,

				length+0,length+1,length+3,
				length+1,length+2,length+4,
				length+2,length+0,length+5

			};
			CoordinateIndicesLinkedList.addIntegers(coordinateIndices);
			return;
		}
		float size=(size01+size12+size20)/2;
		float size0112=size*(float)(2*Math.random()-1);
		float size1220=size*(float)(2*Math.random()-1);
		float size2001=size*(float)(2*Math.random()-1);
		float size011=size*(float)(2*Math.random()-1);
		float size112=size*(float)(2*Math.random()-1);
		float size122=size*(float)(2*Math.random()-1);
		float size220=size*(float)(2*Math.random()-1);
		float size200=size*(float)(2*Math.random()-1);
		float size001=size*(float)(2*Math.random()-1);
		this.getHill3DCoordinatesAndCoordinateIndices(p01,p12,p20,size0112,size1220,size2001,depth+1);
		this.getHill3DCoordinatesAndCoordinateIndices(p01,p1,p12,size011,size112,size0112,depth+1);
		this.getHill3DCoordinatesAndCoordinateIndices(p12,p2,p20,size122,size220,size1220,depth+1);
		this.getHill3DCoordinatesAndCoordinateIndices(p20,p0,p01,size200,size001,size2001,depth+1);
	}
	public Hill3D(Point3f p0,Point3f p1,Point3f p2,float size,int maxDepth,Appearance appearance)
	{
		MaxDepth=maxDepth;
		CoordinatesLinkedList=new LinkedList();
		CoordinateIndicesLinkedList=new LinkedList();
		this.getHill3DCoordinatesAndCoordinateIndices(p0,p1,p2,size,size,size,0);
		Point3f[] coordinates=CoordinatesLinkedList.getAllPoint3f();
		int[] coordinateIndices=CoordinateIndicesLinkedList.getAllInt();
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		int[] stripCounts=new int[CoordinateIndicesLinkedList.length/3];
		for(int i=0;i<CoordinateIndicesLinkedList.length/3;i++)stripCounts[i]=3;
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}