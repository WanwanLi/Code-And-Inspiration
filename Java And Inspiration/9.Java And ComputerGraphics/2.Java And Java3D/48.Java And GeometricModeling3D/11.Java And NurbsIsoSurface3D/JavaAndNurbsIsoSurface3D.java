import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndNurbsIsoSurface3D
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
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom();
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate();
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,1f));
		Appearance1.setMaterial(Material1);
		double value=1;
		double[][][] ctrlValues=
		{
			{
				{2,2,2},
				{2,2,2},
				{2,2,2}
			},
			{
				{2,2,2},
				{2,-100,2},
				{2,2,2}
			},
			{
				{2,2,2},
				{2,2,2},
				{2,2,2}
			}
		};
		double[][][] weights=
		{
			{
				{100,1,1},
				{1,1,1},
				{1,1,1}
			},
			{
				{1,1,1},
				{1,1,1},
				{1,1,100}
			},
			{
				{1,1,1},
				{1,1,1},
				{1,1,1}
			}
		};
	//	int order=1;double[] knots={0.0,0.0,0.2,0.4,0.6,0.8,1.0,1.0};
	//	int order=2;double[] knots={0.0,0.0,0.0,0.25,0.5,0.75,1.0,1.0,1.0};
	//	int order=3;double[] knots={0.0,0.0,0.0,0.0,0.33,0.66,1.0,1.0,1.0,1.0};
	//	int order=4;double[] knots={0.0,0.0,0.0,0.0,0.0,0.5,1.0,1.0,1.0,1.0,1.0};
	//	int order=5;double[] knots={0.0,0.0,0.0,0.0,0.0,0.0,1.0,1.0,1.0,1.0,1.0,1.0};
		int vOrder=2;double[] vKnots=NurbsIsoSurface3D.getBezierUniformNurbsKnots(ctrlValues[0][0].length,vOrder);
	//	int vOrder=3;double[] vKnots=NurbsIsoSurface3D.getStandardUniformNurbsKnots(ctrlPoints[0].length,vOrder);
	//	int uOrder=1;double[] uKnots={0.0,0.0,0.5,1.0,1.0};
		int uOrder=2;double[] uKnots={0.0,0.0,0.0,1.0,1.0,1.0};
		int wOrder=2;double[] wKnots={0.0,0.0,0.0,1.0,1.0,1.0};
		double x0=-1.2,x1=1.2,y0=-1.2,y1=1.2,z0=-1.2,z1=1.2;
		Point3d point0=new Point3d(x0,y0,z0),point1=new Point3d(x1,y1,z1);
		NurbsIsoSurface3D nurbsIsoSurface3D=new NurbsIsoSurface3D(point0,point1,value,ctrlValues,weights,uKnots,vKnots,wKnots,uOrder,vOrder,wOrder);
		nurbsIsoSurface3D.setAppearance(Appearance1);
		TransformGroup1.addChild(nurbsIsoSurface3D);
		TransformGroup1.addChild(new Cell3D(ctrlValues,x0,x1,y0,y1,z0,z1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class NurbsIsoSurface3D extends Shape3D
{
	public NurbsIsoSurface3D(Point3d point0,Point3d point1,double value,double[][][] ctrlValues,double[][][] weights,double[] uKnots,double[] vKnots,double[] wKnots,int uOrder,int vOrder,int wOrder)
	{
		int step=50,h=step,r=step,c=step,v=0,l=ctrlValues.length,m=ctrlValues[0].length,n=ctrlValues[0][0].length;
		double[][] U=this.getParameterMatrix(uKnots,r,uOrder,m);
		double[][] V=this.getParameterMatrix(vKnots,c,vOrder,n);
		double[][] W=this.getParameterMatrix(wKnots,h,wOrder,l);
		double[] values=new double[h*r*c];
		for(int k=0;k<h;k++)for(int i=0;i<r;i++)for(int j=0;j<c;j++)values[k*r*c+i*c+j]=this.getValue(ctrlValues,weights,U,V,W,i,j,k);
		this.setGeometry((new IsoSurface3D(new Value3D(values,point0,point1,h,r,c),value)).getGeometry());
	}
	private Point3d getCoordinate(Point3d[] ctrlPoints,double[] weights,double[][] B,int k)
	{
		int n=ctrlPoints.length;
		double x=0,y=0,z=0,w=0;
		for(int i=0;i<n;i++)
		{
			x+=ctrlPoints[i].x*B[k][i]*weights[i];
			y+=ctrlPoints[i].y*B[k][i]*weights[i];
			z+=ctrlPoints[i].z*B[k][i]*weights[i];
			w+=B[k][i]*weights[i];
		}
		return new Point3d(x/w,y/w,z/w);
	}
	private double getValue(double[][][] ctrlValues,double[][][] weights,double[][] U,double[][] V,double[][] W,int u,int v,int w)
	{
		int l=ctrlValues.length;
		int m=ctrlValues[0].length;
		int n=ctrlValues[0][0].length;
		double value=0,weight=0;
		for(int k=0;k<l;k++)
		{
			for(int i=0;i<m;i++)
			{
				for(int j=0;j<n;j++)
				{
					value+=ctrlValues[k][i][j]*U[u][i]*V[v][j]*W[w][k]*weights[k][i][j];
					weight+=U[u][i]*V[v][j]*W[w][k]*weights[k][i][j];
				}
			}
		}
		return value/weight;
	}
	private double[][] getParameterMatrix(double[] knots,int curvePointsLength,int order,int ctrlPointsLength)
	{
		double[] u=knots;
		int l=curvePointsLength;
		int m=order,n=ctrlPointsLength;
		double d=(u[n]-u[m])/(l-1);
		double[][] b=new double[l][n];
		double[][] B=new double[m+1][n+1];
		for(int k=0;k<l;k++)
		{
			double t=u[m]+k*d;
			for(int j=0;j<=n;j++)B[0][j]=isBetween(t,u[j],u[j+1])?1:0;
			for(int i=1;i<=m;i++)
			{
				for(int j=0;j<n;j++)
				{
					double du0=u[j+0+i]-u[j+0];
					double du1=u[j+1+i]-u[j+1];
					double U0=du0==0?0:(t-u[j])/du0;
					double U1=du1==0?0:(u[j+1+i]-t)/du1;
					B[i][j]=B[i-1][j]*U0+B[i-1][j+1]*U1;
				}
			}
			for(int j=0;j<n;j++)b[k][j]=B[m][j];
		}
		return b;	
	}
	public static double[] getStandardUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		double du=1.0/(l-1);
		for(int i=0;i<l;i++)knots[i]=i*du;
		return knots;
	}
	public static double[] getBezierUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*order;
		double du=1.0/(k-1);
		for(int i=0;i<order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=i*du;
		for(int i=0;i<order;i++)knots[c++]=1;
		return knots;
	}
	public static double[] getSemiUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*(order+1);
		for(int i=0;i<=order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=0.5;
		for(int i=0;i<=order;i++)knots[c++]=1;
		return knots;
	}
	boolean isBetween(double x,double x0,double x1)
	{
		return x==0?x0==0:x0<x&&x<=x1;
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
	public Cell3D(double[][][] values,double x0,double x1,double y0,double y1,double z0,double z1)
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
					if((i+j+k)%2==0)
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

					if((i+j+k)%2==0)
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

					if((i+j+k)%2==0)
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

					if((i+j+k)%2==0)
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
class Value3D
{
	public double[] values;
	public double x0,y0,z0,dx,dy,dz;
	public int level,row,column;
	public Value3D(double[] values,Point3d point0,Point3d point1,int level,int row,int column)
	{
		int l=level,m=row,n=column;
		double x1=point1.x,y1=point1.y,z1=point1.z;
		this.values=values;
		this.level=level;
		this.row=row;
		this.column=column;
		this.x0=point0.x;
		this.y0=point0.y;
		this.z0=point0.z;
		this.dx=(x1-x0)/(n-1);
		this.dy=(y1-y0)/(l-1);
		this.dz=(z1-z0)/(m-1);
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
	public IsoSurface3D(Value3D value,double v)
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
		this.inverseNormal=false;
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
