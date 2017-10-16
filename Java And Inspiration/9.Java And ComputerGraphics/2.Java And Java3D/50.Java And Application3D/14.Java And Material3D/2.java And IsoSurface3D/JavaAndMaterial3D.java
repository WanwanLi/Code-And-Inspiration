import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.image.*;
public class JavaAndMaterial3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background();
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
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		int row=128,column=256,level=128;double r=60;
		ColorSpace3D colorSpace3D=new ColorSpace3D(row,column,level);
		Color3f color0=new Color3f(160.0f/256,110.0f/256,70.0f/256);
		Color3f color1=new Color3f(230.0f/256,190.0f/256,140.0f/256);
		Wood3D wood3D=new Wood3D(color0,color1,-r,r,-r,r,-r,r,row,column,level);
		Color3f color2=new Color3f(175.0f/256,205.0f/256,210.0f/256);
		Color3f color3=new Color3f(0.0f/256,8.0f/256,5.0f/256);
		Marble3D marble3D=new Marble3D(color2,color3,-r,r,-r,r,-r,r,row,column,level);
		r=1.2;
		Value3D value3D=new Value3D(new Function_SuperEllipsoid(),-r,r,-r,r,-r,r);
		//TransformGroup1.addChild(new IsoSurface3D(value3D,1.0,false,colorSpace3D));
		//TransformGroup1.addChild(new IsoSurface3D(value3D,1.0,false,wood3D));
		TransformGroup1.addChild(new IsoSurface3D(value3D,1.0,false,marble3D));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class ColorSpace3D implements Material3D
{
	public int row,column,level;
	public ColorSpace3D(int row,int column,int level)
	{
		this.row=row;
		this.column=column;
		this.level=level;
	} 
	public int getRGB(int X,int Y,int Z)
	{
		double x=255.0*X/column;
		double y=255.0*Y/level;
		double z=255.0*Z/row;
		int red=(int)x,green=(int)y,blue=(int)z;
		return (red<<16)|(green<<8)|blue;
	}
	public int getRow(){return this.row;}
	public int getColumn(){return this.column;}
	public int getLevel(){return this.level;}
}
class Wood3D implements Material3D
{
	public int m=50,n=50,l=50;
	public int row,column,level;
	public Color3f color0,color1;
	public double x0,x1,y0,y1,z0,z1,dx,dy,dz;
	public Wood3D(Color3f color0,Color3f color1,double x0,double x1,double y0,double y1,double z0,double z1,int row,int column,int level)
	{
		this.m=row;
		this.n=column;
		this.l=level;
		this.row=row;
		this.column=column;
		this.level=level;
		this.color0=color0;
		this.color1=color1;
		this.x0=x0;this.x1=x1;
		this.y0=y0;this.y1=y1;
		this.z0=z0;this.z1=z1;
		this.dx=(x1-x0)/(n-1);
		this.dy=(y1-y0)/(l-1);
		this.dz=(z1-z0)/(m-1);
	}
	public Color3f interpolate(Color3f color0,Color3f color1,float k)
	{
		float red=color0.x*(1-k)+color1.x*k;
		float green=color0.y*(1-k)+color1.y*k;
		float blue=color0.z*(1-k)+color1.z*k;
		return new Color3f(red,green,blue);
	}
	public int toRGB(Color3f color)
	{
		int red=(int)(color.x*255);
		int green=(int)(color.y*255);
		int blue=(int)(color.z*255);
		return (red<<16)|(green<<8)|blue;
	}
	public int getRGB(int X,int Y,int Z)
	{
		double x=x0+X*dx;
		double y=y0+Y*dy;
		double z=z0+Z*dz;
		double a=20,b=150,A=0.2;
		double t=Math.atan(z/x);
		double r=Math.sqrt(x*x+z*z);
		double dr=A*Math.sin(a*t+y/b);
		double k=0.5*Math.cos(r+dr)+0.5;
		return toRGB(interpolate(color0,color1,(float)k));
	}
	public int getRow(){return this.row;}
	public int getColumn(){return this.column;}
	public int getLevel(){return this.level;}
}
class Marble3D implements Material3D
{
	public int[][][] grids;
	public int[][] gradientVectors;
	public int l=40,m=40,n=40;
	public int row,column,level;
	public Color3f color0,color1;
	public double x0,x1,y0,y1,z0,z1,dx,dy,dz,dX,dY,dZ;
	public Marble3D(Color3f color0,Color3f color1,double x0,double x1,double y0,double y1,double z0,double z1,int row,int column,int level)
	{
		this.row=row;
		this.column=column;
		this.level=level;
		this.color0=color0;
		this.color1=color1;
		this.x0=x0;this.x1=x1;
		this.y0=y0;this.y1=y1;
		this.z0=z0;this.z1=z1;
		this.dx=(x1-x0)/(column-1);
		this.dy=(y1-y0)/(level-1);
		this.dz=(z1-z0)/(row-1);
		this.dX=(x1-x0)/(n-1);
		this.dY=(y1-y0)/(l-1);
		this.dZ=(z1-z0)/(m-1);
		this.initGrids();
		this.initGradientVectors();
	}
	public void initGrids()
	{
		this.grids=new int[l][m][n];
		for(int k=0;k<l;k++)
		{
			for(int i=0;i<m;i++)
			{
				for(int j=0;j<n;j++)
				{
					int d=(int)(12.0*Math.random());
					this.grids[k][i][j]=d<12?d:11;
				}
			}
		}
	}
	public void initGradientVectors()
	{
		this.gradientVectors=new int[][]
		{
			{1,1,0},{-1,1,0},{1,-1,0},{-1,-1,0},
			{1,0,1},{-1,0,1},{1,0,-1},{-1,0,-1},
			{0,1,1},{0,-1,1},{0,1,-1},{0,-1,-1}
		};
	}
	public Color3f interpolate(Color3f color0,Color3f color1,float k)
	{
		float red=color0.x*(1-k)+color1.x*k;
		float green=color0.y*(1-k)+color1.y*k;
		float blue=color0.z*(1-k)+color1.z*k;
		return new Color3f(red,green,blue);
	}
	public int toRGB(Color3f color)
	{
		int red=(int)(color.x*255);
		int green=(int)(color.y*255);
		int blue=(int)(color.z*255);
		return (red<<16)|(green<<8)|blue;
	}
	public double interpolate1d(double g0,double g1,double u)
	{
		return (1.0-u)*g0+u*g1;
	}
	public double interpolate2d(double g00,double g01,double g10,double g11,double u,double v)
	{
		double g0=this.interpolate1d(g00,g01,v);
		double g1=this.interpolate1d(g10,g11,v);
		return this.interpolate1d(g0,g1,u);
	}
	public double interpolate3d(double g000,double g001,double g010,double g011,double g100,double g101,double g110,double g111,double u,double v,double w)
	{
		double g0=this.interpolate2d(g000,g001,g010,g011,u,v);
		double g1=this.interpolate2d(g100,g101,g110,g111,u,v);
		return this.interpolate1d(g0,g1,w);
	}
	public double dotGradDist(int k0,int i0,int j0,double k,double i,double j)
	{
		int d=this.grids[k0][i0][j0];
		int gradX=this.gradientVectors[d][0];
		int gradY=this.gradientVectors[d][1];
		int gradZ=this.gradientVectors[d][2];
		double distX=j-j0,distY=k-k0,distZ=i-i0;
		double dot=gradX*distX+gradY*distY+gradZ*distZ;
		return 0.5+0.5*dot/(1.414*1.732);
	}
	public int getRGB(int X,int Y,int Z)
	{
		double j=X*dx/dX;
		double k=Y*dy/dY;
		double i=Z*dz/dZ;
		int j0=(int)j,j1=j0+1;
		int k0=(int)k,k1=k0+1;
		int i0=(int)i,i1=i0+1;
		double v=j-j0,w=k-k0,u=i-i0;
		if(i1>=m)i1=m-1;if(j1>=n)j1=n-1;if(k1>=l)k1=l-1;
		double g000=this.dotGradDist(k0,i0,j0,k,i,j);
		double g001=this.dotGradDist(k0,i0,j1,k,i,j);
		double g010=this.dotGradDist(k0,i1,j0,k,i,j);
		double g011=this.dotGradDist(k0,i1,j1,k,i,j);
		double g100=this.dotGradDist(k1,i0,j0,k,i,j);
		double g101=this.dotGradDist(k1,i0,j1,k,i,j);
		double g110=this.dotGradDist(k1,i1,j0,k,i,j);
		double g111=this.dotGradDist(k1,i1,j1,k,i,j);
		double g=this.interpolate3d(g000,g001,g010,g011,g100,g101,g110,g111,u,v,w);
		return toRGB(interpolate(color0,color1,(float)g));
	}
	public int getRow(){return this.row;}
	public int getColumn(){return this.column;}
	public int getLevel(){return this.level;}
}
interface Material3D
{
	public int getRow();
	public int getColumn();
	public int getLevel();
	public int getRGB(int x,int y,int z);
}
class Function_Ellipsoid implements Function
{
	public double value(double x,double y,double z)
	{
		double a=1.0,b=0.5,c=0.4;
		return x*x/(a*a)+y*y/(b*b)+z*z/(c*c);
	}
}
class Function_SuperEllipsoid implements Function
{
	public double value(double x,double y,double z)
	{
		double n=6.0,a=1.0,b=0.5,c=0.4;
		double u=x/a,v=y/b,w=z/c;
		return Math.pow(u,n)+Math.pow(v,n)+Math.pow(w,n);
	}
}
class Function_QuadEllipsoid implements Function
{
	public double value(double x,double y,double z)
	{
		double a=1.0,b=0.5,c=0.4;
		double u=x/a,v=y/b,w=z/c;
		return Math.abs(u)+Math.abs(v)+Math.abs(w);
	}
}
class Function_SingleHyperboloid implements Function
{
	public double value(double x,double y,double z)
	{
		double a=1.0,b=0.5,c=0.4;
		return x*x/(a*a)-y*y/(b*b)+z*z/(c*c);
	}
}
class Function_DoubleHyperboloid implements Function
{
	public double value(double x,double y,double z)
	{
		double a=0.3,b=0.5,c=0.4;
		return x*x/(a*a)-y*y/(b*b)-z*z/(c*c);
	}
}
class Function_ParabolicHyperboloid implements Function
{
	public double value(double x,double y,double z)
	{
		double a=0.3,b=0.5;
		return x*x/(a*a)+y*y/(b*b)-z;
	}
}
class Function_EllipsoidHyperboloid implements Function
{
	public double value(double x,double y,double z)
	{
		double a=0.3,b=0.5;
		return x*x/(a*a)-y*y/(b*b)-z;
	}
}
class Function_KleinBottle implements Function
{
	public double value(double x,double y,double z)
	{
		double a=8.0,b=16.0;
		double w=x*x+y*y+z*z+2*y-1;
		return w*(w*w-a*z*z)+b*x*z*w;
	}
}
interface Function
{
	double value(double x,double y,double z);
}
class Value3D
{
	public double[] values;
	public double x0,y0,z0,dx,dy,dz;
	public int level=60,row=60,column=60;
	public Value3D(Function function,double x0,double x1,double y0,double y1,double z0,double z1)
	{
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
	public Vector3f[] normals;
	public Point3d[] coordinates;
	public TexCoord3f[] texCoords;
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
	public IsoSurface3D(Value3D value,double v,boolean inverseNormal,Material3D material3D)
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
		this.getTextureCoordinates();
		this.setGeometry(this.getGeometryArray());
		this.setAppearance(this.getTexture3DAppearance(material3D));
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
	private void getTextureCoordinates()
	{
		int length=this.coordinates.length;
		this.texCoords=new TexCoord3f[length];
		for(int i=0;i<length;i++)
		{
			double x=(this.coordinates[i].x-this.x0)/(this.dx*(this.n-1));
			double y=(this.coordinates[i].y-this.y0)/(this.dy*(this.l-1));
			double z=(this.coordinates[i].z-this.z0)/(this.dz*(this.m-1));
			this.texCoords[i]=new TexCoord3f((float)x,(float)y,(float)z);
		}
	}
	private GeometryArray getGeometryArray()
	{
		IndexedTriangleArray IndexedTriangleArray1=new IndexedTriangleArray
		(
			coordinates.length,
			IndexedTriangleArray.COORDINATES|
			IndexedTriangleArray.TEXTURE_COORDINATE_3|
			IndexedTriangleArray.NORMALS,
			coordinateIndices.length
		);
		IndexedTriangleArray1.setNormals(0,normals);
		IndexedTriangleArray1.setCoordinates(0,coordinates);
		IndexedTriangleArray1.setTextureCoordinates(0,0,texCoords);
		IndexedTriangleArray1.setNormalIndices(0,coordinateIndices);
		IndexedTriangleArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedTriangleArray1.setTextureCoordinateIndices(0,0,coordinateIndices);
		return IndexedTriangleArray1;
	}
	private Appearance getTexture3DAppearance(Material3D material3D)
	{
		int row=material3D.getRow();
		int column=material3D.getColumn();
		int level=material3D.getLevel();
		BufferedImage[] BufferedImages=new BufferedImage[row];
		for(int z=0;z<row;z++)
		{
			BufferedImages[z]=new BufferedImage(column,level,BufferedImage.TYPE_INT_ARGB);
			for(int y=0;y<level;y++)
			{
				for(int x=0;x<column;x++)
				{
					int RGB=material3D.getRGB(x,y,z);
					BufferedImages[z].setRGB(x,(level-1)-y,RGB);
				}
			}
		}
		ImageComponent3D imageComponent3D=new ImageComponent3D(ImageComponent3D.FORMAT_RGB,BufferedImages);
		Texture3D texture3D=new Texture3D(Texture3D.BASE_LEVEL,Texture3D.RGBA,imageComponent3D.getWidth(),imageComponent3D.getHeight(),imageComponent3D.getDepth());
		texture3D.setImage(0,imageComponent3D);
		Appearance appearance=new Appearance();
		appearance.setMaterial(new Material());
		appearance.setTexture(texture3D);
		TextureAttributes textureAttributes=new TextureAttributes();
		textureAttributes.setTextureMode(TextureAttributes.COMBINE);
		appearance.setTextureAttributes(textureAttributes);
		return appearance;
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
