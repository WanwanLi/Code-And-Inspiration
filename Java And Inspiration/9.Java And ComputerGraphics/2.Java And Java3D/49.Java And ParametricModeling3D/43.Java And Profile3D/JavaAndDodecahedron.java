import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndDodecahedron
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
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
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		BranchGroup1.addChild(MouseTranslate1);
		BranchGroup1.addChild(MouseZoom1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,0f));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new Profile3D(Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
 
class Profile3D extends Shape3D
{
	StringQueue triangleArrayQueue;
	//public Profile3D(boolean[][][] pointsArray3D,Appearance appearance)
	public Profile3D(Appearance appearance)
	{
/*
		int level=pointsArray3D.length;
		int row=pointsArray3D[0].length;
		int column=pointsArray3D[0][0].length;
*/
		this.triangleArrayQueue=new StringQueue();
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
		double r=0.5;
		Point3d[] coordinates=new Point3d[]
		{
			new Point3d(-r,-r,-r),
			new Point3d(-r,-r,r),
			new Point3d(r,-r,r),
			new Point3d(r,-r,-r),

			new Point3d(-r,r,-r),
			new Point3d(-r,r,r),
			new Point3d(r,r,r),
			new Point3d(r,r,-r),
			new Point3d(-r,-r,-r)

		};
		int[] coordinateIndices=new int[]
		{
			0,1,2,3,
			7,6,5,4
		};
		coordinateIndices=new int[]
		{
			0,5,4,0,2,5,2,7,5
		};
/*
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				
			}
		}
*/
		GeometryInfo1.setCoordinates(coordinates);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
	private void addTriangleArray(boolean[][][] pointsArray3D,int i0,int j0)
	{
		for(int i=i0;i<i0+1;i++)
		{
			for(int j=j0;j<j0+1;j++)
			{
				if
			}
		}
	}
}
class StringQueue
{
	private String stringQueue;
	private int length;
	public StringQueue()
	{
		this.stringQueue="";
	}
	public StringQueue(String queue)
	{
		this.stringQueue=queue;
		this.length=this.getLength();
		if(length==0)
		{
			this.stringQueue+=";";
			this.length=1;
		}
	}
	public void enQueue(char character)
	{
		this.stringQueue+=character;
		this.length++;
	}
	public void enQueue(String string)
	{
		this.stringQueue+=string+";";
		this.length++;
	}
	public void enQueue(int integer)
	{
		this.stringQueue+=integer+";";
		this.length++;
	}
	public void enQueue(int[] array)
	{
		int l=array.length;
		for(int i=0;i<l;i++)this.stringQueue+=array[i]+";";
		this.length+=l;
	}
	public void insert(String string,int index)
	{
		this.length++;
		if(index>=length)
		{
			this.stringQueue+=string+";";
			return;
		}
		int i=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(i,Length);
		this.stringQueue=s0+";"+string+s1;
	}
	public void set(int index,String string)
	{
		if(index>=length)return;
		int i=0,j=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		for(j=i+1;j<Length;j++)
		{
			char c=stringQueue.charAt(j);
			if(c==';')break;
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(j,Length);
		this.stringQueue=s0+";"+string+s1;
	}
	public String deQueue()
	{
		String string="";
		if(stringQueue.length()==0)return string;
		int n=0;
		char c=stringQueue.charAt(n++);
		while(c!=';')
		{
			string+=c;
			c=stringQueue.charAt(n++);
		}
		this.stringQueue=stringQueue.substring(n,stringQueue.length());
		this.length--;
		return string;
	}
	private int getLength()
	{
		int i=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')l++;
		}
		return l;
	}
	public int length()
	{
		return this.length;
	}
	public void show()
	{
		System.out.println(stringQueue);
	}
	public double[] getDoubles()
	{
		int l=this.length();
		double[] doubles=new double[l];
		int n=0,i=0;
		String s="";
		char c;
		while(n<l)
		{
			c=stringQueue.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(i++);
			}
			doubles[n++]=Double.parseDouble(s);
			s="";
		}
		return doubles;
	}
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
}