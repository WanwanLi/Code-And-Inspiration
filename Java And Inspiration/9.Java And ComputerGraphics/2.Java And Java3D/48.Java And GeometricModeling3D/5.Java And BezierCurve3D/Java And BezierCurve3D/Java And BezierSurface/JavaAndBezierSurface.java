import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndBezierSurface
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
		BezierSurface BezierSurface1=new BezierSurface();
		TransformGroup1.addChild(BezierSurface1.getBezierControls());
		TransformGroup1.addChild(BezierSurface1.getBezierSurfaceLineStripArray());
		TransformGroup1.addChild(BezierSurface1.getBezierSurfaceQuadArray());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
class BezierSurface
{
	int m=3;
	int n=50;
	final int X=0,Y=1,Z=2,I=3;
	float[] U=new float[m+1];
	float[] V=new float[m+1];
	float[][] B=new float[][]
	{
		{1f,0f,0f,0f},
		{-3f,3f,0f,0f},
		{3f,-6f,3f,0f},
		{-1f,3f,-3f,1f}
	};
	float[][][] P=new float[][][]
	{
		{
			{-0.8f,-0.6f,-0.5f,1f},
			{-0.2f,0.2f,-0.5f,1f},
			{0.2f,0.3f,-0.5f,1f},
			{0.8f,-0.5f,-0.5f,1f}
		},
		{
			{-0.8f,-0.1f,-0.2f,1f},
			{-0.2f,0.4f,-0.2f,1f},
			{0.2f,0.4f,-0.2f,1f},
			{0.8f,-0.1f,-0.2f,1f}
		},
		{
			{-0.8f,-0.1f,0.2f,1f},
			{-0.2f,0.4f,0.2f,1f},
			{0.2f,0.4f,0.2f,1f},
			{0.8f,-0.1f,0.2f,1f}
		},
		{
			{-0.8f,-0.6f,0.5f,1f},
			{-0.2f,0.2f,0.5f,1f},
			{0.2f,0.3f,0.5f,1f},
			{0.8f,-0.5f,0.5f,1f}
		}
	};
	float[][] PX=new float[m+1][m+1];
	float[][] PY=new float[m+1][m+1];
	float[][] PZ=new float[m+1][m+1];
	float[][] PI=new float[m+1][m+1];
	float[][][] Coordinates=new float[n][n][4];
	public BezierSurface()
	{
		Matrix Matrix1=new Matrix();
		for(int i=0;i<m+1;i++)
		{
			for(int j=0;j<m+1;j++)
			{
				PX[i][j]=P[i][j][X];
				PY[i][j]=P[i][j][Y];
				PZ[i][j]=P[i][j][Z];
				PI[i][j]=P[i][j][I];
			}
		}
		for(int i=0;i<n;i++)
		{
		       	 for(int j=0;j<n;j++)
		       	 {
				float u=i*(1f/n);
				U[0]=1f;
				U[1]=u;
				U[2]=u*u;
				U[3]=u*u*u;
				float v=j*(1f/n);
				V[0]=1f;
				V[1]=v;
				V[2]=v*v;
				V[3]=v*v*v;
				Coordinates[i][j][X]=Matrix1.getBezierCoordinate(U,B,PX,V);
				Coordinates[i][j][Y]=Matrix1.getBezierCoordinate(U,B,PY,V);
				Coordinates[i][j][Z]=Matrix1.getBezierCoordinate(U,B,PZ,V);
				Coordinates[i][j][I]=Matrix1.getBezierCoordinate(U,B,PI,V);
				Coordinates[i][j][X]/=Coordinates[i][j][I];
				Coordinates[i][j][Y]/=Coordinates[i][j][I];
				Coordinates[i][j][Z]/=Coordinates[i][j][I];
		       	}
		}
	}
	public Shape3D getBezierControls()
	{
		Point3f[] controlPoints=new Point3f[2*(m+1)*(m+1)];
		int v=0;
		for(int i=0;i<m+1;i++)for(int j=0;j<m+1;j++)controlPoints[v++]=new Point3f(P[i][j][X],P[i][j][Y],P[i][j][Z]);
		for(int j=0;j<m+1;j++)for(int i=0;i<m+1;i++)controlPoints[v++]=new Point3f(P[i][j][X],P[i][j][Y],P[i][j][Z]);
		int[] number=new int[2*(m+1)];
		for(int i=0;i<2*(m+1);i++)number[i]=m+1;
		System.out.println(LineStripArray.COORDINATES);
		LineStripArray LineStripArray1=new LineStripArray(2*(m+1)*(m+1),1,number);
		LineStripArray1.setCoordinates(0,controlPoints);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(LineStripArray1);
		return shape3D;
	}
	public Shape3D getBezierSurfaceLineStripArray()
	{
		Point3f[] surfacePoints=new Point3f[2*n*n];
		int v=0;
		for(int i=0;i<n;i++)for(int j=0;j<n;j++)surfacePoints[v++]=new Point3f(Coordinates[i][j][X],Coordinates[i][j][Y],Coordinates[i][j][Z]);
		for(int j=0;j<n;j++)for(int i=0;i<n;i++)surfacePoints[v++]=new Point3f(Coordinates[i][j][X],Coordinates[i][j][Y],Coordinates[i][j][Z]);
		int[] number=new int[2*n];
		for(int i=0;i<2*n;i++)number[i]=n;
		LineStripArray LineStripArray1=new LineStripArray(2*n*n,1,number);
		LineStripArray1.setCoordinates(0,surfacePoints);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(LineStripArray1);
		return shape3D;
	}
	public Shape3D getBezierSurfaceQuadArray()
	{
		int p=0;
		QuadArray QuadArray1=new QuadArray(n*n*4,3);
		for(int i=0;i<n-1;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				Point3f P00=new Point3f(Coordinates[i+0][j+0][X],Coordinates[i+0][j+0][Y],Coordinates[i+0][j+0][Z]);
				Point3f P01=new Point3f(Coordinates[i+0][j+1][X],Coordinates[i+0][j+1][Y],Coordinates[i+0][j+1][Z]);
				Point3f P11=new Point3f(Coordinates[i+1][j+1][X],Coordinates[i+1][j+1][Y],Coordinates[i+1][j+1][Z]);
				Point3f P10=new Point3f(Coordinates[i+1][j+0][X],Coordinates[i+1][j+0][Y],Coordinates[i+1][j+0][Z]);
				QuadArray1.setCoordinate(p+0,P00);
				QuadArray1.setCoordinate(p+1,P01);
				QuadArray1.setCoordinate(p+2,P11);
				QuadArray1.setCoordinate(p+3,P10);
				Vector3f v1=new Vector3f(P00.x-P01.x,P00.y-P01.y,P00.z-P01.z);
				Vector3f v2=new Vector3f(P11.x-P01.x,P11.y-P01.y,P11.z-P01.z);
				Vector3f v=new Vector3f();
				v.cross(v2,v1);
				v.normalize();
				QuadArray1.setNormal(p+0,v);
				QuadArray1.setNormal(p+1,v);
				QuadArray1.setNormal(p+2,v);
				QuadArray1.setNormal(p+3,v);
				p+=4;
			}
		}
		Shape3D shape3D=new Shape3D();
		Appearance Appearance1=new Appearance();
		ColoringAttributes ColoringAttributes1=new ColoringAttributes();
		ColoringAttributes1.setColor(new Color3f(1f,1f,0f));
		PolygonAttributes PolygonAttributes1=new PolygonAttributes();
		System.out.println(PolygonAttributes.CULL_NONE);
		System.out.println(PolygonAttributes.CULL_BACK);
		System.out.println(PolygonAttributes.CULL_FRONT);
		System.out.println(PolygonAttributes.POLYGON_LINE);
		PolygonAttributes1.setCullFace(0);
		Appearance1.setPolygonAttributes(PolygonAttributes1);
		Appearance1.setColoringAttributes(ColoringAttributes1);
		shape3D.setGeometry(QuadArray1);
		shape3D.setAppearance(Appearance1);
		return shape3D;
	}
}
class Matrix
{
	int m=3;
	public float getBezierCoordinate(float[] U,float[][] B,float[][] P,float[] V)
	{
		float[] coordinates=new float[m+1];
		float[] t=new float[m+1];
		float[][] T=new float[m+1][m+1];
		float coordinate=0f;
		for(int j=0;j<m+1;j++)for(int i=0;i<m+1;i++)coordinates[j]+=U[i]*B[i][j];
		for(int j=0;j<m+1;j++){t[j]=coordinates[j];coordinates[j]=0;}
		for(int j=0;j<m+1;j++)for(int i=0;i<m+1;i++)coordinates[j]+=t[i]*P[i][j];
		T=this.getTranspose(B);
		for(int j=0;j<m+1;j++){t[j]=coordinates[j];coordinates[j]=0;}
		for(int j=0;j<m+1;j++)for(int i=0;i<m+1;i++)coordinates[j]+=t[i]*T[i][j];
		for(int i=0;i<m+1;i++)coordinate+=coordinates[i]*V[i];
		return coordinate;
	}
	private  float[][] getTranspose(float[][] B)
	{
		float[][] M=new float[m+1][m+1];
		for(int i=0;i<m+1;i++)for(int j=0;j<m+1;j++)M[i][j]=B[j][i];
		return M;
	}
}