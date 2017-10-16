import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndIsoSurface3D
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
		double r=1.2;
		Value3D value3D=new Value3D(new Function_Value4(),-r,r,-r,r,-r,r);
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0,1,0));
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(Material1);
		TransformGroup TransformGroup2=new TransformGroup();
		IsoSurface3D isoSurface3D=new IsoSurface3D(value3D,1.0,false,Appearance1);
		TransformGroup1.addChild(TransformGroup2);
		TransformGroup2.addChild(isoSurface3D);
		Transform3D Transform3d=new Transform3D();
		Transform3d.setScale(new Vector3d(0.999,0.999,0.999));
		TransformGroup TransformGroup3=new TransformGroup(Transform3d);
		isoSurface3D=new IsoSurface3D(value3D,1.0,true,Appearance1);
		TransformGroup1.addChild(TransformGroup3);
		TransformGroup3.addChild(isoSurface3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Function_Value1 implements Function
{
	public double value(double x,double y,double z)
	{
		double a=1.0,b=0.5,c=0.4;
		return x*x/(a*a)+y*y/(b*b)+z*z/(c*c);
	}
}
class Function_Value2 implements Function
{
	public double value(double x,double y,double z)
	{
		double a=1.0,b=0.5,c=0.4;
		return x*x/(a*a)-y*y/(b*b)+z*z/(c*c);
	}
}
class Function_Value3 implements Function
{
	public double value(double x,double y,double z)
	{
		double a=0.3,b=0.5,c=0.4;
		return x*x/(a*a)-y*y/(b*b)-z*z/(c*c);
	}
}
class Function_Value4 implements Function
{
	public double value(double x,double y,double z)
	{
		double a=0.3,b=0.5;
		return x*x/(a*a)+y*y/(b*b)-z;
	}
}
class Function_Value5 implements Function
{
	public double value(double x,double y,double z)
	{
		double a=0.3,b=0.5;
		return x*x/(a*a)-y*y/(b*b)-z;
	}
}
interface Function
{
	double value(double x,double y,double z);
}
class Value3D
{
	public double[] values;
	public Function function;
	public double x0,y0,z0,dx,dy,dz;
	public int level=60,row=60,column=60;
	public Value3D(Function function,double x0,double x1,double y0,double y1,double z0,double z1)
	{
		this.function=function;
		int l=level,m=row,n=column;
		this.values=new double[l*m*n];
		this.x0=x0;
		this.y0=y0;
		this.z0=z0;
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
					this.values[k*m*n+i*n+j]=function.value(x,y,z);
				}
			}
		}
	}
}
class IsoSurface3D extends Shape3D
{
	public int m,n,l;
	public double[] values;
	public Function function;
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
	public IsoSurface3D(Value3D value,double v,boolean inverseNormal,Appearance appearance)
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
		this.function=value.function;
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
		double d=this.dx+this.dy+this.dz;
		float dfx=(float)((function.value(x+d,y,z)-v)/d);
		float dfy=(float)((function.value(x,y+d,z)-v)/d);
		float dfz=(float)((function.value(x,y,z+d)-v)/d);
		Vector3f gradient=new Vector3f(k*dfx,k*dfy,k*dfz);
		gradient.normalize();
		return gradient;
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
}
