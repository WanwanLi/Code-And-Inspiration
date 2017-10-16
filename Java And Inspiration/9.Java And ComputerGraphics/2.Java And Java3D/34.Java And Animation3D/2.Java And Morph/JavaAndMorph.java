import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.*;
public class JavaAndMorph
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
		Material1.setDiffuseColor(new Color3f(1f,0f,0f));
		Appearance1.setMaterial(Material1);
		float R=0.3f,r=0.1f;
		double theta=Math.PI/2,length=0.4;
		GeometryArray[] GeometryArrays=new GeometryArray[4];	
		GeometryArrays[0]=getStraightPipe(R,r,0);
		GeometryArrays[1]=getStraightPipe(R,r,length);	
		GeometryArrays[2]=getCurlyPipe(R,r,theta);
		GeometryArrays[3]=getCurlyPipe(R,r,3*theta);
		Morph Morph1=new Morph(GeometryArrays,Appearance1);
		Morph1.setCapability(Morph.ALLOW_WEIGHTS_READ);
		Morph1.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
		TransformGroup1.addChild(Morph1);
		Alpha Alpha1=new Alpha();
		Alpha1.setLoopCount(-1);
		Alpha1.setIncreasingAlphaDuration(6000);
		Alpha1.setDecreasingAlphaDuration(6000);
		Alpha1.setMode(Alpha.INCREASING_ENABLE|Alpha.DECREASING_ENABLE);
		Behavior_Morph Behavior_Morph1=new Behavior_Morph(TransformGroup1,Morph1,Alpha1);
		Behavior_Morph1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(Behavior_Morph1);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	private static GeometryArray getCurlyPipe(float R,float r,double theta)
	{
		int m=20,n=40;
		double w=2*Math.PI/m;
		double cosw=Math.cos(w);
		double sinw=Math.sin(w);
		double dx=R*(1-cosw);
		double dy=-R*sinw;
		double[] transformMatrix=new double[]
		{
			cosw,-sinw,0,dx,
			sinw, cosw,0,dy,
			     0,       0,1,  0,
			     0,       0,0,  1
		};
		Transform3D circleTransform3D=new Transform3D(transformMatrix);
		Point3d[] circlePoint3d=new Point3d[m];
		circlePoint3d[0]=new Point3d(R+r,0,0);
		for(int i=1;i<m;i++)
		{
			circlePoint3d[i]=new Point3d();
			circleTransform3D.transform(circlePoint3d[i-1],circlePoint3d[i]);
		}
		Transform3D rotYTransform3D=new Transform3D();
		rotYTransform3D.rotY(theta/n);
		System.out.println(IndexedQuadArray.COORDINATES);
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(n*m,1,4*m*n);
		int index=0;
		for(int i=0;i<n;i++)
		{
			IndexedQuadArray1.setCoordinates(i*m,circlePoint3d);
			for(int j=0;j<m;j++)
			{
				rotYTransform3D.transform(circlePoint3d[j]);
				int[] coordinateIndices=new int[]
				{
					((i+0)%n)*m+((j+0)%m),
					((i+1)%n)*m+((j+0)%m),
					((i+1)%n)*m+((j+1)%m),
					((i+0)%n)*m+((j+1)%m)
				};
				IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
				index+=4;
			}
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		return GeometryInfo1.getGeometryArray();
	}
	private static GeometryArray getStraightPipe(float R,float r,double length)
	{
		int m=20,n=40;
		double w=2*Math.PI/m;
		double cosw=Math.cos(w);
		double sinw=Math.sin(w);
		double dx=R*(1-cosw);
		double dy=-R*sinw;
		double[] transformMatrix=new double[]
		{
			cosw,-sinw,0,dx,
			sinw, cosw,0,dy,
			     0,       0,1,  0,
			     0,       0,0,  1
		};
		Transform3D circleTransform3D=new Transform3D(transformMatrix);
		Point3d[] circlePoint3d=new Point3d[m];
		circlePoint3d[0]=new Point3d(R+r,0,0);
		for(int i=1;i<m;i++)
		{
			circlePoint3d[i]=new Point3d();
			circleTransform3D.transform(circlePoint3d[i-1],circlePoint3d[i]);
		}
		Transform3D translationTransform3D=new Transform3D();
		translationTransform3D.set(new Vector3d(0,0,-length/n));
		System.out.println(IndexedQuadArray.COORDINATES);
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(n*m,1,4*m*n);
		int index=0;
		for(int i=0;i<n;i++)
		{
			IndexedQuadArray1.setCoordinates(i*m,circlePoint3d);
			for(int j=0;j<m;j++)
			{
				translationTransform3D.transform(circlePoint3d[j]);
				int[] coordinateIndices=new int[]
				{
					((i+0)%n)*m+((j+0)%m),
					((i+1)%n)*m+((j+0)%m),
					((i+1)%n)*m+((j+1)%m),
					((i+0)%n)*m+((j+1)%m)
				};
				IndexedQuadArray1.setCoordinateIndices(index,coordinateIndices);
				index+=4;
			}
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		return GeometryInfo1.getGeometryArray();
	}
}
class Behavior_Morph extends Behavior
{
	TransformGroup transformGroup;
	Morph morph;
	Alpha alpha;
	public Behavior_Morph(TransformGroup transformGroup,Morph morph,Alpha alpha)
	{
		this.transformGroup=transformGroup;
		this.morph=morph;
		this.alpha=alpha;
	}
	public void initialize(){this.wakeupOn(new WakeupOnElapsedTime(10));}
	public void processStimulus(Enumeration enumeration)
	{
		int n=4;
		double[] weights=new double[n];
		double alphaValue=alpha.value();
		double unit=1.0/(n-1);
		int index=(int)(alphaValue/unit);
		weights[index]=(((index+1)%n)*unit-alphaValue)/unit;
		weights[((index+1)%n)]=1-weights[index];
		morph.setWeights(weights);
		Transform3D transform3D=new Transform3D();
		transform3D.rotX(Math.PI/2);
		transformGroup.setTransform(transform3D);
		this.wakeupOn(new WakeupOnElapsedTime(10));
	}
}







