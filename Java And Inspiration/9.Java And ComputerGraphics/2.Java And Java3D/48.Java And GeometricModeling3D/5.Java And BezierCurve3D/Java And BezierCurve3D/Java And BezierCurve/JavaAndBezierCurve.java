import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndBezierCurve
{
	public static void main(String[] args)
	{
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
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom();
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		MouseTranslate MouseTranslate1=new MouseTranslate();
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BezierCurve BezierCurve1=new BezierCurve();
		TransformGroup1.addChild(BezierCurve1.getBezierControls());
		TransformGroup1.addChild(BezierCurve1.getBezierCurve());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
class BezierCurve
{
	int m=3;
	int n=50;
	final int X=0,Y=1,Z=2,I=3;
	float[] U=new float[m+1];
	float[][] B=new float[][]
	{
		{1f,0f,0f,0f},
		{-3f,3f,0f,0f},
		{3f,-6f,3f,0f},
		{-1f,3f,-3f,1f}
	};
	float[][] P=new float[][]
	{
		{-0.8f,-0.6f,0f,1f},
		{-0.2f,0.2f,0f,1f},
		{0.2f,0.3f,0f,1f},
		{0.8f,-0.5f,0f,1f}
	};
	float[][] Coordinates=new float[n][3];
	public BezierCurve()
	{
		Matrix Matrix1=new Matrix();
		for(int i=0;i<n;i++)
		{
			float u=i*(1f/n);
			U[0]=1f;
			U[1]=u;
			U[2]=u*u;
			U[3]=u*u*u;
			float[] M=Matrix1.getBezierCoordinates(m,U,B,P);
			Coordinates[i][X]=M[X]/M[I];
			Coordinates[i][Y]=M[Y]/M[I];
			Coordinates[i][Z]=M[Z]/M[I];
		}
	}
	public Shape3D getBezierControls()
	{
		Point3f[] controlPoints=new Point3f[m+1];
		for(int i=0;i<m+1;i++)controlPoints[i]=new Point3f(P[i][X],P[i][Y],P[i][Z]);
		int[] number=new int[]{m+1};
		System.out.println(LineStripArray.COORDINATES);
		LineStripArray LineStripArray1=new LineStripArray(m+1,1,number);
		LineStripArray1.setCoordinates(0,controlPoints);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(LineStripArray1);
		return shape3D;
	}
	public Shape3D getBezierCurve()
	{
		Point3f[] curvePoints=new Point3f[n];
		for(int i=0;i<n;i++)curvePoints[i]=new Point3f(Coordinates[i][X],Coordinates[i][Y],Coordinates[i][Z]);
		int[] number=new int[]{n};
		LineStripArray LineStripArray1=new LineStripArray(n,1,number);
		LineStripArray1.setCoordinates(0,curvePoints);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(LineStripArray1);
		return shape3D;
	}
}
class Matrix
{
	public float[] getBezierCoordinates(int m,float[] U,float[][] B,float[][] P)
	{
		float[] coordinates=new float[m+1];
		for(int j=0;j<m+1;j++)for(int i=0;i<m+1;i++)coordinates[j]+=U[i]*B[i][j];
		for(int j=0;j<m+1;j++){U[j]=coordinates[j];coordinates[j]=0;}
		for(int j=0;j<m+1;j++)for(int i=0;i<m+1;i++)coordinates[j]+=U[i]*P[i][j];
		return coordinates;
	}
}