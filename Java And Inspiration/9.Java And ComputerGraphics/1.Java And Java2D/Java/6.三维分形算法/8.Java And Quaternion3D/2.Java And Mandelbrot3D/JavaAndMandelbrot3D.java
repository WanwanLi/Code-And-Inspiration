import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndMandelbrot3D
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
		double r=1.2,root=4.0,zoom=0.2,o=1;int size=5;
		Mandelbrot3D mandelbrot3D=new Mandelbrot3D(-1.5*r,0.5*r,-r,r,-r,r,root);
		mandelbrot3D.filter(mandelbrot3D.getGaussFilter(zoom,o,size,size,size));
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0,1,0));
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(Material1);
		IsoSurface3D isoSurface3D=new IsoSurface3D(mandelbrot3D,0.7,false,Appearance1);
		TransformGroup1.addChild(isoSurface3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Mandelbrot3D
{
	public double[] values;
	public double x0,y0,z0,dx,dy,dz;
	double p=-1.0,q=0.2,s=0,v=0,t=0,root;
	public int level=100,row=100,column=150;
	public Mandelbrot3D(double x0,double x1,double y0,double y1,double z0,double z1,double root)
	{
		int l=level,m=row,n=column;
		this.values=new double[l*m*n];
		this.x0=x0;
		this.y0=y0;
		this.z0=z0;
		this.root=root;
		this.dx=(x1-x0)/(n-1);
		this.dy=(y1-y0)/(l-1);
		this.dz=(z1-z0)/(m-1);
		for(int k=0;k<l;k++)
		{
			double y=y0+k*dy;
			for(int i=0;i<m;i++)
			{
				double z=z0+i*dz;
				for(int j=0;j<n;j++)
				{
					double x=x0+j*dx;
					this.values[k*m*n+i*n+j]=this.Mandelbrot(x,y,z,t);
				}
			}
		}
	}
	public double Mandelbrot(double p,double q,double s,double v)
	{
		double x0=0f,y0=0f,z0=0f,t0=0f;
		double x,y,z,t;
		for(int i=0;i<100;i++)
		{
		    	x=x0*x0-y0*y0-z0*z0-t0*t0+p;
			y=2*x0*y0+q;
			z=2*x0*z0+s;
			t=2*x0*t0+v;
			if (x*x+y*y+z*z+t*t>root)return 1.0;		         
			x0=x;
			y0=y;
			z0=z;
			t0=t;
		}
		return 0.0;	
	}
	public void filter(double[][][] Filter)
	{
		int level=Filter.length;
		int row=Filter[0].length;
		int column=Filter[0][0].length;
		if(level%2==0)level++;
		if(row%2==0)row++;
		if(column%2==0)column++;
		int r=row/2,c=column/2,l=level/2;
		int width=this.column, height=this.level, depth=this.row;
		double[] newValues=new double[width*height*depth];
		for(int k=l;k<height-l;k++)
		{
			for(int i=r;i<depth-r;i++)
			{
				for(int j=c;j<width-c;j++)
				{
					this.values[k*depth*width+i*width+j]=this.getFilterValue(Filter,l,r,c,k,i,j);
				}
			}
		}
	}
	private double getFilterValue(double[][][] filter,int l,int r,int c,int k,int i,int j)
	{
		int t0=k-l,t1=k+l;
		int u0=i-r,u1=i+r;
		int v0=j-c,v1=j+c;
		int depth=this.row;
		int height=this.level;
		int width=this.column;
		double value=0.0,weight=0.0;
		if(t0<0||u0<0||v0<0||t1>=height||u1>=depth||v1>=width)
		{
			return this.values[k*depth*width+i*width+j];
		}
		for(int t=t0;t<=t1;t++)
		{
			for(int u=u0;u<=u1;u++)
			{
				for(int v=v0;v<=v1;v++)
				{
					value+=this.values[t*depth*width+u*width+v]*filter[t-t0][u-u0][v-v0];
					weight+=filter[t-t0][u-u0][v-v0];
				}
			}
		}
		return value/weight;
	}
	public double Gauss(double x,double y,double z,double o)
	{
		return Math.exp(-(x*x+y*y+z*z)/(o*o))/Math.pow(o*Math.sqrt(2*Math.PI),3);
	}
	public double[][][] getGaussFilter(double zoom,double o,int level,int row,int column)
	{
		if(row%2==0)row++;
		if(level%2==0)level++;
		if(column%2==0)column++;
		double max=zoom*o,min=-max;
		double dx=(max-min)/(column-1);
		double dy=(max-min)/(level-1);
		double dz=(max-min)/(row-1);
		double[][][] GaussFilter=new double[level][row][column];
		for(int k=0;k<level;k++)
		{
			double y=min+k*dy;
			for(int i=0;i<row;i++)
			{
				double z=min+i*dz;
				for(int j=0;j<column;j++)
				{
					double x=min+j*dx;
					GaussFilter[k][i][j]=Gauss(x,y,z,o);
				}
			}
		}
		return GaussFilter;
	}
}
class IsoSurface3D extends Shape3D
{
	public int m,n,l;
	public double[] values;
	public Vector3f[] normals;
	public Point3d[] coordinates;
	public int[] coordinateIndices;
	public boolean inverseNormal;
	public double v,x0,y0,z0,dx,dy,dz;
	public boolean[][][] isNotOnIsoSurface;
	public int[][][] upEdgeCoordinateIndices;
	public int[][][] leftEdgeCoordinateIndices;
	public int[][][] frontEdgeCoordinateIndices;
	public int[][][] upCenterEdgeCoordinateIndices;
	public int[][][] leftCenterEdgeCoordinateIndices;
	public int[][][] frontCenterEdgeCoordinateIndices;
	public IsoSurface3D(Mandelbrot3D value,double v,boolean inverseNormal,Appearance appearance)
	{
		this.v=v;
		this.x0=value.x0;
		this.y0=value.y0;
		this.z0=value.z0;
		this.dx=value.dx;
		this.dy=value.dy;
		this.dz=value.dz;
		this.l=value.level;
		this.m=value.row;
		this.n=value.column;
		this.values=value.values;
		this.inverseNormal=inverseNormal;
		this.isNotOnIsoSurface=new boolean[l][m][n];
		this.upEdgeCoordinateIndices=new int[l][m][n];
		this.leftEdgeCoordinateIndices=new int[l][m][n];
		this.frontEdgeCoordinateIndices=new int[l][m][n];
		this.upCenterEdgeCoordinateIndices=new int[l][m][n];
		this.leftCenterEdgeCoordinateIndices=new int[l][m][n];
		this.frontCenterEdgeCoordinateIndices=new int[l][m][n];
		this.coordinates=new Point3d[this.getCoordinatesCount()];
		this.coordinateIndices=new int[this.getCoordinateIndicesCount()];
		this.normals=new Vector3f[this.coordinates.length];
		this.getNormals();
		this.getCoordinates();
		this.getCoordinateIndices();
		this.setGeometry(this.getGeometryArray());
		this.setAppearance(appearance);
	}
	private int getCoordinatesCount()
	{
		int c=0;
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(isBetween(k,i,j,k,i,j+1))this.upEdgeCoordinateIndices[k][i][j]=c++;
					else this.upEdgeCoordinateIndices[k][i][j]=-1;
					if(isBetween(k,i,j,k,i+1,j))this.leftEdgeCoordinateIndices[k][i][j]=c++;
					else this.leftEdgeCoordinateIndices[k][i][j]=-1;
					if(isBetween(k,i,j,k+1,i,j))this.frontEdgeCoordinateIndices[k][i][j]=c++;
					else this.frontEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==0)
					{
						if(isBetween(k,i,j,k,i+1,j+1))this.upCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.upCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(isBetween(k,i,j,k+1,i+1,j))this.leftCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.leftCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(isBetween(k,i,j,k+1,i,j+1))this.frontCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.frontCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(isBetween(k,i+1,j,k,i,j+1))this.upCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.upCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(isBetween(k+1,i,j,k,i+1,j))this.leftCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.leftCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(isBetween(k+1,i,j,k,i,j+1))this.frontCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.frontCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(isBetween(k,i,j,k,i,j+1))this.upEdgeCoordinateIndices[k][i][j]=c++;
					else this.upEdgeCoordinateIndices[k][i][j]=-1;
					if(isBetween(k,i,j,k,i+1,j))this.leftEdgeCoordinateIndices[k][i][j]=c++;
					else this.leftEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==0)
					{
						if(isBetween(k,i,j,k,i+1,j+1))this.upCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.upCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(isBetween(k,i+1,j,k,i,j+1))this.upCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.upCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(isBetween(k,i,j,k,i,j+1))this.upEdgeCoordinateIndices[k][i][j]=c++;
					else this.upEdgeCoordinateIndices[k][i][j]=-1;
					if(isBetween(k,i,j,k+1,i,j))this.frontEdgeCoordinateIndices[k][i][j]=c++;
					else this.frontEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==0)
					{
						if(isBetween(k,i,j,k+1,i,j+1))this.frontCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.frontCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(isBetween(k+1,i,j,k,i,j+1))this.frontCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.frontCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					if(isBetween(k,i,j,k,i+1,j))this.leftEdgeCoordinateIndices[k][i][j]=c++;
					else this.leftEdgeCoordinateIndices[k][i][j]=-1;
					if(isBetween(k,i,j,k+1,i,j))this.frontEdgeCoordinateIndices[k][i][j]=c++;
					else this.frontEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==0)
					{
						if(isBetween(k,i,j,k+1,i+1,j))this.leftCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.leftCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(isBetween(k+1,i,j,k,i+1,j))this.leftCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.leftCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					if(isBetween(k,i,j,k+1,i,j))this.frontEdgeCoordinateIndices[k][i][j]=c++;
					else this.frontEdgeCoordinateIndices[k][i][j]=-1;
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					if(isBetween(k,i,j,k,i+1,j))this.leftEdgeCoordinateIndices[k][i][j]=c++;
					else this.leftEdgeCoordinateIndices[k][i][j]=-1;
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(isBetween(k,i,j,k,i,j+1))this.upEdgeCoordinateIndices[k][i][j]=c++;
					else this.upEdgeCoordinateIndices[k][i][j]=-1;
				}
			}
		}
		return c;
	}
	private int getCoordinates()
	{
		int c=0;
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k,i,j+1);
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k,i+1,j);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k+1,i,j);
					if((i+j+k)%2==0)
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k,i+1,j+1);
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k+1,i+1,j);
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k+1,i,j+1);
					}
					else
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i+1,j,k,i,j+1);
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k+1,i,j,k,i+1,j);
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k+1,i,j,k,i,j+1);
					}
					
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k,i,j+1);
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k,i+1,j);
					if((i+j+k)%2==0)
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k,i+1,j+1);
					}
					else
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i+1,j,k,i,j+1);
					}
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k,i,j+1);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k+1,i,j);
					if((i+j+k)%2==0)
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k+1,i,j+1);
					}
					else
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k+1,i,j,k,i,j+1);
					}
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k,i+1,j);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k+1,i,j);
					if((i+j+k)%2==0)
					{
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k+1,i+1,j);
					}
					else
					{
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k+1,i,j,k,i+1,j);
					}
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k+1,i,j);
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k,i+1,j);
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(k,i,j,k,i,j+1);
				}
			}
		}
		return c;
	}
	private int getCoordinateIndicesCount()
	{
		int c=0;
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					int b=0;
					if((i+j+k)%2==0)
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1)c+=12;
						else b++;
						if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1)c+=12;
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else b++;
						if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=12;
						else b++;
						if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=12;
						else b++;
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=12;
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=12;
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=12;
						else b++;
					}
					else
					{
						if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else b++;
						if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else b++;
						if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=12;
						else b++;
						if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=6;
						else if(leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=12;
						else b++;
						if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=6;
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else b++;
					}
					this.isNotOnIsoSurface[k][i][j]=b==5?true:false;
				}
			}
		}
		return c;
	}
	private void getCoordinateIndices()
	{
		int c=0;
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(isNotOnIsoSurface[k][i][j])continue;
					if((i+j+k)%2==0)
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
					}
					else
					{
						if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							this.coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							this.coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							this.coordinateIndices[c++]=this.coordinateIndices[c-2];
							this.coordinateIndices[c++]=this.coordinateIndices[c-4];
							this.coordinateIndices[c++]=this.coordinateIndices[c-6];
						}
					}
				}
			}
		}
	}
	private int getNormals()
	{
		int c=0;
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k,i,j+1);
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k,i+1,j);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k+1,i,j);
					if((i+j+k)%2==0)
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k,i+1,j+1);
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k+1,i+1,j);
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k+1,i,j+1);
					}
					else
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i+1,j,k,i,j+1);
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k+1,i,j,k,i+1,j);
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k+1,i,j,k,i,j+1);
					}
					
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k,i,j+1);
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k,i+1,j);
					if((i+j+k)%2==0)
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k,i+1,j+1);
					}
					else
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i+1,j,k,i,j+1);
					}
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k,i,j+1);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k+1,i,j);
					if((i+j+k)%2==0)
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k+1,i,j+1);
					}
					else
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k+1,i,j,k,i,j+1);
					}
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k,i+1,j);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k+1,i,j);
					if((i+j+k)%2==0)
					{
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k+1,i+1,j);
					}
					else
					{
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k+1,i,j,k,i+1,j);
					}
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k+1,i,j);
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k,i+1,j);
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.normals[c++]=this.getGradVector(k,i,j,k,i,j+1);
				}
			}
		}
		return c;
	}
	private GeometryArray getGeometryArray()
	{
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
		GeometryInfo1.setNormals(normals);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setNormalIndices(coordinateIndices);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		return GeometryInfo1.getGeometryArray();
	}
	private boolean isBetween(int k0,int i0,int j0,int k1,int i1,int j1)
	{
		boolean l0=values[k0*m*n+i0*n+j0]<v;
		boolean l1=v<values[k1*m*n+i1*n+j1];
		boolean g0=values[k0*m*n+i0*n+j0]>v;
		boolean g1=v>values[k1*m*n+i1*n+j1];
		return (l0&&l1)||(g0&&g1);
	}
	private Point3d getMidPoint(int k0,int i0,int j0,int k1,int i1,int j1)
	{
		double x0=xCoordinate(j0);
		double y0=yCoordinate(k0);
		double z0=zCoordinate(i0);
		double x1=xCoordinate(j1);
		double y1=yCoordinate(k1);
		double z1=zCoordinate(i1);
		double v0=values[k0*m*n+i0*n+j0];
		double v1=values[k1*m*n+i1*n+j1];
		double dx=x1-x0,dy=y1-y0,dz=z1-z0;
		double x=x0+dx*(v-v0)/(v1-v0);
		double y=y0+dy*(v-v0)/(v1-v0);
		double z=z0+dz*(v-v0)/(v1-v0);
		return new Point3d(x,y,z);
	}
	private Vector3f getGradVector(int k0,int i0,int j0,int k1,int i1,int j1)
	{
		double x0=xCoordinate(j0);
		double y0=yCoordinate(k0);
		double z0=zCoordinate(i0);
		double x1=xCoordinate(j1);
		double y1=yCoordinate(k1);
		double z1=zCoordinate(i1);
		double v0=values[k0*m*n+i0*n+j0];
		double v1=values[k1*m*n+i1*n+j1];
		double dx=x1-x0,dy=y1-y0,dz=z1-z0;
		double x=x0+dx*(v-v0)/(v1-v0);
		double y=y0+dy*(v-v0)/(v1-v0);
		double z=z0+dz*(v-v0)/(v1-v0);
		return newGradient3f(x,y,z);
	}
	private Vector3f newGradient3f(double x,double y,double z)
	{
		int k=inverseNormal?-1:1;
		double d=(this.dx+this.dy+this.dz)/3.0;
		float dfx=(float)((getValue(x+d,y,z)-v)/d);
		float dfy=(float)((getValue(x,y+d,z)-v)/d);
		float dfz=(float)((getValue(x,y,z+d)-v)/d);
		Vector3f gradient=new Vector3f(k*dfx,k*dfy,k*dfz);
		gradient.normalize();
		return gradient;
	}
	private double getValue(double x,double y,double z)
	{
		int i0=iCoordinate(z);
		int j0=jCoordinate(x);
		int k0=kCoordinate(y);
		int i1=i0+1<m-1?i0+1:m-1;
		int j1=j0+1<n-1?j0+1:n-1;
		int k1=k0+1<l-1?k0+1:l-1;
		double v000=values[k0*m*n+i0*n+j0];
		double v001=values[k0*m*n+i0*n+j1];
		double v010=values[k0*m*n+i1*n+j0];
		double v011=values[k0*m*n+i1*n+j1];
		double v100=values[k1*m*n+i0*n+j0];
		double v101=values[k1*m*n+i0*n+j1];
		double v110=values[k1*m*n+i1*n+j0];
		double v111=values[k1*m*n+i1*n+j1];
		double a1=(y-(y0+k0*dy))/dy,a0=1-a1;
		double b1=(z-(z0+i0*dz))/dz,b0=1-b1;
		double c1=(x-(x0+j0*dx))/dx,c0=1-c1;
		double value=0.0;
		value+=a0*b0*c0*v000;
		value+=a0*b0*c1*v001;
		value+=a0*b1*c0*v010;
		value+=a0*b1*c1*v011;
		value+=a1*b0*c0*v100;
		value+=a1*b0*c1*v101;
		value+=a1*b1*c0*v110;
		value+=a1*b1*c1*v111;
		return value;
	}
	private double xCoordinate(int j)
	{
		return this.x0+this.dx*j;
	}
	private double yCoordinate(int k)
	{
		return this.y0+this.dy*k;
	}
	private double zCoordinate(int i)
	{
		return this.z0+this.dz*i;
	}
	private int iCoordinate(double z)
	{
		int i=(int)((z-this.z0)/this.dz);
		if(i>this.m-2)i=this.m-2;
		return i;
	}
	private int jCoordinate(double x)
	{
		int j=(int)((x-this.x0)/this.dx);
		if(j>this.n-2)j=this.n-2;
		return j;
	}
	private int kCoordinate(double y)
	{
		int k=(int)((y-this.y0)/this.dy);
		if(k>this.l-2)k=this.l-2;
		return k;
	}
}
