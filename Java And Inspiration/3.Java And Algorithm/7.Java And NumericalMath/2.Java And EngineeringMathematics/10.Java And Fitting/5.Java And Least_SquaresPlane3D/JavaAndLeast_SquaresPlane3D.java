import java.applet.*;
import java.awt.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.applet.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndLeast_SquaresPlane3D extends Applet
{
	public  void init()
	{
		Canvas3D canvas3D=new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		this.setLayout(new BorderLayout());
		this.add(canvas3D);	
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0.5f,-0.5f,-1f);
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
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,0f));
		Appearance1.setMaterial(Material1);
		int m=2;
		int l=5;
		double[][] x=new double[][]
		{
			{1,2,3,4,5},
			{0.11,0.25,0.37,0.42,0.55}
		};
		double[] fx=new double[]{8.3,13.7,22.5,27.9,34.4};
		Least_SquaresPlane3D Least_SquaresPlane3D1=new Least_SquaresPlane3D(m,x,fx);
		for(int i=0;i<m+1;i++)System.out.println(Least_SquaresPlane3D1.A[i]);
		double a=1.24236;
		double b=5.809767;
		double c=7.906977;
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		Transform3D Transform3D1=new Transform3D();
		Transform3D1.setScale(0.1);
		Transform3D1.setTranslation(new Vector3f(-0.5f,-0.5f,0f));
		TransformGroup TransformGroup2=new TransformGroup(Transform3D1);
		Shape3D Shape3D1=Least_SquaresPlane3D1.getLeast_SquaresPlane3D(0,0,5,5,Appearance1);
		TransformGroup2.addChild(Shape3D1);
		System.out.println(PointArray.COORDINATES|PointArray.COLOR_4);
		PointArray PointArray1=new PointArray(l,13);
		for(int i=0;i<l;i++)PointArray1.setCoordinate(i,new Point3d(x[0][i],fx[i],x[1][i]));
		for(int i=0;i<l;i++)PointArray1.setColor(i,new Color4f((float)Math.random(),(float)Math.random(),(float)Math.random(),0.8f));
		PointAttributes PointAttributes1=new PointAttributes();
		PointAttributes1.setPointAntialiasingEnable(true);
		PointAttributes1.setPointSize(25f);
		Appearance Appearance2=new Appearance();
		Appearance2.setPointAttributes(PointAttributes1);
		Shape3D Shape3D2=new Shape3D();
		Shape3D2.setGeometry(PointArray1);
		Shape3D2.setAppearance(Appearance2);
		TransformGroup2.addChild(Shape3D2);
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndLeast_SquaresPlane3D(),300,300);
	}
}
class Least_SquaresPlane3D
{
	public double[] A;
	private int m;
	public Least_SquaresPlane3D(int m,double[][] x,double[] fx)
	{
		int l=fx.length;
		double[][] a=new double[m+1][m+1];
		double[] b=new double[m+1];
		double[][] c=new double[m+1][l];
		double[][] t=new double[l][m+1];
		for(int j=0;j<l;j++)c[0][j]=1;
		for(int i=0;i<m;i++)for(int j=0;j<l;j++)c[i+1][j]=x[i][j];
		t=this.getTransposeMatrix(m+1,l,c);
		for(int i=0;i<m+1;i++)for(int j=0;j<m+1;j++)for(int k=0;k<l;k++)a[i][j]+=c[i][k]*t[k][j];
		for(int i=0;i<m+1;i++)for(int j=0;j<l;j++)b[i]+=c[i][j]*fx[j];
		Gauss Gauss1=new Gauss();
		this.A=Gauss1.X(m+1,a,b);
		this.m=m;
	}
	public double[][] getTransposeMatrix(int m,int n,double[][] M)
	{
		double[][] T=new double[n][m];
		for(int i=0;i<n;i++)for(int j=0;j<m;j++)T[i][j]=M[j][i];
		return T;
	}
	
	public double FX(double[] X)
	{
		double FX=A[0];
		for(int i=0;i<m;i++)FX+=A[i+1]*X[i];
		return FX;
	}
	public double Y(double X,double Z)
	{
		return A[0]+A[1]*X+A[2]*Z;
	}

	public Shape3D getLeast_SquaresPlane3D(double x0,double z0,double x1,double z1,Appearance appearance)
	{
		int n=100,m=100;
		Point3d[] coordinates=new Point3d[2*m*n];
		double dx=(x1-x0)/n,dz=(z1-z0)/m;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				double x=x0+i*dx;
				double z=z0+j*dz;
				double y=Y(x,z);
				coordinates[i*n+j]=new Point3d(x,y,z);
			}
		}
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				double x=x0+i*dx;
				double z=z0+j*dz;
				double y=Y(x,z);
				coordinates[(i+m)*n+j]=new Point3d(x,y,z);
			}
		}
		System.out.println(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo GeometryInfo1=new GeometryInfo(4);
		int[] coordinateIndices=new int[2*2*(m-1)*n];
		int v=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=i*n+j;
				coordinateIndices[v++]=(i-1)*n+j;
			}
		}
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i+m-1)*n+j;
				coordinateIndices[v++]=(i+m)*n+j;
			}
		}
		int[] stripCounts=new int[2*(m-1)];
		for(int i=0;i<2*(m-1);i++)stripCounts[i]=2*n;
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		Shape3D shape3D=new Shape3D();
		shape3D.setGeometry(GeometryInfo1.getGeometryArray());
		shape3D.setAppearance(appearance);
		return shape3D;
	}
}
class Gauss
{
	private double Abs(double d)
	{
		return (d>=0?d:-d);
	}
	public double[] x(int n,double[][] a,double[] b)
	{
		double G=0;
		double[] x=new double[n];
		for(int k=0;k<n;k++)
		{
			for(int i=k+1;i<n;i++)
			{
				
				G=a[i][k]/a[k][k];
				for(int j=k;j<n;j++)a[i][j]-=G*a[k][j];
				b[i]-=G*b[k];		
			}
		}
		for(int i=n-1;i>=0;i--)
		{
			G=0;
			for(int j=i+1;j<n;j++)G+=a[i][j]*x[j];
			x[i]=(b[i]-G)/a[i][i];
		}
		return x;
	}
	public double[] X(int n,double[][] a,double[] b)
	{
		double G=0;
		double[] x=new double[n];
		for(int k=0;k<n;k++)
		{
			int r=k;
			double abs=Abs(a[k][k]);
			for(int i=k+1;i<n;i++)
			{
				if(Abs(a[i][k])>abs)
				{
					abs=Abs(a[i][k]);
					r=i;
				}
			}
			if(r!=k)
			{
				double t=0;
				for(int j=k;j<n;j++)
				{
					t=a[r][j];
					a[r][j]=a[k][j];
					a[k][j]=t;
				}
				t=b[r];
				b[r]=b[k];
				b[k]=t;
			}
			for(int i=k+1;i<n;i++)
			{
				
				G=a[i][k]/a[k][k];
				for(int j=k;j<n;j++)a[i][j]-=G*a[k][j];
				b[i]-=G*b[k];		
			}
		}
		for(int i=n-1;i>=0;i--)
		{
			G=0;
			for(int j=i+1;j<n;j++)G+=a[i][j]*x[j];
			x[i]=(b[i]-G)/a[i][i];
		}
		return x;
	}
	public double[] getX(int n,double[][] a,double[] b)
	{
		int[] R=new int[n];
		double G=0;
		double[] x=new double[n];
		for(int i=0;i<n;i++)R[i]=i;
		for(int k=0;k<n;k++)
		{
			int r=k;
			int c=k;
			double abs=Abs(a[k][k]);
			for(int i=k;i<n;i++)
			{
				for(int j=k;j<n;j++)
				{
					if(Abs(a[i][j])>abs)
					{
						abs=Abs(a[i][j]);
						r=i;
						c=j;
					}
				}
			}
			if(r!=k)
			{
				double t=0;
				for(int j=0;j<n;j++)
				{
					t=a[r][j];
					a[r][j]=a[k][j];
					a[k][j]=t;
				}
				t=b[r];
				b[r]=b[k];
				b[k]=t;
			}
			if(c!=k)
			{
				for(int i=0;i<n;i++)
				{
					double t=a[i][c];
					a[i][c]=a[i][k];
					a[i][k]=t;
				}
				int Rc=R[c];
				R[c]=R[k];
				R[k]=Rc;
			}
			for(int i=k+1;i<n;i++)
			{
				
				G=a[i][k]/a[k][k];
				for(int j=k;j<n;j++)a[i][j]-=G*a[k][j];
				b[i]-=G*b[k];		
			}
		}
		for(int i=n-1;i>=0;i--)
		{
			G=0;
			for(int j=i+1;j<n;j++)G+=a[i][j]*x[j];
			x[i]=(b[i]-G)/a[i][i];
		}
		double[] X=new double[n];
		for(int i=0;i<n;i++)X[R[i]]=x[i];
		return X;
	}
}