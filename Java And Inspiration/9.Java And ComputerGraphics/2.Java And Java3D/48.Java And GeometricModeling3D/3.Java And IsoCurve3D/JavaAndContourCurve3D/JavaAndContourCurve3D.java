import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndContourCurve3D
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
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,0f));
		Appearance1.setMaterial(Material1);
		Transform3D Transform3D=new Transform3D();
		Transform3D.setScale(new Vector3d(0.2,0.2,0.2));
		TransformGroup1.setTransform(Transform3D);
		double r=0.8;
		Surface3D CassiniEllipse3D=new Surface3D(new Function_CassiniEllipse(),-r,r,-r,r,Appearance1);
		TransformGroup1.addChild(CassiniEllipse3D);
		int l=100;double[] y=new double[l];for(int i=0;i<l;i++)y[i]=(i-l/2)*0.04;
		TransformGroup1.addChild(new ContourCurve3D(CassiniEllipse3D,y,null));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
} 
class Function_CassiniEllipse implements Function
{
	public Point3d surface(double x,double z)
	{
		double a=0.49,b=0.5,c=x*x+z*z+a*a;
		double y=c*c-4*a*a*x*x-b*b*b*b;
		return new Point3d(x,-y,z);
	}
}
class Function_HyperbolicParaboloid implements Function
{
	public Point3d surface(double x,double z)
	{
		double y=0.5*(x*x/0.2-z*z/0.1);
		return new Point3d(x,y,z);
	}
}
class Function_WaveField implements Function
{
	public Point3d surface(double x,double z)
	{
		double y=2*Math.cos(x*x)*Math.sin(z*z)/Math.exp(0.25*(x*x+z*z))-1;
		return new Point3d(x,y,z);
	}
}
class Function_ElectricField implements Function
{
	public Point3d surface(double x,double z)
	{
		double Q=0.2;
		double D=1.0;
		double y=Q/Math.sqrt((x-D)*(x-D)+z*z)-Q/Math.sqrt((x+D)*(x+D)+z*z);
		return new Point3d(x,y,z);
	}
}
interface Function
{
	Point3d surface(double u,double v);
}
class ContourCurve3D extends Shape3D
{
	public int m,n;
	public Point3d[] surfaceCoordinates;
	public int[][][] upEdgeCoordinateIndices;
	public int[][][] leftEdgeCoordinateIndices;
	public int[][][] centerEdgeCoordinateIndices;
	public Point3d[] coordinates;
	public int[] coordinateIndices;
	public double[] y;
	public ContourCurve3D(Surface3D surface,double[] y,Appearance appearance)
	{
		this.y=y;
		this.n=surface.n;
		this.m=surface.m;
		this.surfaceCoordinates=surface.coordinates;
		this.upEdgeCoordinateIndices=new int[y.length][m][n];
		this.leftEdgeCoordinateIndices=new int[y.length][m][n];
		this.centerEdgeCoordinateIndices=new int[y.length][m][n];
		this.coordinates=new Point3d[this.initEdgeCoordinateIndices()];
		this.coordinateIndices=new int[this.initCoordinateIndices()];
		this.getCoordinates();
		this.getCoordinateIndices();
		this.setGeometry(this.getIndexedLineArray());
		this.setAppearance(appearance);
	}
	private GeometryArray getIndexedLineArray()
	{
		IndexedLineArray IndexedLineArray1=new IndexedLineArray(coordinates.length,IndexedLineArray.COORDINATES,coordinateIndices.length);
		IndexedLineArray1.setCoordinates(0,coordinates);
		IndexedLineArray1.setCoordinateIndices(0,coordinateIndices);
		return IndexedLineArray1;
	}
	private int initEdgeCoordinateIndices()
	{
		int c=0;
		for(int k=0;k<y.length;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(yIsBetween(y[k],i,j,i,j+1))this.upEdgeCoordinateIndices[k][i][j]=c++;
					else this.upEdgeCoordinateIndices[k][i][j]=-1;
					if(yIsBetween(y[k],i,j,i+1,j))this.leftEdgeCoordinateIndices[k][i][j]=c++;
					else this.leftEdgeCoordinateIndices[k][i][j]=-1;
					if(yIsBetween(y[k],i+1,j,i,j+1))this.centerEdgeCoordinateIndices[k][i][j]=c++;
					else this.centerEdgeCoordinateIndices[k][i][j]=-1;
					
				}
			}
			for(int j=0;j<n-1;j++)
			{
				if(yIsBetween(y[k],m-1,j,m-1,j+1))this.upEdgeCoordinateIndices[k][m-1][j]=c++;
				else this.upEdgeCoordinateIndices[k][m-1][j]=-1;
			}
			for(int i=0;i<m-1;i++)
			{
				if(yIsBetween(y[k],i,n-1,i+1,n-1))this.leftEdgeCoordinateIndices[k][i][n-1]=c++;
				else this.leftEdgeCoordinateIndices[k][i][n-1]=-1;
			}
		}
		return c;
	}
	private int initCoordinateIndices()
	{
		int c=0;
		for(int k=0;k<y.length;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)c+=2;
					if(leftEdgeCoordinateIndices[k][i][j]!=-1&&centerEdgeCoordinateIndices[k][i][j]!=-1)c+=2;
					if(centerEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1)c+=2;
					if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&centerEdgeCoordinateIndices[k][i][j]!=-1)c+=2;
					if(centerEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1)c+=2;
					if(leftEdgeCoordinateIndices[k][i][j+1]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1)c+=2;
				}
			}
		}
		return c;
	}
	private void getCoordinateIndices()
	{
		int c=0;
		for(int k=0;k<y.length;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)
					{
						this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
						this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
					}
					if(leftEdgeCoordinateIndices[k][i][j]!=-1&&centerEdgeCoordinateIndices[k][i][j]!=-1)
					{
						this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
						this.coordinateIndices[c++]=centerEdgeCoordinateIndices[k][i][j];
					}
					if(centerEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1)
					{
						this.coordinateIndices[c++]=centerEdgeCoordinateIndices[k][i][j];
						this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
					}
					if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&centerEdgeCoordinateIndices[k][i][j]!=-1)
					{
						this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
						this.coordinateIndices[c++]=centerEdgeCoordinateIndices[k][i][j];
					}
					if(centerEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1)
					{
						this.coordinateIndices[c++]=centerEdgeCoordinateIndices[k][i][j];
						this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
					}
					if(leftEdgeCoordinateIndices[k][i][j+1]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1)
					{
						this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
						this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
					}
				}
			}
		}
	}
	private void getCoordinates()
	{
		int c=0;
		for(int k=0;k<y.length;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(y[k],i,j,i,j+1);
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(y[k],i,j,i+1,j);
					if(centerEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(y[k],i+1,j,i,j+1);
					
				}
			}
			for(int j=0;j<n-1;j++)
			{
				if(upEdgeCoordinateIndices[k][m-1][j]!=-1)this.coordinates[c++]=this.getMidPoint(y[k],m-1,j,m-1,j+1);
			}
			for(int i=0;i<m-1;i++)
			{
				if(leftEdgeCoordinateIndices[k][i][n-1]!=-1)this.coordinates[c++]=this.getMidPoint(y[k],i,n-1,i+1,n-1);
			}
		}
	}
	private boolean yIsBetween(double y,int i0,int j0,int i1,int j1)
	{
		boolean l0=surfaceCoordinates[i0*n+j0].y<y;
		boolean l1=y<surfaceCoordinates[i1*n+j1].y;
		boolean g0=surfaceCoordinates[i0*n+j0].y>y;
		boolean g1=y>surfaceCoordinates[i1*n+j1].y;
		return (l0&&l1)||(g0&&g1);
	}
	private Point3d getMidPoint(double y,int i0,int j0,int i1,int j1)
	{
		double x0=surfaceCoordinates[i0*n+j0].x;
		double y0=surfaceCoordinates[i0*n+j0].y;
		double z0=surfaceCoordinates[i0*n+j0].z;
		double x1=surfaceCoordinates[i1*n+j1].x;
		double y1=surfaceCoordinates[i1*n+j1].y;
		double z1=surfaceCoordinates[i1*n+j1].z;
		double dx=x1-x0,dz=z1-z0;
		double x=x0+dx*(y-y0)/(y1-y0);
		double z=z0+dz*(y-y0)/(y1-y0);
		return new Point3d(x,y,z);
	}
}

class Surface3D extends Shape3D
{
	public int n=100,m=100;
	public Point3d[] coordinates=new Point3d[m*n];
	public Surface3D(Function function,Appearance appearance)
	{
		for(int i=0;i<m;i++)
		{
			double u=(0.0+i)/(m-1);
			for(int j=0;j<n;j++)
			{
				double v=(0.0+j)/(n-1);
				this.coordinates[i*n+j]=function.surface(u,v);
			}
		}
		this.setGeometry(this.getStriangleStripArray());
		this.setAppearance(appearance);
	}
	public Surface3D(Function function,double u0,double u1,double v0,double v1,Appearance appearance)
	{
		double du=(u1-u0)/(n-1),dv=(v1-v0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double v=v0+i*dv;
			for(int j=0;j<n;j++)
			{
				double u=u0+j*du;
				this.coordinates[i*n+j]=function.surface(u,v);
			}
		}
		this.setGeometry(this.getStriangleStripArray());
		this.setAppearance(appearance);
	}
	public Surface3D(Function function,double u0,double u1,double v0,double v1,int doubleSurface,Appearance appearance)
	{
		double du=(u1-u0)/(n-1),dv=(v1-v0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double v=v0+i*dv;
			for(int j=0;j<n;j++)
			{
				double u=u0+j*du;
				this.coordinates[i*n+j]=function.surface(u,v);
			}
		}
		this.setGeometry(this.getStriangleStripArray(doubleSurface));
		this.setAppearance(appearance);
	}
	GeometryArray getStriangleStripArray()
	{
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		int[] coordinateIndices=new int[2*(m-1)*n];
		int v=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		return GeometryInfo1.getGeometryArray();
	}
	GeometryArray getStriangleStripArray(int doubleSurface)
	{
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		int[] coordinateIndices=new int[4*(m-1)*n];
		int v=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		for(int i=1;i<m;i++)
		{
			for(int j=n-1;j>=0;j--)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[2*(m-1)];
		for(int i=0;i<2*(m-1);i++)stripCounts[i]=2*n;
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		return GeometryInfo1.getGeometryArray();
	}
}
