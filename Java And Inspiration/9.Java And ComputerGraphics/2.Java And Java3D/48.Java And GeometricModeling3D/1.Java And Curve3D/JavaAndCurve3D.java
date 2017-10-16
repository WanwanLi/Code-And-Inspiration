import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndCurve3D
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
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(1.5f);
		ColoringAttributes ColoringAttributes1=new ColoringAttributes();
		ColoringAttributes1.setColor(new Color3f(0,1,0));
		Appearance Appearance1=new Appearance();
		Appearance1.setLineAttributes(LineAttributes1);
		Appearance1.setColoringAttributes(ColoringAttributes1);
		Transform3D Transform3D=new Transform3D();
		Transform3D.setScale(new Vector3d(0.2,0.05,0.2));
		TransformGroup1.setTransform(Transform3D);
		TransformGroup1.addChild(new Curve3D(new Function_Curve1(),0,1.25,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
class Function_Curve1 implements Function
{
	public Point3d curve(double t)
	{
		double w=100;
		double x=2*t*Math.sin(w*t)*(Math.PI/30);
		double y=t*0.1*w;
		double z=t*Math.cos(t*0.5*w);
		return new Point3d(x,y,z);
	}
	public Point3d surface(double u,double v){return new Point3d(0,0,0);}
}
interface Function
{
	Point3d curve(double t);
	Point3d surface(double u,double v);
} 
class Curve3D extends Shape3D
{
	public int n=1000;
	public Point3d[] coordinates=new Point3d[n];
	public Curve3D(Function function,Appearance appearance)
	{
		for(int i=0;i<n;i++)
		{
			double t=(0.0+i)/(n-1);
			this.coordinates[i]=function.curve(t);
		}
		this.setGeometry(this.getIndexedLineArray());
		this.setAppearance(appearance);
	}
	public Curve3D(Function function,double x0,double x1,Appearance appearance)
	{
		double dx=(x1-x0)/(n-1);
		for(int i=0;i<n;i++)
		{
			double x=x0+i*dx;
			this.coordinates[i]=function.curve(x);
		}
		this.setGeometry(this.getIndexedLineArray());
		this.setAppearance(appearance);
	}
	GeometryArray getIndexedLineArray()
	{
		int[] coordinateIndices=new int[2*(n-1)];
		int c=0;
		for(int i=0;i<n-1;i++)
		{
			coordinateIndices[c++]=i;
			coordinateIndices[c++]=i+1;
		}
		IndexedLineArray IndexedLineArray1=new IndexedLineArray(coordinates.length,IndexedLineArray.COORDINATES,coordinateIndices.length);
		IndexedLineArray1.setCoordinates(0,coordinates);
		IndexedLineArray1.setCoordinateIndices(0,coordinateIndices);
		return IndexedLineArray1;
	}
}