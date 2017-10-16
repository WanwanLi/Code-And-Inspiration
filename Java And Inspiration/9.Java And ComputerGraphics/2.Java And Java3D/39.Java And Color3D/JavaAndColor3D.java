import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndColor3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),100);
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
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom();
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate();
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		TransformGroup1.addChild(new Color3D().getHSBcone(0.5f,0.7f,true));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Color3D
{
	public Shape3D getRGBcube(float r,boolean hasMaterial)
	{
		int p=0,c=0,n=0;
		Point3f p000=new Point3f(-r/2,-r/2,-r/2);
		Point3f p001=new Point3f(-r/2,-r/2,+r/2);
		Point3f p010=new Point3f(-r/2,+r/2,-r/2);
		Point3f p011=new Point3f(-r/2,+r/2,+r/2);
		Point3f p100=new Point3f(+r/2,-r/2,-r/2);
		Point3f p101=new Point3f(+r/2,-r/2,+r/2);
		Point3f p110=new Point3f(+r/2,+r/2,-r/2);
		Point3f p111=new Point3f(+r/2,+r/2,+r/2);
		Color3f c000=new Color3f(0f,0f,0f);
		Color3f c001=new Color3f(0f,0f,1f);
		Color3f c010=new Color3f(0f,1f,0f);
		Color3f c011=new Color3f(0f,1f,1f);
		Color3f c100=new Color3f(1f,0f,0f);
		Color3f c101=new Color3f(1f,0f,1f);
		Color3f c110=new Color3f(1f,1f,0f);
		Color3f c111=new Color3f(1f,1f,1f);
		Vector3f normal0=new Vector3f(-1f,0f,0f);
		Vector3f normal1=new Vector3f(0f,0f,-1f);
		Vector3f normal2=new Vector3f(0f,-1f,0f);
		Vector3f normal3=new Vector3f(0f,1f,0f);
		Vector3f normal4=new Vector3f(1f,0f,0f);
		Vector3f normal5=new Vector3f(0f,0f,1f);
		QuadArray QuadArray1=new QuadArray(4*6,QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.COLOR_3);
		QuadArray1.setCoordinate(p++,p000);
		QuadArray1.setCoordinate(p++,p001);
		QuadArray1.setCoordinate(p++,p011);
		QuadArray1.setCoordinate(p++,p010);
		QuadArray1.setColor(c++,c000);
		QuadArray1.setColor(c++,c001);
		QuadArray1.setColor(c++,c011);
		QuadArray1.setColor(c++,c010);
		QuadArray1.setNormal(n++,normal0);
		QuadArray1.setNormal(n++,normal0);
		QuadArray1.setNormal(n++,normal0);
		QuadArray1.setNormal(n++,normal0);
		QuadArray1.setCoordinate(p++,p000);
		QuadArray1.setCoordinate(p++,p010);
		QuadArray1.setCoordinate(p++,p110);
		QuadArray1.setCoordinate(p++,p100);
		QuadArray1.setColor(c++,c000);
		QuadArray1.setColor(c++,c010);
		QuadArray1.setColor(c++,c110);
		QuadArray1.setColor(c++,c100);
		QuadArray1.setNormal(n++,normal1);
		QuadArray1.setNormal(n++,normal1);
		QuadArray1.setNormal(n++,normal1);
		QuadArray1.setNormal(n++,normal1);
		QuadArray1.setCoordinate(p++,p000);
		QuadArray1.setCoordinate(p++,p100);
		QuadArray1.setCoordinate(p++,p101);
		QuadArray1.setCoordinate(p++,p001);
		QuadArray1.setColor(c++,c000);
		QuadArray1.setColor(c++,c100);
		QuadArray1.setColor(c++,c101);
		QuadArray1.setColor(c++,c001);
		QuadArray1.setNormal(n++,normal2);
		QuadArray1.setNormal(n++,normal2);
		QuadArray1.setNormal(n++,normal2);
		QuadArray1.setNormal(n++,normal2);
		QuadArray1.setCoordinate(p++,p111);
		QuadArray1.setCoordinate(p++,p110);
		QuadArray1.setCoordinate(p++,p010);
		QuadArray1.setCoordinate(p++,p011);
		QuadArray1.setColor(c++,c111);
		QuadArray1.setColor(c++,c110);
		QuadArray1.setColor(c++,c010);
		QuadArray1.setColor(c++,c011);
		QuadArray1.setNormal(n++,normal3);
		QuadArray1.setNormal(n++,normal3);
		QuadArray1.setNormal(n++,normal3);
		QuadArray1.setNormal(n++,normal3);
		QuadArray1.setCoordinate(p++,p111);
		QuadArray1.setCoordinate(p++,p101);
		QuadArray1.setCoordinate(p++,p100);
		QuadArray1.setCoordinate(p++,p110);
		QuadArray1.setColor(c++,c111);
		QuadArray1.setColor(c++,c101);
		QuadArray1.setColor(c++,c100);
		QuadArray1.setColor(c++,c110);
		QuadArray1.setNormal(n++,normal4);
		QuadArray1.setNormal(n++,normal4);
		QuadArray1.setNormal(n++,normal4);
		QuadArray1.setNormal(n++,normal4);
		QuadArray1.setCoordinate(p++,p111);
		QuadArray1.setCoordinate(p++,p011);
		QuadArray1.setCoordinate(p++,p001);
		QuadArray1.setCoordinate(p++,p101);
		QuadArray1.setColor(c++,c111);
		QuadArray1.setColor(c++,c011);
		QuadArray1.setColor(c++,c001);
		QuadArray1.setColor(c++,c101);
		QuadArray1.setNormal(n++,normal5);
		QuadArray1.setNormal(n++,normal5);
		QuadArray1.setNormal(n++,normal5);
		QuadArray1.setNormal(n++,normal5);
		Shape3D RGBcube=new Shape3D();
		RGBcube.setGeometry(QuadArray1);
		if(hasMaterial)
		{
			Appearance Appearance1=new Appearance();
			Appearance1.setMaterial(new Material());
			RGBcube.setAppearance(Appearance1);
		}
		return RGBcube;
	}
	public Shape3D getHSBcone(float r,float h,boolean hasMaterial)
	{
		int p=0,c=0,n=0,m=256;
		double a=2*Math.PI/m;
		Point3f p000=new Point3f(0,-h/2,0);
		Point3f p111=new Point3f(0,h/2,0);
		Color3f c000=new Color3f(0f,0f,0f);
		Color3f c111=new Color3f(1f,1f,1f);
		Vector3f n010=new Vector3f(0f,1f,0f);
		TriangleArray TriangleArray1=new TriangleArray(2*m*3,TriangleArray.COORDINATES|TriangleArray.NORMALS|TriangleArray.COLOR_3);
		for(int i=0;i<m;i++)
		{
			float x=(float)(h*Math.cos((i+0)*a));
			float y=-r;
			float z=-(float)(h*Math.sin((i+0)*a));
			Vector3f nXYZ=new Vector3f(x,y,z);
			TriangleArray1.setCoordinate(p++,p000);
			TriangleArray1.setColor(c++,c000);
			TriangleArray1.setNormal(n++,nXYZ);
			x=(float)(r*Math.cos((i+1)*a));
			y=h/2;
			z=-(float)(r*Math.sin((i+1)*a));
			Point3f pXYZ=new Point3f(x,y,z);
			float[] RGB=this.getRGBvalue((i+1f)/m);
			Color3f cRGB=new Color3f(RGB[0],RGB[1],RGB[2]);
			TriangleArray1.setCoordinate(p++,pXYZ);
			TriangleArray1.setColor(c++,cRGB);
			TriangleArray1.setNormal(n++,nXYZ);
			x=(float)(r*Math.cos((i+0)*a));
			z=-(float)(r*Math.sin((i+0)*a));
			pXYZ=new Point3f(x,y,z);
			RGB=this.getRGBvalue((i+0f)/m);
			cRGB=new Color3f(RGB[0],RGB[1],RGB[2]);
			TriangleArray1.setCoordinate(p++,pXYZ);
			TriangleArray1.setColor(c++,cRGB);
			TriangleArray1.setNormal(n++,nXYZ);
		}
		for(int i=0;i<m;i++)
		{
			TriangleArray1.setCoordinate(p++,p111);
			TriangleArray1.setColor(c++,c111);
			TriangleArray1.setNormal(n++,n010);
			float x=(float)(r*Math.cos((i+0)*a));
			float y=h/2;
			float z=-(float)(r*Math.sin((i+0)*a));
			Point3f pXYZ=new Point3f(x,y,z);
			float[] RGB=this.getRGBvalue((i+0f)/m);
			Color3f cRGB=new Color3f(RGB[0],RGB[1],RGB[2]);
			Vector3f nXYZ=new Vector3f();
			TriangleArray1.setCoordinate(p++,pXYZ);
			TriangleArray1.setColor(c++,cRGB);
			TriangleArray1.setNormal(n++,n010);
			x=(float)(r*Math.cos((i+1)*a));
			z=-(float)(r*Math.sin((i+1)*a));
			pXYZ=new Point3f(x,y,z);
			RGB=this.getRGBvalue((i+1f)/m);
			cRGB=new Color3f(RGB[0],RGB[1],RGB[2]);
			TriangleArray1.setCoordinate(p++,pXYZ);
			TriangleArray1.setColor(c++,cRGB);
			TriangleArray1.setNormal(n++,n010);
		}
		Shape3D HSBcone=new Shape3D();
		HSBcone.setGeometry(TriangleArray1);
		if(hasMaterial)
		{
			Appearance Appearance1=new Appearance();
			Appearance1.setMaterial(new Material());
			HSBcone.setAppearance(Appearance1);
		}
		return HSBcone;
	}
	private float[] getRGBvalue(float hue)
	{
		float red=0f,green=0f,blue=0f,high=0f,mid=0f,low;
		float saturation=1,brightness=1;
		float range=saturation*brightness;
		high=brightness;
		low=brightness-range;
		float H=6*hue%6;
		int n=(int)H;
		float h=H-n;
		mid=(n%2==0?low+h*range:low+(1-h)*range);
		switch(n)
		{
			case 0:red=high;green=mid;blue=low;break;
			case 1:red=mid;green=high;blue=low;break;
			case 2:red=low;green=high;blue=mid;break;
			case 3:red=low;green=mid;blue=high;break;
			case 4:red=mid;green=low;blue=high;break;
			case 5:red=high;green=low;blue=mid;break;
		}
		float[] RGBvalue=new float[3];
		RGBvalue[0]=red;
		RGBvalue[1]=green;
		RGBvalue[2]=blue;
		return RGBvalue;
	}
}
