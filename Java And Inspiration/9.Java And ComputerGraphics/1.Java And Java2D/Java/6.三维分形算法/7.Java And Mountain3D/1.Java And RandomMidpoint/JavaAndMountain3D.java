import java.applet.*;
import java.awt.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndMountain3D extends Applet
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
		float xdim=2f;
		float zdim=1.5f;
		float ydim=1.2f;
		double Hurst=0.8;
		int maxDepth=8;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.green));
		Material1.setSpecularColor(new Color3f(Color.black));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new Mountain3D(xdim,ydim,zdim,Hurst,maxDepth,Appearance1));
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,-ydim/10,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		Appearance1=new Appearance();
		Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.blue));
		Appearance1.setMaterial(Material1);
		TransformGroup2.addChild(new Box(xdim/1.5f,ydim/8,zdim/1.5f,Appearance1));
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndMountain3D(),400,400);
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
class Mountain3D extends Shape3D
{
	int MaxDepth;
	LinkedList CoordinatesLinkedList;
	LinkedList CoordinateIndicesLinkedList;
	float decay=0.5f;
	float Abs(float f){return (f<0?-f:f);}
	private Point3f getRandomDisplacementMidPoint3f(Point3f p0,Point3f p1,Point3f p2,Point3f p3,float size)
	{
		return new Point3f((p0.x+p1.x+p2.x+p3.x)/4,Abs((p0.y+p1.y+p2.y+p3.y)/4+size*(float)(Math.random()-0.5)),(p0.z+p1.z+p2.z+p3.z)/4);
	}
	private Point3f getMiddlePoint3f(Point3f p0,Point3f p1)
	{
		return new Point3f((p0.x+p1.x)/2,(p0.y+p1.y)/2,(p0.z+p1.z)/2);
	}
	private void getMountain3DCoordinatesAndCoordinateIndices(Point3f p0,Point3f p1,Point3f p2,Point3f p3,float size,int depth)
	{
		Point3f p0123=getRandomDisplacementMidPoint3f(p0,p1,p2,p3,size);
		Point3f p01=getMiddlePoint3f(p0,p1);
		Point3f p12=getMiddlePoint3f(p1,p2);
		Point3f p23=getMiddlePoint3f(p2,p3);
		Point3f p30=getMiddlePoint3f(p3,p0);
		if(depth>=MaxDepth)
		{
			int length=CoordinatesLinkedList.length;
			CoordinatesLinkedList.add(p0);//0
			CoordinatesLinkedList.add(p1);//1
			CoordinatesLinkedList.add(p2);//2
			CoordinatesLinkedList.add(p3);//3
			CoordinatesLinkedList.add(p0123);//4
			CoordinatesLinkedList.add(p01);//5
			CoordinatesLinkedList.add(p12);//6
			CoordinatesLinkedList.add(p23);//7
			CoordinatesLinkedList.add(p30);//8
			int[] coordinateIndices=new int[]
			{
				length+0,length+5,length+4,length+8,
				length+1,length+6,length+4,length+5,
				length+2,length+7,length+4,length+6,
				length+3,length+8,length+4,length+7
			};
			CoordinateIndicesLinkedList.addIntegers(coordinateIndices);
			return;
		}
		this.getMountain3DCoordinatesAndCoordinateIndices(p0,p01,p0123,p30,size*decay,depth+1);
		this.getMountain3DCoordinatesAndCoordinateIndices(p1,p12,p0123,p01,size*decay,depth+1);
		this.getMountain3DCoordinatesAndCoordinateIndices(p2,p23,p0123,p12,size*decay,depth+1);
		this.getMountain3DCoordinatesAndCoordinateIndices(p3,p30,p0123,p23,size*decay,depth+1);
	}
	public Mountain3D(float xdim,float ydim,float zdim,double Hurst,int maxDepth,Appearance appearance)
	{
		MaxDepth=maxDepth;
		this.decay=(float)(1.0/Math.pow(2,Hurst));
		CoordinatesLinkedList=new LinkedList();
		CoordinateIndicesLinkedList=new LinkedList();
		Point3f p0=new Point3f(-xdim/2,0f,zdim/2);
		Point3f p1=new Point3f(xdim/2,0f,zdim/2);
		Point3f p2=new Point3f(xdim/2,0f,-zdim/2);
		Point3f p3=new Point3f(-xdim/2,0f,-zdim/2);
		this.getMountain3DCoordinatesAndCoordinateIndices(p0,p1,p2,p3,ydim,0);
		Point3f[] coordinates=CoordinatesLinkedList.getAllPoint3f();
		int[] coordinateIndices=CoordinateIndicesLinkedList.getAllInt();
		int m=CoordinatesLinkedList.length;
		int n=CoordinateIndicesLinkedList.length;
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(m,1,n);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}