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
		double[][][] values222=
		{
			{{1,0},	{1,0}},
			{{0,0},	{0,0}}
		};
		double r=1.2;
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0,1,0));
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(Material1);
		IsoSurface3D isoSurface3D=new IsoSurface3D(values222,0.5,-r,r,-r,r,-r,r,Appearance1);
		TransformGroup1.addChild(isoSurface3D);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(2f);
		LineAttributes1.setLineAntialiasingEnable(true);
		Appearance Appearance2=new Appearance();
		Appearance2.setLineAttributes(LineAttributes1);
		Cell3D cell3D=new Cell3D(values222,-r,r,-r,r,-r,r,Appearance2);
		TransformGroup1.addChild(cell3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}

class Function_Sphere implements Function
{
	public double value(double x,double y,double z)
	{
		double a=2.59,b=1.2,c=1.1;
		return x*x/a-y*y/b+z*z/c;
	}
}
interface Function
{
	double value(double x,double y,double z);
}
class IsoSurface3D extends Shape3D
{
	public int m,n,l;
	public Point3d[] coordinates;
	public int[] coordinateIndices;
	public double v;
	public double[] values;
	public Point3d[] valueCoordinates;
	public int[][][] upEdgeCoordinateIndices;
	public int[][][] leftEdgeCoordinateIndices;
	public int[][][] frontEdgeCoordinateIndices;
	public int[][][] upCenterEdgeCoordinateIndices;
	public int[][][] leftCenterEdgeCoordinateIndices;
	public int[][][] frontCenterEdgeCoordinateIndices;
	public boolean[][][] isNotOnIsoSurface;
	public IsoSurface3D(double[][][] values,double v,double x0,double x1,double y0,double y1,double z0,double z1,Appearance appearance)
	{
		this.v=v;
		this.getValueCoordinates(values,x0,x1,y0,y1,z0,z1);
		this.isNotOnIsoSurface=new boolean[l][m][n];
		this.upEdgeCoordinateIndices=new int[l][m][n];
		this.leftEdgeCoordinateIndices=new int[l][m][n];
		this.frontEdgeCoordinateIndices=new int[l][m][n];
		this.upCenterEdgeCoordinateIndices=new int[l][m][n];
		this.leftCenterEdgeCoordinateIndices=new int[l][m][n];
		this.frontCenterEdgeCoordinateIndices=new int[l][m][n];
		this.coordinates=new Point3d[this.getCoordinatesCount()];
		this.coordinateIndices=new int[this.getCoordinateIndicesCount()];
		this.getCoordinates();
		this.getCoordinateIndices();
		this.setGeometry(this.getGeometryArray());
		this.setAppearance(appearance);
	}
	private void getValueCoordinates(double[][][] values,double x0,double x1,double y0,double y1,double z0,double z1)
	{
		this.l=values.length;
		this.m=values[0].length;
		this.n=values[0][0].length;
		this.values=new double[l*m*n];
		this.valueCoordinates=new Point3d[l*m*n];
		double dx=(x1-x0)/(n-1);
		double dy=(y1-y0)/(l-1);
		double dz=(z1-z0)/(m-1);
		for(int k=0;k<l;k++)
		{
			double y=y0+k*dy;
			for(int i=0;i<m;i++)
			{
				double z=z0+i*dz;
				for(int j=0;j<n;j++)
				{
					double x=x0+j*dx;
					this.values[k*m*n+i*n+j]=values[k][i][j];
					this.valueCoordinates[k*m*n+i*n+j]=new Point3d(x,y,z);
				}
			}
		}
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
					if(vIsBetween(v,k,i,j,k,i,j+1))this.upEdgeCoordinateIndices[k][i][j]=c++;
					else this.upEdgeCoordinateIndices[k][i][j]=-1;
					if(vIsBetween(v,k,i,j,k,i+1,j))this.leftEdgeCoordinateIndices[k][i][j]=c++;
					else this.leftEdgeCoordinateIndices[k][i][j]=-1;
					if(vIsBetween(v,k,i,j,k+1,i,j))this.frontEdgeCoordinateIndices[k][i][j]=c++;
					else this.frontEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==1)
					{
						if(vIsBetween(v,k,i,j,k,i+1,j+1))this.upCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.upCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(vIsBetween(v,k,i,j,k+1,i+1,j))this.leftCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.leftCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(vIsBetween(v,k,i,j,k+1,i,j+1))this.frontCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.frontCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(vIsBetween(v,k,i+1,j,k,i,j+1))this.upCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.upCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(vIsBetween(v,k+1,i,j,k,i+1,j))this.leftCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.leftCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(vIsBetween(v,k+1,i,j,k,i,j+1))this.frontCenterEdgeCoordinateIndices[k][i][j]=c++;
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
					if(vIsBetween(v,k,i,j,k,i,j+1))this.upEdgeCoordinateIndices[k][i][j]=c++;
					else this.upEdgeCoordinateIndices[k][i][j]=-1;
					if(vIsBetween(v,k,i,j,k,i+1,j))this.leftEdgeCoordinateIndices[k][i][j]=c++;
					else this.leftEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==1)
					{
						if(vIsBetween(v,k,i,j,k,i+1,j+1))this.upCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.upCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(vIsBetween(v,k,i+1,j,k,i,j+1))this.upCenterEdgeCoordinateIndices[k][i][j]=c++;
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
					if(vIsBetween(v,k,i,j,k,i,j+1))this.upEdgeCoordinateIndices[k][i][j]=c++;
					else this.upEdgeCoordinateIndices[k][i][j]=-1;
					if(vIsBetween(v,k,i,j,k+1,i,j))this.frontEdgeCoordinateIndices[k][i][j]=c++;
					else this.frontEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==1)
					{
						if(vIsBetween(v,k,i,j,k+1,i,j+1))this.frontCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.frontCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(vIsBetween(v,k+1,i,j,k,i,j+1))this.frontCenterEdgeCoordinateIndices[k][i][j]=c++;
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
					if(vIsBetween(v,k,i,j,k,i+1,j))this.leftEdgeCoordinateIndices[k][i][j]=c++;
					else this.leftEdgeCoordinateIndices[k][i][j]=-1;
					if(vIsBetween(v,k,i,j,k+1,i,j))this.frontEdgeCoordinateIndices[k][i][j]=c++;
					else this.frontEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==1)
					{
						if(vIsBetween(v,k,i,j,k+1,i+1,j))this.leftCenterEdgeCoordinateIndices[k][i][j]=c++;
						else this.leftCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(vIsBetween(v,k+1,i,j,k,i+1,j))this.leftCenterEdgeCoordinateIndices[k][i][j]=c++;
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
					if(vIsBetween(v,k,i,j,k+1,i,j))this.frontEdgeCoordinateIndices[k][i][j]=c++;
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
					if(vIsBetween(v,k,i,j,k,i+1,j))this.leftEdgeCoordinateIndices[k][i][j]=c++;
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
					if(vIsBetween(v,k,i,j,k,i,j+1))this.upEdgeCoordinateIndices[k][i][j]=c++;
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
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k,i,j+1);
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k,i+1,j);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k+1,i,j);
					if((i+j+k)%2==1)
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k,i+1,j+1);
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k+1,i+1,j);
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k+1,i,j+1);
					}
					else
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i+1,j,k,i,j+1);
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k+1,i,j,k,i+1,j);
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k+1,i,j,k,i,j+1);
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
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k,i,j+1);
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k,i+1,j);
					if((i+j+k)%2==1)
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k,i+1,j+1);
					}
					else
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i+1,j,k,i,j+1);
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
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k,i,j+1);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k+1,i,j);
					if((i+j+k)%2==1)
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k+1,i,j+1);
					}
					else
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k+1,i,j,k,i,j+1);
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
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k,i+1,j);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k+1,i,j);
					if((i+j+k)%2==1)
					{
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k+1,i+1,j);
					}
					else
					{
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k+1,i,j,k,i+1,j);
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
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k+1,i,j);
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k,i+1,j);
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)this.coordinates[c++]=this.getMidPoint(v,k,i,j,k,i,j+1);
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
						if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
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
					}
				}
			}
		}
	}
	private GeometryArray getGeometryArray()
	{
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		return GeometryInfo1.getGeometryArray();
	}
	private boolean vIsBetween(double v,int k0,int i0,int j0,int k1,int i1,int j1)
	{
		boolean l0=values[k0*m*n+i0*n+j0]<v;
		boolean l1=v<values[k1*m*n+i1*n+j1];
		boolean g0=values[k0*m*n+i0*n+j0]>v;
		boolean g1=v>values[k1*m*n+i1*n+j1];
		return (l0&&l1)||(g0&&g1);
	}
	private Point3d getMidPoint(double v,int k0,int i0,int j0,int k1,int i1,int j1)
	{
		double x0=valueCoordinates[k0*m*n+i0*n+j0].x;
		double y0=valueCoordinates[k0*m*n+i0*n+j0].y;
		double z0=valueCoordinates[k0*m*n+i0*n+j0].z;
		double x1=valueCoordinates[k1*m*n+i1*n+j1].x;
		double y1=valueCoordinates[k1*m*n+i1*n+j1].y;
		double z1=valueCoordinates[k1*m*n+i1*n+j1].z;
		double v0=values[k0*m*n+i0*n+j0];
		double v1=values[k1*m*n+i1*n+j1];
		double dx=x1-x0,dy=y1-y0,dz=z1-z0;
		double x=x0+dx*(v-v0)/(v1-v0);
		double y=y0+dy*(v-v0)/(v1-v0);
		double z=z0+dz*(v-v0)/(v1-v0);
		return new Point3d(x,y,z);
	}
}
class Cell3D extends Shape3D
{
	public int l,m,n;
	public Point3d[] coordinates;
	public int[] coordinateIndices;
	public Color3f[] colors;
	public double[] values;
	public double maxValue=-Double.MAX_VALUE;
	public double minValue=Double.MAX_VALUE;
	public Cell3D(double[][][] values,double x0,double x1,double y0,double y1,double z0,double z1,Appearance appearance)
	{
		this.l=values.length;
		this.m=values[0].length;
		this.n=values[0][0].length;
		this.coordinates=new Point3d[l*m*n];
		this.coordinateIndices=new int[(l-1)*(m-1)*(n-1)*2*6+(m-1)*(n-1)*2*3+(l-1)*(n-1)*2*3+(l-1)*(m-1)*2*3+(l-1)*2+(m-1)*2+(n-1)*2];
		this.colors=new Color3f[l*m*n];
		this.values=new double[l*m*n];
		double dx=(x1-x0)/(n-1);
		double dy=(y1-y0)/(l-1);
		double dz=(z1-z0)/(m-1);
		for(int k=0;k<l;k++)
		{
			double y=y0+k*dy;
			for(int i=0;i<m;i++)
			{
				double z=z0+i*dz;
				for(int j=0;j<n;j++)
				{
					double x=x0+j*dx;
					this.coordinates[k*m*n+i*n+j]=new Point3d(x,y,z);
					this.values[k*m*n+i*n+j]=values[k][i][j];
					if(values[k][i][j]>maxValue)maxValue=values[k][i][j];
					if(values[k][i][j]<minValue)minValue=values[k][i][j];
				}
			}
		}
		int c=0;
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);

					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);

					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);
					if((i+j+k)%2==1)
					{
						this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
						this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+1);

						this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
						this.coordinateIndices[c++]=(k+1)*m*n+(i+1)*n+(j+0);

						this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
						this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+1);
					}
					else
					{
						this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);
						this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);

						this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);
						this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);

						this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);
						this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);
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
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);

					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);

					if((i+j+k)%2==1)
					{
						this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
						this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+1);
					}
					else
					{
						this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);
						this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);
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
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);

					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);

					if((i+j+k)%2==1)
					{
						this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
						this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+1);
					}
					else
					{
						this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);
						this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);
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
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);

					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);

					if((i+j+k)%2==1)
					{
						this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
						this.coordinateIndices[c++]=(k+1)*m*n+(i+1)*n+(j+0);
					}
					else
					{
						this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);
						this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);
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
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);
				}
			}
		}
		for(int k=0;k<l;k++)for(int i=0;i<m;i++)for(int j=0;j<n;j++)this.colors[k*m*n+i*n+j]=this.getColor(i,j,k);
		this.setGeometry(this.getIndexedLineArray());
		this.setAppearance(appearance);
	}
	private Color3f getColor(int i,int j,int k)
	{
		double red=0,green=0,blue=0;
		double hue=0.5*(values[k*m*n+i*n+j]-minValue)/(maxValue-minValue);
		double H=6*hue%6;
		int n=(int)H;
		double h=H-n;
		double m=n%2==0?h:1-h;
		switch(n)
		{
			case 0:red=1;green=m;blue=0;break;
			case 1:red=m;green=1;blue=0;break;
			case 2:red=0;green=1;blue=m;break;
			case 3:red=0;green=m;blue=1;break;
			case 4:red=m;green=0;blue=1;break;
			case 5:red=1;green=0;blue=m;break;
		}
		return new Color3f((float)red,(float)green,(float)blue);
	}
	private GeometryArray getIndexedLineArray()
	{
		IndexedLineArray IndexedLineArray1=new IndexedLineArray(coordinates.length,IndexedLineArray.COORDINATES|PointArray.COLOR_3,coordinateIndices.length);
		IndexedLineArray1.setCoordinates(0,coordinates);
		IndexedLineArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedLineArray1.setColors(0,colors);
		IndexedLineArray1.setColorIndices(0,coordinateIndices);
		return IndexedLineArray1;
	}
}