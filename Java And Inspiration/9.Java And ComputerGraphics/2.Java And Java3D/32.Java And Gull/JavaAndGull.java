import java.io.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndGull
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
		BranchGroup1.addChild(MouseRotate1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,0.8f,0f));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new Gull(Appearance1));
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		BranchGroup1.compile();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}

class Gull extends Shape3D
{
	int maxSize=1024*8;
	byte[] CompressedGeometryData=new byte[maxSize];
	int Size=0;
	public Gull(Appearance appearance)
	{
		this.getCompressedGeometryData();
		CompressedGeometryHeader CompressedGeometryHeader1=new CompressedGeometryHeader();
		CompressedGeometryHeader1.bufferType=CompressedGeometryHeader.TRIANGLE_BUFFER;
		CompressedGeometryHeader1.bufferDataPresent=CompressedGeometryHeader.NORMAL_IN_BUFFER;
		CompressedGeometryHeader1.start=0;
		CompressedGeometryHeader1.size=Size;
		CompressedGeometry CompressedGeometry1=new CompressedGeometry(CompressedGeometryHeader1,CompressedGeometryData);
		this.setGeometry(CompressedGeometry1);
		this.setAppearance(appearance);
	}
	private void getCompressedGeometryData()
	{
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader("Gull.j3d"));
			String stringLine=BufferedReader1.readLine();
			String stringData="";
			int p=0;
			char c=stringLine.charAt(p++);
			int l=0;
			while(stringLine!=null)
			{
				while(c!=','&&p<stringLine.length())
				{
					stringData+=c;
					c=stringLine.charAt(p++);
				}
				if(p==stringLine.length())
				{
					stringLine=BufferedReader1.readLine();
					if(stringLine==null)
					{
						if(!stringData.equals(""))CompressedGeometryData[Size++]=Byte.parseByte(stringData);
						break;
					}
					p=0;
				}
				if(!stringData.equals(""))CompressedGeometryData[Size++]=Byte.parseByte(stringData);
				stringData="";
				c=stringLine.charAt(p++);
				if(l>=500)break;
			}
		}
		catch(Exception e){}
	}
}