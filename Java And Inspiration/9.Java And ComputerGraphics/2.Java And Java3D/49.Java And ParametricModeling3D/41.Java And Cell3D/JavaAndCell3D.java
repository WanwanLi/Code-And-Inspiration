import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndCell3D
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
		PointAttributes PointAttributes1=new PointAttributes();
		PointAttributes1.setPointSize(1.0f);
		PointAttributes1.setPointAntialiasingEnable(true);
		Appearance Appearance1=new Appearance();
		Appearance1.setPointAttributes(PointAttributes1);
		Transform3D Transform3D=new Transform3D();
		Transform3D.setScale(new Vector3d(0.2,0.2,0.2));
		TransformGroup1.setTransform(Transform3D);
		double[][][] values222=
		{
			{{0,1},	{0,1}},
			{{0,1},	{0,1}}
		};
		double[][][] values333=
		{
			{{0,1,2},	{0,1,2},	{0,1,2}},
			{{0,1,2},	{0,1,2},	{0,1,2}},
			{{0,1,2},	{0,1,2},	{0,1,2}}
		};
		Cell3D cell3D=new Cell3D(values222,-1.2,1.2,-1.2,1.2,-1.2,1.2);
		TransformGroup1.addChild(cell3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
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
