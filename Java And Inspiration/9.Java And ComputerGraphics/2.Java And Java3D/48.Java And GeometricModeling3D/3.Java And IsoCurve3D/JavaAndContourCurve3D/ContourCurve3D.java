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
		int l=50;double[] y=new double[l];for(int i=0;i<l;i++)y[i]=(i-l/2)*0.1;
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
	public int[][] upEdgeCoordinateIndices;
	public int[][] leftEdgeCoordinateIndices;
	public int[][] centerEdgeCoordinateIndices;
	public int[][] upEdgeIndex;
	public int[][] leftEdgeIndex;
	public int[][] centerEdgeIndex;
	public Point3d[] coordinates;
	public int[] coordinateIndices;
	public double[] y;
	public ContourCurve3D(Surface3D surface,double[] y,Appearance appearance)
	{
		this.y=y;
		this.n=surface.n;
		this.m=surface.m;
		this.surfaceCoordinates=surface.coordinates;
		this.upEdgeCoordinateIndices=new int[m][n];
		this.leftEdgeCoordinateIndices=new int[m][n];
		this.centerEdgeCoordinateIndices=new int[m][n];
		this.upEdgeIndex=new int[m][n];
		this.leftEdgeIndex=new int[m][n];
		this.centerEdgeIndex=new int[m][n];
		this.coordinates=new Point3d[this.getCoordinatesCount()];
		this.coordinateIndices=new int[this.getCoordinateIndicesCount()];
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
	private int getCoordinatesCount()
	{
		int c=0;
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				int index=getYIndex(i,j,i,j+1);
				if(index!=-1){this.upEdgeCoordinateIndices[i][j]=c++;this.upEdgeIndex[i][j]=index;}
				else{this.upEdgeCoordinateIndices[i][j]=-1;this.upEdgeIndex[i][j]=-1;}
				index=getYIndex(i,j,i+1,j);
				if(index!=-1){this.leftEdgeCoordinateIndices[i][j]=c++;this.leftEdgeIndex[i][j]=index;}
				else{this.leftEdgeCoordinateIndices[i][j]=-1;this.leftEdgeIndex[i][j]=-1;}
				index=getYIndex(i+1,j,i,j+1);
				if(index!=-1){this.centerEdgeCoordinateIndices[i][j]=c++;this.centerEdgeIndex[i][j]=index;}
				else{this.centerEdgeCoordinateIndices[i][j]=-1;this.centerEdgeIndex[i][j]=-1;}
			}
		}
		for(int i=m-1;i<m;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				int index=getYIndex(i,j,i,j+1);
				if(index!=-1){this.upEdgeCoordinateIndices[i][j]=c++;this.upEdgeIndex[i][j]=index;}
				else{this.upEdgeCoordinateIndices[i][j]=-1;this.upEdgeIndex[i][j]=-1;}
			}
		}
		for(int i=0;i<m-1;i++)
		{
			for(int j=n-1;j<n;j++)
			{
				int index=getYIndex(i,j,i+1,j);
				if(index!=-1){this.leftEdgeCoordinateIndices[i][j]=c++;this.leftEdgeIndex[i][j]=index;}
				else{this.leftEdgeCoordinateIndices[i][j]=-1;this.leftEdgeIndex[i][j]=-1;}
			}
		}
		return c;
	}
	private int getCoordinateIndicesCount()
	{
		int c=0;
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				if(upEdgeIndex[i][j]!=-1&&upEdgeIndex[i][j]==leftEdgeIndex[i][j])c+=2;
				if(leftEdgeIndex[i][j]!=-1&&leftEdgeIndex[i][j]==centerEdgeIndex[i][j])c+=2;
				if(centerEdgeIndex[i][j]!=-1&&centerEdgeIndex[i][j]==upEdgeIndex[i][j])c+=2;
				if(upEdgeIndex[i+1][j]!=-1&&upEdgeIndex[i+1][j]==centerEdgeIndex[i][j])c+=2;
				if(centerEdgeIndex[i][j]!=-1&&centerEdgeIndex[i][j]==leftEdgeIndex[i][j+1])c+=2;
				if(leftEdgeIndex[i][j+1]!=-1&&leftEdgeIndex[i][j+1]==upEdgeIndex[i+1][j])c+=2;
			}
		}
		return c;
	}
	private void getCoordinates()
	{
		int c=0;
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				if(upEdgeIndex[i][j]!=-1)this.coordinates[c++]=this.getMidPoint(y[upEdgeIndex[i][j]],i,j,i,j+1);
				if(leftEdgeIndex[i][j]!=-1)this.coordinates[c++]=this.getMidPoint(y[leftEdgeIndex[i][j]],i,j,i+1,j);
				if(centerEdgeIndex[i][j]!=-1)this.coordinates[c++]=this.getMidPoint(y[centerEdgeIndex[i][j]],i+1,j,i,j+1);
			}
		}
		for(int i=m-1;i<m;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				if(upEdgeIndex[i][j]!=-1)this.coordinates[c++]=this.getMidPoint(y[upEdgeIndex[i][j]],i,j,i,j+1);
			}
		}
		for(int i=0;i<m-1;i++)
		{
			for(int j=n-1;j<n;j++)
			{
				if(leftEdgeIndex[i][j]!=-1)this.coordinates[c++]=this.getMidPoint(y[leftEdgeIndex[i][j]],i,j,i+1,j);
			}
		}
	}
	private void getCoordinateIndices()
	{
		int c=0;
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				if(upEdgeIndex[i][j]!=-1&&upEdgeIndex[i][j]==leftEdgeIndex[i][j])
				{
					this.coordinateIndices[c++]=upEdgeCoordinateIndices[i][j];
					this.coordinateIndices[c++]=leftEdgeCoordinateIndices[i][j];
				}
				if(leftEdgeIndex[i][j]!=-1&&leftEdgeIndex[i][j]==centerEdgeIndex[i][j])
				{
					this.coordinateIndices[c++]=leftEdgeCoordinateIndices[i][j];
					this.coordinateIndices[c++]=centerEdgeCoordinateIndices[i][j];
				}
				if(centerEdgeIndex[i][j]!=-1&&centerEdgeIndex[i][j]==upEdgeIndex[i][j])
				{
					this.coordinateIndices[c++]=centerEdgeCoordinateIndices[i][j];
					this.coordinateIndices[c++]=upEdgeCoordinateIndices[i][j];
				}
				if(upEdgeIndex[i+1][j]!=-1&&upEdgeIndex[i+1][j]==centerEdgeIndex[i][j])
				{
					this.coordinateIndices[c++]=upEdgeCoordinateIndices[i+1][j];
					this.coordinateIndices[c++]=centerEdgeCoordinateIndices[i][j];
				}
				if(centerEdgeIndex[i][j]!=-1&&centerEdgeIndex[i][j]==leftEdgeIndex[i][j+1])
				{
					this.coordinateIndices[c++]=centerEdgeCoordinateIndices[i][j];
					this.coordinateIndices[c++]=leftEdgeCoordinateIndices[i][j+1];
				}
				if(leftEdgeIndex[i][j+1]!=-1&&leftEdgeIndex[i][j+1]==upEdgeIndex[i+1][j])
				{
					this.coordinateIndices[c++]=leftEdgeCoordinateIndices[i][j+1];
					this.coordinateIndices[c++]=upEdgeCoordinateIndices[i+1][j];
				}
			}
		}
	}
	private int getYIndex(int i0,int j0,int i1,int j1)
	{
		for(int i=0;i<y.length;i++)if(yIsBetween(y[i],i0,j0,i1,j1))return i;
		return -1;
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
	public int n=50,m=50;
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
