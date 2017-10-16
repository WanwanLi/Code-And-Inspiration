import java.io.*;
import java.awt.Color;
import java.util.Enumeration;
import java.util.Calendar;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndTeapot
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
		Appearance Appearance2=new Appearance();
		Material Material2=new Material();
		Material2.setDiffuseColor(new Color3f(1f,0f,0f));
		Appearance2.setMaterial(Material2);
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(0.15);
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Teapot(Appearance2));
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
class Teapot extends TransformGroup
{
	public Teapot(Appearance appearance)
	{
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader("Teapot.bezier"));
			String stringLine=BufferedReader1.readLine();
			int n=Integer.parseInt(stringLine);
			int[][] index=new int[n][16];
			String stringData="";
			for(int i=0;i<n;i++)
			{
				stringLine=BufferedReader1.readLine();
				int p=0;
				for(int j=0;j<16;j++)
				{
					char c=stringLine.charAt(p++);
					while(c!=','&&p<stringLine.length()){stringData+=c;c=stringLine.charAt(p++);}
					if(p==stringLine.length())stringData+=c;
					index[i][j]=Integer.parseInt(stringData);
					stringData="";
				}
			}
			stringLine=BufferedReader1.readLine();
			int m=Integer.parseInt(stringLine);
			Point3d[] point3ds=new Point3d[m];
			stringData="";
			stringLine=BufferedReader1.readLine();
			int p=0;
			for(int i=0;i<m;i++)
			{
				int k=0;
				double[] d=new double[3];
				while(k<3)
				{
					char c=stringLine.charAt(p++);
					while(c!=','&&p<stringLine.length()){stringData+=c;c=stringLine.charAt(p++);}
					if(p==stringLine.length()){stringData+=c;stringLine=BufferedReader1.readLine();p=0;}
					d[k++]=Double.parseDouble(stringData);
					stringData="";
				}
				point3ds[i]=new Point3d(d[0],d[1],d[2]);
			}
			Point3d[][] controlPoints=new Point3d[4][4];
			for(int k=0;k<n;k++)
			{
				for(int i=0;i<4;i++)
				{
					for(int j=0;j<4;j++)
					{
						controlPoints[i][j]=point3ds[index[k][i+4*j]-1];
					}
				}
				Bezier3D bezier3D=new Bezier3D(controlPoints);
				bezier3D.setAppearance(appearance);
				this.addChild(bezier3D);
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
class Bezier2D extends Shape3D
{
	static int H=6;
	public static final int NUMBER=(1<<H);
	int coordinateIndex=0;
	public Point3d[] Coordinates=new Point3d[NUMBER];
	int[] stripCounts=new int[]{NUMBER};
	private void devideControlPoints(int h,Point3d P0,Point3d P1,Point3d P2,Point3d P3)
	{
		if(h>=H)Coordinates[coordinateIndex++]=new Point3d(P3);
		else
		{
			Point3d P01=new Point3d();
			P01.add(P0,P1);
			P01.scale(0.5);
			Point3d P12=new Point3d();
			P12.add(P1,P2);
			P12.scale(0.5);
			Point3d P23=new Point3d();
			P23.add(P2,P3);
			P23.scale(0.5);
			Point3d P0112=new Point3d();
			P0112.add(P01,P12);
			P0112.scale(0.5);
			Point3d P1223=new Point3d();
			P1223.add(P12,P23);
			P1223.scale(0.5);
			Point3d P01121223=new Point3d();
			P01121223.add(P0112,P1223);
			P01121223.scale(0.5);
			this.devideControlPoints(h+1,P0,P01,P0112,P01121223);
			this.devideControlPoints(h+1,P01121223,P1223,P23,P3);
		}
	}
	public Bezier2D(Point3d P0,Point3d P1,Point3d P2,Point3d P3)
	{
		coordinateIndex=0;
		this.devideControlPoints(0,P0,P1,P2,P3);	
		LineStripArray LineStripArray1=new LineStripArray(NUMBER,GeometryArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,Coordinates);
		this.setGeometry(LineStripArray1);
	}
}
class Bezier3D extends Shape3D
{
	public Bezier3D(Point3d[][] controlPoints)
	{
		int m=Bezier2D.NUMBER;
		int n=Bezier2D.NUMBER;
		Point3d[] coordinates=new Point3d[m*n];
		int coordinateIndex=0;
		Point3d[] Point3d4=new Point3d[4];
		Bezier2D[] Bezier2D4=new Bezier2D[4];
		for(int k=0;k<4;k++)Bezier2D4[k]=new Bezier2D(controlPoints[k][0],controlPoints[k][1],controlPoints[k][2],controlPoints[k][3]);
		for(int i=0;i<m;i++)
		{
			for(int k=0;k<4;k++)Point3d4[k]=Bezier2D4[k].Coordinates[i];
			Bezier2D bezier2D=new Bezier2D(Point3d4[0],Point3d4[1],Point3d4[2],Point3d4[3]);
			for(int j=0;j<n;j++)coordinates[coordinateIndex++]=bezier2D.Coordinates[j];
		}
		int[] coordinateIndices=new int[(m-1)*(2*n)];
		coordinateIndex=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{			
				coordinateIndices[coordinateIndex++]=i*n+j;	
				coordinateIndices[coordinateIndex++]=(i-1)*n+j;	
			}
		}
		int[] stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
}